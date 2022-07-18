package com.platzi.android.firestore.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.platzi.android.firestore.R
import com.platzi.android.firestore.databinding.ActivityLoginBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    fun onStartClicked(view: View) {
        Utils().printLog("$TAG: onStartClicked")
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                Utils().printLog("$TAG: onStartClicked: task = $task")
                if (task.isSuccessful) {
                    val username = binding.username.text.toString()
                    Utils().printLog("$TAG: onStartClicked: isSuccessful, username = $username")
                    startMainActivity(username)
                } else {
                    Utils().printLog("$TAG: onStartClicked: is not successful")
                    showErrorMessage(view)
                }
            }
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
