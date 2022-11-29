package cz.cvut.fit.vmm.controller

import cz.cvut.fit.vmm.ImageGetter
import cz.cvut.fit.vmm.MatchedImage
import cz.cvut.fit.vmm.Repository
import cz.cvut.fit.vmm.lire.Searcher
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import mu.KotlinLogging
import org.springframework.ui.Model

@RestController
class Controller {
    private val logger = KotlinLogging.logger {}

    /*@PostMapping("/match", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun match(@RequestParam("file") uploadedImage: MultipartFile, @RequestParam("features") features: MutableList<Pair<String, Double>>, @RequestParam("count") count: Int): List<MatchedImage> {
        logger.info { "/match $features" }
        val searcher = Searcher(uploadedImage.inputStream)
        return searcher.search(features ,count)
    }*/

   /* @PostMapping("/match", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun match(
        @RequestParam("file") uploadedImage: MultipartFile,
        @RequestParam("colorLayout", required = false) colorLayout: Boolean,
        @RequestParam("colorLayout_weight", required = false) coloLayout_weight: String,
        @RequestParam("edgeHistogram", required = false) edgeHistogram: Boolean,
        @RequestParam("edgeHistogram_weight", required = false) edgeHistogram_weight: String,
        @RequestParam("scalableColor", required = false) scalableColor: Boolean,
        @RequestParam("scalableColor_weight", required = false) scalableColor_weight: String,
        @RequestParam("count") count: Int): List<MatchedImage> {
        //logger.info { "/match $features" }
        val searcher = Searcher(uploadedImage.inputStream)
        val features : MutableList<Pair<String, Double>> = mutableListOf()
        if(colorLayout){
            features.add(Pair("ColorLayout", coloLayout_weight.toDouble()))
            //println(coloLayout_weight.toDouble())
        }
        if(edgeHistogram){
            features.add(Pair("EdgeHistogram", edgeHistogram_weight.toDouble()))
            //println(edgeHistogram_weight.toDouble())
        }
        if(scalableColor){
            features.add(Pair("ScalableColor", scalableColor_weight.toDouble()))
        }
        return searcher.search(features ,count)
    }*/


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
        logger.info { "/img $fileName" }
        return ImageGetter.get(fileName)
    }
}