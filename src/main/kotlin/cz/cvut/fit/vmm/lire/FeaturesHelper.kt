package cz.cvut.fit.vmm.lire

import net.semanticmetadata.lire.builders.DocumentBuilder
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder
import net.semanticmetadata.lire.imageanalysis.features.GlobalFeature
import net.semanticmetadata.lire.imageanalysis.features.global.ColorLayout
import net.semanticmetadata.lire.imageanalysis.features.global.DominantColor
import net.semanticmetadata.lire.imageanalysis.features.global.EdgeHistogram
import net.semanticmetadata.lire.imageanalysis.features.global.ScalableColor
import org.apache.lucene.document.Document
import java.io.InputStream
import javax.imageio.ImageIO

object FeaturesHelper {
    fun getFeatureClass(featureStr: String): Class<out GlobalFeature> {
        return when(featureStr){
            "ColorLayout" -> ColorLayout::class.java
            "EdgeHistogram" -> EdgeHistogram::class.java
            "ScalableColor" -> ScalableColor::class.java
            "DominantColor" -> DominantColor::class.java

            else -> throw IllegalArgumentException("unknown feature")
        }
    }

    fun getDocumentFieldName(klass: Class<out GlobalFeature>): String {
        return when(klass){
            ColorLayout::class.java -> DocumentBuilder.FIELD_NAME_COLORLAYOUT
            EdgeHistogram::class.java -> DocumentBuilder.FIELD_NAME_EDGEHISTOGRAM
            ScalableColor::class.java -> DocumentBuilder.FIELD_NAME_SCALABLECOLOR
            DominantColor::class.java -> "customDominantColor"
            else -> throw IllegalArgumentException("unknown features class")
        }
    }

    fun createDocument(inputStream: InputStream): Document {
        val globalDocumentBuilder = GlobalDocumentBuilder()
        globalDocumentBuilder.addExtractor(ColorLayout::class.java)
        globalDocumentBuilder.addExtractor(EdgeHistogram::class.java)
        globalDocumentBuilder.addExtractor(ScalableColor::class.java)
        globalDocumentBuilder.addExtractor(DominantColor::class.java)

        return globalDocumentBuilder.createDocument(ImageIO.read(inputStream), "search_subject")
    }
}