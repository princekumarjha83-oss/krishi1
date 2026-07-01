package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farms")
data class FarmProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val location: String,
    val cropType: String,
    val sowingDate: String,
    val soilType: String,
    val sizeAcres: Double
)

@Entity(tableName = "expenses")
data class FarmExpense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val farmId: Int,
    val category: String, // e.g. Seeds, Fertilizer, Pesticides, Water, Labor, Income
    val amount: Double,
    val description: String,
    val isIncome: Boolean,
    val date: Long = System.currentTimeMillis()
)

@Entity(tableName = "chats")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String, // "user" or "mitra"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String? = null // Optional local file path or base64 for disease analysis
)

@Entity(tableName = "crop_tasks")
data class CropTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val farmId: Int,
    val taskName: String,
    val dueDate: String,
    val isCompleted: Boolean = false
)
