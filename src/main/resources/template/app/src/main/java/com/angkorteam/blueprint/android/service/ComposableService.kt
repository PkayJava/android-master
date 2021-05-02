package ${pkg}.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ComposableService : Service() {

    private lateinit var binder: ServiceBinder

    override fun onCreate() {
        super.onCreate()
        this.binder = ServiceBinder()
    }

    override fun onBind(intent: Intent): IBinder? {
        return this.binder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val serviceName = intent.getStringExtra(NAME)
        val onService = this.binder.registry[serviceName]
        if (onService != null) {
            onService(intent)
        }
        return START_NOT_STICKY
    }

    companion object {
        const val NAME = "serviceName"
    }

}