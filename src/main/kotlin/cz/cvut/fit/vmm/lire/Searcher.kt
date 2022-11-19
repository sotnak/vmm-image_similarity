package cz.cvut.fit.vmm.lire

import cz.cvut.fit.vmm.MatchedImage
import net.semanticmetadata.lire.builders.DocumentBuilder
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder
import net.semanticmetadata.lire.filters.RerankFilter
import net.semanticmetadata.lire.imageanalysis.features.GlobalFeature
import net.semanticmetadata.lire.imageanalysis.features.global.*
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher
import net.semanticmetadata.lire.searchers.ImageSearchHits
import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.store.FSDirectory
import java.io.InputStream
import java.nio.file.Paths
import javax.imageio.ImageIO


class Searcher(inputStream: InputStream) {
    private val indexReader: DirectoryReader = DirectoryReader.open(FSDirectory.open(Paths.get("index")))
    private val document = createDocument(inputStream)

    // equivalent of static methods
    companion object{
        private fun getFeature(featureStr: String): Class<out GlobalFeature> {
            return when(featureStr){
                "ColorLayout" -> ColorLayout::class.java
                "EdgeHistogram" -> EdgeHistogram::class.java
                "ScalableColor" -> ScalableColor::class.java

                else -> throw IllegalArgumentException("unknown feature")
            }
        }

        private fun getFieldName(klass: Class<out GlobalFeature>): String {
            return when(klass){
                ColorLayout::class.java -> DocumentBuilder.FIELD_NAME_COLORLAYOUT
                EdgeHistogram::class.java -> DocumentBuilder.FIELD_NAME_EDGEHISTOGRAM
                ScalableColor::class.java -> DocumentBuilder.FIELD_NAME_SCALABLECOLOR
                else -> throw IllegalArgumentException("unknown features class")
            }
        }

        private fun createDocument(inputStream: InputStream): Document {
            val globalDocumentBuilder = GlobalDocumentBuilder()
            globalDocumentBuilder.addExtractor(ColorLayout::class.java)
            globalDocumentBuilder.addExtractor(EdgeHistogram::class.java)
            globalDocumentBuilder.addExtractor(ScalableColor::class.java)

            return globalDocumentBuilder.createDocument(ImageIO.read(inputStream), "search_subject")
        }
    }

    private fun fastSearch(klass: Class<out GlobalFeature>, hitCount: Int): ImageSearchHits {
        val searcher = GenericFastImageSearcher(hitCount, klass)
        return searcher.search(document, indexReader)
    }

    private fun reRank(klass: Class<out GlobalFeature>, hits: ImageSearchHits): ImageSearchHits {
        val fieldName = getFieldName(klass)

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
                results[url]?.distance = results[url]?.distance!! + distance * weight   //just results[url] += distance * weight
            } else {
                results[url] = MatchedImage(url, fileName, distance * weight)
            }
        }
    }

    fun search(searchOptions: List<Pair<String, Double>>, hitCount: Int): List<MatchedImage>{

        val weightedKlasses = searchOptions.map { Pair( getFeature( it.first ), it.second) }.sortedBy { (_, value) -> value }

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