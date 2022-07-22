package com.platzi.android.firestore.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.platzi.android.firestore.R
import com.platzi.android.firestore.adapter.CryptosAdapter
import com.platzi.android.firestore.adapter.CryptosAdapterListener
import com.platzi.android.firestore.databinding.ActivityTraderBinding
import com.platzi.android.firestore.databinding.CoinInfoBinding
import com.platzi.android.firestore.model.User
import com.platzi.android.firestore.network.Callback
import com.platzi.android.firestore.network.FirestoreService
import com.platzi.android.firestore.tools.Constants
import com.platzi.android.firestore.ui.activity.tools.Utils
import com.squareup.picasso.Picasso

//import kotlinx.android.synthetic.main.activity_trader.*


/**
 * @author Santiago Carrillo
 * 2/14/19.
 */
class TraderActivity : AppCompatActivity(), CryptosAdapterListener {

    private lateinit var binding: ActivityTraderBinding

    lateinit var firestoreService: FirestoreService
    lateinit var cryptosAdapter: CryptosAdapter
    private var username: String? = null
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreService = FirestoreService(FirebaseFirestore.getInstance())
        username = intent.extras!![Constants.USERNAME_KEY].toString()
        binding.usernameTextView.text = username

        setRecyclerView()
        loadCryptos()

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, getString(R.string.generating_new_cryptos), Snackbar.LENGTH_SHORT)
                .setAction("Info", null).show()
        }

    }

    private fun loadCryptos() {
        Utils().printLog("loadCryptos")
        firestoreService.getCryptos(object: Callback<List<Crypto>>{

            override fun onSuccess(cryptoList: List<Crypto>?) {

                // Get user cryptos
                firestoreService.findUserByID(username!!, object: Callback<User>{

                    override fun onSuccess(result: User?) {
                        user = result
                        if(user!!.cryptoList == null){
                            var userCryptoList = mutableListOf<Crypto>()
                            for(crypto in cryptoList!!){
                                val cryptoUser = Crypto()
                                cryptoUser.apply {
                                    name = crypto.name
                                    available = crypto.available
                                    imageUrl = crypto.imageUrl
                                    userCryptoList.add(cryptoUser)
                                }
                            }

                            // Add list to database
                            user!!.cryptoList = userCryptoList
                            firestoreService.updateUser(user!!, null)
                        }

                        loadUserCryptos()
                    }

                    override fun onFailed(exception: Exception) {
                        TODO("Not yet implemented")
                    }
                })

                cryptosAdapter.cryptosList = cryptoList!!
                Utils().printLog("loadCryptos: onSuccess: ${cryptoList!!.toString()}")
                this@TraderActivity.runOnUiThread{
                    cryptosAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailed(exception: Exception) {
                Utils().printLog("loadCryptos: onFailed: ${exception}")
                showGeneralServerErrorMessage()
            }

        })
    }

    private fun loadUserCryptos(){
        if(user != null && user!!.cryptoList != null){
            binding.infoPanel.removeAllViews()
            for(crypto in user!!.cryptoList!!){
                addUserCryptoInfoRow(crypto)
            }
        }
    }

    private fun addUserCryptoInfoRow(crypto: Crypto) {
        //val view = LayoutInflater.from(this).inflate(R.layout.coin_info, binding.infoPanel, false)
        val coinBinding = CoinInfoBinding.inflate(layoutInflater)
        coinBinding.coinLabel.text = getString(R.string.coin_info, crypto.name, crypto.available.toString())
        Picasso.get().load(crypto.imageUrl).into(coinBinding.coinIcon)

        binding.infoPanel.addView(coinBinding.root)
    }


    private fun setRecyclerView() {
        cryptosAdapter = CryptosAdapter(this)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = cryptosAdapter
    }


    fun showGeneralServerErrorMessage() {
        Snackbar.make(binding.fab, getString(R.string.error_while_connecting_to_the_server), Snackbar.LENGTH_LONG)
            .setAction("Info", null).show()
    }

    override fun onBuyCryptoClicked(crypto: Crypto) {

    }
}