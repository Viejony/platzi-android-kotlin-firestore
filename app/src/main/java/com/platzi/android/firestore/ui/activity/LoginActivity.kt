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
import com.platzi.android.firestore.tools.Constants
import com.platzi.android.firestore.ui.activity.tools.Utils

/**
 * @author Santiago Carrillo
 * github sancarbar
 * 1/29/19.
 */

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

        // Using Firestore auth
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                Utils().printLog("$TAG: onStartClicked: task = $task")

                // Successful task: now lets to check the user
                if (task.isSuccessful) {
                    val username = binding.username.text.toString()

                    firestoreService.findUserByID(username, object: Callback<User>{

                        override fun onSuccess(result: User?) {
                            // User don't exists in Firestore and will be added
                            if(result == null){
                                val userDocument = User()
                                userDocument.username = username
                                saveUserAndStartActivity(userDocument, view)
                            }
                            else{ // User already exists in Firestore
                                startMainActivity(username)
                            }
                        }

                        override fun onFailed(exception: Exception) {
                            showErrorMessage(view)
                            view.isEnabled = true
                        }

                    })  // End of callback

                }
                else {  // Error with task
                    Utils().printLog("$TAG: onStartClicked: is not successful")
                    showErrorMessage(view)
                    view.isEnabled = true
                }
            }
    }

    private fun saveUserAndStartActivity(user: User, view: View){
        firestoreService.setDocument(
            user,
            Constants.USERS_COLLECTION_NAME,
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
        intent.putExtra(Constants.USERNAME_KEY, username)
        startActivity(intent)
        finish()
    }

}
