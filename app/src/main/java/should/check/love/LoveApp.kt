package should.check.love

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator


class LoveApp : Application() {
    companion object {
        private lateinit var instance: LoveApp

        public fun getInstance(): LoveApp {
            return instance
        }
    }

    var englishTranslator: FirebaseTranslator? = null

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        instance = this
    }
}