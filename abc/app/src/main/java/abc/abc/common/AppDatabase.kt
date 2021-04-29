package abc.abc.common

import androidx.room.Database
import androidx.room.RoomDatabase
import abc.abc.dao.HelloDao
import abc.abc.entity.HelloEntity

@Database(entities = [HelloEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun helloDao(): HelloDao

    companion object {
        const val DATABASE_NAME: String = "abc.db"
    }

}
