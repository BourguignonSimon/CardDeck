package com.example.carddeck.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.carddeck.ui.emotion.EmotionSelectionScreen
import com.example.carddeck.ui.emotion.EmotionSelectionViewModel
import com.example.carddeck.ui.group.GroupViewScreen
import com.example.carddeck.ui.group.GroupViewViewModel
import com.example.carddeck.ui.home.HomeScreen
import com.example.carddeck.ui.session.create.CreateSessionScreen
import com.example.carddeck.ui.session.create.CreateSessionViewModel
import com.example.carddeck.ui.session.join.JoinSessionScreen
import com.example.carddeck.ui.session.join.JoinSessionViewModel

object Destinations {
    const val HOME = "home"
    const val CREATE_SESSION = "create_session"
    const val JOIN_SESSION = "join_session"
    const val EMOTION_SELECTION = "emotion_selection"
    const val GROUP_VIEW = "group_view"
}

@Composable
fun CardDeckNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.HOME,
        modifier = modifier
    ) {
        composable(Destinations.HOME) {
            HomeScreen(
                onCreateSession = { navController.navigate(Destinations.CREATE_SESSION) },
                onJoinSession = { navController.navigate(Destinations.JOIN_SESSION) }
            )
        }
        composable(Destinations.CREATE_SESSION) {
            val viewModel: CreateSessionViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            CreateSessionScreen(
                state = state,
                onCreateAnother = viewModel::createSession,
                onOpenGroupView = { code ->
                    navController.navigate("${Destinations.GROUP_VIEW}/$code")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Destinations.JOIN_SESSION) {
            val viewModel: JoinSessionViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            JoinSessionScreen(
                state = state,
                onCodeChange = viewModel::onCodeChanged,
                onJoinClicked = viewModel::joinSession,
                onBack = { navController.popBackStack() }
            )
            val joinedCode = state.joinedSessionCode
            if (joinedCode != null) {
                LaunchedEffect(joinedCode) {
                    navController.navigate("${Destinations.EMOTION_SELECTION}/$joinedCode") {
                        popUpTo(Destinations.HOME)
                    }
                    viewModel.onNavigationHandled()
                }
            }
        }
        composable(
            route = "${Destinations.EMOTION_SELECTION}/{${NavArgs.SESSION_CODE}}",
            arguments = listOf(navArgument(NavArgs.SESSION_CODE) { type = NavType.StringType })
        ) {
            val viewModel: EmotionSelectionViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            EmotionSelectionScreen(
                state = state,
                onEmotionClick = viewModel::toggleEmotion,
                onSubmit = viewModel::submitSelection,
                onSubmissionHandled = viewModel::onSubmissionHandled,
                onFinished = { navController.navigate(Destinations.HOME) { popUpTo(Destinations.HOME) { inclusive = true } } }
            )
        }
        composable(
            route = "${Destinations.GROUP_VIEW}/{${NavArgs.SESSION_CODE}}",
            arguments = listOf(navArgument(NavArgs.SESSION_CODE) { type = NavType.StringType })
        ) {
            val viewModel: GroupViewViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            GroupViewScreen(
                sessionCode = it.arguments?.getString(NavArgs.SESSION_CODE).orEmpty(),
                state = state,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
