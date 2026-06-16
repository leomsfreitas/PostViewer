package br.edu.ifsp.scl.sc3045366.postviewer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// comentário escrito pelo usuário, salvo no dispositivo
@Entity(tableName = "local_comments")
data class LocalComment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postId: Int,
    val body: String
)
