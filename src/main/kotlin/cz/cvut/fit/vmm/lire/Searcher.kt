package cz.cvut.fit.vmm.lire

import net.semanticmetadata.lire.imageanalysis.features.GlobalFeature
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher
import java.io.InputStream
import java.nio.file.Paths
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.store.FSDirectory


object Searcher {
    inline fun <reified T: GlobalFeature> search(image: InputStream, hitCount: Int){
        val searcher = GenericFastImageSearcher(hitCount, T::class.java)
        val indexReader = DirectoryReader.open(FSDirectory.open(Paths.get("index")))
        val hits = searcher.search(image, indexReader)

        for (i in 0 until hits.length()){
            val document = indexReader.document(hits.documentID(i))
            println(document.fields)
        }
    }
}