package com.redcom1988.cafej3.theme


import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.redcom1988.cafej3.util.collectAsState
import com.redcom1988.core.util.inject
import com.redcom1988.domain.preference.ApplicationPreference
import com.redcom1988.domain.theme.Themes

private val LightColorScheme = lightColorScheme(
    primary              = Brown,
    onPrimary            = Color(0xFFFFFFFF),
    primaryContainer     = BrownPale,
    onPrimaryContainer   = Color(0xFF3A1500),

    secondary            = Terracotta,
    onSecondary          = Color(0xFFFFFFFF),
    secondaryContainer   = TerracottaPale,
    onSecondaryContainer = Color(0xFF311300),

    tertiary             = Clay,
    onTertiary           = Color(0xFFFFFFFF),
    tertiaryContainer    = ClayPale,
    onTertiaryContainer  = Color(0xFF341100),

    error                = ExpenseRed,
    onError              = Color(0xFFFFFFFF),
    errorContainer        = ExpenseRedContainerLight,
    onErrorContainer      = Color(0xFF410E04),

    background           = CreamLight,
    onBackground         = Color(0xFF2A1A12),

    surface              = Color(0xFFFFFFFF),
    onSurface            = Color(0xFF2A1A12),
    surfaceVariant       = Color(0xFFE9E2D3),
    onSurfaceVariant     = Taupe,

    outline              = Taupe,
    outlineVariant       = Color(0xFFEFE6D8),
    inverseSurface       = Color(0xFF362A20),
    inverseOnSurface     = CreamLight,
    inversePrimary       = BrownLight,

    scrim                = Color(0xFF000000),
    surfaceTint          = Brown,
)

private val DarkColorScheme = darkColorScheme(
    primary              = BrownLight,
    onPrimary            = Color(0xFF4A1B00),
    primaryContainer     = BrownDark,
    onPrimaryContainer   = BrownPale,

    secondary            = TerracottaLight,
    onSecondary          = Color(0xFF44230A),
    secondaryContainer   = TerracottaDark,
    onSecondaryContainer = TerracottaPale,

    tertiary             = ClayLight,
    onTertiary           = Color(0xFF4C1C00),
    tertiaryContainer    = ClayDark,
    onTertiaryContainer  = ClayPale,

    error                = ErrorLight,
    onError              = Color(0xFF690004),
    errorContainer        = ExpenseRedContainerDark,
    onErrorContainer      = Color(0xFFFFDAD4),

    background           = CreamDarkest,
    onBackground         = CreamLight,

    surface              = CreamDark,
    onSurface            = CreamLight,
    surfaceVariant       = TaupeDark,
    onSurfaceVariant     = TaupeLight,

    outline              = Taupe,
    outlineVariant       = TaupeDark,
    inverseSurface       = CreamLight,
    inverseOnSurface     = CreamDark,
    inversePrimary       = Brown,

    scrim                = Color(0xFF000000),
    surfaceTint          = BrownLight,
)

val LocalColorScheme = compositionLocalOf { lightColorScheme() }
val darkTheme = DarkColorScheme
val lightTheme = LightColorScheme

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val applicationPreference = inject<ApplicationPreference>()
    val theme by applicationPreference.appTheme().collectAsState()
    val systemBarColor = Color.Transparent

    val colorScheme = when (theme) {
        Themes.DARK -> darkTheme
        Themes.LIGHT -> lightTheme
        else -> if (isDarkTheme) darkTheme else lightTheme
    }
    CompositionLocalProvider(
        LocalColorScheme provides colorScheme
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background),
        ) {
            MaterialTheme(
                colorScheme = colorScheme,
                typography = Typography,
                content = content,
            )
            StatusBarColor(systemBarColor, colorScheme)
            NavigationBarColor(systemBarColor, colorScheme)
        }
    }
}

@Suppress("DEPRECATION")
@Composable
fun NavigationBarColor(color: Color, colorScheme: ColorScheme) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.navigationBarColor = color.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val windowInsetsController = WindowInsetsControllerCompat(window, view)
            windowInsetsController.isAppearanceLightNavigationBars = colorScheme == lightTheme
        }
    }
}

@Suppress("DEPRECATION")
@Composable
fun StatusBarColor(color: Color, colorScheme: ColorScheme) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = color.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val windowInsetsController = WindowInsetsControllerCompat(window, view)
            windowInsetsController.isAppearanceLightStatusBars = colorScheme == lightTheme
        }
    }
}