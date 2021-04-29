package ${pkg}.repository

import ${pkg}.common.NetworkState
import ${pkg}.dto.AuthenticateResponseDto
import ${pkg}.network.OkHttpNetwork

class NetworkRepository(
    private var network: OkHttpNetwork
) : Repository {

    override suspend fun login(
        username: String,
        password: String
    ): NetworkState<AuthenticateResponseDto> {
        return try {
            NetworkState.Success(this.network.authenticate(username, password))
        } catch (e: Exception) {
            NetworkState.Failure(e.message ?: "An error occurred");
        }
    }

}