package com.platzi.android.firestore.model

import com.platzi.android.firestore.ui.activity.Crypto

data class User(
    var username: String = "",
    var cryptoList: List<Crypto>? = null
)