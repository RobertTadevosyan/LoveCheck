package should.check.love.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import should.check.love.main.model.Error

abstract class BaseActivity<R : BaseRepository, VM : BaseViewModel<R>> : AppCompatActivity() {

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(getViewModelClass())
        setObservers()
    }

    abstract fun getViewModelClass(): Class<VM>

    protected open fun setObservers() {
        viewModel.getErrorLiveData().observe(this, Observer {
            onError(it)
        })
    }

    abstract fun onError(error: Error)

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onUIDestroyed()
    }
}