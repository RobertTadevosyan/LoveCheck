package should.check.love.base

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response
import should.check.love.main.model.CheckResult
import should.check.love.main.model.Error

abstract class BaseRepository {
    protected var disposable = CompositeDisposable()

    fun onUIDestroyed() {
        if (!disposable.isDisposed) {
            disposable.dispose()
            disposable.clear()
        }
    }

    var errorLiveData: MutableLiveData<Error> = MutableLiveData()

    protected fun onError(response: Response<CheckResult>) {
        errorLiveData.postValue(Error(response.code(), response.errorBody()?.string()))
    }
}