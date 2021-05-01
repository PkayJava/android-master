package ${pkg}

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ${pkg}.common.LocalService
import ${pkg}.common.OverlayWindowPermission
import ${pkg}.common.PictureInPicturePermission
import ${pkg}.view.*

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalGetImage
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val p2pModel: PictureInPictureScreenModel by viewModels()

    val overlayModel: OverlayWindowScreenModel by viewModels()

    private lateinit var localServiceConnection: LocalServiceConnection

    @ExperimentalAnimatedInsets
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.localServiceConnection = LocalServiceConnection()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                val controller = rememberNavController()
                NavHost(
                    navController = controller,
                    startDestination = "/login"
                ) {
                    composable(
                        route = "/login"
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val model: LoginScreenModel = viewModel("LoginScreenModel", factory)
                        LoginScreen(controller = controller, model = model)
                    }
                    composable(
                        route = "/menu/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val model: MenuScreenModel = viewModel("MenuScreenModel", factory)
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        MenuScreen(
                            accessId = accessId,
                            secretId = secretId,
                            controller = controller,
                            model = model
                        )
                    }
                    composable(
                        route = "/barcode/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val model: BarcodeScreenModel = viewModel("BarcodeScreenModel", factory)
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        BarcodeScreen(
                            accessId = accessId,
                            secretId = secretId,
                            controller = controller,
                            model = model
                        )
                    }
                    composable(
                        route = "/luhn/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val model: ImageOCRScreenModel = viewModel("ImageOCRScreenModel", factory)
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        ImageOCRScreen(
                            accessId = accessId,
                            secretId = secretId,
                            controller = controller,
                            model = model
                        )
                    }
                    composable(
                        route = "/camera/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                        val model: TakePictureScreenModel =
                            viewModel("TakePictureScreenModel", factory)
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        TakePictureScreen(
                            accessId = accessId,
                            secretId = secretId,
                            controller = controller,
                            model = model
                        )
                    }
                    composable(
                        route = "/p2p/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        PictureInPictureScreen(
                            accessId = accessId,
                            secretId = secretId,
                            controller = controller,
                            model = p2pModel
                        )
                    }
                    composable(
                        route = "/overlay/{accessId}/{secretId}"
                    ) { navBackStackEntry ->
                        val accessId = navBackStackEntry.arguments?.getString("accessId")!!
                        val secretId = navBackStackEntry.arguments?.getString("secretId")!!
                        OverlayWindowScreen(
                            accessId = accessId,
                            secretId = secretId,
                            controller = controller,
                            model = overlayModel
                        )
                    }
                }
            }
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            p2pModel.updateState(PictureInPictureScreenModel.DataState.P2P)
        } else {
            p2pModel.updateState(PictureInPictureScreenModel.DataState.Normal)
        }
    }

    override fun onStart() {
        super.onStart()
        var intent = Intent(this, LocalService::class.java)
        bindService(intent, this.localServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(this.localServiceConnection)
    }

    override fun onResume() {
        super.onResume()
        if (!PictureInPicturePermission.hasPictureInPicture(this)) {
            p2pModel.updateState(PictureInPictureScreenModel.DataState.Permission)
        } else {
            p2pModel.updateState(PictureInPictureScreenModel.DataState.Normal)
        }

        if (!OverlayWindowPermission.hasOverlay(this)) {
            overlayModel.updateState(OverlayWindowScreenModel.DataState.Permission)
        } else {
            overlayModel.updateState(OverlayWindowScreenModel.DataState.HIDE)
            if (localServiceConnection.service?.overlayStatus == "HIDE") {
                overlayModel.updateState(OverlayWindowScreenModel.DataState.SHOW)
            } else if (localServiceConnection.service?.overlayStatus == "SHOW") {
                overlayModel.updateState(OverlayWindowScreenModel.DataState.HIDE)
            }
        }
    }

    inner class LocalServiceConnection :
        ServiceConnection {

        var service: LocalService? = null

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val localBinder = service as LocalService.LocalBinder
            localBinder.service.lifecycleOwner = this@MainActivity
            this.service = localBinder.service
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d("test", "service is disconnected")
            this.service = null
        }
    }

}