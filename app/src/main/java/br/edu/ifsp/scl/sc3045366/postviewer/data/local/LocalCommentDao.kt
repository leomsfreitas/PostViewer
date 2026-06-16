package br.edu.ifsp.scl.sc3045366.postviewer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// como o app lê e salva comentários locais
@Dao
interface LocalCommentDao {
    // a lista na tela atualiza em tempo real quando um comentário é salvo
    @Query("SELECT * FROM local_comments WHERE postId = :postId")
    fun getByPostId(postId: Int): Flow<List<LocalComment>>

    @Insert
    suspend fun insert(comment: LocalComment)
}
