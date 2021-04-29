package abc.abc

import android.app.Application
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.File

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    fun createDataStore(
        name: String,
        corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
        migrations: List<DataMigration<Preferences>> = listOf(),
        scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = corruptionHandler,
            migrations = migrations,
            scope = scope
        ) {
            File(this.filesDir, "datastore/$name.pref")
        }

}