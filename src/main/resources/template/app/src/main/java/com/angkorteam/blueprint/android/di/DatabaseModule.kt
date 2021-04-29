package ${pkg}.di

import androidx.room.Room
import ${pkg}.MainApplication
import ${pkg}.common.AppDatabase
import ${pkg}.dao.HelloDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(app: MainApplication): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideHelloDao(db: AppDatabase): HelloDao {
        return db.helloDao()
    }

}
