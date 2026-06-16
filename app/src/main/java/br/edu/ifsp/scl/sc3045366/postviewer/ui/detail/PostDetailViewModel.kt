package br.edu.ifsp.scl.sc3045366.postviewer.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.scl.sc3045366.postviewer.data.local.AppDatabase
import br.edu.ifsp.scl.sc3045366.postviewer.data.local.LocalComment
import br.edu.ifsp.scl.sc3045366.postviewer.data.remote.ApiComment
import br.edu.ifsp.scl.sc3045366.postviewer.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class PostDetailUiState {
    object Loading : PostDetailUiState()
    data class Success(
        val apiComments: List<ApiComment>,
        val localComments: List<LocalComment>
    ) : PostDetailUiState()
    data class Error(val message: String) : PostDetailUiState()
}

// gerencia os dados da tela de detalhe: une comentários da api com os salvos localmente
class PostDetailViewModel(
    application: Application,
    private val postId: Int
) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).localCommentDao()

    private val apiComments = MutableStateFlow<List<ApiComment>>(emptyList())
    private val isLoading = MutableStateFlow(true)
    private val error = MutableStateFlow<String?>(null)

    // quando qualquer fonte muda (api ou banco), a tela atualiza automaticamente
    val uiState: StateFlow<PostDetailUiState> = combine(
        isLoading,
        error,
        apiComments,
        dao.getByPostId(postId)
    ) { loading, err, api, local ->
        when {
            err != null -> PostDetailUiState.Error(err)
            loading -> PostDetailUiState.Loading
            else -> PostDetailUiState.Success(api, local)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PostDetailUiState.Loading)

    init {
        loadComments()
    }

    private fun loadComments() {
        viewModelScope.launch {
            try {
                apiComments.value = RetrofitClient.apiService.getComments(postId)
                isLoading.value = false
            } catch (e: Exception) {
                error.value = e.message ?: "Unknown error"
                isLoading.value = false
            }
        }
    }

    fun addLocalComment(body: String) {
        viewModelScope.launch {
            dao.insert(LocalComment(postId = postId, body = body))
        }
    }

    companion object {
        // necessário porque (PostDetailViewModel) recebe (postId) além do (Application)
        fun factory(postId: Int, application: Application) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return PostDetailViewModel(application, postId) as T
            }
        }
    }
}
