package should.check.love.restApi

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import should.check.love.main.model.CheckResult

interface JsonApi {

    @GET("getPercentage")
    fun checkLoveResultAsync(
        @Query("fname") firstName: String,
        @Query("sname") secondName: String
    ): Observable<Response<CheckResult>>
}