package com.miquelcms.rickmorty

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miquelcms.rickmorty.core.designsystem.theme.RickMortyTheme
import com.miquelcms.rickmorty.core.domain.theme.ThemeMode
import com.miquelcms.rickmorty.navigation.RickMortyNavigation
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            if (!state.isLoading) {
                val isDarkTheme = state.themeMode.isDarkTheme()
                SystemBarsTheme(isDarkTheme)
                RickMortyTheme(darkTheme = isDarkTheme) {
                    RickMortyNavigation(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = {
                            viewModel.onAction(MainAction.OnDarkThemeChange(!isDarkTheme))
                        },
                    )
                }
            }
        }
    }

    @Composable
    private fun SystemBarsTheme(isDarkTheme: Boolean) {
        DisposableEffect(isDarkTheme) {
            val style = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { isDarkTheme }
            enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
            onDispose {}
        }
    }
}

@Composable
private fun ThemeMode.isDarkTheme(): Boolean =
    when (this) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
