package com.magarusik.criptoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.magarusik.criptoapp.api.ApiFactory
import com.magarusik.criptoapp.database.AppDatabase
import com.magarusik.criptoapp.pojo.CoinPriceInfo
import com.magarusik.criptoapp.pojo.CoinPriceInfoRawData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    val priceList = db.coinPriceInfoDao().getPriceList()

    init {
        loadData()
    }

    private fun loadData() {
        val disposable = ApiFactory
            .apiService
            .getTopCoinsInfo(limit = 50)
            .map { it.data?.map { data -> data.coinInfo?.name }?.joinToString(",").toString() }
            .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }
            .map { getPriseListFromRawData(it)!! }
            .delaySubscription(10,TimeUnit.MINUTES)
            .repeat()
            .retry()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    db.coinPriceInfoDao().insertPriceList(it)
                    Log.d("OK_MESSAGE", "Success:$it")
                }, {
                    Log.d("BUG_MESSAGE", "Bug:${it.message}")
                }
            )
        compositeDisposable.add(disposable)
    }

    private fun getPriseListFromRawData(coinPriceInfoRawData: CoinPriceInfoRawData):
            List<CoinPriceInfo>? {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject ?: return null
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    fun getDetailInfo(fSym: String = "BTC"): LiveData<CoinPriceInfo> =
        db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}