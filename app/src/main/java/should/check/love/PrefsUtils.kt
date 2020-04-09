package should.check.love

import android.content.Context
import android.content.SharedPreferences

object PrefsUtils {
    const val F_N = "f_n"
    const val S_N = "s_n"
    const val PERCENT = "percent"
    private fun getPreferences(): SharedPreferences {
        return LoveApp.getInstance().getSharedPreferences("ScoreSettings", Context.MODE_PRIVATE)
    }

    fun saveScore(firstName: String, secondName: String, percent: Int) {
        val editor = getPreferences().edit()
        editor.putString(F_N, firstName)
        editor.putString(S_N, secondName)
        editor.putString(PERCENT, percent.toString())
        editor.apply()
    }

    fun getLastScore(): HashMap<String, String> {
        val prefs = getPreferences()
        val result = HashMap<String, String>()
        result[F_N] = prefs.getString(F_N, "") ?: ""
        result[S_N] = prefs.getString(S_N, "") ?: ""
        result[PERCENT] = prefs.getString(PERCENT, "") ?: ""
        return result

    }
}