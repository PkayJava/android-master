package ${pkg}.di

import com.google.gson.GsonBuilder
import ${pkg}.network.HelloService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHelloService(): HelloService {
        return Retrofit.Builder()
            .baseUrl("https://api.mockaroo.com/api/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(HelloService::class.java)
    }

    @Singleton
    @Provides
    @Named("key")
    fun provideKey(): String {
        return "50b0d8c0"
    }

}
