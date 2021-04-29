package ${pkg}.common

import androidx.room.Database
import androidx.room.RoomDatabase
import ${pkg}.dao.HelloDao
import ${pkg}.entity.HelloEntity

@Database(entities = [HelloEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun helloDao(): HelloDao

    companion object {
        const val DATABASE_NAME: String = "${app_name}.db"
    }

}
