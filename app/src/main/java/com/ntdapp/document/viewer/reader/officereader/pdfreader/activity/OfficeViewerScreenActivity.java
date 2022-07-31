package com.ntdapp.document.viewer.reader.officereader.pdfreader.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.amazic.ads.util.Admod;
import com.amazic.ads.util.AppOpenManager;
import com.example.ads.AppIronSource;
import com.ntdapp.document.viewer.reader.officereader.pdfreader.BuildConfig;
import com.ntdapp.document.viewer.reader.officereader.pdfreader.R;
import com.ntdapp.document.viewer.reader.officereader.pdfreader.ScreenShotScreenActivity;
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.Constants;
import com.ntdapp.document.viewer.reader.officereader.pdfreader.util.Utils;
import com.office.allreader.allofficefilereader.common.IOfficeToPicture;
import com.office.allreader.allofficefilereader.constant.EventConstant;
import com.office.allreader.allofficefilereader.constant.MainConstant;
import com.office.allreader.allofficefilereader.fc.util.IOUtils;
import com.office.allreader.allofficefilereader.macro.DialogListener;
import com.office.allreader.allofficefilereader.officereader.AppFrame;
import com.office.allreader.allofficefilereader.officereader.beans.AImageButton;
import com.office.allreader.allofficefilereader.officereader.beans.AImageCheckButton;
import com.office.allreader.allofficefilereader.officereader.beans.AToolsbar;
import com.office.allreader.allofficefilereader.officereader.database.DBService;
import com.office.allreader.allofficefilereader.res.ResKit;
import com.office.allreader.allofficefilereader.ss.sheetbar.SheetBar;
import com.office.allreader.allofficefilereader.system.FileKit;
import com.office.allreader.allofficefilereader.system.IControl;
import com.office.allreader.allofficefilereader.system.IMainFrame;
import com.office.allreader.allofficefilereader.system.MainControl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class OfficeViewerScreenActivity extends AppCompatActivity implements IMainFrame {
    private static final String TAG = "AppActivity";
    private String action;
    private AppFrame appFrame;
    private int applicationType = -1;


    private Object f268bg = -7829368;
    private SheetBar bottomBar;
    private MainControl control;
    private DBService dbService;
    private AImageCheckButton eraserButton;
    private String fileName;
    private String filePath;
    private Intent filesDataRecievingIntent;
    private boolean fullscreen;
    private View gapView;
    private boolean isDispose;
    private boolean isThumbnail;
    private boolean marked;
    private AImageButton pageDown;
    private AImageButton pageUp;
    private AImageCheckButton penButton;
    private AImageButton settingsButton;
    private String tempFileExtension;
    private String tempFilePath;
    private Toast toast;
    private AToolsbar toolsbar;
    private String type;


    private WindowManager f269wm = null;
    private WindowManager.LayoutParams wmParams = null;
    private boolean writeLog = true;

    private ImageView fab, fabSnapScreen, fabShare, fabFavorite;

    private RelativeLayout layoutFabGotoPage, layoutFabSnapScreen, layoutFabShare, toolbar_view, v_fab;
    private LinearLayout ll_option, layoutFabFavorite;
    private TextView tv_work;
    private String filename = "";

    private ConstraintLayout layoutPdf;

    private boolean mIsShowMenu = false;

    private boolean mIsFavorite = false;

    private String mUrlPowerPoint;
    boolean fromSplash = false;

    @Override
    public void changePage() {
    }

    @Override
    public void changeZoom() {
    }

    @Override
    public void completeLayout() {
    }

    @Override
    public void error(int i) {
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    public DialogListener getDialogListener() {
        return null;
    }

    @Override
    public byte getPageListViewMovingPosition() {
        return 0;
    }

    @Override
    public String getTXTDefaultEncode() {
        return "GBK";
    }

    @Override
    public int getTopBarHeight() {
        return 0;
    }

    @Override
    public byte getWordDefaultView() {
        return 0;
    }

    @Override
    public boolean isChangePage() {
        return true;
    }

    @Override
    public boolean isDrawPageNumber() {
        return true;
    }

    @Override
    public boolean isIgnoreOriginalSize() {
        return false;
    }

    @Override
    public boolean isPopUpErrorDlg() {
        return true;
    }

    @Override
    public boolean isShowFindDlg() {
        return true;
    }

    @Override
    public boolean isShowPasswordDlg() {
        return true;
    }

    @Override
    public boolean isShowProgressBar() {
        return true;
    }

    @Override
    public boolean isShowTXTEncodeDlg() {
        return true;
    }

    @Override
    public boolean isShowZoomingMsg() {
        return true;
    }

    @Override
    public boolean isTouchZoom() {
        return true;
    }

    @Override
    public boolean isZoomAfterLayoutForWord() {
        return true;
    }

    public void onCurrentPageChange() {
    }

    @Override
    public boolean onEventMethod(View view, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, byte b) {
        return false;
    }

    public void onPagesCountChange() {
    }

    @Override
    public void setIgnoreOriginalSize(boolean z) {
    }

    @Override
    public void updateViewImages(List<Integer> list) {
    }

    public static String getFileName(Uri uri) {
        String path;
        int lastIndexOf;
        if (uri == null || (lastIndexOf = (path = uri.getPath()).lastIndexOf(47)) == -1) {
            return null;
        }
        return path.substring(lastIndexOf + 1);
    }

    public static String getMimeType(Context context, Uri uri) {
        if (uri.getScheme().equals("content")) {
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri));
        }
        return MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
    }

    public static void copy(Context context, Uri uri, File file) {
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            if (openInputStream != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                IOUtils.copy(openInputStream, fileOutputStream);
                openInputStream.close();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.control = new MainControl(this);
        AppFrame appFrame = new AppFrame(getApplicationContext());
        this.appFrame = appFrame;
        appFrame.post(new Runnable() { // from class: com.allreader.office.allofficefilereader.officereader.AppActivity.1
            @Override
            public void run() {
                OfficeViewerScreenActivity.this.init();
            }
        });
        this.control.setOffictToPicture(new IOfficeToPicture() { // from class: com.allreader.office.allofficefilereader.officereader.AppActivity.2
            private Bitmap bitmap;

            @Override // com.allreader.office.allofficefilereader.common.IOfficeToPicture
            public void dispose() {
            }

            @Override // com.allreader.office.allofficefilereader.common.IOfficeToPicture
            public byte getModeType() {
                return 1;
            }

            @Override
            public boolean isZoom() {
                return false;
            }

            @Override
            public void setModeType(byte b) {
            }

            @Override
            public Bitmap getBitmap(int i, int i2) {
                if (i == 0 || i2 == 0) {
                    return null;
                }
                Bitmap bitmap = this.bitmap;
                if (!(bitmap != null && bitmap.getWidth() == i && this.bitmap.getHeight() == i2)) {
                    Bitmap bitmap2 = this.bitmap;
                    if (bitmap2 != null) {
                        bitmap2.recycle();
                    }
                    this.bitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
                }
                return this.bitmap;
            }

            @Override
            public void callBack(Bitmap bitmap) {
                OfficeViewerScreenActivity.this.saveBitmapToFile(bitmap);
            }
        });
        setContentView(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.layout.activity_office);
        FrameLayout layoutMain = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.appFrame);
        layoutMain.addView(this.appFrame);

        initData();
        handleEvents();
        //ads
        //Admod.getInstance().loadBanner(OfficeViewerScreenActivity.this, getString(R.string.banner_all));

        fromSplash = getIntent().getBooleanExtra("fromSplash", false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppIronSource.getInstance().loadBanner(this);
    }

    private void initData() {
        fab = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.fab);
        fabSnapScreen = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.fab_snapscreen);
        fabShare = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.fab_share);
        fabFavorite = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.fab_favourite);
        layoutFabGotoPage = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.layout_fab_gotopage);
        layoutFabSnapScreen = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.layout_fab_snapscreen);
        layoutFabShare = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.layout_fab_share);
        layoutFabFavorite = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.layout_fab_favourite);
        layoutPdf = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.layoutPdf);
        ll_option = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.ll_option);
        toolbar_view = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.toolbar_view);
        tv_work = findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.tv_work);


        mUrlPowerPoint = getIntent().getStringExtra(Constants.URL);
        File file = new File(mUrlPowerPoint);
        filename = file.getName();

        setColorToolbar();

        mIsFavorite = Utils.INSTANCE.isFileFavorite(mUrlPowerPoint, this);
        if (mIsFavorite) {
            fabFavorite.setImageResource(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.option_bookmark);
        } else {
            fabFavorite.setImageResource(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.option_bookmark_unselect);
        }
    }

    private void handleEvents() {
        findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.appFrame).setOnClickListener(view -> {
            mIsShowMenu = false;
            ll_option.setVisibility(View.GONE);
            layoutFabGotoPage.setVisibility(View.GONE);
            layoutFabSnapScreen.setVisibility(View.GONE);
            layoutFabShare.setVisibility(View.GONE);
            layoutFabFavorite.setVisibility(View.GONE);
        });

        fab.setOnClickListener(view -> {
            if (!mIsShowMenu) {
                mIsShowMenu = true;
                ll_option.setVisibility(View.VISIBLE);
                layoutFabGotoPage.setVisibility(View.GONE);
                layoutFabSnapScreen.setVisibility(View.VISIBLE);
                layoutFabShare.setVisibility(View.VISIBLE);
                layoutFabFavorite.setVisibility(View.VISIBLE);
            } else {
                mIsShowMenu = false;
                ll_option.setVisibility(View.GONE);
                layoutFabGotoPage.setVisibility(View.GONE);
                layoutFabSnapScreen.setVisibility(View.GONE);
                layoutFabShare.setVisibility(View.GONE);
                layoutFabFavorite.setVisibility(View.GONE);
            }
        });


        layoutFabShare.setOnClickListener(view -> {
            ll_option.setVisibility(View.GONE);
            mIsShowMenu = false;
            AppOpenManager.getInstance().disableAppResumeWithActivity(OfficeViewerScreenActivity.class);
            Uri fileUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    new File(mUrlPowerPoint)
            );
            Intent intent = Utils.INSTANCE.fileShareIntent(getString(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.string.fab_share), fileUri);
            startActivity(Intent.createChooser(intent, "share.."));
        });

        layoutFabSnapScreen.setOnClickListener(view -> {
            ll_option.setVisibility(View.GONE);
            mIsShowMenu = false;
            Bitmap bitmap = Utils.INSTANCE.takeScreenshot(layoutPdf);
            String path = Utils.INSTANCE.saveBitmap(bitmap, this);
            Intent intent = new Intent(this, ScreenShotScreenActivity.class);
            intent.putExtra("path", path);
            intent.putExtra("pathtitle", mUrlPowerPoint);
            startActivity(intent);
        });

        layoutFabFavorite.setOnClickListener(view -> {
            if (mIsFavorite) {
                mIsFavorite = false;
                fabFavorite.setImageResource(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.option_bookmark_unselect);
            } else {
                mIsFavorite = true;
                fabFavorite.setImageResource(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.option_bookmark);
            }
            Utils.INSTANCE.setFileFavorite(mUrlPowerPoint, this, mIsFavorite);
        });

        findViewById(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.id.btnBack).setOnClickListener(view -> {
            if (fromSplash) {
                startActivity(new Intent(OfficeViewerScreenActivity.this, HomeScreenActivity.class));
                finish();
            } else {
                finish();
            }
        });
    }


    public void init() {
        String str;
        this.filesDataRecievingIntent = getIntent();
        this.dbService = new DBService(getApplicationContext());
        this.action = this.filesDataRecievingIntent.getAction();
        this.type = this.filesDataRecievingIntent.getType();
        this.tempFileExtension = null;
        try {
            if (TextUtils.isEmpty(mUrlPowerPoint)) {
                mUrlPowerPoint = getIntent().getStringExtra(Constants.URL);
            }
            Uri data = Uri.fromFile(new File(mUrlPowerPoint));
            String mimeType = getMimeType(this, data);
            this.tempFileExtension = mimeType;
            this.filePath = mUrlPowerPoint;
            Log.d(TAG, "onCreate: File Uri:" + this.filePath);
            Log.d(TAG, "onCreate: File Mime Type:" + this.tempFileExtension);
            Log.d(TAG, "onCreate: File Name" + this.fileName);
        } catch (Exception e) {
            Log.d("File Error", e.toString());
        }

        Log.d(TAG, "init: filePath" + this.filePath);
        try {
            openFile();
        } catch (Exception e2) {
            Log.e(TAG, "Exception:", e2);
        }

    }

    public String getFilePathFromExternalAppsURI(Context context, Uri uri, String str) {
        String fileName = getFileName(uri);
        this.fileName = fileName;
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        File filesDir = getFilesDir();
        File file = new File(filesDir, this.fileName + str);
        copy(context, uri, file);
        return file.getAbsolutePath();
    }

    private void openFile() {
        if (this.filePath == null) {
            this.filePath = this.filesDataRecievingIntent.getDataString();
            int indexOf = getFilePath().indexOf(":");
            if (indexOf > 0) {
                this.filePath = this.filePath.substring(indexOf + 3);
            }
            this.filePath = Uri.decode(this.filePath);
        }
        int lastIndexOf = this.filePath.lastIndexOf(File.separator);
        if (lastIndexOf > 0) {
            setTitle(this.filePath.substring(lastIndexOf + 1));
        } else {
            setTitle(this.filePath);
        }
        if (FileKit.instance().isSupport(this.filePath)) {
            this.dbService.insertRecentFiles(MainConstant.TABLE_RECENT, this.filePath);
        }
        createView();
        this.control.openFile(this.filePath);
        initMarked();
    }


    public void saveBitmapToFile(Bitmap bitmap) {
        if (bitmap != null) {
            if (this.tempFilePath == null) {
                if ("mounted".equals(Environment.getExternalStorageState())) {
                    this.tempFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                }
                File file = new File(this.tempFilePath + File.separatorChar + "tempPic");
                if (!file.exists()) {
                    file.mkdir();
                }
                this.tempFilePath = file.getAbsolutePath();
            }
            File file2 = new File(this.tempFilePath + File.separatorChar + "export_image.jpg");
            try {
                if (file2.exists()) {
                    file2.delete();
                }
                file2.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException unused) {
            } catch (Throwable th) {
                bitmap.recycle();
                throw th;
            }
            bitmap.recycle();
        }
    }

    public void setButtonEnabled(boolean z) {
        if (this.fullscreen) {
            this.pageUp.setEnabled(z);
            this.pageDown.setEnabled(z);
            this.penButton.setEnabled(z);
            this.eraserButton.setEnabled(z);
            this.settingsButton.setEnabled(z);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Object actionValue = this.control.getActionValue(EventConstant.PG_SLIDESHOW, null);
        if (actionValue != null && ((Boolean) actionValue).booleanValue()) {
            this.f269wm.removeView(this.pageUp);
            this.f269wm.removeView(this.pageDown);
            this.f269wm.removeView(this.penButton);
            this.f269wm.removeView(this.eraserButton);
            this.f269wm.removeView(this.settingsButton);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        AppOpenManager.getInstance().enableAppResumeWithActivity(OfficeViewerScreenActivity.class);
        Object actionValue = this.control.getActionValue(EventConstant.PG_SLIDESHOW, null);
        if (actionValue != null && ((Boolean) actionValue).booleanValue()) {
            this.wmParams.gravity = 53;
            this.wmParams.x = 5;
            this.f269wm.addView(this.penButton, this.wmParams);
            this.wmParams.gravity = 53;
            this.wmParams.x = 5;
            WindowManager.LayoutParams layoutParams = this.wmParams;
            layoutParams.y = layoutParams.height;
            this.f269wm.addView(this.eraserButton, this.wmParams);
            this.wmParams.gravity = 53;
            this.wmParams.x = 5;
            WindowManager.LayoutParams layoutParams2 = this.wmParams;
            layoutParams2.y = layoutParams2.height * 2;
            this.f269wm.addView(this.settingsButton, this.wmParams);
            this.wmParams.gravity = 19;
            this.wmParams.x = 5;
            this.wmParams.y = 0;
            this.f269wm.addView(this.pageUp, this.wmParams);
            this.wmParams.gravity = 21;
            this.f269wm.addView(this.pageDown, this.wmParams);
        }
    }

    @Override
    public void onBackPressed() {
        Object actionValue = this.control.getActionValue(EventConstant.PG_SLIDESHOW, null);
        if (actionValue == null || !((Boolean) actionValue).booleanValue()) {
            if (this.control.getReader() != null) {
                this.control.getReader().abortReader();
            }
            if (this.marked != this.dbService.queryItem(MainConstant.TABLE_STAR, this.filePath)) {
                if (!this.marked) {
                    this.dbService.deleteItem(MainConstant.TABLE_STAR, this.filePath);
                } else {
                    this.dbService.insertStarFiles(MainConstant.TABLE_STAR, this.filePath);
                }
                Intent intent = new Intent();
                intent.putExtra(MainConstant.INTENT_FILED_MARK_STATUS, this.marked);
                setResult(-1, intent);
            }
            MainControl mainControl = this.control;
            if (mainControl == null || !mainControl.isAutoTest()) {
                if (fromSplash) {
                    startActivity(new Intent(OfficeViewerScreenActivity.this, HomeScreenActivity.class));
                    finish();
                } else {
                    super.onBackPressed();
                }
            } else {
                System.exit(0);
            }
        } else {
            fullScreen(false);
            this.control.actionEvent(EventConstant.PG_SLIDESHOW_END, null);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);

    }

    @Override
    protected void onDestroy() {
        dispose();
        super.onDestroy();
    }

    @Override
    public void showProgressBar(boolean z) {
        setProgressBarIndeterminateVisibility(z);
    }

    private void createView() {
        String lowerCase = this.filePath.toLowerCase();
        if (lowerCase.endsWith(MainConstant.FILE_TYPE_DOC) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOCX) || lowerCase.endsWith(MainConstant.FILE_TYPE_TXT) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOT) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_DOTM)) {
            this.applicationType = 0;
            // this.toolsbar = new WPToolsbar(getApplicationContext(), this.control);
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_XLS) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLT) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_XLSM)) {
            this.applicationType = 1;
            // this.toolsbar = new SSToolsbar(getApplicationContext(), this.control);
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_PPT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POT) || lowerCase.endsWith(MainConstant.FILE_TYPE_PPTM) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTX) || lowerCase.endsWith(MainConstant.FILE_TYPE_POTM)) {
            this.applicationType = 2;
            // this.toolsbar = new PGToolsbar(getApplicationContext(), this.control);
        } else if (lowerCase.endsWith(MainConstant.FILE_TYPE_PDF)) {
            this.applicationType = 3;
            // this.toolsbar = new PDFToolsbar(getApplicationContext(), this.control);
        } else {
            this.applicationType = 0;
//            this.toolsbar = new WPToolsbar(getApplicationContext(), this.control);
        }
