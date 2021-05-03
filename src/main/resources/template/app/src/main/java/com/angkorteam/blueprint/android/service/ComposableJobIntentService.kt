package ${pkg}.service

import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.JobIntentService
import ${pkg}.MainApplication

class ComposableJobIntentService : JobIntentService(), ServiceRegistry {

    private lateinit var hander: Handler

    private lateinit var binder: ServiceBinder<ComposableJobIntentService>

    override lateinit var registry: MutableMap<String, Any>

    override fun onCreate() {
        super.onCreate()
        this.binder = ServiceBinder(this)
        this.hander = Handler(Looper.getMainLooper())
        this.registry = mutableMapOf()
    }

    override fun onBind(intent: Intent): IBinder? {
        return this.binder
    }

    override fun onHandleWork(intent: Intent) {
        val serviceName = intent.getStringExtra(NAME)
        val onService = this.binder.handlers[serviceName]
        if (onService != null) {
            onService(intent, this.registry)
        }
    }

    companion object {
        const val DEBUG = MainApplication.DEBUG
        const val NAME = ComposableService.NAME
        const val TAG = "JobIntentService"
    }

}