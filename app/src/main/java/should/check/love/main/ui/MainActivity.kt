package should.check.love.main.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import should.check.love.R
import should.check.love.base.BaseActivity
import should.check.love.base.Util
import should.check.love.main.model.CheckResult
import should.check.love.main.model.MainActivityRepository
import should.check.love.main.viewModel.MainActivityViewModel
import should.check.love.resultAndShare.ui.ResAndShareActivity

class MainActivity : BaseActivity<MainActivityRepository, MainActivityViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnClickListeners()
        MobileAds.initialize(this) {}
        loadAd()
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    override fun onResume() {
        super.onResume()
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.getCheckResultLiveData().observe(this, Observer {
            startHandler(it)
        })
    }

    private fun startHandler(checkResult: CheckResult) {
        Handler().postDelayed({
            stopAnimation()
            val intent = Intent(this, ResAndShareActivity::class.java)
            intent.putExtra("data", checkResult)
            startActivity(intent)
        }, 1000)
    }

    private fun setOnClickListeners() {
        btn_check.setOnClickListener {
            if (areFieldsValid()) {
                Util.hideKeyboard(this)
                startAnimation()
                viewModel.checkLove(et_first_name.text.toString(), et_second_name.text.toString())
            } else {
                Toast.makeText(this, "Please enter names in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        return et_first_name.text.toString().trim().isNotEmpty() && et_second_name.text.toString().trim().isNotEmpty()
    }

    private fun startAnimation() {
        cl_loading.visibility = View.VISIBLE
        lav_thumbDown.progress = 0f
        lav_thumbDown.playAnimation()
    }

    private fun stopAnimation() {
        cl_loading.visibility = View.GONE
        lav_thumbDown.progress = 0f
        lav_thumbDown.pauseAnimation()
    }


    override fun getViewModelClass(): Class<MainActivityViewModel> {
        return MainActivityViewModel::class.java
    }

    override fun onError() {
        stopAnimation()
    }


}
