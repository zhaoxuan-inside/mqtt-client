package org.zhaoxuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zhaoxuan.entity.PointEdgeFeatureEntity;
import org.zhaoxuan.mapper.PointEdgeFeatureMapper;
import org.zhaoxuan.service.PointEdgeFeatureService;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PointEdgeFeatureServiceImpl
        extends ServiceImpl<PointEdgeFeatureMapper, PointEdgeFeatureEntity>
        implements PointEdgeFeatureService {

}
