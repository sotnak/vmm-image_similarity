package cz.cvut.fit.vmm.lire

import net.semanticmetadata.lire.imageanalysis.features.global.ColorLayout
import net.semanticmetadata.lire.imageanalysis.features.global.EdgeHistogram
import net.semanticmetadata.lire.imageanalysis.features.global.ScalableColor
import net.semanticmetadata.lire.indexers.parallel.ParallelIndexer
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
object Indexer {
    fun indexImages(threadCount: Int = 3){
        val indexer = ParallelIndexer(threadCount, "index", "src/main/resources/images/")
        indexer.addExtractor(EdgeHistogram::class.java)
        indexer.addExtractor(ColorLayout::class.java)
        indexer.addExtractor(ScalableColor::class.java)
        indexer.run()
    }

    @PostConstruct
    fun initIndex() = indexImages(7)
}