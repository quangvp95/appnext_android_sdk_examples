package com.example.appnextapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.SwitchCompat
import com.appnext.ads.fullscreen.FullScreenVideo
import com.appnext.ads.fullscreen.RewardedVideo
import com.appnext.ads.fullscreen.Video
import com.appnext.ads.interstitial.Interstitial
import com.appnext.appnextnativeadsexamples.InFeedExample1
import com.appnext.appnextnativeadsexamples.InFeedExample2
import com.appnext.banners.BannerAdRequest
import com.appnext.banners.BannerListener
import com.appnext.banners.BannerView
import com.appnext.base.Appnext
import com.appnext.core.AppnextAdCreativeType
import com.appnext.core.AppnextError
import com.appnext.core.callbacks.OnAdClicked
import com.appnext.core.callbacks.OnAdClosed
import com.appnext.core.callbacks.OnAdError
import com.appnext.core.callbacks.OnAdLoaded
import com.appnext.core.callbacks.OnVideoEnded
import com.appnext.nativeads.NativeAd
import com.appnext.nativeads.designed_native_ads.AppnextDesignedNativeAdData
import com.appnext.nativeads.designed_native_ads.interfaces.AppnextDesignedNativeAdViewCallbacks
import com.appnext.nativeads.designed_native_ads.views.AppnextDesignedNativeAdView

class MainActivity : ComponentActivity(), View.OnClickListener {
    private var interstitial: Interstitial? = null
    private var fullscreen: FullScreenVideo? = null
    private var rewardedMulti: RewardedVideo? = null
    private var rewardedNormal: RewardedVideo? = null

    private var onVideoEnded: OnVideoEnded? = null
    private var onAdLoaded: OnAdLoaded? = null
    private var onAdError: OnAdError? = null
    private var onAdClosed: OnAdClosed? = null
    private var onAdClicked: OnAdClicked? = null

    private var bannerView1: BannerView? = null
    private var bannerView2: BannerView? = null
    private var bannerView3: BannerView? = null
    private var interLoadButton: Button? = null
    private var interShowButton: Button? = null
    private var fullScreenLoadButton: Button? = null
    private var fullScreenShowButton: Button? = null
    private var multiRewardedLoadButton: Button? = null
    private var multiRewardedShowButton: Button? = null
    private var normalRewardedLoadButton: Button? = null
    private var normalRewardedShowButton: Button? = null
    private var bannerSpinner: Spinner? = null
    private val placementIDInter = "103029bd-5625-4ba2-9293-8a29461b8692"
    private val placementIDFullScreen = "faed8533-5061-418b-8ad1-7b19a066ef8a"
    private val placementIDRewardedNormal = "37d5b4a5-500b-44fb-b745-788084be2794"
    private val placementIDRewardedMulti = "0445b326-976b-4d49-99b6-ccc0c5284346"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Appnext.init(this)
        setVies()
        initSuggest()

        onVideoEnded = OnVideoEnded { showToast("videoEnded") }
        onAdLoaded = object : OnAdLoaded {
            fun adLoaded(banner: String?) {
                showToast("adLoaded")
            }

            override fun adLoaded(p0: String?, p1: AppnextAdCreativeType?) {
                TODO("Not yet implemented")
            }
        }

        onAdError = OnAdError { error -> showToast("adError $error") }
        onAdClosed = OnAdClosed { showToast("onAdClosed") }
        onAdClicked = OnAdClicked { showToast("adClicked") }

        interstitial = Interstitial(this, placementIDInter)
        interstitial?.onAdClickedCallback = onAdClicked
        interstitial?.onAdClosedCallback = onAdClosed
        interstitial?.onAdErrorCallback = onAdError
        interstitial?.onAdLoadedCallback = onAdLoaded

        fullscreen = FullScreenVideo(this, placementIDFullScreen)
        fullscreen?.onAdClickedCallback = onAdClicked
        fullscreen?.onAdClosedCallback = onAdClosed
        fullscreen?.onAdErrorCallback = onAdError
        fullscreen?.onAdLoadedCallback = onAdLoaded
        fullscreen?.onVideoEndedCallback = onVideoEnded
        fullscreen?.isBackButtonCanClose = true
        fullscreen?.isShowClose = true

        rewardedMulti = RewardedVideo(this, placementIDRewardedMulti)
        rewardedMulti?.mode = RewardedVideo.VIDEO_MODE_MULTI
        rewardedMulti?.videoLength = Video.VIDEO_LENGTH_SHORT
        rewardedMulti?.multiTimerLength = 9
        rewardedMulti?.rollCaptionTime = -1
        rewardedMulti?.onAdClickedCallback = onAdClicked
        rewardedMulti?.onAdClosedCallback = onAdClosed
        rewardedMulti?.onAdErrorCallback = onAdError
        rewardedMulti?.onAdLoadedCallback = onAdLoaded
        rewardedMulti?.onVideoEndedCallback = onVideoEnded
        rewardedMulti?.setRewardedServerSidePostback(
            "TransactionId",
            "UserId",
            "TypeCurrency",
            "Amount",
            "CustomParameter"
        )

        rewardedNormal = RewardedVideo(this, placementIDRewardedNormal)
        rewardedNormal?.mode = RewardedVideo.VIDEO_MODE_NORMAL
        rewardedNormal?.onAdClickedCallback = onAdClicked
        rewardedNormal?.onAdClosedCallback = onAdClosed
        rewardedNormal?.onAdErrorCallback = onAdError
        rewardedNormal?.onAdLoadedCallback = onAdLoaded
        rewardedNormal?.onVideoEndedCallback = onVideoEnded
        rewardedNormal?.setRewardedServerSidePostback(
            "TransactionId",
            "UserId",
            "TypeCurrency",
            "Amount",
            "CustomParameter"
        )

