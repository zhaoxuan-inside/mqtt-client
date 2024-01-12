package org.zhaoxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.zhaoxuan.common.enums.LogEnum;
import org.zhaoxuan.entity.MeasuringPointEntity;
import org.zhaoxuan.entity.SensorEntity;
import org.zhaoxuan.mapper.SensorMapper;
import org.zhaoxuan.service.SensorService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class SensorServiceImpl
        extends ServiceImpl<SensorMapper, SensorEntity>
        implements SensorService {

    private final SensorMapper sensorMapper;

    @Override
    public UUID getMeasuringPointId(Integer chNo, String axial) {
        MPJLambdaWrapper<SensorEntity> query = new MPJLambdaWrapper<>();
        query.leftJoin(MeasuringPointEntity.class, MeasuringPointEntity::getSensorId, SensorEntity::getId)
                .eq(SensorEntity::getChNo, String.valueOf(chNo))
                .eq(MeasuringPointEntity::getSensorAxial, String.valueOf(axial))
                .select(MeasuringPointEntity::getId)
                .last(" limit 1 ");
        UUID uuid = sensorMapper.selectJoinOne(UUID.class, query);
        if (ObjectUtils.isEmpty(uuid)) {
            log.warn(LogEnum.NOT_FOUND_MEASURING_POINT_ID.getPrint(), chNo, axial);
        }
        return uuid;
    }

    @Override
    public SensorEntity getByChNo(Integer chNo) {
        LambdaQueryWrapper<SensorEntity> query = new LambdaQueryWrapper<>();
        query.eq(SensorEntity::getChNo, String.valueOf(chNo));
        return getOne(query);
    }

}
