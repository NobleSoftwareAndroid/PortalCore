package com.noblesoftware.portalcorelibrary

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.theme.PortalCoreTheme
import com.noblesoftware.portalcorelibrary.sample.BottomSheetSampleScreen
import com.noblesoftware.portalcorelibrary.sample.CommonSampleScreen
import com.noblesoftware.portalcorelibrary.sample.ContainerSampleScreen
import com.noblesoftware.portalcorelibrary.sample.DialogSampleScreen
import com.noblesoftware.portalcorelibrary.sample.FullEdgeSampleScreen
import com.noblesoftware.portalcorelibrary.sample.RichTextEditorSampleScreen
import com.noblesoftware.portalcorelibrary.sample.SnackBarSampleScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PortalCoreTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.background_body))
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = MainRoute) {
                        composable<MainRoute> { MainScreen(navHostController = navController) }
                        composable<CommonRoute> { CommonSampleScreen(navHostController = navController) }
                        composable<SnackBarRoute> { SnackBarSampleScreen(navHostController = navController) }
                        composable<BottomSheetRoute> { BottomSheetSampleScreen(navHostController = navController) }
                        composable<ContainerRoute> { ContainerSampleScreen(navHostController = navController) }
                        composable<FullEdgeRoute> { FullEdgeSampleScreen(navHostController = navController) }
                        composable<DialogRoute> { DialogSampleScreen(navHostController = navController) }
                        composable<RichTextEditorRoute> { RichTextEditorSampleScreen(navHostController = navController) }
                    }
                }
            }
        }
    }
}