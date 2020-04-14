package should.check.love.main.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import kotlinx.android.synthetic.main.activity_main.*
import should.check.love.LoveApp
import should.check.love.R
import should.check.love.base.BaseActivity
import should.check.love.base.Util
import should.check.love.main.model.CheckResult
import should.check.love.main.model.Error
import should.check.love.main.model.MainActivityRepository
import should.check.love.main.viewModel.MainActivityViewModel
import should.check.love.resultAndShare.ui.ResAndShareActivity

class MainActivity : BaseActivity<MainActivityRepository, MainActivityViewModel>() {

    private lateinit var mInterstitialAd: InterstitialAd
    private var currentNativeAd: UnifiedNativeAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnClickListeners()
        MobileAds.initialize(this) {}
    }

    override fun onResume() {
        super.onResume()
        loadAds()
    }

    private fun loadAds() {
        adView.loadAd(LoveApp.getInstance().getAdRequest())
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-7373646242058248/8054721167"
//        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/8691691433"  /*Test ad*/
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                if (!mInterstitialAd.isLoaded && !mInterstitialAd.isLoading) {
                    mInterstitialAd.loadAd(AdRequest.Builder().build())
                }
            }
        }
        mInterstitialAd.loadAd(LoveApp.getInstance().getAdRequest())
        loadNativeAd()
    }

    private fun loadNativeAd() {
        try {
            val videoOptions = VideoOptions.Builder()
                .setStartMuted(true)
                .build()

            val adOptions = NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build()

            val builder = AdLoader.Builder(this, "ca-app-pub-7373646242058248/6937645263")
//            val builder = AdLoader.Builder(this, "ca-app-pub-3940256099942544/1044960115") /*Test ad*/
            builder.withNativeAdOptions(adOptions)
            builder.forUnifiedNativeAd { ad: UnifiedNativeAd ->
                currentNativeAd?.destroy()
                currentNativeAd = ad
                loadNativeAdViews(ad)
            }.build()
            val adLoader = builder.build()

            adLoader.loadAd(LoveApp.getInstance().getAdRequest())
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    private fun loadNativeAdViews(unifiedNativeAd: UnifiedNativeAd) {
        try {
            val adView = layoutInflater
                .inflate(R.layout.ad_unified, null) as UnifiedNativeAdView
            populateUnifiedNativeAdView(unifiedNativeAd, adView)
            // Assumes you have a placeholder FrameLayout in your View layout
            // (with id ad_frame) where the ad is to be placed.
            ad_frame.removeAllViews()
            ad_frame.addView(adView)
        } catch (throwable: Throwable) {
            ad_frame.removeAllViews()
            throwable.printStackTrace()
        }
    }

    private fun populateUnifiedNativeAdView(
        ad: UnifiedNativeAd,
        adView: UnifiedNativeAdView
    ) {
        val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
        headlineView.text = ad.headline
        adView.headlineView = headlineView

        val adAdvertiser = adView.findViewById<TextView>(R.id.ad_advertiser)
        adAdvertiser.text = ad.advertiser
        adView.advertiserView = adAdvertiser

        val bodyView = adView.findViewById<TextView>(R.id.ad_body)
        bodyView.text = ad.body
        adView.bodyView = bodyView

        val callToActionView = adView.findViewById<TextView>(R.id.ad_call_to_action)
        callToActionView.text = ad.callToAction
        adView.callToActionView = callToActionView

        val iconView = adView.findViewById<ImageView>(R.id.ad_app_icon)
        iconView.setImageDrawable(ad.icon.drawable)
        adView.iconView = iconView

        val priceView = adView.findViewById<TextView>(R.id.ad_price)
        priceView.text = ad.price
        adView.priceView = priceView

        val starRatingView = adView.findViewById<RatingBar>(R.id.ad_stars)
        starRatingView.rating = ad.starRating?.toFloat() ?: 0f
        adView.starRatingView = starRatingView

        val storeView = adView.findViewById<TextView>(R.id.ad_store)
        storeView.text = ad.store
        adView.storeView = storeView


        val mediaView = adView.findViewById<MediaView>(R.id.ad_media)
        mediaView.setMediaContent(ad.mediaContent)
        adView.mediaView = mediaView
//        mediaView.setImageScaleType(ImageView.ScaleType.CE)

        adView.setNativeAd(ad)
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.getCheckResultLiveData().observe(this, Observer {
            startHandler(it)
        })
    }

    private fun startHandler(checkResult: CheckResult) {
        Handler().postDelayed({
            stopAnimation()
            val intent = Intent(this, ResAndShareActivity::class.java)
            intent.putExtra("data", checkResult)
            startActivity(intent)
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            }
        }, 1000)
    }

    private fun setOnClickListeners() {
        btn_check.setOnClickListener {
            if (areFieldsValid()) {
                Util.hideKeyboard(this)
                startAnimation()
                viewModel.checkLove(et_first_name.text.toString(), et_second_name.text.toString())
            } else {
                LoveApp.getInstance().getTextToSpeechWithDefaultLocale()?.speak(getString(R.string.enter_all_values), TextToSpeech.QUEUE_FLUSH, null)
                Toast.makeText(this, getString(R.string.enter_all_values), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        return et_first_name.text.toString().trim().isNotEmpty() && et_second_name.text.toString().trim().isNotEmpty()
    }

    private fun startAnimation() {
        cl_loading.visibility = View.VISIBLE
        lav_thumbDown.progress = 0f
        lav_thumbDown.playAnimation()
    }

    private fun stopAnimation() {
        cl_loading.visibility = View.GONE
        lav_thumbDown.progress = 0f
        lav_thumbDown.pauseAnimation()
    }


    override fun getViewModelClass(): Class<MainActivityViewModel> {
        return MainActivityViewModel::class.java
    }

    override fun onError(error: Error) {
        stopAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        currentNativeAd?.destroy()
        viewModel.onUIDestroyed()
    }
}
