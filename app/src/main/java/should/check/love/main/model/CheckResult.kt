package should.check.love.main.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class CheckResult() : Parcelable {
    @SerializedName("fname")
    var fname = ""

    @SerializedName("sname")
    var sname = ""

    @SerializedName("percentage")
    var percentage = ""

    @SerializedName("result")
    var result = ""

    constructor(parcel: Parcel) : this() {
        fname = parcel.readString() ?: ""
        sname = parcel.readString() ?: ""
        percentage = parcel.readString() ?: ""
        result = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fname)
        parcel.writeString(sname)
        parcel.writeString(percentage)
        parcel.writeString(result)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckResult> {
        override fun createFromParcel(parcel: Parcel): CheckResult {
            return CheckResult(parcel)
        }

        override fun newArray(size: Int): Array<CheckResult?> {
            return arrayOfNulls(size)
        }
    }


}