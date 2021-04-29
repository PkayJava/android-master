package ${pkg}.repository

import ${pkg}.common.NetworkState
import ${pkg}.dto.AuthenticateResponseDto

interface Repository {

    suspend fun login(username: String, password: String): NetworkState<AuthenticateResponseDto>

}