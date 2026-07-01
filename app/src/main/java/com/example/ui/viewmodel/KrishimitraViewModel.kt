package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.DiseaseAnalysisResult
import com.example.data.api.GeminiClient
import com.example.data.db.AppDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.CropTask
import com.example.data.model.FarmExpense
import com.example.data.model.FarmProfile
import com.example.data.repository.FarmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KrishimitraViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "KrishimitraViewModel"
    private val repository: FarmRepository

    // Database Initialization
    init {
        val database = AppDatabase.getDatabase(application)
        repository = FarmRepository(
            database.farmDao(),
            database.expenseDao(),
            database.chatDao(),
            database.taskDao()
        )
        // Prepopulate data if empty so the UI is immediately fully interactive
        seedDatabaseIfEmpty()
    }

    // Tab Navigation State
    private val _currentTab = MutableStateFlow(Tab.SPLASH)
    val currentTab: StateFlow<Tab> = _currentTab.asStateFlow()

    fun navigateTo(tab: Tab) {
        _currentTab.value = tab
    }

    // Language State
    private val _selectedLanguage = MutableStateFlow("English")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    fun changeLanguage(language: String) {
        _selectedLanguage.value = language
    }

    // Lists from Database
    val farms: StateFlow<List<FarmProfile>> = repository.allFarms
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<FarmExpense>> = repository.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chats: StateFlow<List<ChatMessage>> = repository.allChats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<CropTask>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Farm Profile (Default is first farm)
    private val _selectedFarmId = MutableStateFlow(1)
    val selectedFarmId: StateFlow<Int> = _selectedFarmId.asStateFlow()

    fun selectFarm(id: Int) {
        _selectedFarmId.value = id
        updateWeatherForSelectedFarm()
    }

    // Dynamic Weather State
    private val _weatherState = MutableStateFlow(WeatherInfo())
    val weatherState: StateFlow<WeatherInfo> = _weatherState.asStateFlow()

    // Crop Disease Scanning State
    private val _scanningState = MutableStateFlow<ScanningState>(ScanningState.Idle)
    val scanningState: StateFlow<ScanningState> = _scanningState.asStateFlow()

    private val _scannedImage = MutableStateFlow<Bitmap?>(null)
    val scannedImage: StateFlow<Bitmap?> = _scannedImage.asStateFlow()

    // Chat Typing State
    private val _isAITyping = MutableStateFlow(false)
    val isAITyping: StateFlow<Boolean> = _isAITyping.asStateFlow()

    // Voice Feedback State (Speaking / listening indicator)
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    fun setSpeaking(speaking: Boolean) {
        _isSpeaking.value = speaking
    }

    // --- Action Methods ---

    // Send AI Chat Message
    fun sendChatMessage(text: String) {
        if (text.trim().isEmpty()) return

        viewModelScope.launch {
            // 1. Save user message to database
            val userMsg = ChatMessage(sender = "user", text = text)
            repository.insertChat(userMsg)

            // 2. Set AI typing state
            _isAITyping.value = true

            // 3. Prepare Chat History for Context
            val currentHistory = chats.value.takeLast(10).map { Pair(it.sender, it.text) }

            // 4. Fire Gemini call
            val aiResponse = GeminiClient.generateChatResponse(text, currentHistory)

            // 5. Save AI response to database
            val aiMsg = ChatMessage(sender = "mitra", text = aiResponse)
            repository.insertChat(aiMsg)

            // 6. Complete typing state
            _isAITyping.value = false

            // Trigger voice effect if user wanted voice feedback (simplified voice simulation)
            setSpeaking(true)
        }
    }

    // Clear Chats
    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearAllChats()
        }
    }

    // Scan Crop for Disease
    fun setScannedImageBitmap(bitmap: Bitmap) {
        _scannedImage.value = bitmap
        _scanningState.value = ScanningState.Idle
    }

    fun scanCropDisease(bitmap: Bitmap, cropType: String) {
        _scannedImage.value = bitmap
        _scanningState.value = ScanningState.Scanning

        viewModelScope.launch {
            try {
                val result = GeminiClient.analyzeCropDisease(bitmap, cropType)
                _scanningState.value = ScanningState.Success(result)
            } catch (e: Exception) {
                Log.e(TAG, "Disease scanning failed: ${e.message}")
                _scanningState.value = ScanningState.Error("An error occurred during disease analysis. Please try again.")
            }
        }
    }

    fun resetScanning() {
        _scanningState.value = ScanningState.Idle
        _scannedImage.value = null
    }

    // Add New Farm Profile
    fun addFarm(name: String, location: String, crop: String, sowingDate: String, soilType: String, size: Double) {
        viewModelScope.launch {
            repository.insertFarm(
                FarmProfile(
                    name = name,
                    location = location,
                    cropType = crop,
                    sowingDate = sowingDate,
                    soilType = soilType,
                    sizeAcres = size
                )
            )
        }
    }

    fun deleteFarmProfile(farm: FarmProfile) {
        viewModelScope.launch {
            repository.deleteFarm(farm)
        }
    }

    // Add Expense/Income
    fun addExpenseRecord(amount: Double, category: String, description: String, isIncome: Boolean, farmId: Int) {
        viewModelScope.launch {
            repository.insertExpense(
                FarmExpense(
                    farmId = farmId,
                    category = category,
                    amount = amount,
                    description = description,
                    isIncome = isIncome
                )
            )
        }
    }

    fun deleteExpenseRecord(expense: FarmExpense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    // Tasks Management
    fun addNewTask(farmId: Int, name: String, dueDate: String) {
        viewModelScope.launch {
            repository.insertTask(
                CropTask(
                    farmId = farmId,
                    taskName = name,
                    dueDate = dueDate,
                    isCompleted = false
                )
            )
        }
    }

    fun toggleTaskCompletion(task: CropTask) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTaskRecord(task: CropTask) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    // Update Weather Based on Farm
    private fun updateWeatherForSelectedFarm() {
        val selectedFarm = farms.value.find { it.id == _selectedFarmId.value }
        if (selectedFarm != null) {
            _weatherState.value = when (selectedFarm.cropType.lowercase()) {
                "wheat" -> WeatherInfo(
                    temperature = "28°C",
                    humidity = "65%",
                    windSpeed = "12 km/h",
                    rainChance = "0%",
                    condition = "Sunny Intervals",
                    district = selectedFarm.location,
                    irrigationAdvice = "Best day for sowing & light watering. Evaporation rate will be low tomorrow."
                )
                "cotton" -> WeatherInfo(
                    temperature = "32°C",
                    humidity = "55%",
                    windSpeed = "18 km/h",
                    rainChance = "10%",
                    condition = "Mostly Clear",
                    district = selectedFarm.location,
                    irrigationAdvice = "Foliage moisture is low. Monitor water levels. Drip irrigation recommended tonight."
                )
                "rice", "paddy" -> WeatherInfo(
                    temperature = "26°C",
                    humidity = "85%",
                    windSpeed = "10 km/h",
                    rainChance = "80%",
                    condition = "Heavy Showers Forecasted",
                    district = selectedFarm.location,
                    irrigationAdvice = "Suspend manual watering. Heavy rains are expected over the next 24 hours. Ensure drainage."
                )
                else -> WeatherInfo(
                    temperature = "29°C",
                    humidity = "60%",
                    windSpeed = "14 km/h",
                    rainChance = "15%",
                    condition = "Partly Cloudy",
                    district = selectedFarm.location,
                    irrigationAdvice = "Soil conditions are favorable. Follow standard weekly watering cycle."
                )
            }
        }
    }

    // --- Seeding Methods ---

    private fun seedDatabaseIfEmpty() {
        viewModelScope.launch {
            // We use a simple first-run check (if farms is empty)
            repository.allFarms.collect { currentFarms ->
                if (currentFarms.isEmpty()) {
                    Log.d(TAG, "Seeding default data in database...")
                    // 1. Seed default Farm Profiles
                    repository.insertFarm(
                        FarmProfile(
                            id = 1,
                            name = "Wheat Block A",
                            location = "Pune District, MH",
                            cropType = "Wheat",
                            sowingDate = "Oct 12, 2025",
                            soilType = "Black Cotton Soil",
                            sizeAcres = 2.5
                        )
                    )
                    repository.insertFarm(
                        FarmProfile(
                            id = 2,
                            name = "Cotton Block C",
                            location = "Amravati, Vidarbha",
                            cropType = "Cotton",
                            sowingDate = "Nov 05, 2025",
                            soilType = "Alluvial Soil",
                            sizeAcres = 4.0
                        )
                    )

                    // 2. Seed default expenses
                    repository.insertExpense(
                        FarmExpense(
                            farmId = 1,
                            category = "Seeds",
                            amount = 12000.0,
                            description = "High Yield certified HD-3226 wheat seeds",
                            isIncome = false
                        )
                    )
                    repository.insertExpense(
                        FarmExpense(
                            farmId = 1,
                            category = "Fertilizer",
                            amount = 8500.0,
                            description = "NPK 19-19-19 chemical spray bags",
                            isIncome = false
                        )
                    )
                    repository.insertExpense(
                        FarmExpense(
                            farmId = 1,
                            category = "Income",
                            amount = 45000.0,
                            description = "Advance sale of grade A wheat grains to local market",
                            isIncome = true
                        )
                    )

                    // 3. Seed default crop schedules/tasks
                    repository.insertTask(
                        CropTask(
                            farmId = 1,
                            taskName = "Irrigate Block A tonight",
                            dueDate = "Tonight, 9:00 PM",
                            isCompleted = false
                        )
                    )
                    repository.insertTask(
                        CropTask(
                            farmId = 1,
                            taskName = "Apply organic mulch to Cotton rows",
                            dueDate = "Tomorrow morning",
                            isCompleted = false
                        )
                    )
                    repository.insertTask(
                        CropTask(
                            farmId = 1,
                            taskName = "Conduct crop-disease scan on leaves",
                            dueDate = "Jul 05, 2026",
                            isCompleted = true
                        )
                    )

                    // 4. Seed welcome chat
                    repository.insertChat(
                        ChatMessage(
                            sender = "mitra",
                            text = "Namaste! I am Mitra AI, your personal farming ecosystem companion. I'm here to help you solve crop diseases, track your mandi prices, calculate expenses, and give you smart scientific irrigation advice.\n\nAsk me anything! For example: \"How do I treat Wheat Rust organically?\""
                        )
                    )

                    // Update initial weather
                    _selectedFarmId.value = 1
                    updateWeatherForSelectedFarm()
                }
            }
        }
    }
}

// Active UI Navigation tabs
enum class Tab {
    SPLASH, HOME, CHAT, CAMERA, MARKET, PROFILE
}

// Custom data wrappers
data class WeatherInfo(
    val temperature: String = "28°C",
    val humidity: String = "65%",
    val windSpeed: String = "12 km/h",
    val rainChance: String = "0%",
    val condition: String = "Cloudy",
    val district: String = "Pune District, MH",
    val irrigationAdvice: String = "Best day for sowing & light watering. Evaporation rate will be low tomorrow."
)

sealed interface ScanningState {
    object Idle : ScanningState
    object Scanning : ScanningState
    data class Success(val result: DiseaseAnalysisResult) : ScanningState
    data class Error(val message: String) : ScanningState
}
