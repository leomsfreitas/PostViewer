package br.edu.ifsp.scl.sc3045366.postviewer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.edu.ifsp.scl.sc3045366.postviewer.ui.detail.PostDetailScreen
import br.edu.ifsp.scl.sc3045366.postviewer.ui.navigation.Screen
import br.edu.ifsp.scl.sc3045366.postviewer.ui.posts.PostListScreen

// define quais telas existem e como navegar entre elas
@Composable
fun AppNavGraph(navHostController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navHostController, startDestination = Screen.PostList.route) {
        composable(route = Screen.PostList.route) {
            PostListScreen(
                modifier = modifier,
                onPostClick = { postId ->
                    navHostController.navigate(Screen.PostDetail.createRoute(postId))
                }
            )
        }
        composable(
            route = Screen.PostDetail.route,
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments!!.getInt("postId")
            PostDetailScreen(postId = postId, modifier = modifier)
        }
    }
}
