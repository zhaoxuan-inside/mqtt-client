package org.zhaoxuan.biz.impl;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.zhaoxuan.bean.IndexDataBean;
import org.zhaoxuan.bean.ReceiveHeaderBean;
import org.zhaoxuan.biz.MqttMessageHandleBiz;
import org.zhaoxuan.common.enums.DirEnum;
import org.zhaoxuan.common.enums.IndexDataCodeEnum;
import org.zhaoxuan.common.enums.LogEnum;
import org.zhaoxuan.common.enums.ResponseEnum;
import org.zhaoxuan.common.utils.SensorUtil;
import org.zhaoxuan.entity.PointEdgeFeatureEntity;
import org.zhaoxuan.service.PointEdgeFeatureService;
import org.zhaoxuan.service.SensorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class IndexDataMessageHandleBizImpl implements MqttMessageHandleBiz {

    private final SensorUtil sensorUtil;
    private final SensorService sensorService;
    private final PointEdgeFeatureService pointEdgeFeatureService;

    @Override
    public int analysePayload(ReceiveHeaderBean bean, String payload) {
        List<IndexDataBean> beans = JSONUtil.toList(payload, IndexDataBean.class);
        if (ObjectUtils.isEmpty(beans)) {
            log.info(LogEnum.EMPTY_PAYLOAD.getPrint(), bean.toPath());
            return ResponseEnum.JSON_ANALYSE_FAILURE.getCode();
        }

        Map<String, PointEdgeFeatureEntity> map = new HashMap<>();
        for (IndexDataBean indexDataBean : beans) {
            if (ObjectUtils.isEmpty(indexDataBean.getTime())) {
                log.warn(LogEnum.NOT_FOUND_TIME.getPrint(), bean.toPath());
                return ResponseEnum.JSON_ANALYSE_FAILURE.getCode();
            }
            PointEdgeFeatureEntity pointEdgeFeatureEntity = new PointEdgeFeatureEntity();
            pointEdgeFeatureEntity.setTimestamp(indexDataBean.getTime().get(0));
            List<IndexDataBean.IndexDataContentBean> content = indexDataBean.getContent();
            for (IndexDataBean.IndexDataContentBean indexDataContentBean : content) {
                List<Double> values = indexDataContentBean.getValues();
                if (ObjectUtils.isEmpty(values)) {
                    log.warn(LogEnum.NOT_FOUND_VALUE.getPrint(), bean.toPath());
                    continue;
                }
                String key = indexDataBean.getChNo() + "," + indexDataContentBean.getDir();
                PointEdgeFeatureEntity getByKey = map.get(key);
                if (ObjectUtils.isEmpty(getByKey)) {
                    getByKey = pointEdgeFeatureEntity.clone();
                    DirEnum dir = DirEnum.getByCode(indexDataContentBean.getDir());
                    if (ObjectUtils.isEmpty(dir)) {
                        continue;
                    }
                    UUID measuringPointId = sensorService.getMeasuringPointId(indexDataBean.getChNo(), dir.getPoint());
                    if (ObjectUtils.isEmpty(measuringPointId)) {
                        continue;
                    }
                    getByKey.setId(UUID.randomUUID());
                    getByKey.setMeasuringPointId(measuringPointId);
                }
                IndexDataCodeEnum indexDataCode = IndexDataCodeEnum.getByCode(indexDataContentBean.getCode());
                if (ObjectUtils.isEmpty(indexDataCode)) {
                    continue;
                }
                switch (indexDataCode) {
                    case TEMPERATURE:
                        getByKey.setTemperature(values.get(0));
                        break;
                    case VELOCITY:
                        getByKey.setSpeedRms(values.get(0));
                        break;
                    case HIGH_FREQ_VALUE:
                        getByKey.setAccelerationRms(values.get(0));
                        break;
                }
                map.put(key, getByKey);
            }
        }
        pointEdgeFeatureService.saveBatch(map.values());
        return ResponseEnum.SUCCESS.getCode();
    }


}
