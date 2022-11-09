package cz.cvut.fit.vmm.controller

import cz.cvut.fit.vmm.ImageGetter
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

data class Message(val endPoint: String, val text: String)

@RestController
class Controller {
    @PostMapping("/match")
    fun match(@RequestParam("file") uploadedImage: MultipartFile): Message = Message("match", uploadedImage.originalFilename ?: uploadedImage.name)

    @GetMapping("/img", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun img(@RequestParam fileName: String): ByteArrayResource = ImageGetter.get(fileName)
}