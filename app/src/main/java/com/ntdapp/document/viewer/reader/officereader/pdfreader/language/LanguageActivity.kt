package com.ntdapp.document.viewer.reader.officereader.pdfreader.language

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amazic.ads.callback.NativeCallback
import com.amazic.ads.util.Admod
import com.example.ads.AppIronSource
import com.example.ads.funtion.AdCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.ntdapp.document.viewer.reader.officereader.pdfreader.R
import com.ntdapp.document.viewer.reader.officereader.pdfreader.activity.IntroScreenActivity
import com.ntdapp.document.viewer.reader.officereader.pdfreader.language.adapter.LanguageAdapterMain
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.CheckInternet
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.SystemUtil
import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : AppCompatActivity(), IClickLanguage {
    private var adapter: LanguageAdapterMain? = null
    private var model: LanguageModel = LanguageModel()
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        // load and show ads native language
        sharedPreferences = getSharedPreferences("MY_PRE", MODE_PRIVATE)

        /*if (!AppPurchase.getInstance().isPurchased(this)) {
            fr_ads.visibility = View.VISIBLE
            loadNativeLanguage()
        } else {
            fr_ads.visibility = View.GONE
        }*/
        //fr_ads.visibility = View.VISIBLE
        //loadNativeLanguage()


        // Ads Inter
        if (!AppIronSource.getInstance().isInterstitialReady) {
            AppIronSource.getInstance().loadInterstitial(this, AdCallback())
        }

        adapter = LanguageAdapterMain(this, setLanguageDefault(), this)
        rcl_language.adapter = adapter
    }

    override fun onClick(data: LanguageModel) {
        adapter?.setSelectLanguage(data)
        model = data
    }

    private fun setLanguageDefault(): List<LanguageModel>? {
        val lists: MutableList<LanguageModel> = ArrayList()
        val key: String = SystemUtil.getPreLanguage(this)
        lists.add(LanguageModel("English", "en", false, R.drawable.ic_english_flag))
        lists.add(LanguageModel("French", "fr", false, R.drawable.ic_french_flag))
        lists.add(LanguageModel("Portuguese", "pt", false, R.drawable.ic_portuguese_flag))
        lists.add(LanguageModel("Spanish", "es", false, R.drawable.ic_spanish))
        lists.add(LanguageModel("German", "de", false, R.drawable.ic_german_flag))
        Log.e("", "setLanguageDefault: $key")
        for (i in lists.indices) {
            if (!sharedPreferences!!.getBoolean("nativeLanguage", false)) {
                if (key == lists[i].isoLanguage) {
                    val data = lists[i]
                    data.isCheck = true
                    lists.remove(lists[i])
                    lists.add(0, data)
                    break
                }
            } else {
                if (key == lists[i].isoLanguage) {
                    lists[i].isCheck = true
                }
            }
        }
        return lists
    }

    fun ivDone(v: View) {
        if (model != null) {
            SystemUtil.setPreLanguage(this@LanguageActivity, model.isoLanguage)
        }
        SystemUtil.setLocale(this)
        startNextActivity()
    }

    private fun startNextActivity() {
        if (AppIronSource.getInstance().isInterstitialReady) {
            AppIronSource.getInstance()
                .showInterstitial(this@LanguageActivity, object : AdCallback() {
                    override fun onAdClosed() {
                        super.onAdClosed()
                        val intent = Intent(this@LanguageActivity, IntroScreenActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                })
        } else {
            val intent = Intent(this, IntroScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadNativeLanguage() {
        try {
            Admod.getInstance()
                .loadNativeAd(this, getString(R.string.native_language), object : NativeCallback {
                    override fun onNativeAdLoaded(nativeAd: NativeAd?) {
                        val adView = LayoutInflater.from(this@LanguageActivity)
                            .inflate(R.layout.layout_native_language, null) as NativeAdView
                        fr_ads.removeAllViews()
                        fr_ads.addView(adView)
                        Admod.getInstance().pushAdsToViewCustom(nativeAd, adView)
                    }

                    override fun onAdFailedToLoad() {

                    }

                })
        } catch (e: Exception) {
            e.printStackTrace()
            fr_ads.removeAllViews()
        }

        if (!CheckInternet(this).haveNetworkConnection()) {
            fr_ads.removeAllViews()
        }
    }

}