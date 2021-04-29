package ${pkg}.dto

import com.google.gson.annotations.SerializedName

data class HelloDto(

    @SerializedName("id")
    var id: Int?,

    @SerializedName("name")
    var name: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("avatar")
    var avatar: String,

    @SerializedName("date_of_birth")
    var dateOfBirth: String,

    @SerializedName("gender")
    var gender: String,

    @SerializedName("phrase")
    var phrase: String,

    @SerializedName("puppy_file")
    var puppyFile: String,

    )