package com.ntdapp.document.viewer.reader.officereader.pdfreader.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazic.ads.callback.InterCallback;
import com.amazic.ads.util.Admod;
import com.example.ads.AppIronSource;
import com.example.ads.funtion.AdCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.ntdapp.document.viewer.reader.officereader.pdfreader.R;
import com.ntdapp.document.viewer.reader.officereader.pdfreader.activity.HomeScreenActivity;
import com.ntdapp.document.viewer.reader.officereader.pdfreader.adapter.IntroAdapter;
import com.ntdapp.document.viewer.reader.officereader.pdfreader.model.Guid;

import java.util.ArrayList;

public class IntroScreenActivity extends AppCompatActivity {
    private TextView tv_skip, tv_next, tv_back;
    private ViewPager2 view_pager2;
    private ImageView circle_indicator;
    private IntroAdapter introAdapter;
    private ArrayList<Guid> mHelpGuid;
    private View view_1, view_2, view_3;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set status bar gradient
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            if (window != null) {
                Drawable background = getResources().getDrawable(R.drawable.bg_gradient_toolbar); //bg_gradient is your gradient.
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
                window.setBackgroundDrawable(background);
            }
        }
        setContentView(R.layout.activity_intro_screen);
        //init ads
        if (!AppIronSource.getInstance().isInterstitialReady()) {
            AppIronSource.getInstance().loadInterstitial(this, new AdCallback());
        }
        tv_skip = findViewById(R.id.tv_skip);
        tv_next = findViewById(R.id.tv_next);
        tv_back = findViewById(R.id.tv_back);
        view_pager2 = findViewById(R.id.view_pager2);
        circle_indicator = findViewById(R.id.circle);
        view_1 = findViewById(R.id.view_1);
        view_2 = findViewById(R.id.view_2);
        view_3 = findViewById(R.id.view_3);
        mHelpGuid = new ArrayList<>();
        mHelpGuid.add(new Guid(R.drawable.ic_intro1, getResources().getString(R.string.Professional_Document_Reader), getResources().getString(R.string.Our_app_can_read)));
        mHelpGuid.add(new Guid(R.drawable.ic_intro2, getResources().getString(R.string.Manager_Document_Tools), getResources().getString(R.string.Supply_many_integrated)));
        mHelpGuid.add(new Guid(R.drawable.ic_intro3, getResources().getString(R.string.Snap_Image_on_Document), getResources().getString(R.string.Easy_Snap_Image)));

        introAdapter = new IntroAdapter(mHelpGuid, IntroScreenActivity.this);
        view_pager2.setAdapter(introAdapter);
        view_pager2.setClipToPadding(false);
        view_pager2.setClipChildren(false);
        view_pager2.setOffscreenPageLimit(3);
        view_pager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(100));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View view, float position) {
                float r = 1 - Math.abs(position);
                view.setScaleY(0.8f + r * 0.2f);
                float absPosition = Math.abs(position);
                view.setAlpha(1.0f - (1.0f - 0.3f) * absPosition);
            }
        });
        view_pager2.setPageTransformer(compositePageTransformer);
        view_pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("InvalidAnalyticsName")
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        tv_next.setText(getResources().getString(R.string.next));
                        tv_skip.setVisibility(View.VISIBLE);
                        tv_back.setVisibility(View.GONE);
                        view_1.setBackground(getResources().getDrawable(R.drawable.bg_circle_blue));
                        view_2.setBackground(getResources().getDrawable(R.drawable.bg_circle));
                        view_3.setBackground(getResources().getDrawable(R.drawable.bg_circle));
                        tv_next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                view_pager2.setCurrentItem(position + 1);
                            }
                        });
                        break;
                    case 1:
                        tv_next.setText(getResources().getString(R.string.next));
                        tv_back.setVisibility(View.VISIBLE);
                        view_2.setBackground(getResources().getDrawable(R.drawable.bg_circle_blue));
                        view_1.setBackground(getResources().getDrawable(R.drawable.bg_circle));
                        view_3.setBackground(getResources().getDrawable(R.drawable.bg_circle));
                        tv_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                view_pager2.setCurrentItem(position - 1);
                            }
                        });
                        tv_next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                view_pager2.setCurrentItem(position + 1);
                            }
                        });
                        tv_skip.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        tv_next.setText(getResources().getString(R.string.get_started));
                        view_3.setBackground(getResources().getDrawable(R.drawable.bg_circle_blue));
                        view_2.setBackground(getResources().getDrawable(R.drawable.bg_circle));
                        view_1.setBackground(getResources().getDrawable(R.drawable.bg_circle));
                        tv_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                view_pager2.setCurrentItem(position - 1);
                            }
                        });
                        tv_next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /*Admod.getInstance().showInterAds(IntroScreenActivity.this, mInterstitialAd, new InterCallback() {
                                    @Override
                                    public void onAdClosed() {
                                        gotoNextScreen();
                                    }

                                    @Override
                                    public void onAdFailedToLoad(LoadAdError i) {
                                        gotoNextScreen();
                                    }

                                });*/
                                if (AppIronSource.getInstance().isInterstitialReady()) {
                                    AppIronSource.getInstance().showInterstitial(IntroScreenActivity.this, new AdCallback() {
                                        @Override
                                        public void onAdClosed() {
                                            super.onAdClosed();
                                            gotoNextScreen();
                                        }
                                    });
                                } else {
                                    gotoNextScreen();
                                }
                            }
                        });
                        tv_skip.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Admod.getInstance().showInterAds(IntroScreenActivity.this, mInterstitialAd, new InterCallback() {
                    @Override
                    public void onAdClosed() {
                        gotoNextScreen();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError i) {
                        gotoNextScreen();
                    }

                });*/
                if (AppIronSource.getInstance().isInterstitialReady()) {
                    AppIronSource.getInstance().showInterstitial(IntroScreenActivity.this, new AdCallback() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            gotoNextScreen();
                        }
                    });
                } else {
                    gotoNextScreen();
                }
            }
        });
        //loadAdsInter();
    }

    private void gotoNextScreen() {
        startActivity(new Intent(IntroScreenActivity.this, HomeScreenActivity.class));
        finish();
    }

    private void loadAdsInter() {
        Admod.getInstance().loadInterAds(IntroScreenActivity.this, getString(R.string.inter_intro), new InterCallback() {
            @Override
            public void onInterstitialLoad(InterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                mInterstitialAd = interstitialAd;
            }
        });
    }
}