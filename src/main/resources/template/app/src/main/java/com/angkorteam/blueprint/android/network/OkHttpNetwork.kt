package ${pkg}.network

import com.google.gson.Gson
import okhttp3.OkHttpClient
import kotlin.Throws
import ${pkg}.dto.AuthenticateResponseDto
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.RequestBody
import org.apache.commons.io.IOUtils
import java.lang.Exception
import java.nio.charset.StandardCharsets

class OkHttpNetwork(private val baseUrl: String, private val gson: Gson) {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .build()

    @Throws(Exception::class)
    fun authenticate(username: String, password: String): AuthenticateResponseDto {
        val body: RequestBody = FormBody.Builder()
            .addEncoded("username", username)
            .addEncoded("password", password)
            .build()
        val request: Request = Request.Builder()
            .url("$baseUrl/user/authenticate")
            .post(body)
            .build()
        try {
            var response = client.newCall(request).execute()
            response.use {
                return if (it.code == 200) {
                    val json = IOUtils.toString(it.body?.byteStream(),StandardCharsets.UTF_8)
                    gson.fromJson(json, AuthenticateResponseDto::class.java)
                } else {
                    throw Exception(it.message)
                }
            }
        } catch (e: Throwable) {
            throw Exception("Access Denied")
        }
    }
}