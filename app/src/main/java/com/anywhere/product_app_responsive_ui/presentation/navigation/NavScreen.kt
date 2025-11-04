package com.anywhere.product_app_responsive_ui.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.anywhere.product_app_responsive_ui.presentation.model.ProductUiState
import com.anywhere.product_app_responsive_ui.presentation.ui.product_screens.ProductDetailScreen
import com.anywhere.product_app_responsive_ui.presentation.ui.product_screens.ProductListScreen
import com.anywhere.product_app_responsive_ui.presentation.viewmodel.ProductViewModel

sealed class Screen(val route: String) {
    object ProductList : Screen("product_list")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Int): String {
            return "product_detail/$productId"
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: ProductViewModel,
    windowSizeClass: WindowSizeClass
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ProductList.route
    ) {

        // Product List Screen
        composable(
            route = Screen.ProductList.route,

            // When entering ProductList (after back navigation)
            popEnterTransition = {
                fadeIn(animationSpec = tween(300)) + //starts transparent → becomes fully visible in 300ms
                        slideInHorizontally(
                            initialOffsetX = { -300 }, // slide to right
                            animationSpec = tween(300)
                        )
            },

            // When leaving ProductList (navigating to ProductDetail)
            exitTransition = {
                fadeOut(animationSpec = tween(250)) +
                        slideOutHorizontally(
                            targetOffsetX = { -300 }, // slide to left
                            animationSpec = tween(250)
                        )
            }
        ) {
            ProductListScreen(
                viewModel = viewModel,
                onProductClick = { product ->
                    navController.navigate(Screen.ProductDetail.createRoute(product.id ?: 0))
                },
                windowSizeClass = windowSizeClass
            )
        }


        //  Product Detail Screen
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            ),

            // When entering ProductDetail (forward navigation)
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        slideInHorizontally(
                            initialOffsetX = { 300 }, // slide to left
                            animationSpec = tween(300)
                        )
            },

            // When leaving ProductDetail (pressing Back)
            popExitTransition = {
                fadeOut(animationSpec = tween(250)) +
                        slideOutHorizontally(
                            targetOffsetX = { 300 }, // slide to right
                            animationSpec = tween(250)
                        )
            }
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val product = (uiState as? ProductUiState.Success)
                ?.products
                ?.find { it.id == productId }
                ?: return@composable

            ProductDetailScreen(
                product = product,
                onBackClick = { navController.popBackStack() },
                windowSizeClass = windowSizeClass
            )
        }
    }
}
