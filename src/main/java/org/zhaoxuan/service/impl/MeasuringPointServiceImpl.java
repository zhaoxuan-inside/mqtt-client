package org.zhaoxuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zhaoxuan.entity.MeasuringPointEntity;
import org.zhaoxuan.mapper.MeasuringPointMapper;
import org.zhaoxuan.service.MeasuringPointService;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MeasuringPointServiceImpl
        extends ServiceImpl<MeasuringPointMapper, MeasuringPointEntity>
        implements MeasuringPointService {

}
