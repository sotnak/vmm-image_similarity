package cz.cvut.fit.vmm.controller

import net.semanticmetadata.lire.builders.DocumentBuilder
import net.semanticmetadata.lire.imageanalysis.features.GlobalFeature
import net.semanticmetadata.lire.imageanalysis.features.global.ColorLayout
import net.semanticmetadata.lire.imageanalysis.features.global.EdgeHistogram
import net.semanticmetadata.lire.imageanalysis.features.global.ScalableColor

object FeaturesHelper {
    fun getFeatureClass(featureStr: String): Class<out GlobalFeature> {
        return when(featureStr){
            "ColorLayout" -> ColorLayout::class.java
            "EdgeHistogram" -> EdgeHistogram::class.java
            "ScalableColor" -> ScalableColor::class.java

            else -> throw IllegalArgumentException("unknown feature")
        }
    }

    fun getDocumentFieldName(klass: Class<out GlobalFeature>): String {
        return when(klass){
            ColorLayout::class.java -> DocumentBuilder.FIELD_NAME_COLORLAYOUT
            EdgeHistogram::class.java -> DocumentBuilder.FIELD_NAME_EDGEHISTOGRAM
            ScalableColor::class.java -> DocumentBuilder.FIELD_NAME_SCALABLECOLOR
            else -> throw IllegalArgumentException("unknown features class")
        }
    }
}