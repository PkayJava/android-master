package ${pkg}.service

import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.JobIntentService
import ${pkg}.MainApplication

class ComposableJobIntentService : JobIntentService() {

    private lateinit var binder: ServiceBinder

    private lateinit var hander: Handler

    override fun onCreate() {
        super.onCreate()
        this.binder = ServiceBinder()
        this.hander = Handler(Looper.getMainLooper())
    }

    override fun onBind(intent: Intent): IBinder? {
        return this.binder
    }

    override fun onHandleWork(intent: Intent) {
        val serviceName = intent.getStringExtra(NAME)
        val onService = this.binder.registry[serviceName]
        if (onService != null) {
            onService(intent)
        }
    }

    companion object {
        const val DEBUG = MainApplication.DEBUG
        const val NAME = ComposableService.NAME
        const val TAG = "JobIntentService"
    }

}