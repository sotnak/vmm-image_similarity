package cz.cvut.fit.vmm

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

class InputStreamCloner(inputStream: InputStream) {

    private val baos = ByteArrayOutputStream()

    init {
        inputStream.transferTo(baos)
    }

    fun clone() = ByteArrayInputStream(baos.toByteArray())

    fun getByteArray(): ByteArray = baos.toByteArray()
}