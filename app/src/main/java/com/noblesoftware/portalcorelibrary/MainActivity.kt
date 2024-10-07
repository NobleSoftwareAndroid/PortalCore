package com.noblesoftware.portalcorelibrary

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noblesoftware.portalcore.theme.PortalCoreTheme
import com.noblesoftware.portalcorelibrary.sample.BottomSheetSampleScreen
import com.noblesoftware.portalcorelibrary.sample.CommonSampleScreen
import com.noblesoftware.portalcorelibrary.sample.ContainerSampleScreen
import com.noblesoftware.portalcorelibrary.sample.SnackBarSampleScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PortalCoreTheme {
                val navController = rememberNavController()
                Scaffold {
                    NavHost(navController, startDestination = MainRoute, Modifier.padding(it)) {
                        composable<MainRoute> { MainScreen(navHostController = navController) }
                        composable<CommonRoute> { CommonSampleScreen(navHostController = navController) }
                        composable<SnackBarRoute> { SnackBarSampleScreen(navHostController = navController) }
                        composable<BottomSheetRoute> { BottomSheetSampleScreen(navHostController = navController) }
                        composable<ContainerRoute> { ContainerSampleScreen(navHostController = navController) }
                    }
                }
            }
        }
    }
}