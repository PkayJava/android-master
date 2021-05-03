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
import ${pkg}.service.ServiceRegistry

@Composable
fun <T : ServiceRegistry> ServiceEffect(
        serviceName: String,
        serviceClass: Class<T>,
        onConnected: (registry: MutableMap<String, Any>) -> Unit,
        onDisconnected: (registry: MutableMap<String, Any>) -> Unit,
        onService: (intent: Intent, registry: MutableMap<String, Any>) -> Unit
) {
    // Grab the current context in this part of the UI tree
    val context by rememberUpdatedState(LocalContext.current)

    // Safely use the latest onSystemEvent lambda passed to the function
    val currentOnService by rememberUpdatedState(onService)

    // If either context or systemAction changes, unregister and register again
    DisposableEffect(context, serviceName) {
        var intent = Intent(context, serviceClass)

        var internalRegistry: MutableMap<String, Any>? = null

        val connection = object : ServiceConnection {

            lateinit var binder: ServiceBinder<T>

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                this.binder = service as ServiceBinder<T>
                this.binder.handlers[serviceName] = currentOnService
                internalRegistry = this.binder.service.registry
                onConnected(this.binder.service.registry)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                this.binder.handlers.remove(serviceName)
                onDisconnected(this.binder.service.registry)
            }

        }

        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)

        // When the effect leaves the Composition, remove the callback
        onDispose {
            onDisconnected(internalRegistry!!)
            connection.binder.handlers.remove(serviceName)
            context.unbindService(connection)
        }
    }
}
