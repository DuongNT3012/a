package com.document.officereader.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.*
import android.os.Build.VERSION
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.amazic.ads.callback.NativeCallback
import com.amazic.ads.util.Admod
import com.amazic.ads.util.AppOpenManager
import com.document.allreader.allofficefilereader.fc.openxml4j.opc.PackagingURIHelper
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.OnCompleteListener
import com.google.android.play.core.tasks.Task
import com.document.officereader.R
import com.document.officereader.language_nav.LanguageNavActivity
import com.document.officereader.util.Constants
import com.document.officereader.util.Utils.getAllFileFavorites
import com.document.officereader.util.Utils.getAllFileRecents
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.dialog_exit.view.*
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat


class HomeScreenActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION = 202
    private var mDirFile: File? = null

    private var mType = Constants.ALL
    private var mIsLoadFinish = true
    private var isCheckResume = false
    private var checkAdsResume = false

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
        setContentView(R.layout.activity_home)
        initData()
        checkPermission()
        handleEvents()
        setNav()
        //ads
        loadadsNavHome()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadAllFile()
                } else {
                    Toast.makeText(applicationContext, "You not use feature!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun initData() {
        mDirFile = File(Environment.getExternalStorageDirectory().toString())
        displayingStorageOfDevice()
    }

    @SuppressLint("WrongConstant")
    private fun handleEvents() {
        btnFind.setOnClickListener {
//            val intent = Intent(this, SearchScreenActivity::class.java)
//            intent.putExtra(Constants.TYPE, Constants.ALL)
//            startActivity(intent)
            mType = Constants.ALL
            showAds()
        }

        btnChooseFile.setOnClickListener {
            val intent = Intent("android.intent.action.OPEN_DOCUMENT")
            intent.addCategory("android.intent.category.OPENABLE")
            intent.type = "*/*"
            intent.putExtra(
                "android.intent.extra.MIME_TYPES",
                arrayOf(
                    "application/pdf",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/vnd.ms-powerpoint",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "text/plain"
                )
            )
            startActivityForResult(intent, 10)

        }
        btnRefresh.setOnClickListener {
            if (mIsLoadFinish) {
                mFileRecents.clear()
                mFileWords.clear()
                mFilePdfs.clear()
                mFileAlls.clear()
                mFileExcels.clear()
                mFilePowerpoints.clear()
                mFileTexts.clear()
                mFileScreenshots.clear()
                mFileFavorutes.clear()
                loadAllFile()
            }
        }

        btnAll.setOnClickListener {
            mType = Constants.ALL
            showAds()
        }

        btnPdf.setOnClickListener {
            mType = Constants.PDF
            showAds()
        }

        btnWord.setOnClickListener {
            mType = Constants.WORD
            showAds()
        }

        btnPowerpoint.setOnClickListener {
            mType = Constants.POWER_POINT
            showAds()
        }

        btnText.setOnClickListener {
            mType = Constants.TEXT
            showAds()
        }

        btnExcel.setOnClickListener {
            mType = Constants.EXCEL
            showAds()
        }

        btnScreenshot.setOnClickListener {
            mType = Constants.IMAGE
            showAds()
        }

        btnRecent.setOnClickListener {
            mType = Constants.RECENT
            showAds()
        }

        btnFavorite.setOnClickListener {
            mType = Constants.FAVORITE
            showAds()
        }


        llFeedback.setOnClickListener {
            showFeedback()
            draw_home.closeDrawer(GravityCompat.START)
        }
        llRate.setOnClickListener {
            showDialogRate(false)
            draw_home.closeDrawer(GravityCompat.START)
        }
    }

    private fun checkPermission() {
        if (VERSION.SDK_INT >= 23) {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    try {
                        checkAdsResume = true
                        AppOpenManager.getInstance()
                            .disableAppResumeWithActivity(HomeScreenActivity::class.java)

                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        intent.addCategory("android.intent.category.DEFAULT")
                        intent.data =
                            Uri.parse(String.format("package:%s", applicationContext.packageName))
                        startActivityForResult(intent, 2296)
                    } catch (e: Exception) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                        startActivityForResult(intent, 2296)
                    }
                } else {
                    loadAllFile()
                }
            } else {
                checkAdsResume = true
                AppOpenManager.getInstance()
                    .disableAppResumeWithActivity(HomeScreenActivity::class.java)
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@HomeScreenActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_PERMISSION
                    )
                } else {
                    loadAllFile()
                }
            }
        } else {
            loadAllFile()
        }
    }

    private fun loadAllFile() {
        mFileRecents.clear()
        mFileWords.clear()
        mFilePdfs.clear()
        mFileAlls.clear()
        mFileExcels.clear()
        mFilePowerpoints.clear()
        mFileTexts.clear()
        mFileScreenshots.clear()
        mFileFavorutes.clear()
        LoadAllFile().execute()
        allFileScreenShot
        mFileFavorutes.addAll(getAllFileFavorites(this))
        mFileRecents.addAll(getAllFileRecents(this))
        setNumberFile()
    }

    @SuppressLint("SetTextI18n")
    private fun setNumberFile() {
        txtNumberPdf.text = "" + mFilePdfs.size + " " + resources.getString(R.string.Files)
        txtNumberExcel.text = "" + mFileExcels.size + " " + resources.getString(R.string.Files)
        txtNumberWord.text = "" + mFileWords.size + " " + resources.getString(R.string.Files)
        txtNumberPowerpoint.text =
            "" + mFilePowerpoints.size + " " + resources.getString(R.string.Files)
        txtNumberText.text = "" + mFileTexts.size + " " + resources.getString(R.string.Files)
        txtNumberScreenShot.text =
            "" + mFileScreenshots.size + " " + resources.getString(R.string.Files)
        txtNumberAll.text = "" + mFileAlls.size + " " + resources.getString(R.string.Files)
        txtNumberFavourite.text =
            "" + mFileFavorutes.size + " " + resources.getString(R.string.Files)
        txtNumberRecent.text = "" + mFileRecents.size + " " + resources.getString(R.string.Files)
    }

    override fun onResume() {
        super.onResume()

        if (isCheckResume == true) {
            AppOpenManager.getInstance().enableAppResumeWithActivity(HomeScreenActivity::class.java)
        }
        if (checkAdsResume == true) {
            isCheckResume = true
        }

        setNumberFile()

    }

    private fun getAllFile(mDirFile: File?) {
        try {
            val listFile = mDirFile!!.listFiles()
            if (listFile != null && listFile.isNotEmpty()) {
                for (i in listFile.indices) {
                    if (listFile[i].isDirectory) {
                        getAllFile(listFile[i])
                    } else {
                        val name = listFile[i].name
                        if (name.endsWith(".pdf")) {
                            mFilePdfs.add(listFile[i])
                            mFileAlls.add(listFile[i])
                        } else if (name.endsWith(".doc") || name.endsWith(".docx")) {
                            mFileWords.add(listFile[i])
                            mFileAlls.add(listFile[i])
                        } else if (name.endsWith(".xlsx") || name.endsWith(".xls")) {
                            mFileExcels.add(listFile[i])
                            mFileAlls.add(listFile[i])
                        } else if (name.endsWith(".pptx") || name.endsWith(".ppt")) {
                            mFilePowerpoints.add(listFile[i])
                            mFileAlls.add(listFile[i])
                        } else if (name.endsWith(".txt")) {
                            mFileTexts.add(listFile[i])
                            mFileAlls.add(listFile[i])
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val allFileScreenShot: Unit
        private get() {
            try {
                val path =
                    applicationContext.getExternalFilesDir(null)!!.absolutePath + "/AllDocument"
                val file = File(path)
                val listFile = file.listFiles()
                if (listFile != null) {
                    for (i in listFile.indices) {
                        val fileImage = listFile[i]
                        if (fileImage != null && fileImage.isFile) {
                            if (fileImage.name.endsWith(".png") || fileImage.name.endsWith(".jpg")) {
                                mFileScreenshots.add(fileImage)
                                mFileAlls.add(fileImage)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private fun gotoNextScreen() {
        val intent = Intent(this@HomeScreenActivity, ListFileOfficeScreenActivity::class.java)
        intent.putExtra(Constants.TYPE, mType)
        startActivityForResult(intent, 202)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 202) {
            loadAllFile()
        } else if (requestCode == 2296) {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    loadAllFile()
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else if (requestCode == 10 && data != null) {
            val uri = data.data
            val copyFileToInternalStorage: String =
                copyFileToInternalStorage(uri!!, "myFileName").toString()
            if (copyFileToInternalStorage != "") {
                val file = File(copyFileToInternalStorage)
                val name = file.name
                if (name.endsWith(".pdf")) {
                    val intent = Intent(this, PdfViewerScreenActivity::class.java)
                    intent.putExtra(Constants.URL, file.path)
                    startActivity(intent)
                } else if (name.endsWith(".doc") || name.endsWith(".docx")) {
                    val intent = Intent(this, OfficeViewerScreenActivity::class.java)
                    intent.putExtra(Constants.URL, file.path)
                    startActivity(intent)
                } else if (name.endsWith(".xlsx") || name.endsWith(".xls")) {
                    val intent = Intent(this, OfficeViewerScreenActivity::class.java)
                    intent.putExtra(Constants.URL, file.path)
                    startActivity(intent)
                } else if (name.endsWith(".pptx") || name.endsWith(".ppt")) {
                    val intent = Intent(this, OfficeViewerScreenActivity::class.java)
                    intent.putExtra(Constants.URL, file.path)
                    startActivity(intent)
                } else if (name.endsWith(".txt")) {
                    val intent = Intent(this, OfficeViewerScreenActivity::class.java)
                    intent.putExtra(Constants.URL, file.path)
                    startActivity(intent)
                } else if (name.endsWith(".jpg") || name.endsWith(".png")) {
                    val intent = Intent(this, ShotViewerScrenActivity::class.java)
                    intent.putExtra(Constants.URL, file.path)
                    startActivity(intent)
                }
            }
        }
    }

    private fun showAds() {
        gotoNextScreen()
    }

    inner class LoadAllFile : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            mIsLoadFinish = false
        }

        override fun onPostExecute(unused: Void?) {
            super.onPostExecute(unused)
            mIsLoadFinish = true
            setNumberFile()
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            getAllFile(mDirFile)
            return null
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mDirFile = null

    }

    companion object {
        var mFilePdfs = ArrayList<File>()
        var mFileWords = ArrayList<File>()
        var mFilePowerpoints = ArrayList<File>()
        var mFileExcels = ArrayList<File>()
        var mFileTexts = ArrayList<File>()
        var mFileScreenshots = ArrayList<File>()
        var mFileAlls = ArrayList<File>()
        var mFileFavorutes = ArrayList<File>()
        var mFileRecents = ArrayList<File>()
    }

    private fun copyFileToInternalStorage(uri: Uri, str: String): String? {
        val file: File
        val query = contentResolver.query(uri, arrayOf("_display_name", "_size"), null, null, null)
        val columnIndex = query!!.getColumnIndex("_display_name")
        val columnIndex2 = query.getColumnIndex("_size")
        query.moveToFirst()
        val string = query.getString(columnIndex)
        java.lang.Long.toString(query.getLong(columnIndex2))
        if (str != "") {
            val file2: File =
                File(filesDir.path + PackagingURIHelper.FORWARD_SLASH_STRING.toString() + str)
            if (!file2.exists()) {
                file2.mkdir()
            }
            file =
                File(filesDir.path + PackagingURIHelper.FORWARD_SLASH_STRING.toString() + str + PackagingURIHelper.FORWARD_SLASH_STRING.toString() + string)
        } else {
            file = File(filesDir.path + PackagingURIHelper.FORWARD_SLASH_STRING.toString() + string)
        }
        try {
            val openInputStream = contentResolver.openInputStream(uri)
            val fileOutputStream = FileOutputStream(file)
            val bArr = ByteArray(1024)
            while (true) {
                val read = openInputStream!!.read(bArr)
                if (read == -1) {
                    break
                }
                fileOutputStream.write(bArr, 0, read)
            }
            openInputStream.close()
            fileOutputStream.close()
        } catch (e: Exception) {
            Log.e("Exception", e.message!!)
        }
        return file.path
    }

    private fun displayingStorageOfDevice() {
        val statFs = StatFs(Environment.getDataDirectory().absolutePath)
        val internalStorageSpace: Float =
            statFs.blockCount.toFloat() * statFs.blockSize.toFloat() / 1.07374182E9f
        val internalUsedSpace: Float =
            statFs.availableBlocks.toFloat() * statFs.blockSize.toFloat() / 1.07374182E9f
        val internalFreeSpace: Float =
            statFs.blockCount.toFloat() * statFs.blockSize.toFloat() / 1.07374182E9f - statFs.availableBlocks
                .toFloat() * statFs.blockSize.toFloat() / 1.07374182E9f
        val decimalFormat = DecimalFormat("#.##")
        val string = resources.getString(R.string.total_space)
        val string2 = resources.getString(R.string.free_space)
        txtFreeStore.text = string2 + decimalFormat.format(internalFreeSpace.toDouble()) + " GB"
        txtTotalStore.text = string + decimalFormat.format(internalStorageSpace.toDouble()) + " GB"
        this.pbStore.max = internalStorageSpace.toInt()
        this.pbStore.progress = internalUsedSpace.toInt()
    }

    private fun setNav() {
        main_navigation_menu.bringToFront()
        img_nav.setOnClickListener {
            if (draw_home.isDrawerVisible(GravityCompat.START)) {
                draw_home.closeDrawer(GravityCompat.START)
            } else {
                draw_home.openDrawer(GravityCompat.START)
            }

        }

        share_nav.setOnClickListener {
            isCheckResume = true
            AppOpenManager.getInstance()
                .disableAppResumeWithActivity(HomeScreenActivity::class.java)
            draw_home.closeDrawer(GravityCompat.START)
            shareApp()
        }

        abount_nav.setOnClickListener {
            draw_home.closeDrawer(GravityCompat.START)
            startActivity(Intent(this@HomeScreenActivity, AboutScreenActivity::class.java))
        }

        llLanguage.setOnClickListener {
            startActivity(Intent(this@HomeScreenActivity, LanguageNavActivity::class.java))
            finish()
        }
        llMoreApp.setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/developer?id=NTDApp")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }
    }

    private fun shareApp() {
        val intentShare = Intent(Intent.ACTION_SEND)
        intentShare.type = "text/plain"
        intentShare.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        intentShare.putExtra(
            Intent.EXTRA_TEXT, "Download application :"
                    + "https://play.google.com/store/apps/details?id=" + packageName
        )
        startActivity(Intent.createChooser(intentShare, "Share with"))
    }

    override fun onBackPressed() {
        if (!SharePrefUtils.isRated(this)) {
            val count: Int = SharePrefUtils.getCountOpenApp(this)
            Log.e("TAG", "onBackPressed: $count")
            if (count == 1 || count == 2 || count == 4 || count == 5 || count == 7 || count == 9) {
                showDialogRate(true)
                return
            } else {
                showDialogExit()
            }
        } else if (SharePrefUtils.isRated(this)) {
            showDialogExit()
        }
    }

    private fun showDialogRate(isExit: Boolean) {
        //home_native_ads.visibility = View.GONE
        val dialog = Dialog(this@HomeScreenActivity)
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_rate, null, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val w = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val h = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.setLayout(w, h)
        val btnNotNow = view.findViewById<TextView>(R.id.btn_notnow)
        val btnRate = view.findViewById<TextView>(R.id.btn_rate)
        val rtb = view.findViewById<RatingBar>(R.id.rtb)
        val icRate = view.findViewById<ImageView>(R.id.icRate)


        rtb.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar, rating, fromUser -> // Do what you want
                when (rating) {
                    0.0f -> {
                        // Glide.with(this).load(R.drawable.ic_star_0).into(icRate);
                        icRate.setImageResource(R.drawable.ic_star_0)
                    }
                    1.0f -> {
                        icRate.setImageResource(R.drawable.ic_star_1)
                    }
                    2.0f -> {
                        icRate.setImageResource(R.drawable.ic_star_2)
                    }
                    3.0f -> {
                        icRate.setImageResource(R.drawable.ic_star_3)
                    }
                    4.0f -> {
                        icRate.setImageResource(R.drawable.ic_star_4)
                    }
                    5.0f -> {
                        icRate.setImageResource(R.drawable.ic_star_5)
                    }
                    else -> {
                        icRate.setImageResource(R.drawable.ic_star_0)
                    }
                }
            }

        btnNotNow.setOnClickListener {
            if (isExit) {
                finishAffinity()
            } else {
                home_native_ads.visibility = View.VISIBLE
                dialog.dismiss()
            }
        }
        btnRate.setOnClickListener(View.OnClickListener {

            if (rtb.rating == 0f) {
                Toast.makeText(this@HomeScreenActivity, "Please feedback", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            if (rtb.rating <= 3.0) {
                isCheckResume = true
                AppOpenManager.getInstance()
                    .disableAppResumeWithActivity(HomeScreenActivity::class.java)
                SharePrefUtils.forceRated(this@HomeScreenActivity)
                val uriText =
                    """
                    mailto:nguyenduong30121999@gmail.com?subject=Feedback OfficeReader &body=Rate : ${rtb.rating}
                    Content: 
                    """.trimIndent()
                val uri = Uri.parse(uriText)
                val sendIntent = Intent(Intent.ACTION_SENDTO)
                sendIntent.data = uri
                startActivity(Intent.createChooser(sendIntent, "Send Email"))
                if (isExit) {
                    finishAffinity()
                } else {
                    dialog.dismiss()
                    home_native_ads.visibility = View.VISIBLE
                }
            } else {
                SharePrefUtils.forceRated(this@HomeScreenActivity)
                reviewApp(this@HomeScreenActivity, isExit)
                dialog.dismiss()
                home_native_ads.visibility = View.VISIBLE
            }
        })
        dialog.show()
    }

    fun reviewApp(context: Context?, isExit: Boolean) {
        val manager: ReviewManager = ReviewManagerFactory.create(context!!)
        val request: Task<ReviewInfo> =
            manager.requestReviewFlow()
        request.addOnCompleteListener(
            OnCompleteListener<ReviewInfo> { task: Task<ReviewInfo?> ->
                if (task.isSuccessful()) {
                    // We can get the ReviewInfo object
                    val reviewInfo: ReviewInfo = task.getResult()
                    Log.e("ReviewInfo", "" + reviewInfo.toString())
                    val flow: Task<Void> =
                        manager.launchReviewFlow((context as Activity?)!!, reviewInfo)
                    flow.addOnCompleteListener(OnCompleteListener<Void> { task2: Task<Void?> ->
                        Log.e("ReviewSucces", "" + task2.toString())
                        if (isExit) {
                            finishAffinity()
                        }
                    })
                } else {
                    if (isExit)
                    // There was some problem, continue regardless of the result.
                        Log.e("ReviewError", "" + task.getException().toString())
                    finishAffinity()
                }
            }
        )
    }

    fun showFeedback() {
        //home_native_ads.visibility = View.GONE
        val dialog = Dialog(this@HomeScreenActivity)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_feedback, null, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val w = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val h = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.setLayout(w, h)

        val btnDiscard = view.findViewById<TextView>(R.id.btnDiscard)
        val btnSend = view.findViewById<TextView>(R.id.btnSend)
        val content = view.findViewById<EditText>(R.id.content)

        btnDiscard.setOnClickListener {
            dialog.dismiss()
            home_native_ads.visibility = View.VISIBLE
        }
        btnSend.setOnClickListener {
            isCheckResume = true
            AppOpenManager.getInstance()
                .disableAppResumeWithActivity(HomeScreenActivity::class.java)
            val uriText =
                """
                    mailto:nguyenduong30121999@gmail.com?subject=Feedback OfficeReader &body=Content : ${content.text}
                    """.trimIndent()
            val uri = Uri.parse(uriText)
            val sendIntent = Intent(Intent.ACTION_SENDTO)
            sendIntent.data = uri
            startActivity(Intent.createChooser(sendIntent, "Send Email"))
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDialogExit() {
        //home_native_ads.visibility = View.GONE
        val dialog = Dialog(this@HomeScreenActivity)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_exit, null, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        loadadsNavExit(view)

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val w = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val h = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.setLayout(w, h)

        val btnCancel = view.findViewById<ImageView>(R.id.btnCancel)
        val btnQuit = view.findViewById<ImageView>(R.id.btnQuit)

        btnCancel.setOnClickListener {
            dialog.dismiss()
            home_native_ads.visibility = View.VISIBLE
        }
        btnQuit.setOnClickListener {
            finishAffinity()
        }
        dialog.show()
    }

    private fun loadadsNavHome() {
        home_native_ads.visibility = View.VISIBLE
        Admod.getInstance()
            .loadNativeAd(this, getString(R.string.native_home), object : NativeCallback {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    val adView = LayoutInflater.from(this@HomeScreenActivity)
                        .inflate(R.layout.native_custom, null) as NativeAdView
                    home_native_ads.removeAllViews()
                    home_native_ads.addView(adView)
                    Admod.getInstance().pushAdsToViewCustom(nativeAd, adView)
                }

                override fun onAdFailedToLoad() {
                    home_native_ads.visibility = View.GONE
                }
            })

    }

    private fun loadadsNavExit(v: View) {
        v.exit_native_ads.visibility = View.VISIBLE
        Admod.getInstance()
            .loadNativeAd(this, getString(R.string.native_exit), object : NativeCallback {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    val adView = LayoutInflater.from(this@HomeScreenActivity)
                        .inflate(R.layout.native_custom, null) as NativeAdView
                    v.exit_native_ads.removeAllViews()
                    v.exit_native_ads.addView(adView)
                    Admod.getInstance().pushAdsToViewCustom(nativeAd, adView)
                }

                override fun onAdFailedToLoad() {
                    v.exit_native_ads.visibility = View.GONE
                }
            })

    }
}