package com.platzi.android.firestore.network

import com.google.firebase.firestore.FirebaseFirestore
import com.platzi.android.firestore.model.User
import com.platzi.android.firestore.tools.Constants
import com.platzi.android.firestore.ui.activity.Crypto
import com.platzi.android.firestore.ui.activity.tools.Utils

class FirestoreService(val firebaseFirestore: FirebaseFirestore) {

    fun setDocument(data: Any, collectionName: String, id: String, callback: Callback<Void>){
        firebaseFirestore
            .collection(collectionName)
            .document(id)
            .set(data)
            .addOnSuccessListener { callback.onSuccess(null) }
            .addOnFailureListener { exception -> callback.onFailed(exception) }
    }

    fun updateUser(user: User, callback: Callback<User>?){
        firebaseFirestore
            .collection(Constants.USERS_COLLECTION_NAME)
            .document(user.username)
            .update("cryptosList", user.cryptoList)
            .addOnSuccessListener { result ->
                callback?.onSuccess(user)
            }
            .addOnFailureListener { exception ->
                callback?.onFailed(exception)
            }
    }

    fun updateCrypto(crypto: Crypto){
        firebaseFirestore
            .collection(Constants.CRYPTO_COLLECTION_NAME)
            .document(crypto.getDocumentID())
            .update("available", crypto.available)
    }

    fun getCryptos(callback: Callback<List<Crypto>>?){
        Utils().printLog("getCryptos")
        firebaseFirestore
            .collection(Constants.CRYPTO_COLLECTION_NAME)
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    val cryptoList = result.toObjects(Crypto::class.java)
                    Utils().printLog("getCryptos: onSuccess: document = ${document.data}")
                    callback?.onSuccess(cryptoList)
                }
            }
            .addOnFailureListener { exception ->
                Utils().printLog("getCryptos: onFailure")
                callback?.onFailed(exception)
            }
    }

    fun findUserByID(id: String, callback: Callback<User>?){
        firebaseFirestore
            .collection(Constants.USERS_COLLECTION_NAME)
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                if(result.data != null){
                    callback?.onSuccess(result.toObject(User::class.java))
                }
                else{
                    callback?.onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->
                callback?.onFailed(exception)
            }
    }

}