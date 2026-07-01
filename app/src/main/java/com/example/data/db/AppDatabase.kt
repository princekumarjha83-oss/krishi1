package com.example.data.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.data.model.ChatMessage
import com.example.data.model.CropTask
import com.example.data.model.FarmExpense
import com.example.data.model.FarmProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmDao {
    @Query("SELECT * FROM farms ORDER BY id DESC")
    fun getAllFarms(): Flow<List<FarmProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarm(farm: FarmProfile)

    @Delete
    suspend fun deleteFarm(farm: FarmProfile)

    @Query("SELECT * FROM farms WHERE id = :id")
    suspend fun getFarmById(id: Int): FarmProfile?
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<FarmExpense>>

    @Query("SELECT * FROM expenses WHERE farmId = :farmId ORDER BY date DESC")
    fun getExpensesForFarm(farmId: Int): Flow<List<FarmExpense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: FarmExpense)

    @Delete
    suspend fun deleteExpense(expense: FarmExpense)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY timestamp ASC")
    fun getAllChats(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatMessage)

    @Query("DELETE FROM chats")
    suspend fun clearAllChats()
}

@Dao
interface TaskDao {
    @Query("SELECT * FROM crop_tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<CropTask>>

    @Query("SELECT * FROM crop_tasks WHERE farmId = :farmId ORDER BY dueDate ASC")
    fun getTasksForFarm(farmId: Int): Flow<List<CropTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: CropTask)

    @Update
    suspend fun updateTask(task: CropTask)

    @Delete
    suspend fun deleteTask(task: CropTask)
}

@Database(
    entities = [FarmProfile::class, FarmExpense::class, ChatMessage::class, CropTask::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun farmDao(): FarmDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun chatDao(): ChatDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "krishimitra_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
