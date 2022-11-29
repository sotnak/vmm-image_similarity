package cz.cvut.fit.vmm

import java.nio.charset.StandardCharsets
import java.util.*

object ImageEncoder {
    fun encode(byteArray: ByteArray): String {
        val encodeBase64 = Base64.getEncoder().encode(byteArray)
        return String(encodeBase64, StandardCharsets.UTF_8)
    }
}