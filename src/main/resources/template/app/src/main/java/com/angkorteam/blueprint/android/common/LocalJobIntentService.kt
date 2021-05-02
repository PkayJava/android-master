package ${pkg}.common

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.JobIntentService
import ${pkg}.MainApplication

class LocalJobIntentService : JobIntentService() {

    override fun onCreate() {
        super.onCreate()
        if (DEBUG) {
            Log.d(TAG, "onCreate")
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        if (DEBUG) {
            Log.d(TAG, "onBind")
        }
        return LocalBinder()
    }

    override fun onHandleWork(intent: Intent) {
        if (DEBUG) {
            Log.d(TAG, "onHandleWork")
        }
    }

    inner class LocalBinder : Binder() {
        val service: LocalJobIntentService
            get() = this@LocalJobIntentService
    }

    companion object {
        const val DEBUG = MainApplication.DEBUG
        const val TAG = "LocalJobIntentService"
    }

}