package ${pkg}.common

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.compose.foundation.Image
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ${pkg}.MainActivity
import ${pkg}.R
import ${pkg}.view.OverlayWindowScreenModel
import java.util.*

@ExperimentalMaterialApi
class LocalService : Service() {

    var lifecycleOwner: MainActivity? = null

    var overlayStatus: String? = null
    lateinit var overlayView: View

    override fun onBind(intent: Intent?): IBinder? {
        return LocalBinder()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("test", "service on create")
    }

    @ExperimentalCoroutinesApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var action = intent?.extras?.getString("ACTION")
        if ("SHOW" == action) {
            var windowManager = ContextCompat.getSystemService(this, WindowManager::class.java)
            overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_window, null, false)
            ViewTreeLifecycleOwner.set(overlayView, lifecycleOwner)
            ViewTreeSavedStateRegistryOwner.set(overlayView, lifecycleOwner)

            var width = 0
            var height = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                var metric = windowManager!!.currentWindowMetrics
                width = metric.bounds.width()
                height = metric.bounds.height()
            } else {
                val metrics = DisplayMetrics()
                windowManager!!.defaultDisplay.getMetrics(metrics)
                width = metrics.widthPixels
                height = metrics.heightPixels
            }

            var radio = 16f / 9f;
            var w = width / 2f;
            var h = width / radio;

            var composeView = overlayView.findViewById<ComposeView>(R.id.compose_view)
            composeView.setContent {
                Image(
                    painter = painterResource(id = R.drawable.picture_in_picture),
                    contentDescription = ""
                )
            }

            val popupLayoutParams = WindowManager.LayoutParams(
                w.toInt(), h.toInt(),
                popupLayoutParamType(),
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                PixelFormat.TRANSLUCENT
            )
            popupLayoutParams.gravity = Gravity.LEFT or Gravity.TOP
            popupLayoutParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE

            var centerX = (width / 2f) - (w / 2f)
            var centerY = (height / 2f) - (h / 2f)

            popupLayoutParams.x = centerX.toInt()
            popupLayoutParams.y = centerY.toInt()

            Objects.requireNonNull(windowManager)?.addView(overlayView, popupLayoutParams)
            overlayStatus = "SHOW"
            lifecycleOwner?.overlayModel?.updateState(OverlayWindowScreenModel.DataState.SHOW)
        } else if ("HIDE" == action) {
            var windowManager = ContextCompat.getSystemService(this, WindowManager::class.java)
            Objects.requireNonNull(windowManager)?.removeView(overlayView)
            overlayStatus = "HIDE"
            lifecycleOwner?.overlayModel?.updateState(OverlayWindowScreenModel.DataState.HIDE)
        }

        return START_NOT_STICKY
    }

    fun popupLayoutParamType(): Int {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_PHONE else WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("test", "service on destroy")
    }

    inner class LocalBinder : Binder() {
        val service: LocalService
            get() = this@LocalService
    }

}