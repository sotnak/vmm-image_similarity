package cz.cvut.fit.vmm.controller

import cz.cvut.fit.vmm.MatchedImage
import cz.cvut.fit.vmm.Repository
import cz.cvut.fit.vmm.lire.Searcher
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
class MainController {
    var repository = Repository
    private val logger = KotlinLogging.logger {}

    @RequestMapping("/")
    fun index(model: Model): String {
        return "index"
    }


    @PostMapping("/oldMatch", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun oldMatch(@RequestParam("file") uploadedImage: MultipartFile, @RequestParam("feature") feature: String, @RequestParam("count") count: Int, model: Model): String {
        logger.info { "/oldMatch $feature" }
        repository.remove()
        val searcher = Searcher(uploadedImage.inputStream)
        repository.addToList(searcher.search(mutableListOf(Pair(feature, 1.0)), count) as MutableList<MatchedImage>)
        val lists: List<MatchedImage> = repository.getList()
        model.addAttribute("oldMatch", lists)
        model.addAttribute("image", repository.getUploadImage())
        return "oldMatch"
    }


    @PostMapping("/match", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun match(
        @RequestParam("file") uploadedImage: MultipartFile,
        @RequestParam("colorLayout", required = false) colorLayout: Boolean,
        @RequestParam("colorLayout_weight", required = false) coloLayout_weight: String,
        @RequestParam("edgeHistogram", required = false) edgeHistogram: Boolean,
        @RequestParam("edgeHistogram_weight", required = false) edgeHistogram_weight: String,
        @RequestParam("scalableColor", required = false) scalableColor: Boolean,
        @RequestParam("scalableColor_weight", required = false) scalableColor_weight: String,
        @RequestParam("count") count: Int,
        model: Model): String {
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
        repository.remove()
        repository.addToList(searcher.search(features, count) as MutableList<MatchedImage>)
        val lists: List<MatchedImage> = repository.getList()
        model.addAttribute("match", lists)
        model.addAttribute("image", repository.getUploadImage())
        return "match" //searcher.search(features ,count)
    }

}