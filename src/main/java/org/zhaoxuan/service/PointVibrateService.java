package org.zhaoxuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zhaoxuan.entity.PointVibrateEntity;

import java.sql.Timestamp;
import java.util.UUID;

public interface PointVibrateService extends IService<PointVibrateEntity> {
    void updateByMeasuringPointIdTimestamp(PointVibrateEntity pointVibrate);

    long countByMeasuringPointIdTimestamp(UUID measuringPointVibrateId, Timestamp timestamp);

}
