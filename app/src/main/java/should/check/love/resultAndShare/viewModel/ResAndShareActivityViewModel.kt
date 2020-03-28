package should.check.love.main.viewModel

import androidx.lifecycle.LiveData
import should.check.love.base.BaseViewModel
import should.check.love.main.model.CheckResult
import should.check.love.main.model.MainActivityRepository
import should.check.love.resultAndShare.ResAndShareActivityRepository


class ResAndShareActivityViewModel : BaseViewModel<ResAndShareActivityRepository>() {
    var checkResult: CheckResult? = null
    override fun createRepo(): ResAndShareActivityRepository {
        return ResAndShareActivityRepository()
    }


}