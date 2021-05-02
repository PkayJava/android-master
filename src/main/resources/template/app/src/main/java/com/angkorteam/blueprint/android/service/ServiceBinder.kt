package ${pkg}.service

import android.content.Intent
import android.os.Binder

class ServiceBinder : Binder() {

    val registry =
        mutableMapOf<String, (intent: Intent, registry: MutableMap<String, Any>) -> Unit>()

}