package org.zhaoxuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.zhaoxuan.entity.SensorEntity;

@Mapper
public interface SensorMapper
        extends BaseMapper<SensorEntity>,
        MPJBaseMapper<SensorEntity> {

}
