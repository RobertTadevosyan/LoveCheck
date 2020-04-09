package should.check.love.resultAndShare.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_res_and_share.*
import should.check.love.LoveApp
import should.check.love.R
import should.check.love.base.BaseActivity
import should.check.love.main.model.Error
import should.check.love.resultAndShare.ResAndShareActivityRepository
import should.check.love.resultAndShare.viewModel.ResAndShareActivityViewModel
import java.util.*


class ResAndShareActivity :
    BaseActivity<ResAndShareActivityRepository, ResAndShareActivityViewModel>() {
    private var callbackManager: CallbackManager? = null
    private var shareDialog: ShareDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res_and_share)
        viewModel.checkResult = intent.extras?.getParcelable("data")
        setOnClickListeners()
        loadAd()
        initFacebookShareDialog()
        initUI()
    }

    private fun initFacebookShareDialog() {
        shareDialog = ShareDialog(this)
        callbackManager = CallbackManager.Factory.create()
        shareDialog!!.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
            override fun onSuccess(result: Sharer.Result?) {
                println()
            }

            override fun onCancel() {
                println()
            }

            override fun onError(error: FacebookException?) {
                println()
            }
        })
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
    private fun initUI() {
        LoveApp.getInstance().txtToSpeech?.language = Locale.ENGLISH
        LoveApp.getInstance().txtToSpeech?.speak(viewModel.checkResult?.result, TextToSpeech.QUEUE_FLUSH, null)
        txt_result.text = viewModel.checkResult?.result
        txt_names_result.text =
            makeFirstLetterCapital(viewModel.checkResult?.fname) + getString(R.string.and) + " " + makeFirstLetterCapital(
                viewModel.checkResult?.sname
            ) + "\n${viewModel.checkResult?.percentage} %"
        val percentage = viewModel.checkResult?.percentage?.toInt() ?: 0
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
        if (percentage >= 35) {
            Handler().postDelayed({
                speechShareText()
            }, 2000)
        }
    }

    private fun speechShareText() {
        LoveApp.getInstance().getTextToSpeechWithDefaultLocale()?.speak(viewModel.checkResult?.fname?.replace("/", "") + " " + getString(R.string.invite_friends), TextToSpeech.QUEUE_FLUSH, null)
        shareActionPerformed()
    }

    private fun makeFirstLetterCapital(text: String?): String {
        if (text?.trim()?.length!! < 1) {
            return ""
        }
        val textTrimmed = text.trim()
        var firstName = textTrimmed
        var lastName = ""
        if (textTrimmed.contains(" ")) {
            firstName = textTrimmed.substring(0, textTrimmed.indexOf(" "))
            lastName = textTrimmed.substring(firstName.length + 1)
        }
        firstName = firstName.substring(
            0,
            1
        ).toUpperCase(Locale.getDefault()) + firstName.substring(1).toLowerCase(Locale.getDefault())
        if (lastName.isNotEmpty()) {
            lastName = lastName.substring(
                0,
                1
            ).toUpperCase(Locale.getDefault()) + lastName.substring(1).toLowerCase(Locale.getDefault())
        }
        return "$firstName $lastName"
    }

    private fun setOnClickListeners() {
        share_on_FB_anim.setOnClickListener {
            val content = SharePhotoContent.Builder()
                .addPhoto(SharePhoto.Builder().setBitmap(createScreenShot()).build())
                .setShareHashtag(ShareHashtag.Builder().setHashtag("#loveCalculate").build())
                .build()
            shareDialog!!.show(content, ShareDialog.Mode.AUTOMATIC)
        }
        btn_invite.setOnClickListener {
            shareActionPerformed()
        }
    }

    private fun shareActionPerformed() {
        val message = "https://play.google.com/store/apps/details?id=should.check.love"
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, message)
        if (share.resolveActivity(packageManager) != null) {
            startActivity(
                Intent.createChooser(
                    share,
                    "Check your love :)"
                )
            )
        } else {
            Toast.makeText(
                this,
                "OOOPS!!, There is no app to share the link via :(",
                Toast.LENGTH_LONG
            ).show()
        }
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

    override fun onError(error: Error) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    private fun createScreenShot(): Bitmap {
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        val v1 = window.decorView.rootView
        v1.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(v1.drawingCache, 0, 25, point.x, (point.y * 0.75).toInt())
        v1.isDrawingCacheEnabled = false
        return bitmap
    }

}