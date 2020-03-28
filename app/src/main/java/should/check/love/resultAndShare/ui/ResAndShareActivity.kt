package should.check.love.resultAndShare.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_res_and_share.*
import kotlinx.android.synthetic.main.activity_res_and_share.adView
import should.check.love.R
import should.check.love.base.BaseActivity
import should.check.love.main.model.CheckResult
import should.check.love.main.viewModel.ResAndShareActivityViewModel
import should.check.love.resultAndShare.ResAndShareActivityRepository
import java.util.*


class ResAndShareActivity :
    BaseActivity<ResAndShareActivityRepository, ResAndShareActivityViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res_and_share)
        val checkResult = intent.extras?.getParcelable<CheckResult>("data")
        initUI(checkResult)
        setOnClickListeners()
        loadAd()
    }

    override fun onResume() {
        super.onResume()
        loadAd()
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }


    @SuppressLint("SetTextI18n")
    private fun initUI(checkResult: CheckResult?) {
        txt_result.text = checkResult?.result
        txt_names_result.text =
            makeFirstLetterCapital(checkResult?.fname) + " and " + makeFirstLetterCapital(
                checkResult?.sname
            ) + "\n${checkResult?.percentage} %"
        val percentage = checkResult?.percentage?.toInt() ?: 0
        when {
            percentage < 35 -> {
                showBadLove()
            }
            percentage in 35..50 -> {
                showNormLove()
            }
            else -> {
                showGoodLove()
            }
        }
    }

    private fun makeFirstLetterCapital(text: String?): String {
        if (text?.length!! < 1) {
            return ""
        }
        return text.substring(
            0,
            1
        ).toUpperCase(Locale.getDefault()) + text.substring(1).toLowerCase(Locale.getDefault())
    }

    private fun setOnClickListeners() {

    }

    private fun showGoodLove() {
        norm_love_anim.visibility = View.INVISIBLE
        bad_love_anim.visibility = View.INVISIBLE
        good_love_anim.visibility = View.VISIBLE
        good_love_anim.progress = 0f
        good_love_anim.playAnimation()
    }

    private fun showNormLove() {
        good_love_anim.visibility = View.INVISIBLE
        bad_love_anim.visibility = View.INVISIBLE
        norm_love_anim.visibility = View.VISIBLE
        norm_love_anim.progress = 0f
        norm_love_anim.playAnimation()
    }

    private fun showBadLove() {
        norm_love_anim.visibility = View.INVISIBLE
        good_love_anim.visibility = View.INVISIBLE
        bad_love_anim.visibility = View.VISIBLE
        bad_love_anim.progress = 0f
        bad_love_anim.playAnimation()
    }

    override fun setObservers() {
        super.setObservers()
    }

    override fun getViewModelClass(): Class<ResAndShareActivityViewModel> {
        return ResAndShareActivityViewModel::class.java
    }

    override fun onError() {

    }

}