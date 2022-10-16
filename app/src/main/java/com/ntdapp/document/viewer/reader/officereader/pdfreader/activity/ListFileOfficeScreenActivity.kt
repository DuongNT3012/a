package com.ntdapp.document.viewer.reader.officereader.pdfreader.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.amazic.ads.callback.InterCallback
import com.amazic.ads.util.Admod
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.ntdapp.document.viewer.reader.officereader.pdfreader.R
import com.ntdapp.document.viewer.reader.officereader.pdfreader.ShotViewerScrenActivity
import com.ntdapp.document.viewer.reader.officereader.pdfreader.adapter.ListFileOfficeAdapter
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.Constants
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.Utils
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.Utils.getAllFileFavorites
import kotlinx.android.synthetic.main.activity_list_office.*
import kotlinx.android.synthetic.main.activity_list_office.btnBack
import kotlinx.android.synthetic.main.activity_list_office.edtSearch
import kotlinx.android.synthetic.main.activity_list_office.txtTitle
import kotlinx.android.synthetic.main.layout_dialog_order.*
import java.io.File


class ListFileOfficeScreenActivity : AppCompatActivity(), ListFileOfficeAdapter.OnItemListener {

    private var mType = Constants.ALL

    private var mFiles = ArrayList<File>()
    private var mFileTemps = ArrayList<File>()

    private lateinit var mListFileOfficeAdapter: ListFileOfficeAdapter

    private lateinit var mDialog: Dialog

    private lateinit var mIntent: Intent


    private var mTypeSort = 2

    private var mIsLoadFinish = true

