package ${pkg}.common

import android.app.IntentService
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import ${pkg}.MainApplication

class LocalIntentService : IntentService("LocalIntentService") {

    override fun onCreate() {
        super.onCreate()
        if (DEBUG) {
            Log.d(TAG, "onCreate")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        if (DEBUG) {
            Log.d(TAG, "onBind")
        }
        return LocalBinder()
    }

    override fun onHandleIntent(intent: Intent?) {
        if (DEBUG) {
            Log.d(TAG, "onHandleIntent")
        }
    }

    inner class LocalBinder : Binder() {
        val service: LocalIntentService
            get() = this@LocalIntentService
    }

    companion object {
        const val DEBUG = MainApplication.DEBUG
        const val TAG = "MainApplication"
    }

}