package org.zhaoxuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.zhaoxuan.entity.MeasuringPointEntity;

@Mapper
public interface MeasuringPointMapper
        extends BaseMapper<MeasuringPointEntity>,
        MPJBaseMapper<MeasuringPointEntity> {
}
