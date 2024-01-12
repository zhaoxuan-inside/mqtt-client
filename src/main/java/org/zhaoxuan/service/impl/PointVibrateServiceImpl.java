package org.zhaoxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.zhaoxuan.entity.PointVibrateEntity;
import org.zhaoxuan.mapper.PointVibrateMapper;
import org.zhaoxuan.service.PointVibrateService;

import java.sql.Timestamp;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PointVibrateServiceImpl
        extends ServiceImpl<PointVibrateMapper, PointVibrateEntity>
        implements PointVibrateService {

    @Override
    public void updateByMeasuringPointIdTimestamp(PointVibrateEntity pointVibrate) {
        LambdaUpdateWrapper<PointVibrateEntity> update = new LambdaUpdateWrapper<>();
        update.set(!ObjectUtils.isEmpty(pointVibrate.getAcceleration()),
                        PointVibrateEntity::getAcceleration,
                        pointVibrate.getAcceleration())
                .set(!ObjectUtils.isEmpty(pointVibrate.getSamplingRate()),
                        PointVibrateEntity::getSamplingRate,
                        pointVibrate.getSamplingRate())
                .set(!ObjectUtils.isEmpty(pointVibrate.getSamplingNumber()),
                        PointVibrateEntity::getSamplingNumber,
                        pointVibrate.getSamplingNumber())
                .set(!ObjectUtils.isEmpty(pointVibrate.getAccelerationLow()),
                        PointVibrateEntity::getAccelerationLow,
                        pointVibrate.getAccelerationLow())
                .set(!ObjectUtils.isEmpty(pointVibrate.getSamplingRateLow()),
                        PointVibrateEntity::getSamplingRateLow,
                        pointVibrate.getSamplingRateLow())
                .set(!ObjectUtils.isEmpty(pointVibrate.getSamplingNumberLow()),
                        PointVibrateEntity::getSamplingNumberLow,
                        pointVibrate.getSamplingNumberLow())
                .set(!ObjectUtils.isEmpty(pointVibrate.getVelocity()),
                        PointVibrateEntity::getVelocity,
                        pointVibrate.getVelocity())
                .set(!ObjectUtils.isEmpty(pointVibrate.getSamplingRateVelocity()),
                        PointVibrateEntity::getSamplingRateVelocity,
                        pointVibrate.getSamplingRateVelocity())
                .set(!ObjectUtils.isEmpty(pointVibrate.getSamplingNumberVelocity()),
                        PointVibrateEntity::getSamplingNumberVelocity,
                        pointVibrate.getSamplingNumberVelocity())
                .set(!ObjectUtils.isEmpty(pointVibrate.getRpm()),
                        PointVibrateEntity::getRpm,
                        pointVibrate.getRpm())
                .eq(PointVibrateEntity::getMeasuringPointVibrateId, pointVibrate.getMeasuringPointVibrateId())
                .eq(PointVibrateEntity::getTimestamp, pointVibrate.getTimestamp());
        update(update);
    }

    @Override
    public long countByMeasuringPointIdTimestamp(UUID measuringPointVibrateId, Timestamp timestamp) {
        LambdaQueryWrapper<PointVibrateEntity> query = new LambdaQueryWrapper<>();
        query.eq(PointVibrateEntity::getMeasuringPointVibrateId, measuringPointVibrateId)
                .eq(PointVibrateEntity::getTimestamp, timestamp);
        return count(query);
    }
}
