package cz.cvut.fit.vmm.lire

import cz.cvut.fit.vmm.MatchedImage
import mu.KotlinLogging
import net.semanticmetadata.lire.builders.DocumentBuilder
import net.semanticmetadata.lire.filters.RerankFilter
import net.semanticmetadata.lire.imageanalysis.features.GlobalFeature
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher
import net.semanticmetadata.lire.searchers.ImageSearchHits
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.store.FSDirectory
import java.io.InputStream
import java.nio.file.Paths
import kotlin.math.pow
import kotlin.math.sqrt


class Searcher(inputStream: InputStream) {
    private val logger = KotlinLogging.logger {}

    private val indexReader: DirectoryReader = DirectoryReader.open(FSDirectory.open(Paths.get("index")))
    private val document = FeaturesHelper.createDocument(inputStream)

    private fun fastSearch(klass: Class<out GlobalFeature>, hitCount: Int): ImageSearchHits {
        logger.info { "search: $klass" }
        val searcher = GenericFastImageSearcher(hitCount, klass)
        return searcher.search(document, indexReader)
    }

    private fun reRank(klass: Class<out GlobalFeature>, hits: ImageSearchHits): ImageSearchHits {
        logger.info { "rerank: $klass" }
        val fieldName = FeaturesHelper.getDocumentFieldName(klass)

        val filter = RerankFilter(klass, fieldName)
        return filter.filter(hits, indexReader, document)
    }

    private fun addResults(results : MutableMap<String, MatchedImage>, hits: ImageSearchHits, weight: Double){
        for (i in 0 until hits.length()){
            val hitDocument = indexReader.document(hits.documentID(i))
            val filePath = Paths.get(hitDocument.getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0])
            val fileName: String = filePath.fileName.toString()
            val url = "http://localhost:8080/img?fileName=${fileName}"
            val distance = hits.score(i)

            if (results.containsKey(url)){
                results[url]?.distance = sqrt( results[url]?.distance?.pow(2)!! + (distance * weight).pow(2) ) //euclidean distance
            } else {
                results[url] = MatchedImage(url, fileName, distance * weight)
            }
        }
    }

    fun search(searchOptions: List<Pair<String, Double>>, hitCount: Int): List<MatchedImage>{

        val weightedKlasses = searchOptions.map { Pair( FeaturesHelper.getFeatureClass( it.first ), it.second) }.sortedByDescending { (_, value) -> value }

        var hits = fastSearch(weightedKlasses.first().first, hitCount * 2)
        val results : MutableMap<String, MatchedImage> = mutableMapOf()

        addResults(results, hits, weightedKlasses.first().second)

        for ((feature, weight) in weightedKlasses){
            if(feature == weightedKlasses.first().first){
                continue
            }

            hits = reRank(feature, hits)
            addResults(results, hits, weight)
        }

        return results.values.toList().sortedBy { it.distance }.subList(0,hitCount)
    }
}