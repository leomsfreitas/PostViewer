package br.edu.ifsp.scl.sc3045366.postviewer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// banco local do app: persiste os comentários do usuário
@Database(entities = [LocalComment::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun localCommentDao(): LocalCommentDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "postviewer.db"
                ).build().also { instance = it }
            }
        }
    }
}
