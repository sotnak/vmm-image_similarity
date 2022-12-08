package cz.cvut.fit.vmm

import org.springframework.stereotype.Component


object Repository {

    private var list: MutableList<MatchedImage> = mutableListOf()
    lateinit var upload_image: MatchedImage

    fun addToList(zaznam: MutableList<MatchedImage>) {
        list.addAll(zaznam)
    }

    fun getList(): List<MatchedImage> {
        return list
    }

    fun remove(){
        list.removeAll(list)
    }

    fun addUploadImage(matchedImage: MatchedImage){
        upload_image = matchedImage
    }

    fun getUploadImage(): MatchedImage {
        return upload_image
    }

}