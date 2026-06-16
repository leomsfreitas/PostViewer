package br.edu.ifsp.scl.sc3045366.postviewer.data.remote

data class ApiComment(
    val id: Int,
    val postId: Int,
    val name: String,
    val email: String,
    val body: String
)
