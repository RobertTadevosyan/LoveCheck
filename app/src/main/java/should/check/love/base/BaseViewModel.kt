package should.check.love.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import should.check.love.main.model.Error

abstract class BaseViewModel<R : BaseRepository> : ViewModel() {
    protected var repository: R

    init {
        repository = createRepo()
    }

    abstract fun createRepo(): R

    fun onUIDestroyed() {
        repository.onUIDestroyed()
    }


    fun getErrorLiveData(): LiveData<Error> {
        return repository.errorLiveData
    }

}