package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.FarmExpense
import com.example.data.model.FarmProfile
import com.example.ui.viewmodel.KrishimitraViewModel

@Composable
fun ProfileScreen(viewModel: KrishimitraViewModel) {
    val context = LocalContext.current
    val farms by viewModel.farms.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val selectedFarmId by viewModel.selectedFarmId.collectAsState()

    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var showAddTaskDialog by remember { mutableStateOf(false) }

    val primaryGreen = Color(0xFF2E7D32)
    val secondaryGreen = Color(0xFF4CAF50)
    val accentGold = Color(0xFFFFA000)

    // Calculate Real-Time Net profit based on selected farm expenses
    val selectedFarmExpenses = expenses.filter { it.farmId == selectedFarmId }
    val totalIncome = selectedFarmExpenses.filter { it.isIncome }.sumOf { it.amount }
    val totalExpense = selectedFarmExpenses.filter { !it.isIncome }.sumOf { it.amount }
    val netProfit = totalIncome - totalExpense

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE8F5E9), Color(0xFFFFFFFF))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp)
    ) {
        // --- Farmer Profile Banner Header ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(primaryGreen, Color(0xFF1E3522))
                    )
                )
        ) {
            // Ambient field overlay
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("https://lh3.googleusercontent.com/aida-public/AB6AXuCpV8S79m0SqhLHxaAyUp7OqIkZRp1PKqrL71SzioKcv4KJ-K60I2FzrOImNGELKCBNha_jJhaljS9ENJuBhV3E2-1orWO7Ifhrf_KGSseMu6ck83czQuUekUVD2HiJhXaSSiMKwahiXscd7XN9Qs9gVOAkpVCg4ECmRu2HB470UqSTEyJmMwskvZ-qggFiI8ZKA7z8Z50lm2WxpDjhaMsz_RHRrBRrvI1qPeZLo7ynOQGvfOmY_aRw")
                    .crossfade(true)
                    .build(),
                contentDescription = "Farming banner background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.25f
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Large profile picture
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("https://lh3.googleusercontent.com/aida-public/AB6AXuCpV8S79m0SqhLHxaAyUp7OqIkZRp1PKqrL71SzioKcv4KJ-K60I2FzrOImNGELKCBNha_jJhaljS9ENJuBhV3E2-1orWO7Ifhrf_KGSseMu6ck83czQuUekUVD2HiJhXaSSiMKwahiXscd7XN9Qs9gVOAkpVCg4ECmRu2HB470UqSTEyJmMwskvZ-qggFiI8ZKA7z8Z50lm2WxpDjhaMsz_RHRrBRrvI1qPeZLo7ynOQGvfOmY_aRw")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Farmer profile picture",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Prince Kumar Jha",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "UID: IN-FARM-99321",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .background(accentGold, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Senior Partner Farmer",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // --- Multi-Farm Profiles management list ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Registered Fields",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            farms.forEach { farm ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .shadow(1.dp, RoundedCornerShape(16.dp))
                        .clickable { viewModel.selectFarm(farm.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedFarmId == farm.id) Color(0xFFE8F5E9) else Color.White
                    ),
                    border = BorderStroke(
                        width = if (selectedFarmId == farm.id) 1.5.dp else 1.dp,
                        color = if (selectedFarmId == farm.id) primaryGreen else Color.LightGray.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Landscape,
                                contentDescription = "Registered farm landscape block",
                                tint = primaryGreen,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = farm.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                                Text(
                                    text = "${farm.cropType} • ${farm.sizeAcres} Acres • ${farm.soilType}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        // Allow deleting farm profile except if only one left
                        if (farms.size > 1) {
                            IconButton(onClick = { viewModel.deleteFarmProfile(farm) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Farm profile field",
                                    tint = Color.Red.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Farm profit/loss calculator & Expense Tracker (Premium Feature) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Farm Profit & Expense Tracker",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )

                Row(
                    modifier = Modifier.clickable { showAddExpenseDialog = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Log Expense icon",
                        tint = primaryGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Log Transaction",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Net stats overview row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Income card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    border = BorderStroke(1.dp, Color(0xFFC8E6C9))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Total Income", fontSize = 11.sp, color = Color.Gray)
                        Text("₹${String.format("%,.2f", totalIncome)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = primaryGreen)
                        Icon(imageVector = Icons.Default.TrendingUp, contentDescription = "Profit indicator", tint = primaryGreen, modifier = Modifier.size(16.dp))
                    }
                }

                // Total Expense card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Total Expense", fontSize = 11.sp, color = Color.Gray)
                        Text("₹${String.format("%,.2f", totalExpense)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                        Icon(imageVector = Icons.Default.TrendingDown, contentDescription = "Expense indicator", tint = Color.Red, modifier = Modifier.size(16.dp))
                    }
                }

                // Calculated Net profit card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = if (netProfit >= 0) Color(0xFFFFF8E1) else Color(0xFFFFEBEE)),
                    border = BorderStroke(1.dp, if (netProfit >= 0) accentGold else Color.Red)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Net Profit", fontSize = 11.sp, color = Color.Gray)
                        Text("₹${String.format("%,.2f", netProfit)}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = if (netProfit >= 0) Color(0xFFE65100) else Color.Red)
                        Icon(imageVector = Icons.Default.Payments, contentDescription = "Cash indicator", tint = if (netProfit >= 0) accentGold else Color.Red, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Expenses transaction history list
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Transaction Ledger", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (selectedFarmExpenses.isEmpty()) {
                        Text("No transactions logged yet. Use Log Transaction to track seed, labor, water, or sale records.", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
                    } else {
                        selectedFarmExpenses.take(5).forEach { expense ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (expense.isIncome) Icons.Default.TrendingUp else Icons.Default.RemoveCircleOutline,
                                        contentDescription = "Ledger detail item icon",
                                        tint = if (expense.isIncome) primaryGreen else Color.Red,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(text = expense.description, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
                                        Text(text = expense.category, fontSize = 10.sp, color = Color.Gray)
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${if (expense.isIncome) "+" else "-"} ₹${expense.amount}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (expense.isIncome) primaryGreen else Color.Red
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    IconButton(
                                        onClick = { viewModel.deleteExpenseRecord(expense) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete ledger item",
                                            tint = Color.LightGray,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Crop Schedules & Tasks Checklist ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Farming Chores & Schedule",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )

                Row(
                    modifier = Modifier.clickable { showAddTaskDialog = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add schedule chore",
                        tint = primaryGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add Chore",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val currentFarmTasks = tasks.filter { it.farmId == selectedFarmId }
            if (currentFarmTasks.isEmpty()) {
                Text("No chores scheduled for this farm block yet.", fontSize = 12.sp, color = Color.Gray)
            } else {
                currentFarmTasks.forEach { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Checklist checkbox
                                Icon(
                                    imageVector = if (task.isCompleted) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                                    contentDescription = "Toggle task completion",
                                    tint = if (task.isCompleted) primaryGreen else Color.Gray,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { viewModel.toggleTaskCompletion(task) }
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = task.taskName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (task.isCompleted) Color.Gray else Color.Black,
                                        modifier = Modifier.testTag("task_item_title")
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Due date", tint = Color.Gray, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Due: ${task.dueDate}", fontSize = 10.sp, color = Color.Gray)
                                    }
                                }
                            }

                            IconButton(onClick = { viewModel.deleteTaskRecord(task) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete task chore",
                                    tint = Color.LightGray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Satellite NDVI Monitoring Widget ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, primaryGreen.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Satellite telemetry icon",
                            tint = primaryGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "GCP Sentinel Satellite Monitoring",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("NDVI Greenery Index", fontSize = 11.sp, color = Color.Gray)
                            Text("0.78 (Optimal)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = primaryGreen)
                        }
                        Column {
                            Text("Water Stress Index", fontSize = 11.sp, color = Color.Gray)
                            Text("0.12 (Optimal)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = primaryGreen)
                        }
                        Column {
                            Text("Soil Nitrogen Index", fontSize = 11.sp, color = Color.Gray)
                            Text("Medium", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = accentGold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Last Satellite check completed today 08:32 AM. Telemetry indexes indicate healthy moisture density across Block A.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Government Schemes list ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Government Schemes directory",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    SchemeItem("PM Kisan Samman Nidhi", "₹6,000 / year pension delivered to accounts in 3 installments.")
                    SchemeItem("PM Fasal Bima Yojana", "Secure dynamic calamity crop insurance with 1.5% - 2% minimal premiums.")
                    SchemeItem("Solar Pump Subsidy", "Get up to 60% subsidy on installing sustainable off-grid solar borewell pumps.")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Community Forum group join widget ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFE8F5E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Farmer grower forum",
                            tint = primaryGreen
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Join Maharashtra Wheat Growers",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = "Connect with 12,400+ local organic farmers to share experiences.",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Button(
                        onClick = { /* Join community */ },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("JOIN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }

    // --- Log Transaction Dialog ---
    if (showAddExpenseDialog) {
        var category by remember { mutableStateOf("Seeds") }
        var amountStr by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var isIncome by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddExpenseDialog = false },
            title = { Text(text = "Log Farm Transaction") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Type Row toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            onClick = { isIncome = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isIncome) Color.Red else Color.LightGray
                            )
                        ) {
                            Text("Expense")
                        }
                        Button(
                            onClick = { isIncome = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isIncome) primaryGreen else Color.LightGray
                            )
                        ) {
                            Text("Income")
                        }
                    }

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category (e.g. Seeds, Labor, Water, Income)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = amountStr,
                        onValueChange = { amountStr = it },
                        label = { Text("Amount (INR)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Short Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val amountVal = amountStr.toDoubleOrNull() ?: 0.0
                        viewModel.addExpenseRecord(
                            amount = amountVal,
                            category = category.ifEmpty { "Other" },
                            description = description.ifEmpty { "Farm ledger entry" },
                            isIncome = isIncome,
                            farmId = selectedFarmId
                        )
                        showAddExpenseDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddExpenseDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // --- Add Task Chores Dialog ---
    if (showAddTaskDialog) {
        var taskName by remember { mutableStateOf("") }
        var dueDate by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddTaskDialog = false },
            title = { Text(text = "Schedule Crop Chore") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = taskName,
                        onValueChange = { taskName = it },
                        label = { Text("Chore Title (e.g. Harvest crops)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = dueDate,
                        onValueChange = { dueDate = it },
                        label = { Text("Due Date / Time (e.g. July 12)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (taskName.trim().isNotEmpty()) {
                            viewModel.addNewTask(
                                farmId = selectedFarmId,
                                name = taskName,
                                dueDate = dueDate.ifEmpty { "July 10" }
                            )
                        }
                        showAddTaskDialog = false
                    }
                ) {
                    Text("Schedule")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTaskDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SchemeItem(title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Assignment,
            contentDescription = "Scheme document",
            tint = Color(0xFF2E7D32),
            modifier = Modifier
                .padding(top = 2.dp)
                .size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
            Text(text = desc, fontSize = 11.sp, color = Color.Gray, lineHeight = 15.sp)
        }
    }
}
