package org.zhaoxuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zhaoxuan.entity.SensorEntity;

import java.util.UUID;

public interface SensorService extends IService<SensorEntity> {
    UUID getMeasuringPointId(Integer chNo, String axial);

    SensorEntity getByChNo(Integer chNo);

}
