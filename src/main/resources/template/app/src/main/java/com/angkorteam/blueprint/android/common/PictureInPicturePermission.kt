package ${pkg}.common

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract

class PictureInPicturePermission : ActivityResultContract<String, Boolean>() {

    private lateinit var context: Context

    override fun createIntent(context: Context, input: String?): Intent {
        this.context = context
        return Intent(
            "android.settings.PICTURE_IN_PICTURE_SETTINGS",
            Uri.parse("package:" + context.packageName)
        )
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return hasPictureInPicture(context = context)
    }

    companion object {
        fun hasPictureInPicture(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val appOpsManager =
                    context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                var mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    appOpsManager.unsafeCheckOpRaw(
                        AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                        android.os.Process.myUid(),
                        context.packageName
                    )
                } else {
                    appOpsManager.checkOpNoThrow(
                        AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                        android.os.Process.myUid(),
                        context.packageName
                    )
                }
                return mode == AppOpsManager.MODE_ALLOWED
            } else {
                return false
            }
        }
    }

}