        val streamingSwitch = findViewById<View>(R.id.streamingSwitch) as Switch
        streamingSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            Video.setStreamingMode(isChecked)
        }
    }

    private fun setVies() {
        interLoadButton = findViewById<View>(R.id.inter_load_button) as Button
        interLoadButton?.setOnClickListener(this)
        interShowButton = findViewById<View>(R.id.inter_show_button) as Button
        interShowButton!!.setOnClickListener(this)
        fullScreenLoadButton = findViewById<View>(R.id.full_screen_load_button) as Button
        fullScreenLoadButton!!.setOnClickListener(this)
        fullScreenShowButton = findViewById<View>(R.id.full_screen_show_button) as Button
        fullScreenShowButton!!.setOnClickListener(this)
        multiRewardedLoadButton = findViewById<View>(R.id.multi_rewarded_load_button) as Button
        multiRewardedLoadButton!!.setOnClickListener(this)
        multiRewardedShowButton = findViewById<View>(R.id.multi_rewarded_show_button) as Button
        multiRewardedShowButton!!.setOnClickListener(this)
        normalRewardedLoadButton = findViewById<View>(R.id.normal_rewarded_load_button) as Button
        normalRewardedLoadButton!!.setOnClickListener(this)
        normalRewardedShowButton = findViewById<View>(R.id.normal_rewarded_show_button) as Button
        normalRewardedShowButton!!.setOnClickListener(this)
        bannerSpinner = findViewById<View>(R.id.spinner) as Spinner
        bannerView1 = findViewById<View>(R.id.banner1) as BannerView
        bannerView1?.setVisibility(View.INVISIBLE)
        bannerView2 = findViewById<View>(R.id.banner2) as BannerView
        bannerView2!!.visibility = View.INVISIBLE
        bannerView3 = findViewById<View>(R.id.banner3) as BannerView
        bannerView3!!.visibility = View.INVISIBLE
        val arraySpinner = arrayOf(
            "Choose a banner size to show",
            "BANNER (320x50)",
            "LARGE BANNER (320x100)",
            "MEDIUM RECTANGLE (300x250)"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySpinner)
        bannerSpinner?.adapter = adapter
        bannerSpinner?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0, 1 -> {
                        bannerView1?.loadAd(BannerAdRequest())
                        bannerView1?.setVisibility(View.VISIBLE)
                        bannerView1?.setBannerListener(object : BannerListener() {
                            override fun onError(appnextError: AppnextError) {
                                super.onError(appnextError)
                                showToast("No ads")
                            }
                        })
                        bannerView2!!.visibility = View.INVISIBLE
                        bannerView3!!.visibility = View.INVISIBLE
                    }

                    2 -> {
                        bannerView2!!.loadAd(BannerAdRequest())
                        bannerView1?.visibility = View.INVISIBLE
                        bannerView2!!.visibility = View.VISIBLE
                        bannerView2!!.setBannerListener(object : BannerListener() {
                            override fun onError(appnextError: AppnextError) {
                                super.onError(appnextError)
                                showToast("No ads")
                            }
                        })
                        bannerView3!!.visibility = View.INVISIBLE
                    }

                    3 -> {
                        bannerView3!!.loadAd(BannerAdRequest().setMute(true).setAutoPlay(true))
                        bannerView1?.visibility = View.INVISIBLE
                        bannerView2!!.visibility = View.INVISIBLE
                        bannerView3!!.visibility = View.VISIBLE
                        bannerView3!!.setBannerListener(object : BannerListener() {
                            override fun onError(appnextError: AppnextError) {
                                super.onError(appnextError)
                                showToast("No ads")
                            }
                        })
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                bannerView1?.visibility = View.INVISIBLE
                bannerView2!!.visibility = View.INVISIBLE
                bannerView3!!.visibility = View.INVISIBLE
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.in_feed_1_button -> startActivity(Intent(this, InFeedExample1::class.java))
            R.id.in_feed_2_button -> startActivity(Intent(this, InFeedExample2::class.java))
            R.id.inter_load_button -> interstitial!!.loadAd()
            R.id.inter_show_button -> interstitial!!.showAd()
            R.id.full_screen_load_button -> fullscreen!!.loadAd()
            R.id.full_screen_show_button -> fullscreen!!.showAd()
            R.id.multi_rewarded_load_button -> rewardedMulti!!.loadAd()
            R.id.multi_rewarded_show_button -> rewardedMulti!!.showAd()
            R.id.normal_rewarded_load_button -> rewardedNormal!!.loadAd()
            R.id.normal_rewarded_show_button -> rewardedMulti!!.showAd()
            else -> {}
        }
    }

    private fun initSuggest() {
        val appAd = findViewById<View>(R.id.designed_native_ads) as AppnextDesignedNativeAdView
        val adview : NativeAd
        appAd.load(placementIDInter, object : AppnextDesignedNativeAdViewCallbacks {
            override fun onAppnextAdsLoadedSuccessfully() {
                Log.e("nguyenleee", "onAppnextAdsLoadedSuccessfully")
            }

            override fun onAdClicked(p0: AppnextDesignedNativeAdData?) {
                Log.e("nguyenleee", "AppnextDesignedNativeAdData")
            }

            override fun onAppnextAdsError(p0: AppnextError?) {
                Log.e("nguyenleee", "onAppnextAdsError")
            }

        })
    }

    private var mToast: Toast? = null
    private fun showToast(text: String) {
        mToast?.cancel()
        mToast = Toast.makeText(this, text, Toast.LENGTH_LONG)
        mToast?.show()
    }
}
