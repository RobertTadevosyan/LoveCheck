package should.check.love.main.model

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import should.check.love.base.BaseRepository

class MainActivityRepository : BaseRepository() {
    var checkResultLiveData: MutableLiveData<CheckResult> = MutableLiveData()

    fun checkLove(firstName: String, secondName: String) {
        disposable.add(
            getCheckLoveObservable(firstName, secondName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccessful && it.body() != null) {
                        checkResultLiveData.postValue(
                            Gson().fromJson(
                                it.body()?.string(),
                                CheckResult::class.java
                            )
                        )
                    } else {
                        errorLiveData.postValue(Error(it.code(), it.message()))
                    }
                }, {
                    errorLiveData.postValue(Error(999, it.message))
                })
        )
    }

    private fun getCheckLoveObservable(
        firstName: String,
        secondName: String
    ): Observable<Response> {
        return Observable.create { emmiter ->
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://love-calculator.p.rapidapi.com/getPercentage?fname=${firstName}&sname=${secondName}")
                .get()
                .addHeader("x-rapidapi-host", "love-calculator.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "2940cbe69amsh63ede5d9f906568p10abfbjsn8a3b2d5fcff2")
                .build()
            val response = client.newCall(request).execute()
            if (!emmiter.isDisposed) {
                emmiter.onNext(response)
                emmiter.onComplete()
            }
        }
    }


}