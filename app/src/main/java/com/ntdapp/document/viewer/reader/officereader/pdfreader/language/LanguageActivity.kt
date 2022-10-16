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
import com.ntdapp.document.viewer.reader.officereader.pdfreader.R
import com.ntdapp.document.viewer.reader.officereader.pdfreader.language.adapter.LanguageAdapterMain
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.CheckInternet
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.SystemUtil
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.ntdapp.document.viewer.reader.officereader.pdfreader.activity.IntroScreenActivity
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

        fr_ads.visibility = View.VISIBLE
        loadNativeLanguage()

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
        /*val editor = getSharedPreferences("MY_PRE", MODE_PRIVATE).edit()
        editor.putBoolean("nativeLanguage", true)
        editor.apply()*/
        SystemUtil.setLocale(this)
        startSubOrTur()
        finish()
    }

    private fun startSubOrTur() {
        /*if (Constants.remoteSub && !AppPurchase.getInstance().isPurchased(this)) {
            val bundle = Bundle()
            bundle.putString(Constants.KEY_SCREEN, Constants.KEY_LANGUAGE)
            if (Constants.verSub == "v0") {
                showActivity(SubPremiumActivity::class.java, bundle)
            } else {
                showActivity(SubVerTwoActivity::class.java, bundle)
            }
        } else {
            showActivity(TutorialActivity::class.java, null)
        }*/
        showActivity(IntroScreenActivity::class.java, null)
    }

    private fun showActivity(activity: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, activity)
        intent.putExtras(bundle ?: Bundle())
        startActivity(intent)
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