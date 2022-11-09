package cz.cvut.fit.vmm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VmmApplication

fun main(args: Array<String>) {
    runApplication<VmmApplication>(*args)
}