package com.ntdapp.document.viewer.reader.officereader.pdfreader

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ntdapp.document.viewer.reader.officereader.pdfreader.activity.HomeScreenActivity
import com.ntdapp.document.viewer.reader.officereader.pdfreader.activity.OfficeViewerScreenActivity
import com.ntdapp.document.viewer.reader.officereader.pdfreader.activity.PdfViewerScreenActivity
import com.ntdapp.document.viewer.reader.officereader.pdfreader.adapter.ListFileOfficeAdapter
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.Constants
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.Utils
import kotlinx.android.synthetic.main.activity_list_office.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.btnBack
import kotlinx.android.synthetic.main.activity_search.edtSearch
import kotlinx.android.synthetic.main.activity_search.txtTitle
import java.io.File

class SearchScreenActivity : AppCompatActivity(), ListFileOfficeAdapter.OnItemListener {

    private var mType = Constants.ALL

    private var mFiles = ArrayList<File>()

    private var mFileTemps = ArrayList<File>()

    private lateinit var mListFileOfficeAdapter: ListFileOfficeAdapter

    private lateinit var mIntent: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initData()
        handleEvents()
    }

    private fun initData() {
        val type = intent.getStringExtra(Constants.TYPE)
        if (!TextUtils.isEmpty(type)) {
            mType = type!!
        }
        mFiles.addAll(getListFileByType())
        mFileTemps.addAll(mFiles)


        mListFileOfficeAdapter = ListFileOfficeAdapter(mFiles, this)
        rvSearch.adapter = mListFileOfficeAdapter
        edtSearch.showKeyboard()
    }

    fun EditText.showKeyboard() {
        post {
            requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun handleEvents() {
        btnBack.setOnClickListener {
            onBackPressed()
        }
        imgClear.setOnClickListener {
            edtSearch.setText("")
        }
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mFiles.clear()
                if (TextUtils.isEmpty(p0)) {
                    imgClear.visibility = View.GONE
                    mFiles.addAll(mFileTemps)
                } else {
                    imgClear.visibility = View.GONE
                    mFiles.addAll(mFileTemps.filter { file ->
                        file.name.toLowerCase().indexOf(p0.toString().toLowerCase()) != -1
                    })
                }

                if (mFiles.isEmpty()) {
                    ll_empty_seach.visibility = View.VISIBLE
                    mFiles.clear()
                    mListFileOfficeAdapter.notifyDataSetChanged()
                    rvSearch.adapter = mListFileOfficeAdapter
                } else {
//                    ll_empty_seach.visibility = View.GONE
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun getListFileByType(): ArrayList<File> {
        when (mType) {
            Constants.PDF -> {
                txtTitle.text = "SEACH " + "PDF"
                return HomeScreenActivity.mFilePdfs
            }
            Constants.WORD -> {
                txtTitle.text = "SEACH " + "WORD"
                return HomeScreenActivity.mFileWords
            }
            Constants.EXCEL -> {
                txtTitle.text = "SEACH " + "EXCEL"
                return HomeScreenActivity.mFileExcels
            }
            Constants.POWER_POINT -> {
                txtTitle.text = "SEACH " + "POWER POINT"
                return HomeScreenActivity.mFilePowerpoints
            }
            Constants.IMAGE -> {
                txtTitle.text = "SEACH " + "IMAGE"
                return HomeScreenActivity.mFileScreenshots
            }
            Constants.TEXT -> {
                txtTitle.text = "SEACH " + "TEXT"
                return HomeScreenActivity.mFileTexts
            }
            Constants.FAVORITE -> {
                txtTitle.text = "SEACH " + "BOOKMARK"
                return HomeScreenActivity.mFileFavorutes
            }
            Constants.RECENT -> {
                txtTitle.text = "SEACH " + "RECENT FILES"
                return HomeScreenActivity.mFileRecents
            }
            else -> {
                txtTitle.text = "SEACH " + "ALL FILES"
                return HomeScreenActivity.mFileAlls
            }
        }
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


    private fun showAds() {

        gotoNextScreen()
    }

    private fun gotoNextScreen() {
        startActivity(mIntent)
    }
}