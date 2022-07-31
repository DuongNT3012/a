package com.ntdapp.document.viewer.reader.officereader.pdfreader

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.amazic.ads.util.Admod
import com.amazic.ads.util.AppOpenManager
import com.example.ads.AppIronSource
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.ntdapp.document.viewer.reader.officereader.pdfreader.BuildConfig
import com.ntdapp.document.viewer.reader.officereader.pdfreader.R
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.Constants
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.Utils
import kotlinx.android.synthetic.main.activity_screen_viewer.*
import kotlinx.android.synthetic.main.layout_dialog_gotopage.*
import kotlinx.android.synthetic.main.view_fab.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ShotViewerScrenActivity : AppCompatActivity() {

    private var outputPDF = ""
    private var pdfDocument: PdfDocument? = null
    private lateinit var mUrlImage: String
    private var mIsShowMenu = false
    private var mIsFavorite = false
    private var filename = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            if (window != null) {
                var background =
                    resources.getDrawable(R.drawable.bg_gradient_toolbar)
                //bg_gradient is your gradient.
                background.setColorFilter(
                    android.graphics.Color.parseColor("#FF9900"),
                    PorterDuff.Mode.SRC_ATOP
                )
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.parseColor("#FF9900")
                window.setBackgroundDrawable(background)
            }
        }
        setContentView(R.layout.activity_screen_viewer)
        initData()
        handleEvents()
        //ads
        //Admod.getInstance().loadBanner(this@ShotViewerScrenActivity,getString(R.string.banner_all));
    }

    override fun onStart() {
        super.onStart()
        AppIronSource.getInstance().loadBanner(this)
    }

    private fun initData() {
        getExternalFilesDir(null)?.delete()
        outputPDF = getExternalFilesDir(null)?.absolutePath + "temp.pdf"
        mUrlImage = intent.getStringExtra(Constants.URL)!!
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                Uri.fromFile(File(mUrlImage))
            )
            makePDF(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mIsFavorite = Utils.isFileFavorite(mUrlImage, this)
        if(mIsFavorite) {
            fab_favourite.setImageResource(R.drawable.option_bookmark)
        }else{
            fab_favourite.setImageResource(R.drawable.option_bookmark_unselect)
        }
        val file: File = File(mUrlImage)
        filename = file.name
        setColorToolbar()

    }

    private fun handleEvents() {
        btnBack.setOnClickListener {
            onBackPressed()
        }
        pdfView.setOnClickListener {
            mIsShowMenu = false
            ll_option.visibility = View.GONE
            layout_fab_gotopage.visibility = View.GONE
            layout_fab_snapscreen.visibility = View.GONE
            layout_fab_share.visibility = View.GONE
            layout_fab_favourite.visibility = View.GONE
        }
        fab.setOnClickListener {
            if (!mIsShowMenu) {
                mIsShowMenu = true
                ll_option.visibility = View.VISIBLE
                layout_fab_gotopage.visibility = View.GONE
                layout_fab_snapscreen.visibility = View.VISIBLE
                layout_fab_share.visibility = View.VISIBLE
                layout_fab_favourite.visibility = View.VISIBLE
            } else {
                mIsShowMenu = false
                ll_option.visibility = View.GONE
                layout_fab_gotopage.visibility = View.GONE
                layout_fab_snapscreen.visibility = View.GONE
                layout_fab_share.visibility = View.GONE
                layout_fab_favourite.visibility = View.GONE
            }
        }

        layout_fab_gotopage.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.layout_dialog_gotopage)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.btnOke.setOnClickListener {
                val number = dialog.edtPageNumber.text.toString().trim()
                if (number == "") {
                    Toast.makeText(this, getString(R.string.enter_page_number), Toast.LENGTH_LONG)
                        .show()
                } else {
                    dialog.dismiss()
                    pdfView.fromFile(File(mUrlImage))
                        .enableSwipe(true)
                        .defaultPage(number.toInt() - 1)
                        .enableAnnotationRendering(true)
                        .scrollHandle(DefaultScrollHandle(this))
                        .load()
                }
            }
            dialog.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        layout_fab_share.setOnClickListener {
            AppOpenManager.getInstance().disableAppResumeWithActivity(ShotViewerScrenActivity::class.java)
            val fileUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                File(mUrlImage)
            )
            val intent = Utils.fileShareIntent(getString(R.string.fab_share), fileUri)
            startActivity(Intent.createChooser(intent, "share.."))
        }

        layout_fab_snapscreen.setOnClickListener {
            mIsShowMenu = false
            ll_option.visibility = View.GONE
            val bitmap = Utils.takeScreenshot(layoutPdf)
            val path = Utils.saveBitmap(bitmap!!, this)
            val intent = Intent(this, ScreenShotScreenActivity::class.java)
            intent.putExtra("path", path)
            startActivity(intent)
        }
        layout_fab_favourite.setOnClickListener {
            if(mIsFavorite) {
                mIsFavorite = false
                fab_favourite.setImageResource(R.drawable.option_bookmark_unselect)
            }else{
                mIsFavorite = true
                fab_favourite.setImageResource(R.drawable.option_bookmark)
            }
            Utils.setFileFavorite(mUrlImage, this, mIsFavorite)
        }
    }

    @SuppressLint("NewApi")
    fun makePDF(bitmap: Bitmap?) {
        if(bitmap != null) {
            pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page: PdfDocument.Page = pdfDocument!!.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()
            paint.color = Color.parseColor("#FFFFFF")
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            pdfDocument!!.finishPage(page)
            saveFile()
        }else{
            Toast.makeText(applicationContext, "File Load Error!", Toast.LENGTH_LONG).show()
        }
    }


    @SuppressLint("NewApi")
    fun saveFile() {
        if (pdfDocument == null) {
            Log.i("local-dev", "pdfDocument in 'saveFile' function is null")
            return
        }
        val root = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "ImgToPDF"
        )
        var isDirectoryCreated = root.exists()
        if (!isDirectoryCreated) {
            isDirectoryCreated = root.mkdir()
        }
        val file = File(outputPDF)
        try {
            val fileOutputStream = FileOutputStream(file)
            pdfDocument!!.writeTo(fileOutputStream)
            pdfDocument!!.close()
            viewPDFFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun viewPDFFile() {
        pdfView.fromFile(File(outputPDF)).load()
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        AppOpenManager.getInstance().enableAppResumeWithActivity(ShotViewerScrenActivity::class.java)
    }

    private fun setColorToolbar() {
        if (filename.endsWith(".doc")) {
            toolbar_screenshot.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 20) {
                tv_title_screenshot.setText(filename.substring(0, 20) + "....doc")
            } else {
                tv_title_screenshot.setText(filename)
            }
        } else if (filename.endsWith(".ppt")) {
            toolbar_screenshot.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 20) {
                tv_title_screenshot.setText(filename.substring(0, 20) + "....ppt")
            } else {
                tv_title_screenshot.setText(filename)
            }
        } else if (filename.endsWith(".txt")) {
            toolbar_screenshot.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 20) {
                tv_title_screenshot.setText(filename.substring(0, 20) + "....txt")
            } else {
                tv_title_screenshot.setText(filename)
            }
        } else if (filename.endsWith(".xls")) {
            toolbar_screenshot.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 20) {
                tv_title_screenshot.setText(filename.substring(0, 20) + "....xls")
            } else {
                tv_title_screenshot.setText(filename)
            }
        } else if (filename.endsWith(".xml")) {
            toolbar_screenshot.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 20) {
                tv_title_screenshot.setText(filename.substring(0, 20) + "....xml")
            } else {
                tv_title_screenshot.setText(filename)
            }
        } else if (filename.endsWith(".docx")) {
            toolbar_screenshot.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 20) {
                tv_title_screenshot.setText(filename.substring(0, 20) + "....docx")
            } else {
                tv_title_screenshot.setText(filename)
            }
        } else if (filename.endsWith(".png")) {
            toolbar_screenshot.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 20) {
                tv_title_screenshot.setText(filename.substring(0, 20) + "....png")
            } else {
                tv_title_screenshot.setText(filename)
            }
        } else if (filename.endsWith(".html")) {
            toolbar_screenshot.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 20) {
                tv_title_screenshot.setText(filename.substring(0, 20) + "....html")
            } else {
                tv_title_screenshot.setText(filename)
            }
        } else if (filename.endsWith(".pptx")) {
            toolbar_screenshot.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 20) {
                tv_title_screenshot.setText(filename.substring(0, 20) + "....pptx")
            } else {
                tv_title_screenshot.setText(filename)
            }
        } else if (filename.endsWith(".xlsx")) {
            toolbar_screenshot.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 20) {
                tv_title_screenshot.setText(filename.substring(0, 20) + "....xlsx")
            } else {
                tv_title_screenshot.setText(filename)
            }
        } else {
            toolbar_screenshot.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 20) {
                tv_title_screenshot.setText(filename.substring(0, 20) + "....")
            } else {
                tv_title_screenshot.setText(filename)
            }
        }
    }
}