package com.magarusik.criptoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.magarusik.criptoapp.adapters.CoinInfoAdapter
import com.magarusik.criptoapp.pojo.CoinPriceInfo
import kotlinx.android.synthetic.main.actyvity_coin_price_list.*

class CoinPriceListActivity : AppCompatActivity() {
    private lateinit var viewModel: CoinViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actyvity_coin_price_list)
        val adapter = CoinInfoAdapter(this)
        adapter.onCoinClickListener = object : CoinInfoAdapter.OnCoinClickListener {
            override fun onCoinClick(coinPriceInfo: CoinPriceInfo) {
                val intent = CoinDetailActivity.newIntent(
                    this@CoinPriceListActivity,
                    coinPriceInfo.fromSymbol
                )
                startActivity(intent)
            }
        }
        coinPriceList.adapter = adapter
        viewModel = ViewModelProvider(this)[CoinViewModel::class.java]
        viewModel.priceList.observe(this, Observer {
            adapter.coinInfoList = it
        })
    }
}