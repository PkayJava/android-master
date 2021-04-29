package ${pkg}.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthenticateResponseDto(
    @Expose
    @SerializedName("accessId")
    var accessId: String?,
    @Expose
    @SerializedName("secretId")
    var secretId: String?
)