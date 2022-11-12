package cz.cvut.fit.vmm.controller

import cz.cvut.fit.vmm.ImageGetter
import cz.cvut.fit.vmm.MatchedImage
import cz.cvut.fit.vmm.lire.Searcher
import net.semanticmetadata.lire.imageanalysis.features.global.EdgeHistogram
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class Controller {
    @PostMapping("/match", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun match(@RequestParam("file") uploadedImage: MultipartFile, @RequestParam("feature") feature: String, @RequestParam("count") count: Int): List<MatchedImage> = Searcher.search(uploadedImage.inputStream, feature, count)

    @GetMapping("/img", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun img(@RequestParam fileName: String): ByteArrayResource = ImageGetter.get(fileName)
}