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
import mu.KotlinLogging

@RestController
class Controller {
    private val logger = KotlinLogging.logger {}

    @PostMapping("/test", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun test(@RequestParam("file") uploadedImage: MultipartFile,@RequestParam("count") count: Int, cislo : String): List<MatchedImage> {
        val list : MutableList<Pair<String, Double>> = mutableListOf()
        logger.info { "/test $list" }
        if(cislo.isEmpty()){
            println("Cislo je null")
        }
        else{
            println(cislo)
        }
        val searcher = Searcher(uploadedImage.inputStream)
        list.add(Pair("ColorLayout", 1.0))
        list.add(Pair("EdgeHistogram", 2.0))
        return searcher.search(list,count)
    }

    @GetMapping("/img", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun img(@RequestParam fileName: String): ByteArrayResource {
        //logger.info { "/img $fileName" }
        return ImageGetter.get(fileName)
    }
}