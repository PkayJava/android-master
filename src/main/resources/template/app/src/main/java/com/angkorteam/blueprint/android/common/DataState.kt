package ${pkg}.common

data class DataState<out T>(
    val data: T? = null,
    val error: String? = null,
    val loading: Boolean = false,
    val lastId: Int? = null,
) {
    companion object {

        fun <T> success(data: T, lastId: Int?): DataState<T> {
            return DataState(data = data, lastId = lastId)
        }

        fun <T> success(data: T): DataState<T> {
            return DataState(data = data)
        }

        fun <T> error(message: String): DataState<T> {
            return DataState(error = message)
        }

        fun <T> loading(): DataState<T> {
            return DataState(loading = true)
        }

    }
}
