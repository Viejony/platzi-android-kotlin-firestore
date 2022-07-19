package com.platzi.android.firestore.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.platzi.android.firestore.R
import com.platzi.android.firestore.databinding.ActivityLoginBinding
import com.platzi.android.firestore.model.User
import com.platzi.android.firestore.network.Callback
import com.platzi.android.firestore.network.FirestoreService
import com.platzi.android.firestore.network.USERS_COLLECTION_NAME
import com.platzi.android.firestore.ui.activity.tools.Utils

/**
 * @author Santiago Carrillo
 * github sancarbar
 * 1/29/19.
 */


const val USERNAME_KEY = "username_key"

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"
    private lateinit var binding: ActivityLoginBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var firestoreService: FirestoreService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreService = FirestoreService(FirebaseFirestore.getInstance())
    }


    fun onStartClicked(view: View) {
        Utils().printLog("$TAG: onStartClicked")
        view.isEnabled = false
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                Utils().printLog("$TAG: onStartClicked: task = $task")
                if (task.isSuccessful) {
                    val username = binding.username.text.toString()
                    val user: User = User()
                    user.username = username
                    Utils().printLog("$TAG: onStartClicked: isSuccessful, username = $username")
                    saveUserAndStartActivity(user, view)
                } else {
                    Utils().printLog("$TAG: onStartClicked: is not successful")
                    showErrorMessage(view)
                    view.isEnabled = true
                }
            }
    }

    private fun saveUserAndStartActivity(user: User, view: View){
        firestoreService.setDocument(
            user,
            USERS_COLLECTION_NAME,
            user.username,
            object: Callback<Void>{
                override fun onSuccess(result: Void?) {
                    startMainActivity(user.username)
                }

                override fun onFailed(exception: Exception) {
                    showErrorMessage(view)
                    Utils().printLog(exception.toString())
                    view.isEnabled = true
                }
            }
        )
    }

    private fun showErrorMessage(view: View) {
        Snackbar.make(
            view,
            getString(R.string.error_while_connecting_to_the_server),
            Snackbar.LENGTH_LONG
        )
            .setAction("Info", null).show()
    }

    private fun startMainActivity(username: String) {
        val intent = Intent(this@LoginActivity, TraderActivity::class.java)
        intent.putExtra(USERNAME_KEY, username)
        startActivity(intent)
        finish()
    }

}
