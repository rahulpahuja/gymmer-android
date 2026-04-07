package com.m1x.gymmer.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.m1x.gymmer.ui.components.GymDrawerContent
import com.m1x.gymmer.ui.screens.*
import com.m1x.gymmer.ui.screens.state.UserRole
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Workouts : Screen("workouts")
    object Profile : Screen("profile")
    object AttendanceLogs : Screen("attendance_logs")
    object TrainerStudio : Screen("trainer_studio")
    object PaymentDefaulters : Screen("payment_defaulters")
    object BusinessInsights : Screen("business_insights")
    object Wallet : Screen("wallet")
    object NewMemberOnboarding : Screen("onboarding")
    object ExerciseDetail : Screen("exercise_detail")
    object MyTrainees : Screen("my_trainees")
    object Scan : Screen("scan")
    object Nutrition : Screen("nutrition")
    object Community : Screen("community")
    object Chat : Screen("chat")
    object ExerciseList : Screen("exercise_list/{category}") {
        fun createRoute(category: String) = "exercise_list/$category"
    }
    object VideoPlayer : Screen("video_player/{videoTitle}/{videoUrl}") {
        fun createRoute(videoTitle: String, videoUrl: String) = "video_player/$videoTitle/$videoUrl"
    }
}

@Composable
fun GymNavHost(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Login.route

    val openDrawer: () -> Unit = {
        scope.launch { drawerState.open() }
    }

    val closeDrawer: () -> Unit = {
        scope.launch { drawerState.close() }
    }

    // Screens that should show the drawer
    val showDrawer = currentRoute != Screen.Login.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showDrawer,
        drawerContent = {
            if (showDrawer) {
                GymDrawerContent(
                    selectedRoute = currentRoute,
                    onRouteSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        closeDrawer()
                    },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                        closeDrawer()
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.Workouts.route) {
                WorkoutsScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.AttendanceLogs.route) {
                AttendanceLogsScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.TrainerStudio.route) {
                TrainerStudioScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.PaymentDefaulters.route) {
                PaymentDefaultersScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.BusinessInsights.route) {
                BusinessInsightsScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.Wallet.route) {
                WalletScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.NewMemberOnboarding.route) {
                NewMemberOnboardingScreen(navController = navController)
            }
            composable(Screen.ExerciseDetail.route) {
                ExerciseDetailScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.MyTrainees.route) {
                MyTraineesScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.Scan.route) {
                ScanScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.Nutrition.route) {
                NutritionScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.Community.route) {
                CommunityFeedScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(Screen.Chat.route) {
                ChatScreen(navController = navController, onMenuClick = openDrawer)
            }
            composable(
                route = Screen.ExerciseList.route,
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: "All"
                ExerciseListScreen(
                    navController = navController,
                    category = category,
                    onMenuClick = openDrawer
                )
            }
            composable(
                route = Screen.VideoPlayer.route,
                arguments = listOf(
                    navArgument("videoTitle") { type = NavType.StringType },
                    navArgument("videoUrl") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val title = backStackEntry.arguments?.getString("videoTitle") ?: "Video"
                val url = backStackEntry.arguments?.getString("videoUrl") ?: ""
                VideoPlayerScreen(
                    navController = navController,
                    videoTitle = title,
                    videoUrl = url
                )
            }
        }
    }
}
