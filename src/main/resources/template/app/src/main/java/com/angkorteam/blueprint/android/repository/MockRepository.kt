package ${pkg}.repository

import ${pkg}.common.NetworkState
import ${pkg}.dto.AuthenticateResponseDto

class MockRepository(
) : Repository {

    override suspend fun login(
        username: String,
        password: String
    ): NetworkState<AuthenticateResponseDto> {
        return try {
            NetworkState.Success(AuthenticateResponseDto("abc", "abc"))
        } catch (e: Exception) {
            NetworkState.Failure(e.message ?: "An error occurred");
        }
    }

}