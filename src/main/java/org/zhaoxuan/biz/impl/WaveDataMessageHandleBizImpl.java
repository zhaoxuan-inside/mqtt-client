package org.zhaoxuan.biz.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.zhaoxuan.bean.JsonFileBean;
import org.zhaoxuan.bean.ReceiveHeaderBean;
import org.zhaoxuan.bean.WaveDataBean;
import org.zhaoxuan.biz.MqttMessageHandleBiz;
import org.zhaoxuan.cache.WaveDataCache;
import org.zhaoxuan.common.enums.DirEnum;
import org.zhaoxuan.common.enums.LogEnum;
import org.zhaoxuan.common.enums.ResponseEnum;
import org.zhaoxuan.common.enums.WaveDataCodeEnum;
import org.zhaoxuan.common.utils.CrcUtil;
import org.zhaoxuan.common.utils.DataAnalyseUtil;
import org.zhaoxuan.common.utils.FileUtil;
import org.zhaoxuan.common.utils.SensorUtil;
import org.zhaoxuan.config.MqttSender;
import org.zhaoxuan.entity.PointVibrateEntity;
import org.zhaoxuan.entity.SensorEntity;
import org.zhaoxuan.service.PointVibrateService;
import org.zhaoxuan.service.SensorService;

import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class WaveDataMessageHandleBizImpl implements MqttMessageHandleBiz {

    private final Object lock = new Object();

    private final SensorUtil sensorUtil;
    private final FileUtil fileUtil;
    private final WaveDataCache cache;
    private final SensorService sensorService;
    private final PointVibrateService pointVibrateService;

    @Override
    public int analysePayload(ReceiveHeaderBean bean, String payload) {
        WaveDataBean payloadBean = JSONUtil.toBean(payload, WaveDataBean.class);

        Integer packCount = payloadBean.getPackCount();
        Integer packIndex = payloadBean.getPackIndex();
        if (ObjectUtils.isEmpty(packCount) || ObjectUtils.isEmpty(packIndex)) {
            return ResponseEnum.JSON_ANALYSE_FAILURE.getCode();
        }

        String key = generateKey(bean.toPath(), payloadBean);
        byte[] decode = Base64.getDecoder().decode(payloadBean.getContent());
        cache.add(key, decode);

        if (packIndex == 1) {
            PointVibrateEntity pointVibrateEntity = buildPointVibrateEntity(payloadBean);
            if (ObjectUtils.isEmpty(pointVibrateEntity)) {
                cache.remove(key);
                return ResponseEnum.JSON_ANALYSE_FAILURE.getCode();
            }
            synchronized (lock) {
                long count = pointVibrateService.countByMeasuringPointIdTimestamp(
                        pointVibrateEntity.getMeasuringPointVibrateId(),
                        pointVibrateEntity.getTimestamp());
                if (count == 0) {
                    pointVibrateService.save(pointVibrateEntity);
                } else {
                    pointVibrateService.updateByMeasuringPointIdTimestamp(pointVibrateEntity);
                }
            }
        }

        if (packIndex >= packCount) {
            SensorEntity sensor = sensorService.getByChNo(payloadBean.getChNo());
            byte[] bytes = cache.get(key);
            if (ObjectUtils.isEmpty(bytes)) {
                log.info(LogEnum.BYTES_NOT_FOUND.getPrint(), key);
                cache.remove(key);
                return ResponseEnum.JSON_ANALYSE_FAILURE.getCode();
            }
            if (!CrcUtil.checkCRC(payloadBean.getCRC(), bytes)) {
                cache.remove(key);
                return ResponseEnum.CRC_CHECK_FAILURE.getCode();
            }

            Double[] doubles = DataAnalyseUtil.analyseContent(bytes, payloadBean.getCoef());

            JsonFileBean build = JsonFileBean.builder().acc(doubles).build();
            UUID fileName = UUID.randomUUID();
            String fileIncPath = sensorUtil.getFilePath(sensor.getSerialNumber(), fileName.toString());
            String result = JSONObject.toJSON(build).toString();
            try {
                String upload = fileUtil.upload(fileName.toString(), fileIncPath, result);
                PointVibrateEntity pointVibrate = buildWaveDataCodeEntity(payloadBean, doubles.length, upload);
                pointVibrateService.updateByMeasuringPointIdTimestamp(pointVibrate);
                return ResponseEnum.SUCCESS.getCode();
            } catch (Exception ex) {
                log.warn(LogEnum.UPLOAD_FILE_FAILURE.getPrint(), key, ex);
            }
            cache.remove(key);
        }
        return ResponseEnum.FAILURE.getCode();
    }

    private PointVibrateEntity buildWaveDataCodeEntity(WaveDataBean payloadBean,
                                                       Integer samplingNumber,
                                                       String filePath) {

        DirEnum dir = DirEnum.getByCode(payloadBean.getDir());
        if (ObjectUtils.isEmpty(dir)) {
            return null;
        }

        UUID measuringPointId = sensorService.getMeasuringPointId(payloadBean.getChNo(), dir.getPoint());
        if (ObjectUtils.isEmpty(measuringPointId)) {
            return null;
        }
        WaveDataCodeEnum waveDataCodeEnum = WaveDataCodeEnum.getByCode(payloadBean.getCode());
        if (ObjectUtils.isEmpty(waveDataCodeEnum)) {
            return null;
        }
        PointVibrateEntity pointVibrateEntity = PointVibrateEntity.builder().build();
        pointVibrateEntity.setMeasuringPointVibrateId(measuringPointId);
        pointVibrateEntity.setTimestamp(payloadBean.getTime());
        switch (waveDataCodeEnum) {
            case VELOCITY_WAVE:
                pointVibrateEntity.setSamplingNumberVelocity(samplingNumber);
                pointVibrateEntity.setVelocity(filePath);
                break;
            case LOW_FREQ_ACC:
                pointVibrateEntity.setSamplingNumberLow(samplingNumber);
                pointVibrateEntity.setAccelerationLow(filePath);
                break;
            case HIGH_FREQ_ACC:
                pointVibrateEntity.setSamplingNumber(samplingNumber);
                pointVibrateEntity.setAcceleration(filePath);
                break;
        }
        return pointVibrateEntity;
    }

    private PointVibrateEntity buildPointVibrateEntity(WaveDataBean payloadBean) {

        DirEnum dir = DirEnum.getByCode(payloadBean.getDir());
        if (ObjectUtils.isEmpty(dir)) {
            return null;
        }

        UUID measuringPointId = sensorService.getMeasuringPointId(payloadBean.getChNo(), dir.getPoint());
        PointVibrateEntity pointVibrateEntity = PointVibrateEntity.builder()
                .id(UUID.randomUUID())
                .measuringPointVibrateId(measuringPointId)
                .timestamp(payloadBean.getTime())
                .rpm(payloadBean.getRPM())
                .build();

        WaveDataCodeEnum waveDataCodeEnum = WaveDataCodeEnum.getByCode(payloadBean.getCode());
        if (ObjectUtils.isEmpty(waveDataCodeEnum)) {
            log.info(LogEnum.INVALID_CODE.getPrint(), payloadBean.getCode());
            return null;
        }
        switch (waveDataCodeEnum) {
            case VELOCITY_WAVE:
                pointVibrateEntity.setSamplingRateVelocity(payloadBean.getFreq());
                break;
            case LOW_FREQ_ACC:
                pointVibrateEntity.setSamplingRateLow(payloadBean.getFreq());
                break;
            case HIGH_FREQ_ACC:
                pointVibrateEntity.setSamplingRate(payloadBean.getFreq());
                break;
        }

        return pointVibrateEntity;
    }

    private String generateKey(String topic, WaveDataBean bean) {
        StringBuilder sb = new StringBuilder();
        sb.append(topic);
        if (!ObjectUtils.isEmpty(bean)) {
            if (!ObjectUtils.isEmpty(bean.getChNo())) {
                sb.append(bean.getChNo());
            }
            if (!ObjectUtils.isEmpty(bean.getTime())) {
                sb.append(bean.getTime());
            }
            if (!ObjectUtils.isEmpty(bean.getDir())) {
                sb.append(bean.getDir());
            }
            if (!ObjectUtils.isEmpty(bean.getCode())) {
                sb.append(bean.getCode());
            }
        }
        return sb.toString();
    }

}
