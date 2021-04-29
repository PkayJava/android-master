package abc.abc.di

import abc.abc.dao.HelloDao
import abc.abc.network.HelloService
import abc.abc.repository.HelloRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideHelloRepository(dao: HelloDao, service: HelloService): HelloRepository {
        return HelloRepository(dao = dao, service = service)
    }

}
