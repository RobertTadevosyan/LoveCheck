package should.check.love.main.viewModel

import androidx.lifecycle.LiveData
import should.check.love.base.BaseViewModel
import should.check.love.main.model.CheckResult
import should.check.love.main.model.MainActivityRepository


class MainActivityViewModel : BaseViewModel<MainActivityRepository>() {

    fun checkLove(firstName: String, secondName: String) {
        repository.checkLove(firstName, secondName)
    }

    override fun createRepo(): MainActivityRepository {
        return MainActivityRepository()
    }

    fun getCheckResultLiveData(): LiveData<CheckResult> {
        return repository.checkResultLiveData
    }


}