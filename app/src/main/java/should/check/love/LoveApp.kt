package should.check.love

import android.app.Application

class LoveApp : Application() {
    companion object {
        private lateinit var instance: LoveApp

        public fun getInstance(): LoveApp {
            return instance
        }

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}