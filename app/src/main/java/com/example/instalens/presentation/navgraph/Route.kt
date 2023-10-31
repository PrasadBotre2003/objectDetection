package com.example.instalens.presentation.navgraph

import com.example.instalens.presentation.utils.Routes

/**
 * Routing objects for each UI screen for NavGraph
 */
sealed class Route(
    val route: String
) {
    // Creating Route objects for each Screen
    object OnBoardingScreen: Route(route = Routes.ROUTE_ONBOARDING_SCREEN)
    object HomeScreen: Route(route = Routes.ROUTE_HOME_SCREEN)
}
