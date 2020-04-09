package should.check.love

import android.app.Application
import android.speech.tts.TextToSpeech
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

}