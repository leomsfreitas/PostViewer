package br.edu.ifsp.scl.sc3045366.postviewer.ui.navigation

// rotas de navegação do app
sealed class Screen(val route: String) {
    object PostList : Screen(route = "posts")
    object PostDetail : Screen(route = "detail/{postId}") {
        // monta a rota com o id real do post para navegar: "detail/42"
        fun createRoute(postId: Int) = "detail/$postId"
    }
}
