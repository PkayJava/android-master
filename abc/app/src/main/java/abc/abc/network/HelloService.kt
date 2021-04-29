package abc.abc.network

import abc.abc.dto.HelloDto
import retrofit2.http.GET
import retrofit2.http.Query

interface HelloService {

    @GET("97e64f00")
    suspend fun list(
        @Query("key") key: String,
        @Query("count") count: Int
    ): List<HelloDto>

}