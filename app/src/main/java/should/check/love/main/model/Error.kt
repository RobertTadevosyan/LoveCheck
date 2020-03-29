package should.check.love.main.model

class Error {
    var errorCode: Int? = null
    var errorMessage: String? = null

    constructor(errorCode: Int?, errorMessage: String?) {
        this.errorCode = errorCode
        this.errorMessage = errorMessage
    }
}