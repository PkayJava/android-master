package ${pkg}.common

sealed class NetworkState<T> {
    class Success<T>(var data: T) : NetworkState<T>()
    class Failure<T>(var data: String) : NetworkState<T>()
}
