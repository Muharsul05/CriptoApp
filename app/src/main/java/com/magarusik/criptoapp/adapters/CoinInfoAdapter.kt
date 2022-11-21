package com.magarusik.criptoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.magarusik.criptoapp.R
import com.magarusik.criptoapp.pojo.CoinPriceInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_coin_info.view.*

class CoinInfoAdapter(private val context: Context) :
    RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {
    var coinInfoList: List<CoinPriceInfo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_coin_info, parent, false)
        return CoinInfoViewHolder(view)
    }

    override fun getItemCount() = coinInfoList.size

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = coinInfoList[position]
        with(holder) {
            with(coin) {
                val symbolsTemplate = context.resources.getString(R.string.symbols_template)
                val lastUpdateTemplate = context.resources.getString(R.string.last_update_template)
                coinNameView.text = String.format(symbolsTemplate, fromSymbol, toSymbol)
                coinPriceInfoView.text = price.toString()
                coinTimeUpdate.text = String.format(lastUpdateTemplate, getFormattedTime())
                Picasso.get().load(getFullImageUrl()).into(coinLogoView)
                itemView.setOnClickListener {
                    onCoinClickListener?.onCoinClick(this)
                }
            }
        }

    }

    inner class CoinInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coinLogoView: ImageView = itemView.coinLogoView
        val coinNameView: TextView = itemView.coinNameView
        val coinPriceInfoView: TextView = itemView.coinPriceInfoView
        val coinTimeUpdate: TextView = itemView.coinTimeUpdateView
    }

    interface OnCoinClickListener {
        fun onCoinClick(coinPriceInfo: CoinPriceInfo)
    }
}