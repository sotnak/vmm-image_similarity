package cz.cvut.fit.vmm.controller

import cz.cvut.fit.vmm.ImageGetter
import cz.cvut.fit.vmm.MatchedImage
import cz.cvut.fit.vmm.lire.Searcher
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
    fun match(@RequestParam("file") uploadedImage: MultipartFile, @RequestParam("features") features: List<Pair<String, Double>>, @RequestParam("count") count: Int): List<MatchedImage> = Searcher.search(uploadedImage.inputStream, features, count)

    @PostMapping("/oldMatch", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun oldMatch(@RequestParam("file") uploadedImage: MultipartFile, @RequestParam("feature") feature: String, @RequestParam("count") count: Int): List<MatchedImage> = Searcher.search(uploadedImage.inputStream, listOf(Pair(feature, 1.0)), count)

    @GetMapping("/img", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun img(@RequestParam fileName: String): ByteArrayResource = ImageGetter.get(fileName)
}