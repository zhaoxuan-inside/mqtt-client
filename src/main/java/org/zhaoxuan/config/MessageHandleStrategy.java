package org.zhaoxuan.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.zhaoxuan.bean.ReceiveHeaderBean;
import org.zhaoxuan.biz.MqttMessageHandleBiz;
import org.zhaoxuan.biz.impl.IndexDataMessageHandleBizImpl;
import org.zhaoxuan.biz.impl.WaveDataMessageHandleBizImpl;
import org.zhaoxuan.common.constants.ConfigConstant;
import org.zhaoxuan.common.constants.HeaderConstant;
import org.zhaoxuan.common.enums.LogEnum;
import reactor.util.annotation.Nullable;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MessageHandleStrategy {

    private final MqttSender mqttSender;
    private final WaveDataMessageHandleBizImpl waveDataMessageHandleBizImpl;
    private final IndexDataMessageHandleBizImpl indexDataMessageHandleBizImpl;

    @Nullable
    public MqttMessageHandleBiz getHandler(ReceiveHeaderBean bean) {
        if (ObjectUtils.isEmpty(bean) || ObjectUtils.isEmpty(bean.getServiceKey())) {
            return null;
        }

        switch (bean.getServiceKey()) {
            case WAVE_DATA:
                return waveDataMessageHandleBizImpl;
            case INDEX_DATA:
                return indexDataMessageHandleBizImpl;
        }

        return null;
    }

    @Bean
    @ServiceActivator(inputChannel = ConfigConstant.MQTT_IN_MESSAGE_BOUND)
    public MessageHandler handler() {
        return message -> {

            MDC.put(HeaderConstant.TRACE_ID, String.valueOf(System.currentTimeMillis()));

            String topic = (String) message.getHeaders().get(ConfigConstant.MQTT_HEADER_TOPIC);
            String payload = (String) message.getPayload();

            log.info(LogEnum.RECEIVE_MESSAGE.getPrint(), topic, payload);
            if (ObjectUtils.isEmpty(topic)) {
                return;
            }

            ReceiveHeaderBean receiveHeaderBean = new ReceiveHeaderBean();
            ReceiveHeaderBean bean = receiveHeaderBean.toBean(topic);
            if (ObjectUtils.isEmpty(bean)) {
                log.info(LogEnum.INVALID_TOPIC.getPrint(), topic);
                return;
            }

            MqttMessageHandleBiz handler = getHandler(bean);
            if (ObjectUtils.isEmpty(handler)) {
                log.info(LogEnum.INVALID_TOPIC.getPrint(), topic);
                return;
            }
            if (ObjectUtils.isEmpty(payload)) {
                log.info(LogEnum.EMPTY_PAYLOAD.getPrint(), topic);
                return;
            }
            int responseCode = handler.analysePayload(bean, payload);
            mqttSender.sendWithTopic(topic + "/" + responseCode, "");
        };
    }

}
