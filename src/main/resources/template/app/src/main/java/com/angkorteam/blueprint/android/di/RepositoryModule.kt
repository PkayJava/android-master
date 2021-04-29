package ${pkg}.di

import ${pkg}.dao.HelloDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ${pkg}.repository.MockRepository
import ${pkg}.repository.Repository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideRepository(dao: HelloDao): Repository {
        return MockRepository()
    }

}
