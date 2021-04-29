package ${pkg}.di

import ${pkg}.dao.HelloDao
import ${pkg}.network.HelloService
import ${pkg}.repository.HelloRepository
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
