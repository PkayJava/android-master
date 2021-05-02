package ${pkg}.effect

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import ${pkg}.service.ServiceBinder

@Composable
fun ServiceEffect(
        serviceName: String,
        serviceClass: Class<*>,
        onService: (intent: Intent, registry: MutableMap<String, Any>) -> Unit
) {
    // Grab the current context in this part of the UI tree
    val context = LocalContext.current

    // Safely use the latest onSystemEvent lambda passed to the function
    val currentOnService by rememberUpdatedState(onService)

    // If either context or systemAction changes, unregister and register again
    DisposableEffect(context, serviceName) {
        var intent = Intent(context, serviceClass)

        val connection = object : ServiceConnection {

            lateinit var binder: ServiceBinder

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                this.binder = service as ServiceBinder
                this.binder.registry[serviceName] = currentOnService
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                this.binder.registry.remove(serviceName)
            }
        }

        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)

        // When the effect leaves the Composition, remove the callback
        onDispose {
            connection.binder.registry.remove(serviceName)
            context.unbindService(connection)
        }
    }
}
