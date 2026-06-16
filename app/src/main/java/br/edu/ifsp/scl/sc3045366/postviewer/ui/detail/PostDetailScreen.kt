package br.edu.ifsp.scl.sc3045366.postviewer.ui.detail

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// tela de detalhe: mostra comentários da api e os locais, e permite adicionar novos
@Composable
fun PostDetailScreen(postId: Int, modifier: Modifier = Modifier) {
    val application = LocalContext.current.applicationContext as Application
    // cada post tem seu próprio (PostDetailViewModel), o estado não mistura entre posts
    val viewModel: PostDetailViewModel = viewModel(
        key = "detail_$postId",
        factory = PostDetailViewModel.factory(postId, application)
    )

    val uiState by viewModel.uiState.collectAsState()
    var newCommentText by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is PostDetailUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 32.dp)
                )
            }
            is PostDetailUiState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    modifier = Modifier.weight(1f).padding(16.dp)
                )
            }
            is PostDetailUiState.Success -> {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.apiComments) { comment ->
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(comment.name, fontWeight = FontWeight.Bold)
                            Text(comment.body)
                        }
                        HorizontalDivider()
                    }
                    items(state.localComments) { comment ->
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("You", fontWeight = FontWeight.Bold)
                            Text(comment.body)
                        }
                        HorizontalDivider()
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newCommentText,
                onValueChange = { newCommentText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Add a comment...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newCommentText.isNotBlank()) {
                    viewModel.addLocalComment(newCommentText)
                    newCommentText = ""
                }
            }) {
                Text("Post")
            }
        }
    }
}
