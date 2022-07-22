package com.platzi.android.firestore.ui.activity

import java.util.*

class Crypto (
    var name: String = "",
    var imageUrl: String = "",
    var available: Int = 0
){
    fun getDocumentID(): String{
        return name.toLowerCase(Locale.ROOT)
    }

    override fun toString(): String {
        return "{\"name\":\"${name}\", \"available\":\"${available}\", \"imageUrl\":\"${imageUrl}\"}"
    }
}