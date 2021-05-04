package ${pkg}.effect

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Composable
fun SystemInsetsEffect(
        onChange: (systemPadding: PaddingValues, hasStatusBar: Boolean, hasIme: Boolean, hasNavigationBar: Boolean) -> Unit
) {
    var view = LocalView.current
    var screen = LocalDensity.current

    val currentOnChange by rememberUpdatedState(onChange)

    DisposableEffect(view) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, window ->
            var ime = window.getInsets(WindowInsetsCompat.Type.ime())

            var statusBars = window.getInsets(WindowInsetsCompat.Type.statusBars())
            var navigationBars = window.getInsets(WindowInsetsCompat.Type.navigationBars())

            var hasIme = window.isVisible(WindowInsetsCompat.Type.ime())
            var hasStatusBar = window.isVisible(WindowInsetsCompat.Type.statusBars())
            var hasNavigationBar =
                    !hasIme && window.isVisible(WindowInsetsCompat.Type.navigationBars())

            var systemPadding =
                    PaddingValues(
                            start = ((
                                    (if (hasIme) ime.left else 0) +
                                            (if (hasStatusBar) statusBars.left else 0) +
                                            (if (hasNavigationBar) navigationBars.left else 0)
                                    ) / screen.density).dp,
                            top = ((
                                    (if (hasIme) ime.top else 0) +
                                            (if (hasStatusBar) statusBars.top else 0) +
                                            (if (hasNavigationBar) navigationBars.top else 0)
                                    ) / screen.density).dp,
                            end = ((
                                    (if (hasIme) ime.right else 0) +
                                            (if (hasStatusBar) statusBars.right else 0) +
                                            (if (hasNavigationBar) navigationBars.right else 0)
                                    ) / screen.density).dp,
                            bottom = ((
                                    (if (hasIme) ime.bottom else 0) +
                                            (if (hasStatusBar) statusBars.bottom else 0) +
                                            (if (hasNavigationBar) navigationBars.bottom else 0)
                                    ) / screen.density).dp,
                    )
            currentOnChange(systemPadding, hasStatusBar, hasIme, hasNavigationBar)
            window
        }
        onDispose {
            ViewCompat.setOnApplyWindowInsetsListener(view, null)
        }
    }
}