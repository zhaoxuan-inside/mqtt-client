package org.zhaoxuan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.zhaoxuan.entity.PointVibrateEntity;

@Mapper
public interface PointVibrateMapper
        extends BaseMapper<PointVibrateEntity>,
        MPJBaseMapper<PointVibrateEntity> {
}
