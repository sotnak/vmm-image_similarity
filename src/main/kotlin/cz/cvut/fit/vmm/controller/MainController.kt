package cz.cvut.fit.vmm.controller

import cz.cvut.fit.vmm.ImageEncoder
import cz.cvut.fit.vmm.InputStreamCloner
import cz.cvut.fit.vmm.MatchedImage
import cz.cvut.fit.vmm.lire.Searcher
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Controller
class MainController {
    private val logger = KotlinLogging.logger {}

    @RequestMapping("/")
    fun index(model: Model): String {
        return "index"
    }


    @PostMapping("/oldMatch", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun oldMatch(@RequestParam("file") uploadedImage: MultipartFile, @RequestParam("feature") feature: String, @RequestParam("count") count: Int, model: Model): String {
        logger.info { "/oldMatch: count: $count feature: $feature" }

        val cloner = InputStreamCloner(uploadedImage.inputStream)

        val searcher = Searcher(cloner.clone())
        val list: List<MatchedImage> = searcher.search(mutableListOf(Pair(feature, 1.0)), count)
        model.addAttribute("oldMatch", list)

        model.addAttribute("image", ImageEncoder.encode(cloner.getByteArray()))
        return "oldMatch"
    }


    @PostMapping("/match", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun match(
        @RequestParam("file") uploadedImage: MultipartFile,
        @RequestParam("colorLayout_weight", required = false, defaultValue = "0") coloLayout_weight: Double,
        @RequestParam("edgeHistogram_weight", required = false, defaultValue = "0") edgeHistogram_weight: Double,
        @RequestParam("scalableColor_weight", required = false, defaultValue = "0") scalableColor_weight: Double,
        @RequestParam("dominantColor_weight", required = false, defaultValue = "0") dominantColor_weight: Double,
        @RequestParam("count") count: Int,
        model: Model): String {
        logger.info { "/match: count $count colorLayout: $coloLayout_weight edgeHistogram: $edgeHistogram_weight scalableColor: $scalableColor_weight" }

        val cloner = InputStreamCloner(uploadedImage.inputStream)

        val searcher = Searcher(cloner.clone())
        val features : MutableList<Pair<String, Double>> = mutableListOf()
        if(coloLayout_weight != 0.0){
            features.add(Pair("ColorLayout", coloLayout_weight))
        }
        if(edgeHistogram_weight != 0.0){
            features.add(Pair("EdgeHistogram", edgeHistogram_weight))
        }
        if(scalableColor_weight != 0.0){
            features.add(Pair("ScalableColor", scalableColor_weight))
        }
        if(dominantColor_weight != 0.0){
            features.add(Pair("DominantColor", dominantColor_weight))
        }

        val list = searcher.search(features, count)
        model.addAttribute("match", list)

        model.addAttribute("image", ImageEncoder.encode(cloner.getByteArray()))
        return "match"
    }

}