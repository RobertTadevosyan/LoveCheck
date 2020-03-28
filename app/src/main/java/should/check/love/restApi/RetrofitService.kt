package should.check.love.restApi

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import should.check.love.BuildConfig
import should.check.love.LoveApp
import should.check.love.R

object RetrofitService {
    fun getJsonApi(): JsonApi {
        return Retrofit
            .Builder()
            .baseUrl(LoveApp.getInstance().resources.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(JsonApi::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(additionalHeadersInterceptor())
            .addInterceptor(defaultLoggerInterceptor())
            .build()
    }

    private fun defaultLoggerInterceptor(): HttpLoggingInterceptor {
        val defaultLogger = HttpLoggingInterceptor()
        if (BuildConfig.BUILD_TYPE.equals("debug", true)) {
            defaultLogger.level = HttpLoggingInterceptor.Level.BODY
        } else {
            defaultLogger.level = HttpLoggingInterceptor.Level.NONE
        }
        return defaultLogger
    }


    private fun additionalHeadersInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("x-rapidapi-host", "love-calculator.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "2940cbe69amsh63ede5d9f906568p10abfbjsn8a3b2d5fcff2")
                .build()
            chain.proceed(request)
        }

    }
}