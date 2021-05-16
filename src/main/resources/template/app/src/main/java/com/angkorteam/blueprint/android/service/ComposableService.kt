package ${pkg}.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ComposableService : Service(), ServiceRegistry {

    private lateinit var binder: ServiceBinder<ComposableService>

    override lateinit var registry: MutableMap<String, Any>

    override fun onCreate() {
        super.onCreate()
        this.binder = ServiceBinder(this)
        this.registry = mutableMapOf()
    }

    override fun onBind(intent: Intent): IBinder? {
        return this.binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val serviceName = intent.getStringExtra(NAME)
        val onService = this.binder.handlers[serviceName]
        if (onService != null) {
            onService(intent, this.registry)
        }
        return START_NOT_STICKY
    }

    companion object {
        const val NAME = "serviceName"
    }

}