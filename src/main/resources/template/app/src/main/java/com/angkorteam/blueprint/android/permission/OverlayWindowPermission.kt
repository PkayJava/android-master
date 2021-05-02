package ${pkg}.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

class OverlayWindowPermission : ActivityResultContract<String, Boolean>() {

    private lateinit var context: Context

    override fun createIntent(context: Context, input: String?): Intent {
        this.context = context
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.packageName)
            )
        } else {
            Intent(
                "android.settings.action.MANAGE_OVERLAY_PERMISSION",
                Uri.parse("package:" + context.packageName)
            )
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return hasOverlay(context = context)
    }

    companion object {
        fun hasOverlay(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Settings.canDrawOverlays(context)
            } else {
                false
            }
        }
    }

}