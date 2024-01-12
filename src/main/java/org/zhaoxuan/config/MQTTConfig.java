package org.zhaoxuan.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.ObjectUtils;
import org.zhaoxuan.common.constants.ConfigConstant;
import org.zhaoxuan.common.constants.MqttChannelConstant;

import java.util.UUID;

@Setter
@Slf4j
@Configuration
@ConfigurationProperties(ConfigConstant.MQTT_PREFIX)
public class MQTTConfig {

    private String server;
    private String clientId = UUID.randomUUID().toString();
    private String username;
    private String password;
    private boolean cleanSession;
    private String topics;
    private int timeout;
    private int keepAlive;
    private int connectionTimeout;

    public String getClientId() {
        return clientId;
    }

    public String getTopics() {
        return topics;
    }

    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(cleanSession);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setServerURIs(new String[]{server});
        options.setConnectionTimeout(connectionTimeout);
        options.setKeepAliveInterval(keepAlive);
        return options;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    @Bean(name = MqttChannelConstant.MQTT_IN_MESSAGE_BOUND)
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        String[] split = topics.split(";");
        if (ObjectUtils.isEmpty(split)) {
            return null;
        }
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId,
                mqttClientFactory(), split);
        adapter.setCompletionTimeout(timeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }

    @Bean(name = MqttChannelConstant.MQTT_OUT_MESSAGE_BOUND)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = MqttChannelConstant.MQTT_OUT_MESSAGE_BOUND)
    public MessageHandler mqttOutbound(MqttPahoClientFactory factory) {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId + "Sender", factory);
        handler.setAsync(true);
        handler.setConverter(new DefaultPahoMessageConverter());
        return handler;
    }

    @Bean
    public MessageChannel errorChannel() {
        return new DirectChannel();
    }

}