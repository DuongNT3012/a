package com.ntdapp.document.viewer.reader.officereader.pdfreader

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.amazic.ads.callback.InterCallback
import com.amazic.ads.util.Admod
import com.ntdapp.document.viewer.reader.officereader.pdfreader.language.LanguageActivity
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.CheckInternet
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.Constants
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.SystemUtil
import com.google.android.gms.ads.LoadAdError
import com.ntdapp.document.viewer.reader.officereader.pdfreader.activity.HomeScreenActivity
import com.ntdapp.document.viewer.reader.officereader.pdfreader.activity.OfficeViewerScreenActivity
import com.ntdapp.document.viewer.reader.officereader.pdfreader.activity.PdfViewerScreenActivity
import com.ntdapp.document.viewer.reader.officereader.pdfreader.activity.SharePrefUtils


class SplashScreenActivity : AppCompatActivity() {

    companion object {
        var TimeEndAds = 0L
    }


    private var mHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data
        if (uri != null) {
            val file = SystemUtil.fileFromContentUri(this@SplashScreenActivity, uri)
            Log.d(
                "TAG",
                "onCreateUri: $file"
            )
            /*Log.d(
                "TAG",
                "onCreateUri: $uri \n ${Constants.getPathFromUri(this@SplashScreenActivity, uri)}"
            )*/
            var intent: Intent? = null
            var path: String? = null
            try {
                path = Constants.getPathFromUri(this@SplashScreenActivity, uri)
            } catch (e: Exception) {
                path = file.absolutePath
            }
            if (path?.contains(".pdf") == true) {
                /*if (CheckInternet(this@SplashScreenActivity).haveNetworkConnection()) {
                    Admod.getInstance()
                        .loadSplashInterAds(
                            this@SplashScreenActivity,
                            getString(R.string.inter_splash),
                            25000,
                            5000,
                            object : InterCallback() {
                                override fun onAdClosed() {
                                    intent = Intent(
                                        this@SplashScreenActivity,
                                        PdfViewerScreenActivity::class.java
                                    )
                                    intent?.putExtra(Constants.URL, path)
                                    intent?.putExtra("fromSplash", true)
                                    startActivity(intent)
                                    finish()
                                }

                                override fun onAdFailedToLoad(i: LoadAdError) {
                                    super.onAdFailedToLoad(i)
                                    onAdClosed()
                                }
                            })
                } else {*/
                    intent = Intent(this@SplashScreenActivity, PdfViewerScreenActivity::class.java)
                    intent?.putExtra(Constants.URL, path)
                    intent?.putExtra("fromSplash", true)
                    Handler().postDelayed(Runnable {
                        startActivity(intent)
                        finish()
                    }, 2000)
                /*}*/
            } else {
                /*if (CheckInternet(this@SplashScreenActivity).haveNetworkConnection()) {
                    Admod.getInstance()
                        .loadSplashInterAds(
                            this@SplashScreenActivity,
                            getString(R.string.inter_splash),
                            25000,
                            5000,
                            object : InterCallback() {
                                override fun onAdClosed() {
                                    intent = Intent(
                                        this@SplashScreenActivity,
                                        OfficeViewerScreenActivity::class.java
                                    )
                                    intent?.putExtra(Constants.URL, path)
                                    intent?.putExtra("fromSplash", true)
                                    startActivity(intent)
                                    finish()
                                }

                                override fun onAdFailedToLoad(i: LoadAdError) {
                                    super.onAdFailedToLoad(i)
                                    onAdClosed()
                                }
                            })
                } else {*/
                    intent =
                        Intent(this@SplashScreenActivity, OfficeViewerScreenActivity::class.java)
                    intent?.putExtra(Constants.URL, path)
                    intent?.putExtra("fromSplash", true)
                    Handler().postDelayed(Runnable {
                        startActivity(intent)
                        finish()
                    }, 2000)
                /*}*/
            }
        } else {
            Admod.getInstance().setOpenActivityAfterShowInterAds(false)
            /*if (CheckInternet(this@SplashScreenActivity).haveNetworkConnection()) {
                Admod.getInstance()
                    .loadSplashInterAds(
                        this@SplashScreenActivity,
                        getString(R.string.inter_splash),
                        25000,
                        5000,
                        object : InterCallback() {
                            override fun onAdClosed() {
                                loadAds()
                            }

                            override fun onAdFailedToLoad(i: LoadAdError) {
                                super.onAdFailedToLoad(i)
                                loadAds()
                            }
                        })
            } else {*/
                Handler().postDelayed(Runnable {
                    loadAds()
                }, 2000)
            /*}*/
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            if (window != null) {
                val background =
                    resources.getDrawable(R.drawable.bg_gradient_toolbar) //bg_gradient is your gradient.
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = resources.getColor(android.R.color.transparent)
                window.setBackgroundDrawable(background)
            }
        }
        setContentView(R.layout.activity_splash)
        SharePrefUtils.increaseCountOpenApp(this)
    }

    private fun loadAds() {
        gotoNextScreen()
    }

    private fun gotoNextScreen() {
        val sharedPreferences = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        val openLanguage = sharedPreferences.getBoolean("openLanguage", false)
        var intent: Intent? = null
        if (openLanguage) {
            intent = Intent(this@SplashScreenActivity, HomeScreenActivity::class.java)
        } else {
            intent = Intent(this@SplashScreenActivity, LanguageActivity::class.java)
            val editor: SharedPreferences.Editor =
                getSharedPreferences("MY_PRE", Context.MODE_PRIVATE).edit()
            editor.putBoolean("openLanguage", true)
            editor.apply()
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler = null
    }
}