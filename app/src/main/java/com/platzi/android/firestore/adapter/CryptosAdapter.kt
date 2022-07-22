package com.platzi.android.firestore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.platzi.android.firestore.R
import com.platzi.android.firestore.databinding.CryptoRowBinding
import com.platzi.android.firestore.ui.activity.Crypto
import com.platzi.android.firestore.ui.activity.tools.Utils
import com.squareup.picasso.Picasso

class CryptosAdapter(val cryptosAdapterListener: CryptosAdapterListener): RecyclerView.Adapter<CryptosAdapter.ViewHolder>() {

    val TAG = CryptosAdapter::class.simpleName ?: ""
    var cryptosList: List<Crypto> = ArrayList()

    inner class ViewHolder(val itemBinding: CryptoRowBinding, val context: Context): RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = CryptoRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val crypto = cryptosList[position]
        Utils().printLog(TAG, "onBindViewHolder: ${crypto.toString()}")

        holder.itemBinding.apply {
            Picasso.get().load(crypto.imageUrl).into(image)
            nameTextView.text = crypto.name
            availableTextView.text = "${holder.context.getString(R.string.available_message)} ${crypto.available}"
            buyButton.setOnClickListener{
                Utils().printLog(TAG, "onBindViewHolder: setOnClickListener: $crypto")
                cryptosAdapterListener.onBuyCryptoClicked(crypto)
            }
        }

    }

    override fun getItemCount(): Int {
        return cryptosList.size
    }


}