package abc.abc.di

import androidx.room.Room
import abc.abc.MainApplication
import abc.abc.common.AppDatabase
import abc.abc.dao.HelloDao
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
