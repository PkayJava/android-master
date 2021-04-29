package ${pkg}.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hello")
data class HelloEntity(

    // Value from API
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "avatar")
    var avatar: String,

    @ColumnInfo(name = "date_of_birth")
    var dateOfBirth: String,

    @ColumnInfo(name = "gender")
    var gender: String,

    @ColumnInfo(name = "phrase")
    var phrase: String,

    )