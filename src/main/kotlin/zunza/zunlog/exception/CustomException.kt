package zunza.zunlog.exception

abstract class CustomException(message: String): RuntimeException(message) {

    private var errorField = ""

    fun initErrorField(field: String) {
        errorField = field
    }

    fun getErrorField(): String {
        return this.errorField
    }
    abstract fun getStatusCode(): Int
}
