package org.zhaoxuan.biz;

import org.zhaoxuan.bean.ReceiveHeaderBean;

public interface MqttMessageHandleBiz {
    int analysePayload(ReceiveHeaderBean bean, String payload);
}
