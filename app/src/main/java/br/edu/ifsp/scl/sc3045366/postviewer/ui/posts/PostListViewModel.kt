package br.edu.ifsp.scl.sc3045366.postviewer.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.scl.sc3045366.postviewer.data.remote.Post
import br.edu.ifsp.scl.sc3045366.postviewer.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PostListUiState {
    object Loading : PostListUiState()
    data class Success(
        val posts: List<Post>,
        val commentCounts: Map<Int, Int> = emptyMap()
    ) : PostListUiState()
    data class Error(val message: String) : PostListUiState()
}

class PostListViewModel : ViewModel() {
    // estado observado pela tela: pode ser loading, lista de posts ou erro
    private val _uiState = MutableStateFlow<PostListUiState>(PostListUiState.Loading)
    val uiState: StateFlow<PostListUiState> = _uiState

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            try {
                val posts = RetrofitClient.apiService.getPosts()
                val commentCounts = posts.associate { post ->
                    post.id to RetrofitClient.apiService.getComments(post.id).size
                }
                _uiState.value = PostListUiState.Success(posts, commentCounts)
            } catch (e: Exception) {
                _uiState.value = PostListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun totalCommentsById(postId: Int): Int =
        (_uiState.value as? PostListUiState.Success)?.commentCounts?.get(postId) ?: 0
}