//        this.toolsbar.setBackground(setGradientBackground(getResources().getColor(R.color.color_cardBg_allDoc_lower), getResources().getColor(R.color.color_cardBg_allDoc_upper)));
//        this.appFrame.addView(this.toolsbar);
    }

    public GradientDrawable setGradientBackground(int i, int i2) {
        return new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{Integer.parseInt(String.valueOf(i)), Integer.parseInt(String.valueOf(i2))});
    }

    private void setColorToolbar() {
        if (filename.endsWith(".doc") || filename.endsWith(".docm") || filename.endsWith(".dot") || filename.endsWith(".dotx")) {
            toolbar_view.setBackground(new ColorDrawable(Color.parseColor("#0059D3")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = getWindow();
                if (window != null) {
                    Drawable background = getResources().getDrawable(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.bg_gradient_toolbar); //bg_gradient is your gradient.
                    background.setColorFilter(Color.parseColor("#0059D3"), PorterDuff.Mode.SRC_ATOP);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                    window.setBackgroundDrawable(background);
                }
            }
            if (filename.length() > 20) {
                tv_work.setText(filename.substring(0, 20) + "....doc");
            } else {
                tv_work.setText(filename);
            }
        } else if (filename.endsWith(".ppt")) {
            toolbar_view.setBackground(new ColorDrawable(Color.parseColor("#DD7719")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = getWindow();
                if (window != null) {
                    Drawable background = getResources().getDrawable(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.bg_gradient_toolbar); //bg_gradient is your gradient.
                    background.setColorFilter(Color.parseColor("#DD7719"), PorterDuff.Mode.SRC_ATOP);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                    window.setBackgroundDrawable(background);
                }
            }
            if (filename.length() > 20) {
                tv_work.setText(filename.substring(0, 20) + "....ppt");
            } else {
                tv_work.setText(filename);
            }

        } else if (filename.endsWith(".txt")) {
            toolbar_view.setBackground(new ColorDrawable(Color.parseColor("#4D8CBD")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = getWindow();
                if (window != null) {
                    Drawable background = getResources().getDrawable(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.bg_gradient_toolbar); //bg_gradient is your gradient.
                    background.setColorFilter(Color.parseColor("#4D8CBD"), PorterDuff.Mode.SRC_ATOP);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                    window.setBackgroundDrawable(background);
                }
            }
            if (filename.length() > 20) {
                tv_work.setText(filename.substring(0, 20) + "....txt");
            } else {
                tv_work.setText(filename);
            }

        } else if (filename.endsWith(".xls") || filename.endsWith(".xlsm")) {
            toolbar_view.setBackground(new ColorDrawable(Color.parseColor("#08A747")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = getWindow();
                if (window != null) {
                    Drawable background = getResources().getDrawable(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.bg_gradient_toolbar); //bg_gradient is your gradient.
                    background.setColorFilter(Color.parseColor("#08A747"), PorterDuff.Mode.SRC_ATOP);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                    window.setBackgroundDrawable(background);
                }
            }
            if (filename.length() > 20) {
                tv_work.setText(filename.substring(0, 20) + "....xls");
            } else {
                tv_work.setText(filename);
            }

        } else if (filename.endsWith(".xml")) {
            toolbar_view.setBackground(new ColorDrawable(Color.parseColor("#FF000B")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = getWindow();
                if (window != null) {
                    Drawable background = getResources().getDrawable(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.bg_gradient_toolbar); //bg_gradient is your gradient.
                    background.setColorFilter(Color.parseColor("#FF000B"), PorterDuff.Mode.SRC_ATOP);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                    window.setBackgroundDrawable(background);
                }
            }
            if (filename.length() > 20) {
                tv_work.setText(filename.substring(0, 20) + "....xml");
            } else {
                tv_work.setText(filename);
            }

        } else if (filename.endsWith(".docx")) {
            toolbar_view.setBackground(new ColorDrawable(Color.parseColor("#0059D3")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = getWindow();
                if (window != null) {
                    Drawable background = getResources().getDrawable(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.bg_gradient_toolbar); //bg_gradient is your gradient.
                    background.setColorFilter(Color.parseColor("#0059D3"), PorterDuff.Mode.SRC_ATOP);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                    window.setBackgroundDrawable(background);
                }
            }
            if (filename.length() > 20) {
                tv_work.setText(filename.substring(0, 20) + "....docx");
            } else {
                tv_work.setText(filename);
            }

        } else if (filename.endsWith(".png") || filename.endsWith(".jpg") || filename.endsWith(".svg") || filename.endsWith(".gpeg") || filename.endsWith(".tiff")) {
            toolbar_view.setBackground(new ColorDrawable(Color.parseColor("#FF9900")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = getWindow();
                if (window != null) {
                    Drawable background = getResources().getDrawable(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.bg_gradient_toolbar); //bg_gradient is your gradient.
                    background.setColorFilter(Color.parseColor("#FF9900"), PorterDuff.Mode.SRC_ATOP);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                    window.setBackgroundDrawable(background);
                }
            }
            if (filename.length() > 20) {
                tv_work.setText(filename.substring(0, 20) + "....png");
            } else {
                tv_work.setText(filename);
            }

        } else if (filename.endsWith(".html")) {
            toolbar_view.setBackground(new ColorDrawable(Color.parseColor("#FF000B")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = getWindow();
                if (window != null) {
                    Drawable background = getResources().getDrawable(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.bg_gradient_toolbar); //bg_gradient is your gradient.
                    background.setColorFilter(Color.parseColor("#FF000B"), PorterDuff.Mode.SRC_ATOP);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                    window.setBackgroundDrawable(background);
                }
            }
            if (filename.length() > 20) {
                tv_work.setText(filename.substring(0, 20) + "....html");
            } else {
                tv_work.setText(filename);
            }

        } else if (filename.endsWith(".pptx")) {
            toolbar_view.setBackground(new ColorDrawable(Color.parseColor("#DD7719")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = getWindow();
                if (window != null) {
                    Drawable background = getResources().getDrawable(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.bg_gradient_toolbar); //bg_gradient is your gradient.
                    background.setColorFilter(Color.parseColor("#DD7719"), PorterDuff.Mode.SRC_ATOP);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                    window.setBackgroundDrawable(background);
                }
            }
            if (filename.length() > 20) {
                tv_work.setText(filename.substring(0, 20) + "....pptx");
            } else {
                tv_work.setText(filename);
            }

        } else if (filename.endsWith(".xlsx")) {
            toolbar_view.setBackground(new ColorDrawable(Color.parseColor("#08A747")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = getWindow();
                if (window != null) {
                    Drawable background = getResources().getDrawable(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.bg_gradient_toolbar); //bg_gradient is your gradient.
                    background.setColorFilter(Color.parseColor("#08A747"), PorterDuff.Mode.SRC_ATOP);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                    window.setBackgroundDrawable(background);
                }
            }
            if (filename.length() > 20) {
                tv_work.setText(filename.substring(0, 20) + "....xlsx");
            } else {
                tv_work.setText(filename);
            }

        } else {
            toolbar_view.setBackground(new ColorDrawable(Color.parseColor("#FF000B")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final Window window = getWindow();
                if (window != null) {
                    Drawable background = getResources().getDrawable(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.bg_gradient_toolbar); //bg_gradient is your gradient.
                    background.setColorFilter(Color.parseColor("#FF000B"), PorterDuff.Mode.SRC_ATOP);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                    window.setBackgroundDrawable(background);
                }
            }
            if (filename.length() > 20) {
                tv_work.setText(filename.substring(0, 20) + "....");
            } else {
                tv_work.setText(filename);
            }
        }
    }


    public void fileShare() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Uri.fromFile(new File(this.filePath)));
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        intent.putExtra("android.intent.extra.STREAM", arrayList);
        intent.setType("application/octet-stream");
        startActivity(Intent.createChooser(intent, getResources().getText(R.string.sys_share_title)));
    }

    public void initMarked() {
        boolean queryItem = this.dbService.queryItem(MainConstant.TABLE_STAR, this.filePath);
        this.marked = queryItem;
        if (queryItem) {
            // this.toolsbar.setCheckState(EventConstant.FILE_MARK_STAR_ID, (short) 1);
        } else {
            // this.toolsbar.setCheckState(EventConstant.FILE_MARK_STAR_ID, (short) 2);
        }
    }

    private void markFile() {
        this.marked = !this.marked;
    }

    public void resetTitle(String str) {
        if (str != null) {
            setTitle(str);
        }
    }


    @Override
    public Dialog onCreateDialog(int i) {
        return this.control.getDialog(this, i);
    }

    @Override
    public void updateToolsbarStatus() {
        AppFrame appFrame = this.appFrame;
        if (!(appFrame == null || this.isDispose)) {
            int childCount = appFrame.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.appFrame.getChildAt(i);
                if (childAt instanceof AToolsbar) {
                    ((AToolsbar) childAt).updateStatus();
                }
            }
        }
    }

    @Override
    public void setFindBackForwardState(boolean state) {

    }

    public IControl getControl() {
        return this.control;
    }

    public int getApplicationType() {
        return this.applicationType;
    }

    public String getFilePath() {
        return this.filePath;
    }

    @Override
    public boolean doActionEvent(int i, Object obj) {
        if (i == 0) {
            onBackPressed();
        } else if (i == 15) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getResources().getString(R.string.sys_url_wxiwei))));
        } else if (i == 20) {
            updateToolsbarStatus();
        } else if (i == 25) {
            setTitle((String) obj);
        } else if (i == 268435464) {
            markFile();
        } else if (i == 1073741828) {
            this.bottomBar.setFocusSheetButton(((Integer) obj).intValue());
        } else if (i == 536870912) {
        } else if (i != 536870913) {
            switch (i) {
                case EventConstant.APP_DRAW_ID /* 536870937 */:
                    this.control.getSysKit().getCalloutManager().setDrawingMode(1);
                    this.appFrame.post(new Runnable() { // from class: com.allreader.office.allofficefilereader.officereader.AppActivity.3
                        @Override
                        public void run() {
                            OfficeViewerScreenActivity.this.control.actionEvent(EventConstant.APP_INIT_CALLOUTVIEW_ID, null);
                        }
                    });
                    break;
                case EventConstant.APP_BACK_ID /* 536870938 */:
                    this.control.getSysKit().getCalloutManager().setDrawingMode(0);
                    break;
                case EventConstant.APP_PEN_ID /* 536870939 */:
                    if (!((Boolean) obj).booleanValue()) {
                        this.control.getSysKit().getCalloutManager().setDrawingMode(0);
                        break;
                    } else {
                        this.control.getSysKit().getCalloutManager().setDrawingMode(1);
                        this.appFrame.post(new Runnable() { // from class: com.allreader.office.allofficefilereader.officereader.AppActivity.4
                            @Override
                            public void run() {
                                OfficeViewerScreenActivity.this.control.actionEvent(EventConstant.APP_INIT_CALLOUTVIEW_ID, null);
                            }
                        });
                        break;
                    }
                case EventConstant.APP_ERASER_ID /* 536870940 */:
                    if (!((Boolean) obj).booleanValue()) {
                        this.control.getSysKit().getCalloutManager().setDrawingMode(0);
                        break;
                    } else {
                        this.control.getSysKit().getCalloutManager().setDrawingMode(2);
                        break;
                    }
                case EventConstant.APP_COLOR_ID /* 536870941 */:
                    break;
                default:
                    switch (i) {
                        case EventConstant.APP_FINDING /* 788529152 */:
                            String trim = ((String) obj).trim();
                            if (trim.length() > 0 && this.control.getFind().find(trim)) {
                                setFindBackForwardState(true);
                                break;
                            } else {
                                setFindBackForwardState(false);
                                this.toast.setText(getLocalString("DIALOG_FIND_NOT_FOUND"));
                                this.toast.show();
                                break;
                            }
                        case EventConstant.APP_FIND_BACKWARD /* 788529153 */:
                            if (this.control.getFind().findBackward()) {
                                break;
                            } else {
                                this.toast.setText(getLocalString("DIALOG_FIND_TO_BEGIN"));
                                this.toast.show();
                                break;
                            }
                        case EventConstant.APP_FIND_FORWARD /* 788529154 */:
                            try {
                                if (this.control.getFind().findForward()) {
                                    break;
                                } else {
                                    this.toast.setText(getLocalString("DIALOG_FIND_TO_END"));
                                    this.toast.show();
                                    break;
                                }
                            } catch (Exception e) {
                                this.control.getSysKit().getErrorKit().writerLog(e);
                                break;
                            }
                        default:
                            return false;
                    }
            }
        } else {
            fileShare();
        }
        return true;
    }

    @Override
    public void openFileFinish() {
        View view = new View(getApplicationContext());
        this.gapView = view;
        // view.setBackgroundColor(-7829368);
        this.control.getView().setBackgroundResource(com.ntdapp.document.viewer.reader.officereader.pdfreader.R.drawable.bg_office);
        this.appFrame.addView(this.gapView, new LinearLayout.LayoutParams(-1, 1));
        this.appFrame.addView(this.control.getView(), new LinearLayout.LayoutParams(-1, -1));
    }

    @Override
    public int getBottomBarHeight() {
        SheetBar sheetBar = this.bottomBar;
        if (sheetBar != null) {
            return sheetBar.getSheetbarHeight();
        }
        return 0;
    }

    @Override
    public String getAppName() {
        return getString(R.string.app_name);
    }

    @SuppressLint({"WrongConstant", "ResourceType"})
    @Override
    public void fullScreen(boolean z) {
        this.fullscreen = z;
        if (z) {
            this.wmParams.gravity = 53;
            this.wmParams.x = 5;
            this.f269wm.addView(this.penButton, this.wmParams);
            this.wmParams.gravity = 53;
            this.wmParams.x = 5;
            WindowManager.LayoutParams layoutParams = this.wmParams;
            layoutParams.y = layoutParams.height;
            this.f269wm.addView(this.eraserButton, this.wmParams);
            this.wmParams.gravity = 53;
            this.wmParams.x = 5;
            WindowManager.LayoutParams layoutParams2 = this.wmParams;
            layoutParams2.y = layoutParams2.height * 2;
            this.f269wm.addView(this.settingsButton, this.wmParams);
            this.wmParams.gravity = 19;
            this.wmParams.x = 5;
            this.wmParams.y = 0;
            this.f269wm.addView(this.pageUp, this.wmParams);
            this.wmParams.gravity = 21;
            this.f269wm.addView(this.pageDown, this.wmParams);
            ((View) getWindow().findViewById(16908310).getParent()).setVisibility(View.GONE);
            this.toolsbar.setVisibility(View.GONE);
            this.gapView.setVisibility(View.GONE);
            this.penButton.setState((short) 2);
            this.eraserButton.setState((short) 2);
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.flags |= 1024;
            getWindow().setAttributes(attributes);
            getWindow().addFlags(512);
            setRequestedOrientation(0);
            return;
        }
        this.f269wm.removeView(this.pageUp);
        this.f269wm.removeView(this.pageDown);
        this.f269wm.removeView(this.penButton);
        this.f269wm.removeView(this.eraserButton);
        this.f269wm.removeView(this.settingsButton);
        ((View) getWindow().findViewById(16908310).getParent()).setVisibility(View.VISIBLE);
        this.toolsbar.setVisibility(View.VISIBLE);
        this.gapView.setVisibility(View.VISIBLE);
        WindowManager.LayoutParams attributes2 = getWindow().getAttributes();
        attributes2.flags &= -1025;
        getWindow().setAttributes(attributes2);
        getWindow().clearFlags(512);
        setRequestedOrientation(4);
    }

    public void destroyEngine() {
        super.onBackPressed();
    }

    @Override
    public String getLocalString(String str) {
        return ResKit.instance().getLocalString(str);
    }

    @Override
    public boolean isWriteLog() {
        return this.writeLog;
    }

    @Override
    public void setWriteLog(boolean z) {
        this.writeLog = z;
    }

    @Override
    public Object getViewBackground() {
        return this.f268bg;
    }

    @Override
    public boolean isThumbnail() {
        return this.isThumbnail;
    }

    @Override
    public void setThumbnail(boolean z) {
        this.isThumbnail = z;
    }

    @Override
    public File getTemporaryDirectory() {
        File externalFilesDir = getExternalFilesDir(null);
        if (externalFilesDir != null) {
            return externalFilesDir;
        }
        return getFilesDir();
    }

    @Override
    public void dispose() {
        this.isDispose = true;
        MainControl mainControl = this.control;
        if (mainControl != null) {
            mainControl.dispose();
            this.control = null;
        }
        this.toolsbar = null;
        this.bottomBar = null;
        DBService dBService = this.dbService;
        if (dBService != null) {
            dBService.dispose();
            this.dbService = null;
        }
        AppFrame appFrame = this.appFrame;
        if (appFrame != null) {
            int childCount = appFrame.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.appFrame.getChildAt(i);
                if (childAt instanceof AToolsbar) {
                    ((AToolsbar) childAt).dispose();
                }
            }
            this.appFrame = null;
        }
        if (this.f269wm != null) {
            this.f269wm = null;
            this.wmParams = null;
            this.pageUp.dispose();
            this.pageDown.dispose();
            this.penButton.dispose();
            this.eraserButton.dispose();
            this.settingsButton.dispose();
            this.pageUp = null;
            this.pageDown = null;
            this.penButton = null;
            this.eraserButton = null;
            this.settingsButton = null;
        }
    }
}
