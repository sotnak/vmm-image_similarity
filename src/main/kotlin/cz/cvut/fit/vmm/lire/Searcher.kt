package cz.cvut.fit.vmm.lire

import cz.cvut.fit.vmm.MatchedImage
import net.semanticmetadata.lire.builders.DocumentBuilder
import net.semanticmetadata.lire.imageanalysis.features.global.ColorLayout
import net.semanticmetadata.lire.imageanalysis.features.global.EdgeHistogram
import net.semanticmetadata.lire.imageanalysis.features.global.ScalableColor
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.store.FSDirectory
import java.io.InputStream
import java.nio.file.Paths


object Searcher {
    fun search(inputStream: InputStream, featureStr: String, hitCount: Int): List<MatchedImage>{

        val klass = when(featureStr){
            "ColorLayout" -> ColorLayout::class.java
            "EdgeHistogram" -> EdgeHistogram::class.java
            "ScalableColor" -> ScalableColor::class.java

            else -> throw IllegalArgumentException("unknown feature")
        }

        val searcher = GenericFastImageSearcher(hitCount, klass)
        val indexReader = DirectoryReader.open(FSDirectory.open(Paths.get("index")))
        val hits = searcher.search(inputStream, indexReader)
        val results : MutableList<MatchedImage> = mutableListOf()

        for (i in 0 until hits.length()){
            val document = indexReader.document(hits.documentID(i))
            val filePath = Paths.get(document.getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0])
            val fileName: String = filePath.fileName.toString()
            val url = "http://localhost:8080/img?fileName=${fileName}"
            val distance = hits.score(i)
            results.add( MatchedImage(url, fileName, distance) )
        }

        return results
    }
}