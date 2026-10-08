package dev.shiwa.onearunbackend.config

import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.charset.StandardCharsets

@Configuration
class MqttConfig(
    @Value("\${mqtt.broker-url}") private val brokerUrl: String,
    @Value("\${mqtt.client-id-prefix}") private val clientIdPrefix: String,
    @Value("\${mqtt.mqtt-broker-port}") private val brokerPort: Int,
    @Value("\${mqtt.topic:corrida-app/test/hello}") private val topic: String
) {
    @Bean
    fun mqttClient(): Mqtt5Client {
        val clientId = clientIdPrefix + System.currentTimeMillis()
        val host = brokerUrl
            .removePrefix("tcp://")
            .removePrefix("ssl://")
            .removePrefix("mqtt://")
            .removePrefix("mqtts://")
            .substringBefore(":")

        val client = Mqtt5Client.builder()
            .identifier(clientId)
            .serverHost(host)
            .serverPort(brokerPort)
            .automaticReconnectWithDefaultConfig()
            .buildAsync()

        client.connectWith()
            .cleanStart(true)
            .send()
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    System.err.println("Initial MQTT connection failed to $host:$brokerPort: ${throwable.message}. Reconnect mechanism will retry in the background.")
                } else {
                    println("Successfully connected to MQTT broker at $host:$brokerPort")
                    // print all messages received at that topic
                    client.publishes(MqttGlobalPublishFilter.SUBSCRIBED) { publish ->
                        val receivedTopic = publish.topic.toString()
                        val message = String(
                            publish.payloadAsBytes,
                            StandardCharsets.UTF_8
                        )
                        println("Received on $receivedTopic: $message")
                    }
                    client.subscribeWith()
                        .topicFilter(topic)
                        .send()
                        .whenComplete { _, subThrowable ->
                            if (subThrowable != null) {
                                System.err.println("Failed to subscribe to $topic: ${subThrowable.message}")
                            } else {
                                println("Successfully subscribed to topic: $topic")
                            }
                        }
                }
            }

        return client
    }
}
