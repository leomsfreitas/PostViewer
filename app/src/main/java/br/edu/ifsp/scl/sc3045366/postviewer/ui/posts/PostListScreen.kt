package br.edu.ifsp.scl.sc3045366.postviewer.ui.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// lista os posts buscados da API
@Composable
fun PostListScreen(
    onPostClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PostListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val state = uiState) {
            is PostListUiState.Loading -> CircularProgressIndicator()
            is PostListUiState.Error -> Text("Error: ${state.message}")
            is PostListUiState.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.posts) { post ->
                    Text(
                        text = post.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPostClick(post.id) }
                            .padding(16.dp)
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
