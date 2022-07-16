package com.document.officereader.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.amazic.ads.util.Admod
import com.amazic.ads.util.AppOpenManager
import com.nvt.color.ColorPickerDialog
import com.document.officereader.R
import com.document.officereader.customview.cropper.CropImageView
import com.document.officereader.customview.drawingview.brushes.BrushSettings
import com.document.officereader.util.Utils
import kotlinx.android.synthetic.main.activity_screenshot.*
import kotlinx.android.synthetic.main.layout_dialog_gotopage.btnCancel
import kotlinx.android.synthetic.main.layout_dialog_gotopage.btnOke
import kotlinx.android.synthetic.main.layout_dialog_savefile.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class ScreenShotScreenActivity : AppCompatActivity(), CropImageView.OnSetImageUriCompleteListener,
    CropImageView.OnCropImageCompleteListener {

    private lateinit var mBitmap: Bitmap

    private var mFlagAction = 0
    private var mTempImageUri: Uri? = null
    var mRootImagePath: String? = null
    private var mRootImageUri: Uri? = null
    private var mSelectedColor = Color.parseColor("#2187bb")
    private var mIsDoneClick = false


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
        setContentView(R.layout.activity_screenshot)
        initData()
        showCropMode(null)
        setupDrawingView()
        handleEvents()
        //ads
        Admod.getInstance()
            .loadBanner(this@ScreenShotScreenActivity, getString(R.string.banner_all));


    }

    override fun onStart() {
        cropImageView.setOnSetImageUriCompleteListener(this)
        cropImageView.setOnCropImageCompleteListener(this)
        super.onStart()
    }

    private fun initData() {
        mRootImagePath = intent.getStringExtra("path").toString()
        setColorToolbar()
        val f = File(mRootImagePath)
        mBitmap = BitmapFactory.decodeStream(FileInputStream(f))
        mRootImageUri = Uri.fromFile(f)
        cropImageView.setImageUriAsync(mRootImageUri)

        val filePath: File =
            getExternalFilesDir(null)!!.absoluteFile
        val dir = File(filePath.absolutePath.toString() + "/AllDocument/")
        dir.mkdir()
        val nameFile = "screenshot.jpg"
        val file = File(dir, nameFile)
        mTempImageUri = Uri.fromFile(file)


    }

    private fun showCropMode(uri: Uri?) {
        drawingView.visibility = View.INVISIBLE
        cropImageView.visibility = View.VISIBLE
        tvRestore.visibility = View.VISIBLE
        layoutBrush.visibility = View.GONE
        //imgFunctionEraser.visibility = View.INVISIBLE
        // imgColorPicker.visibility = View.INVISIBLE
        // showFunctionActive(imgFunctionCrop)
        if (uri != null) {
            cropImageView.setImageUriAsync(uri)
        }
    }

    private fun showFunctionActive(imageView: ImageView) {
        imgFunctionEraser.background = null
        imgFunctionPen.background = null
        imgFunctionCrop.background = null
        imageView.setBackgroundResource(R.drawable.selector_border_draw_mode)
    }

    private fun handleEvents() {
        tvRestore.setOnClickListener {
            showCropMode(mRootImageUri)
        }
        back_crop.setOnClickListener {
            finish()
        }

        imgFunctionPen.setOnClickListener {
            doActionPen()
        }
        imgFunctionEraser.setOnClickListener {
            doActionEraser()
        }
        imgFunctionCrop.setOnClickListener {
            Log.e("xxxx", "cropImage");
            cropImage()
            /* if (mTempImageUri != null) {
                 cropImageView.setImageUriAsync(mTempImageUri)
             }*/
        }
        imgColorPicker.setOnClickListener {
            showColorPickerDialog()
        }
        imgClose.setOnClickListener {
            layoutBrush.visibility = View.GONE
        }
        imgShare.setOnClickListener {
            AppOpenManager.getInstance()
                .disableAppResumeWithActivity(ScreenShotScreenActivity::class.java)
            var uri: String
            var file: File
            if (mTempImageUri?.path?.contains("content://com.") == true) {
                uri = mTempImageUri!!.path.toString();
                val intent = Utils.fileShareIntent(getString(R.string.fab_share), Uri.parse(uri))
                startActivity(Intent.createChooser(intent, "share.."))
            } else if (mTempImageUri?.path?.contains("content://") == true) {
                file = File(mTempImageUri!!.path)
                uri = "file://" + mTempImageUri!!.path;
                shareFile(file)
            } else {
                file = File(mTempImageUri!!.path)
                uri = "file://" + mTempImageUri!!.path;
                shareFile(file)
            }
            Log.e("xxxxx", mTempImageUri!!.path.toString());
            Log.e("xxxxx22", uri.toString());

        }
        tvDone.setOnClickListener {
            mIsDoneClick = true
            if (mFlagAction == 0) {
                cropImage()
            }
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.layout_dialog_savefile)
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            val name = "ScreenShot_" + System.currentTimeMillis()
            dialog.edtFileName.setText(name)
            dialog.setCancelable(false)
            dialog.btnOke.setOnClickListener {
                val fileName = dialog.edtFileName.text.toString().trim()
                if (fileName == "") {
                    var toast = Toast.makeText(
                        this,
                        getString(R.string.enter_page_number),
                        Toast.LENGTH_LONG
                    )

                    toast.show()
                } else {
                    val inflater = layoutInflater
                    val layout: View = inflater.inflate(
                        R.layout.show_toast, null

                    )
                    val tv = layout.findViewById<View>(R.id.txtvw) as TextView

                    Utils.saveBitmapByName(drawingView.exportDrawing(), this, fileName, layout, tv)
                    dialog?.dismiss()
                    finish()
                }
            }
            dialog.btnCancel.setOnClickListener {
                dialog?.dismiss()
            }

            dialog.show()
        }
        rl_crop.setOnClickListener {
            showCropp()
        }
        rl_draw.setOnClickListener {
            showDraw()
        }


    }

    private fun setColorToolbar() {

        var file = File(intent.getStringExtra("pathtitle").toString())
        var filename = file.name
        if (filename.endsWith(".doc")) {
            layout_top.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 15) {
                tv_title_crop.setText(filename.substring(0, 15) + "....doc")
            } else {
                tv_title_crop.setText(filename)
            }
        } else if (filename.endsWith(".ppt")) {
            layout_top.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 15) {
                tv_title_crop.setText(filename.substring(0, 15) + "....ppt")
            } else {
                tv_title_crop.setText(filename)
            }
        } else if (filename.endsWith(".txt")) {
            layout_top.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 15) {
                tv_title_crop.setText(filename.substring(0, 15) + "....txt")
            } else {
                tv_title_crop.setText(filename)
            }
        } else if (filename.endsWith(".xls")) {
            layout_top.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 15) {
                tv_title_crop.setText(filename.substring(0, 15) + "....xls")
            } else {
                tv_title_crop.setText(filename)
            }
        } else if (filename.endsWith(".xml")) {
            layout_top.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 15) {
                tv_title_crop.setText(filename.substring(0, 15) + "....xml")
            } else {
                tv_title_crop.setText(filename)
            }
        } else if (filename.endsWith(".docx")) {
            layout_top.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 15) {
                tv_title_crop.setText(filename.substring(0, 15) + "....docx")
            } else {
                tv_title_crop.setText(filename)
            }
        } else if (filename.endsWith(".png")) {
            layout_top.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 15) {
                tv_title_crop.setText(filename.substring(0, 15) + "....png")
            } else {
                tv_title_crop.setText(filename)
            }
        } else if (filename.endsWith(".html")) {
            layout_top.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 15) {
                tv_title_crop.setText(filename.substring(0, 15) + "....html")
            } else {
                tv_title_crop.setText(filename)
            }
        } else if (filename.endsWith(".pptx")) {
            layout_top.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 15) {
                tv_title_crop.setText(filename.substring(0, 15) + "....pptx")
            } else {
                tv_title_crop.setText(filename)
            }
        } else if (filename.endsWith(".xlsx")) {
            layout_top.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 15) {
                tv_title_crop.setText(filename.substring(0, 15) + "....xlsx")
            } else {
                tv_title_crop.setText(filename)
            }
        } else if (filename.endsWith(".pdf")) {
            layout_top.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 15) {
                tv_title_crop.setText(filename.substring(0, 15) + "....pdf")
            } else {
                tv_title_crop.setText(filename)
            }
        } else {
            layout_top.setBackground(ColorDrawable(Color.parseColor("#FF9900")))
            if (filename.length > 15) {
                tv_title_crop.setText(filename.substring(0, 15) + "....")
            } else {
                tv_title_crop.setText(filename)
            }
        }
    }

    private fun showCropp() {
        ll_crop.visibility = View.VISIBLE
        ll_draw.visibility = View.GONE
        img_draw_tab.setColorFilter(Color.parseColor("#D3D3D3"))
        img_crop_tab.setColorFilter(Color.parseColor("#2195F1"))
        tv_crop_tab.setTextColor(Color.parseColor("#2195F1"))
        tv_draw_tab.setTextColor(Color.parseColor("#D3D3D3"))
        doActionCrop()
    }

    private fun showDraw() {
        img_draw_tab.setColorFilter(Color.parseColor("#F44336"))
        img_crop_tab.setColorFilter(Color.parseColor("#D3D3D3"))
        tv_draw_tab.setTextColor(Color.parseColor("#F44336"))
        tv_crop_tab.setTextColor(Color.parseColor("#D3D3D3"))
        ll_crop.visibility = View.GONE
        ll_draw.visibility = View.VISIBLE

        val i = mFlagAction
        if (i != 1) {
            mFlagAction = 1
            if (i == 0) {
                cropImage()
            }

        }


    }

    private fun setupDrawingView() {
        drawingView.setUndoAndRedoEnable(true)
        drawingView.setOnActionDownListener {
            layoutBrush.visibility = View.GONE
        }
        brushView.setDrawingView(drawingView)
        sbSize.max = 100
        sbSize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, i: Int, z: Boolean) {
                drawingView.brushSettings.selectedBrushSize = i.toFloat() / 100.0f
            }
        })
        setupUndoAndRedo()
        setBrushSelected(0)
        imgColorPicker.setImageDrawable(ColorDrawable(mSelectedColor))
    }

    private fun setupUndoAndRedo() {
        imgUndo.setOnClickListener { view ->
            setupUndo()
        }
        imgRedo.setOnClickListener { view ->
            setupRedo()
        }
        drawingView.setOnDrawListener {
            setupDraw()
        }
    }

    private fun setBrushSelected(i: Int) {
        val brushSettings: BrushSettings = drawingView.brushSettings
        brushSettings.selectedBrush = i
        sbSize.progress = (brushSettings.selectedBrushSize * 100.0f).toInt()
    }


    fun cropImage() {
        cropImageView.saveCroppedImageAsync(mTempImageUri)
    }

    private fun doActionCrop() {
        if (mFlagAction != 0) {
            saveTempImageAfterDraw()
            mFlagAction = 0
            showCropMode(mTempImageUri)
        }
    }

    private fun saveTempImageAfterDraw() {
        if (mFlagAction != 0) {
            try {
                Utils.saveBitmap(drawingView.exportDrawing(), this)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun showColorPickerDialog() {
        val colorPicker = ColorPickerDialog(
            this,
            Color.BLACK, // color init
            true, // true is show alpha
            object : ColorPickerDialog.OnColorPickerListener {
                override fun onCancel(dialog: ColorPickerDialog?) {
                    // handle click button Cancel
                }

                override fun onOk(dialog: ColorPickerDialog?, colorPicker: Int) {
                    // handle click button OK
                    mSelectedColor = colorPicker
                    imgColorPicker.setImageDrawable(ColorDrawable(mSelectedColor))
                    drawingView.brushSettings.color = mSelectedColor
                }
            })

        colorPicker.show()
    }

    private fun setupUndo() {
        layoutBrush.visibility = View.GONE
        drawingView.undo()
//        imgUndo.setImageResource(if (drawingView.isUndoStackEmpty) R.drawable.ic_undo_inactive_xml else R.drawable.ic_undo_xml)
//        imgRedo.setImageResource(if (drawingView.isRedoStackEmpty) R.drawable.ic_redo_inactive_xml else R.drawable.ic_redo_xml)
        imgUndo.isEnabled = !drawingView.isUndoStackEmpty
        imgRedo.isEnabled = !drawingView.isRedoStackEmpty
    }

    private fun setupRedo() {
        layoutBrush.visibility = View.GONE
        drawingView.redo()
//        imgUndo.setImageResource(if (drawingView.isUndoStackEmpty) R.drawable.ic_undo_inactive_xml else R.drawable.ic_undo_xml)
//        imgRedo.setImageResource(if (drawingView.isRedoStackEmpty) R.drawable.ic_redo_inactive_xml else R.drawable.ic_redo_xml)
        imgUndo.isEnabled = !drawingView.isUndoStackEmpty
        imgRedo.isEnabled = !drawingView.isRedoStackEmpty
    }

    private fun setupDraw() {
        imgUndo.isEnabled = true
        imgRedo.isEnabled = false
//        imgUndo.setImageResource(R.drawable.ic_undo_xml)
//        imgRedo.setImageResource(R.drawable.ic_redo_inactive_xml)
    }

    override fun onSetImageUriComplete(view: CropImageView?, uri: Uri?, error: Exception?) {
        if (error == null) {
//            if (this.mOptions.initialCropWindowRectangle != null) {
//                this.mCropImageView.setCropRect(this.mOptions.initialCropWindowRectangle)
//            }
//            if (this.mOptions.initialRotation > -1) {
//                this.mCropImageView.setRotatedDegrees(this.mOptions.initialRotation)
//                return
//            }
            return
        }
    }

    private fun doActionPen() {
        val i = mFlagAction
        if (i != 1) {
            mFlagAction = 1
            if (i == 0) {
                cropImage()
            }

        } else if (layoutBrush.visibility == View.VISIBLE) {
            layoutBrush.visibility = View.GONE
        } else {
            layoutBrush.visibility = View.VISIBLE
        }
        // showFunctionActive(imgFunctionPen)
        imgFunctionEraser.visibility = View.VISIBLE
        layoutBrush.visibility = View.VISIBLE
        imgColorPicker.visibility = View.VISIBLE
        setBrushSelected(View.VISIBLE)
    }

    private fun doActionEraser() {
        val i = mFlagAction
        if (i != 2) {
            if (i == 0) {
                cropImage()
            }
            mFlagAction = 2
            // showFunctionActive(imgFunctionEraser)
            layoutBrush.visibility = View.VISIBLE
            setBrushSelected(View.INVISIBLE)
        } else if (layoutBrush.visibility == View.VISIBLE) {
            layoutBrush.visibility = View.GONE
        } else {
            layoutBrush.visibility = View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCropImageComplete(view: CropImageView?, cropResult: CropImageView.CropResult?) {
        if (cropResult?.error == null && cropResult != null && cropResult!!.originalUri != null) {
            val uri: Uri = cropResult!!.originalUri
            val i = mFlagAction
            mTempImageUri = uri
            if (i == 1 || i == 2 || mIsDoneClick) {
                val f = File(mRootImagePath)
                val bitmap = BitmapFactory.decodeStream(FileInputStream(f))
                drawingView.setBackgroundImage(bitmap)
                drawingView.visibility = View.VISIBLE
                cropImageView.visibility = View.INVISIBLE
                tvRestore.visibility = View.GONE
                imgRedo.visibility = View.VISIBLE
                imgUndo.visibility = View.VISIBLE
            } else if (i == 0) {
                showCropMode(uri)
            }
        } else {

        }
    }


    /*  private void initAds(){
        interstitialAd = Admod.getInstance().getInterstitalAds(mContext, mContext.getString(R.string.intersitial_file));
    }*/
    private fun shareFile(file: File) {
        val imageUri = FileProvider.getUriForFile(
            this,
            applicationContext.packageName + ".provider",  //(use your app signature + ".provider" )
            file
        )
        val intent = Utils.fileShareIntent(getString(R.string.fab_share), imageUri)
        startActivity(Intent.createChooser(intent, "share.."))
    }

    override fun onResume() {
        super.onResume()
        AppOpenManager.getInstance()
            .enableAppResumeWithActivity(PdfViewerScreenActivity::class.java)
    }


}