package com.document.officereader.language_nav

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amazic.ads.callback.NativeCallback
import com.amazic.ads.util.Admod
import com.document.officereader.R
import com.document.officereader.activity.HomeScreenActivity
import com.document.officereader.activity.IntroScreenActivity
import com.document.officereader.language.LanguageModel
import com.document.officereader.language.adapter.LanguageAdapterMain
import com.document.officereader.language_nav.adapter.LanguageAdapterNav
import com.document.officereader.util.CheckInternet
import com.document.officereader.util.SystemUtil
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.android.synthetic.main.activity_language.*
import kotlinx.android.synthetic.main.activity_language.rcl_language
import kotlinx.android.synthetic.main.activity_language_nav.*

class LanguageNavActivity : AppCompatActivity(),
    IClickLanguageNav {
    private var adapter: LanguageAdapterNav? = null
    private var modelNav: LanguageModelNav = LanguageModelNav()
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_nav)

        // load and show ads native language
        sharedPreferences = getSharedPreferences("MY_PRE", MODE_PRIVATE)

        adapter = LanguageAdapterNav(this, setLanguageDefault(), this)
        rcl_language.adapter = adapter

        iv_back.setOnClickListener {
            startSubOrTur()
            finish()
        }
    }

    override fun onClick(data: LanguageModelNav) {
        adapter?.setSelectLanguage(data)
        modelNav = data

        if (modelNav != null) {
            SystemUtil.setPreLanguage(this@LanguageNavActivity, modelNav.isoLanguage)
        }
        /*val editor = getSharedPreferences("MY_PRE", MODE_PRIVATE).edit()
        editor.putBoolean("nativeLanguage", true)
        editor.apply()*/
        SystemUtil.setLocale(this)
        startSubOrTur()
        finish()
    }

    private fun setLanguageDefault(): List<LanguageModelNav>? {
        val lists: MutableList<LanguageModelNav> = ArrayList()
        val key: String = SystemUtil.getPreLanguage(this)
        lists.add(LanguageModelNav("English", "en", false, R.drawable.ic_english))
        lists.add(LanguageModelNav("Korean", "ko", false, R.drawable.ic_english))
        lists.add(LanguageModelNav("Japanese", "ja", false, R.drawable.ic_english))
        lists.add(LanguageModelNav("French", "fr", false))
        lists.add(LanguageModelNav("Hindi", "hi", false))
        lists.add(LanguageModelNav("Portuguese", "pt", false, R.drawable.ic_portuguese))
        lists.add(LanguageModelNav("Spanish", "es", false, R.drawable.ic_spanish))
        lists.add(LanguageModelNav("Indonesian", "in", false, R.drawable.ic_spanish))
        lists.add(LanguageModelNav("Malay", "ms", false, R.drawable.ic_spanish))
        lists.add(LanguageModelNav("Philippines", "phi", false, R.drawable.ic_spanish))
        lists.add(LanguageModelNav("Chinese", "zh", false, R.drawable.ic_spanish))
        lists.add(LanguageModelNav("German", "de", false))
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
        if (modelNav != null) {
            SystemUtil.setPreLanguage(this@LanguageNavActivity, modelNav.isoLanguage)
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
        showActivity(HomeScreenActivity::class.java, null)
    }

    private fun showActivity(activity: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, activity)
        intent.putExtras(bundle ?: Bundle())
        startActivity(intent)
    }
}