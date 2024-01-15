package org.zhaoxuan.biz.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zhaoxuan.bean.ReceiveHeaderBean;
import org.zhaoxuan.biz.MqttMessageHandleBiz;
import org.zhaoxuan.common.enums.ResponseEnum;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UpdateFileMessageHandleBizImpl implements MqttMessageHandleBiz {
    @Override
    public int analysePayload(ReceiveHeaderBean bean, String payload) {
        return ResponseEnum.SUCCESS.getCode();
    }
}
