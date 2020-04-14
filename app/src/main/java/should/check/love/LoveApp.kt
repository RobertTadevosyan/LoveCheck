package should.check.love

import android.app.Application
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import io.reactivex.plugins.RxJavaPlugins
import java.util.*


class LoveApp : Application() {
    companion object {
        private lateinit var instance: LoveApp

        public fun getInstance(): LoveApp {
            return instance
        }
    }

    var txtToSpeech: TextToSpeech? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        RxJavaPlugins.setErrorHandler { it.printStackTrace() }
        initTextToSpeech()
        Alarm().cancelAlarm(this)
        Alarm().setAlarm(this)
        if (BuildConfig.DEBUG) {
            val testDeviceIds = listOf("CC1E95044FFF3E161BE9EB8381D345ED")
            val configuration =
                RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
            MobileAds.setRequestConfiguration(configuration)
        }
    }

    public fun getTextToSpeechWithDefaultLocale(): TextToSpeech? {
        var locale =
            txtToSpeech?.availableLanguages?.find { it.language == Locale.getDefault().language }
        if (locale == null) {
            locale = Locale.ENGLISH
        }
        txtToSpeech?.language = locale
        return txtToSpeech
    }

    private fun initTextToSpeech() {
        txtToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener {
            var locale =
                txtToSpeech?.availableLanguages?.find { it.language == Locale.getDefault().language }
            if (locale == null) {
                locale = Locale.ENGLISH
            }
            txtToSpeech?.language = locale
        })
    }

    public fun getAdRequest(): AdRequest {
        val extras = Bundle()
        extras.putString("max_ad_content_rating", "MA")
        return AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()
    }

}