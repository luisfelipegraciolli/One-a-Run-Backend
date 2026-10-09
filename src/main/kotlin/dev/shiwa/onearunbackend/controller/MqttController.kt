package dev.shiwa.onearunbackend.controller

import dev.shiwa.onearunbackend.service.MqttService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class MqttController(
    private val mqttService: MqttService,
) {

    @GetMapping("/")
    fun publishMessage(): String {
        val message = "Hello from the backend!"
        mqttService.publish(payload = message)
        return message
    }
}
