package ${pkg}.service

import android.content.Intent
import android.os.Binder

class ServiceBinder<T : ServiceRegistry>(val service: T) : Binder() {

    val handlers =
            mutableMapOf<String, (intent: Intent, registry: MutableMap<String, Any>) -> Unit>()

}