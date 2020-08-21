package com.ydl.list.data

class PersonData {

    var name: String?= null
    var image = 0
    var sign: String? = null

    fun getPersonName():String?{
        return name
    }
    fun  setPersonName(name :String){
        this.name = name
    }

    fun getPersonImage():Int?{
        return image
    }
    fun  setPersonImage(name :Int){
        this.image = image
    }

    fun getPersonSign():String?{
        return sign
    }
    fun  setPersonImage(sign :String){
        this.sign = sign
    }
}