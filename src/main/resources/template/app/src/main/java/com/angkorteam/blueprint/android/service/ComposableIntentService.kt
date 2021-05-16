package ${pkg}.service

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import ${pkg}.MainApplication

class ComposableIntentService : IntentService("IntentService"), ServiceRegistry {

    private lateinit var binder: ServiceBinder<ComposableIntentService>

    private lateinit var hander: Handler

    override lateinit var registry: MutableMap<String, Any>

    override fun onCreate() {
        super.onCreate()
        this.binder = ServiceBinder(this)
        this.hander = Handler(Looper.getMainLooper())
        this.registry = mutableMapOf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return this.binder
    }

    override fun onHandleIntent(intent: Intent?) {
        val serviceName = intent?.getStringExtra(ComposableJobIntentService.NAME)
        val onService = this.binder.handlers[serviceName]
        if (onService != null) {
            onService(intent ?: Intent(), this.registry)
        }
    }

    companion object {
        const val DEBUG = MainApplication.DEBUG
        const val TAG = "IntentService"
        const val NAME = ComposableService.NAME
    }

}