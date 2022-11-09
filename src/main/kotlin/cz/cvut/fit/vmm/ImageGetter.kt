package cz.cvut.fit.vmm

import org.springframework.core.io.ByteArrayResource
import java.nio.file.Files
import java.nio.file.Paths

object ImageGetter {
    fun get(fileName: String): ByteArrayResource{
        val inputStream = ByteArrayResource(Files.readAllBytes(
            Paths.get(
            "src/main/resources/images/${fileName}"
        )));

        return inputStream;
    }
}