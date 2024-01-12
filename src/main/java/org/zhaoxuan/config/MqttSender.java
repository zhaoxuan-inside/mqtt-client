package org.zhaoxuan.config;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.zhaoxuan.common.constants.MqttChannelConstant;

@Component
@MessagingGateway(defaultRequestChannel = MqttChannelConstant.MQTT_OUT_MESSAGE_BOUND)
public interface MqttSender {
    void sendToMqtt(String text);

    void sendWithTopic(@Header(MqttHeaders.TOPIC) String topic,
                       String text);

    void sendWithTopicAndQos(@Header(MqttHeaders.TOPIC) String topic,
                             @Header(MqttHeaders.QOS) Integer Qos,
                             String text);
}