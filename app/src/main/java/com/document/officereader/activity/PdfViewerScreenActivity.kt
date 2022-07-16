package com.document.officereader.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.amazic.ads.util.Admod
import com.amazic.ads.util.AppOpenManager
import com.document.officereader.BuildConfig
import com.document.officereader.R
import com.document.officereader.util.Constants
import com.document.officereader.util.Utils
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import kotlinx.android.synthetic.main.activity_pdf_viewer.*
import kotlinx.android.synthetic.main.layout_dialog_gotopage.*
import kotlinx.android.synthetic.main.view_fab.*
import java.io.File

class PdfViewerScreenActivity : AppCompatActivity() {

    private lateinit var mUrlFilePdf: String
    private var mIsShowMenu = false
    private var mIsFavorite = false
    private var file: File? = null
    var fromSplash = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            if (window != null) {
                var background =
                    resources.getDrawable(R.drawable.bg_gradient_toolbar)
                //bg_gradient is your gradient.
                background.setColorFilter(
                    android.graphics.Color.parseColor("#FF000B"),
                    PorterDuff.Mode.SRC_ATOP
                )
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.parseColor("#FF000B")
                window.setBackgroundDrawable(background)
            }
        }
        setContentView(R.layout.activity_pdf_viewer)
        fab.visibility = View.VISIBLE
        initData()
        handleEvents()
        //ads
        Admod.getInstance()
            .loadBanner(this@PdfViewerScreenActivity, getString(R.string.banner_all));
        fromSplash = intent.getBooleanExtra("fromSplash", false)
    }

    override fun onResume() {
        super.onResume()
        fab.visibility = View.VISIBLE
        AppOpenManager.getInstance()
            .enableAppResumeWithActivity(PdfViewerScreenActivity::class.java)
    }

    private fun initData() {
        mUrlFilePdf = intent.getStringExtra(Constants.URL)!!

        pdfView.fromFile(File(mUrlFilePdf))
            .enableSwipe(true) // allows to block changing pages using swipe
            .enableDoubletap(true)
            .defaultPage(0)
            .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
            .password(null)
            .scrollHandle(null)
            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
            .spacing(0)
            .onPageChange(object : OnPageChangeListener {
                override fun onPageChanged(page: Int, pageCount: Int) {
                    number_pages.setText("Page " + (page + 1).toString() + " of " + pageCount.toString())

                }
            })
            .load()




        mIsFavorite = Utils.isFileFavorite(mUrlFilePdf, this)
        if (mIsFavorite) {
            fab_favourite.setImageResource(R.drawable.option_bookmark)
        } else {
            fab_favourite.setImageResource(R.drawable.option_bookmark_unselect)
        }
        file = File(mUrlFilePdf)
        if (file!!.name.length > 20) {
            tv_title_PDF.setText(file!!.name.substring(0, 20) + "....pdf")
        } else {
            tv_title_PDF.setText(file!!.name)
        }

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
                layout_fab_gotopage.visibility = View.VISIBLE
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
            mIsShowMenu = false
            ll_option.visibility = View.GONE
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
                    try {

                        pdfView.recycle()
                        pdfView.fromFile(File(mUrlFilePdf))
                            .enableSwipe(true)
                            .defaultPage(number.toInt() - 1)
                            .enableAnnotationRendering(true)
                            .scrollHandle(DefaultScrollHandle(this))
                            .onPageChange(object : OnPageChangeListener {
                                override fun onPageChanged(page: Int, pageCount: Int) {
                                    number_pages.setText("Page " + (page + 1).toString() + " of " + pageCount.toString())

                                }
                            })
                            .load()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            dialog.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        layout_fab_share.setOnClickListener {
            AppOpenManager.getInstance()
                .disableAppResumeWithActivity(PdfViewerScreenActivity::class.java)
            val fileUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                File(mUrlFilePdf)
            )
            val intent = Utils.fileShareIntent(getString(R.string.fab_share), fileUri)
            startActivity(Intent.createChooser(intent, "share.."))
        }

        layout_fab_snapscreen.setOnClickListener {
            mIsShowMenu = false
            ll_option.visibility = View.GONE
            fab.visibility = View.GONE
            val bitmap = Utils.takeScreenshot(layoutPdf)
            val path = Utils.saveBitmap(bitmap!!, this)
            val intent = Intent(this, ScreenShotScreenActivity::class.java)
            intent.putExtra("path", path)
            intent.putExtra("pathtitle", mUrlFilePdf)
            startActivity(intent)
        }
        layout_fab_favourite.setOnClickListener {
            if (mIsFavorite) {
                mIsFavorite = false
                fab_favourite.setImageResource(R.drawable.option_bookmark_unselect)
            } else {
                mIsFavorite = true
                fab_favourite.setImageResource(R.drawable.option_bookmark)
            }
            Utils.setFileFavorite(mUrlFilePdf, this, mIsFavorite)
        }
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        setResult(RESULT_OK, returnIntent)
        if (fromSplash) {
            startActivity(Intent(this@PdfViewerScreenActivity, HomeScreenActivity::class.java))
            finish()
        } else {
            finish()
        }
    }
}