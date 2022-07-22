package com.platzi.android.firestore.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.platzi.android.firestore.R
import com.platzi.android.firestore.adapter.CryptosAdapter
import com.platzi.android.firestore.adapter.CryptosAdapterListener
import com.platzi.android.firestore.databinding.ActivityTraderBinding
import com.platzi.android.firestore.network.Callback
import com.platzi.android.firestore.network.FirestoreService
import com.platzi.android.firestore.ui.activity.tools.Utils

//import kotlinx.android.synthetic.main.activity_trader.*


/**
 * @author Santiago Carrillo
 * 2/14/19.
 */
class TraderActivity : AppCompatActivity(), CryptosAdapterListener {

    private lateinit var binding: ActivityTraderBinding

    lateinit var firestoreService: FirestoreService
    lateinit var cryptosAdapter: CryptosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreService = FirestoreService(FirebaseFirestore.getInstance())
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

            override fun onSuccess(result: List<Crypto>?) {
                cryptosAdapter.cryptosList = result!!
                Utils().printLog("loadCryptos: onSuccess: ${result!!.toString()}")
                this@TraderActivity.runOnUiThread{
                    cryptosAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailed(exception: Exception) {
                Utils().printLog("loadCryptos: onFailed: ${exception}")
            }

        })
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