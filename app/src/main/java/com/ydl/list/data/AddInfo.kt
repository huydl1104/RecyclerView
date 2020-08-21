package com.ydl.list.data

class AddInfo {
    private var image: String?=null

    private var drawable = 0


    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }


    fun getDrawable(): Int {
        return drawable
    }

    fun setDrawable(drawable: Int) {
        this.drawable = drawable
    }

}