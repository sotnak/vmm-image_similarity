package cz.cvut.fit.vmm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class VmmApplication

fun main(args: Array<String>) {
    runApplication<VmmApplication>(*args)
}

@RestController
class MessageResource {
    @GetMapping
    fun index(): List<Message> = listOf(
        Message("1", "Hello!"),
        Message("2", "Bonjour!"),
        Message("3", "Privet!"),
        Message("4", "Ahoj!"),
    )
}

data class Message(val id: String?, val text: String)