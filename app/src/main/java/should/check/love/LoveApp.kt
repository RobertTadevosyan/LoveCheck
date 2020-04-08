package should.check.love

import android.app.Application

import io.reactivex.plugins.RxJavaPlugins


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
        RxJavaPlugins.setErrorHandler { it.printStackTrace() }
    }
}