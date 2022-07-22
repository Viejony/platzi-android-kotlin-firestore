package com.platzi.android.firestore.adapter

import com.platzi.android.firestore.ui.activity.Crypto

interface CryptosAdapterListener {

    fun onBuyCryptoClicked(crypto: Crypto)
}