    private var mTimeClick = 0
    private var mInterstitialAd: InterstitialAd? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setContentView(R.layout.activity_list_office)
        //loadAds()
        initData()
        handleEvents()
        //ads
        loadAdsInter()
        Admod.getInstance().loadBanner(this@ListFileOfficeScreenActivity, getString(R.string.banner_all));
    }

    override fun onItemClick(name: String, url: String) {
        Utils.setFileRecent(url, this)
        if (name.endsWith(".pdf")) {
            mIntent = Intent(this, PdfViewerScreenActivity::class.java)
            mIntent.putExtra(Constants.URL, url)
        } else if (name.endsWith(".doc") || name.endsWith(".docx")) {
            mIntent = Intent(this, OfficeViewerScreenActivity::class.java)
            mIntent.putExtra(Constants.URL, url)
        } else if (name.endsWith(".xlsx") || name.endsWith(".xls")) {
            mIntent = Intent(this, OfficeViewerScreenActivity::class.java)
            mIntent.putExtra(Constants.URL, url)
        } else if (name.endsWith(".pptx") || name.endsWith(".ppt")) {
            mIntent = Intent(this, OfficeViewerScreenActivity::class.java)
            mIntent.putExtra(Constants.URL, url)
        } else if (name.endsWith(".png") || name.endsWith(".jpg")) {
            mIntent = Intent(this, ShotViewerScrenActivity::class.java)
            mIntent.putExtra(Constants.URL, url)
        } else if (name.endsWith(".txt")) {
            mIntent = Intent(this, OfficeViewerScreenActivity::class.java)
            mIntent.putExtra(Constants.URL, url)
        }
        showAds()
    }


    private fun initData() {
        val type = intent.getStringExtra(Constants.TYPE)
        mType = if (TextUtils.isEmpty(type)) {
            Constants.ALL
        } else {
            type!!
        }



        mFiles.addAll(getListFileByType())
        mFileTemps.addAll(mFiles)

        if (mFiles.isEmpty()) {
            mFiles.clear()
            mListFileOfficeAdapter = ListFileOfficeAdapter(mFiles, this)
            ll_empty.visibility = View.VISIBLE
        } else {
            mListFileOfficeAdapter = ListFileOfficeAdapter(mFiles, this)
            rvListFile.adapter = mListFileOfficeAdapter
            ll_empty.visibility = View.GONE
        }

        SortFile().execute()



        mDialog = Dialog(this)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.layout_dialog_order)
        mDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        edtSearch.showKeyboard()
    }

    fun EditText.showKeyboard() {
        post {
            requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun handleEvents() {
        btnRefresh.setOnClickListener {
            val currentTime = System.currentTimeMillis() / 1000
            val dTime = currentTime - mTimeClick
            if (mIsLoadFinish && dTime >= 2) {
                mTimeClick = currentTime.toInt()
                refreshFile()
            }
        }
        btnBack.setOnClickListener {
            onBackPressed()
        }
//        edtSearch.setOnClickListener {
//            val intent = Intent(this, SearchScreenActivity::class.java)
//            intent.putExtra(Constants.TYPE, mType)
//            startActivity(intent)
//        }
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mFiles.clear()
                if (TextUtils.isEmpty(p0.toString().trim())) {

                    mFiles.addAll(mFileTemps)
                } else {

                    mFiles.addAll(mFileTemps.filter { file ->
                        file.name.toLowerCase().indexOf(p0.toString().toLowerCase()) != -1
                    })
                }

                if (mFiles.isEmpty()) {
                    ll_empty_seach.visibility = View.VISIBLE
                    ll_empty.visibility = View.GONE
                    mFiles.clear()
                    mListFileOfficeAdapter.notifyDataSetChanged()
                    rvListFile.adapter = mListFileOfficeAdapter
                } else {
                    ll_empty_seach.visibility = View.GONE
                    ll_empty.visibility = View.GONE
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                refreshFile()
            }

        })
        btnOrder.setOnClickListener {
            mDialog.show()
        }
        mDialog.layout_order_by_accessed_time.setOnClickListener {

            mTypeSort = 3
            SortFile().execute()
            mDialog.dismiss()
        }
        mDialog.layout_order_by_created_time.setOnClickListener {

            mTypeSort = 2
            SortFile().execute()
            mDialog.dismiss()
        }
        mDialog.layout_order_by_name.setOnClickListener {

            mTypeSort = 0
            SortFile().execute()
            mDialog.dismiss()
        }
        mDialog.layout_order_by_name_za.setOnClickListener {

            mTypeSort = 1
            SortFile().execute()
            mDialog.dismiss()
        }
    }

    private fun refreshFile() {
        HomeScreenActivity.mFileRecents.clear()
        HomeScreenActivity.mFileWords.clear()
        HomeScreenActivity.mFilePdfs.clear()
        HomeScreenActivity.mFileAlls.clear()
        HomeScreenActivity.mFileExcels.clear()
        HomeScreenActivity.mFilePowerpoints.clear()
        HomeScreenActivity.mFileTexts.clear()
        HomeScreenActivity.mFileScreenshots.clear()
        HomeScreenActivity.mFileFavorutes.clear()
        HomeScreenActivity.mFileFavorutes.addAll(getAllFileFavorites(this))
        LoadAllFile().execute()
    }


    inner class LoadAllFile :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            getAllFile(File(Environment.getExternalStorageDirectory().toString()))
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
            mIsLoadFinish = false
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            mIsLoadFinish = true
            mListFileOfficeAdapter.notifyDataSetChanged()
        }

        private fun getAllFile(dir: File) {
            val listFile = dir.listFiles()
            if (listFile != null && listFile.size > 0) {
                for (i in listFile.indices) {
                    if (listFile[i].isDirectory) {
                        getAllFile(listFile[i])
                    } else {
                        val name = listFile[i].name
                        if (name.endsWith(".pdf")) {
                            HomeScreenActivity.mFilePdfs.add(listFile[i])
                            HomeScreenActivity.mFileAlls.add(listFile[i])
                        } else if (name.endsWith(".doc") || name.endsWith(".docx")) {
                            HomeScreenActivity.mFileWords.add(listFile[i])
                            HomeScreenActivity.mFileAlls.add(listFile[i])
                        } else if (name.endsWith(".xlsx") || name.endsWith(".xls")) {
                            HomeScreenActivity.mFileExcels.add(listFile[i])
                            HomeScreenActivity.mFileAlls.add(listFile[i])
                        } else if (name.endsWith(".pptx") || name.endsWith(".ppt")) {
                            HomeScreenActivity.mFilePowerpoints.add(listFile[i])
                            HomeScreenActivity.mFileAlls.add(listFile[i])
                        } else if (name.endsWith(".png") || name.endsWith(".jpg")) {
                            HomeScreenActivity.mFileScreenshots.add(listFile[i])
                            HomeScreenActivity.mFileAlls.add(listFile[i])
                        } else if (name.endsWith(".txt")) {
                            HomeScreenActivity.mFileTexts.add(listFile[i])
                            HomeScreenActivity.mFileAlls.add(listFile[i])
                        }
                    }
                }
            }
        }

    }

    private fun getListFileByType(): ArrayList<File> {
        when (mType) {
            Constants.PDF -> {
                txtTitle.text = "PDF"
                return HomeScreenActivity.mFilePdfs
            }
            Constants.WORD -> {
                txtTitle.text = "WORD"
                return HomeScreenActivity.mFileWords
            }
            Constants.EXCEL -> {
                txtTitle.text = "EXCEL"
                return HomeScreenActivity.mFileExcels
            }
            Constants.POWER_POINT -> {
                txtTitle.text = "POWERPOINT"
                return HomeScreenActivity.mFilePowerpoints
            }
            Constants.IMAGE -> {
                txtTitle.text = resources.getString(R.string.IMAGE)
                return HomeScreenActivity.mFileScreenshots
            }
            Constants.TEXT -> {
                txtTitle.text = resources.getString(R.string.TEXT)
                return HomeScreenActivity.mFileTexts
            }
            Constants.FAVORITE -> {
                txtTitle.text = resources.getString(R.string.BOOKMARK)
                return HomeScreenActivity.mFileFavorutes
            }
            Constants.RECENT -> {
                txtTitle.text = resources.getString(R.string.RECENT_FILES)
                return HomeScreenActivity.mFileRecents
            }
            else -> {
                txtTitle.text = resources.getString(R.string.ALL_FILES)
                return HomeScreenActivity.mFileAlls
            }
        }
    }

    private fun loadAds() {
        /*var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
                this,
                getString(R.string.ads_inters_id),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        mInterstitialAd = null
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        mInterstitialAd = interstitialAd
                        mInterstitialAd?.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        gotoNextScreen()
                                    }

                                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                        gotoNextScreen()
                                    }

                                    override fun onAdShowedFullScreenContent() {
                                        mInterstitialAd = null
                                        SplashActivity.TimeEndAds = System.currentTimeMillis()
                                    }
                                }
                    }
                })

        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest2 = AdRequest.Builder().build()
        mAdView.loadAd(adRequest2)

        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                mAdView.visibility = View.GONE
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }*/
        //  gotoNextScreen()
    }

    private fun showAds() {
        //gotoNextScreen()


        Admod.getInstance()
            .showInterAds(
                this@ListFileOfficeScreenActivity,
                mInterstitialAd,
                object : InterCallback() {
                    override fun onAdClosed() {
                        gotoNextScreen()
                        mInterstitialAd = null
                        loadAdsInter()

                    }

                    override fun onAdFailedToLoad(i: LoadAdError) {
                        onAdClosed()
                    }
                })

    }

    private fun gotoNextScreen() {
        startActivityForResult(mIntent, 202)
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 202) {
            refreshFile()
        }
    }


    inner class SortFile : AsyncTask<Void, Void, Void>() {

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            mListFileOfficeAdapter.notifyDataSetChanged()
        }

        override fun doInBackground(vararg p0: Void): Void? {
            try {

                when (mTypeSort) {
                    0 -> {
                        mFiles.sortBy { it.name.toLowerCase() }
                    }
                    1 -> {
                        mFiles.sortByDescending { it.name.toLowerCase() }
                    }
                    2 -> {
                        mFiles.sortBy { it.lastModified() * -1 }
                    }
                    3 -> {
                        mFiles.sortBy {
                            Utils.getAccessTimeFile(
                                it.absolutePath,
                                applicationContext
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }


    }

    private fun loadAdsInter() {
        Admod.getInstance().loadInterAds(
            this@ListFileOfficeScreenActivity,
            getString(R.string.inter_file),
            object : InterCallback() {
                override fun onInterstitialLoad(interstitialAd: InterstitialAd) {
                    super.onInterstitialLoad(interstitialAd)
                    mInterstitialAd = interstitialAd
                }
            })
    }

}