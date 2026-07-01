package com.example.data.repository

import com.example.data.db.ChatDao
import com.example.data.db.ExpenseDao
import com.example.data.db.FarmDao
import com.example.data.db.TaskDao
import com.example.data.model.ChatMessage
import com.example.data.model.CropTask
import com.example.data.model.FarmExpense
import com.example.data.model.FarmProfile
import kotlinx.coroutines.flow.Flow

class FarmRepository(
    private val farmDao: FarmDao,
    private val expenseDao: ExpenseDao,
    private val chatDao: ChatDao,
    private val taskDao: TaskDao
) {
    // Farm Profiles
    val allFarms: Flow<List<FarmProfile>> = farmDao.getAllFarms()
    
    suspend fun insertFarm(farm: FarmProfile) {
        farmDao.insertFarm(farm)
    }

    suspend fun deleteFarm(farm: FarmProfile) {
        farmDao.deleteFarm(farm)
    }

    suspend fun getFarmById(id: Int): FarmProfile? {
        return farmDao.getFarmById(id)
    }

    // Expenses/Income
    val allExpenses: Flow<List<FarmExpense>> = expenseDao.getAllExpenses()

    fun getExpensesForFarm(farmId: Int): Flow<List<FarmExpense>> {
        return expenseDao.getExpensesForFarm(farmId)
    }

    suspend fun insertExpense(expense: FarmExpense) {
        expenseDao.insertExpense(expense)
    }

    suspend fun deleteExpense(expense: FarmExpense) {
        expenseDao.deleteExpense(expense)
    }

    // Chats
    val allChats: Flow<List<ChatMessage>> = chatDao.getAllChats()

    suspend fun insertChat(chat: ChatMessage) {
        chatDao.insertChat(chat)
    }

    suspend fun clearAllChats() {
        chatDao.clearAllChats()
    }

    // Crop Tasks
    val allTasks: Flow<List<CropTask>> = taskDao.getAllTasks()

    fun getTasksForFarm(farmId: Int): Flow<List<CropTask>> {
        return taskDao.getTasksForFarm(farmId)
    }

    suspend fun insertTask(task: CropTask) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: CropTask) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: CropTask) {
        taskDao.deleteTask(task)
    }
}
