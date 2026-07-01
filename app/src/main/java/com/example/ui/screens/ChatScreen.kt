package com.example.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.example.ui.viewmodel.KrishimitraViewModel
import com.example.ui.viewmodel.Tab
import java.util.Locale

@Composable
fun ChatScreen(viewModel: KrishimitraViewModel) {
    val context = LocalContext.current
    val chats by viewModel.chats.collectAsState()
    val isAITyping by viewModel.isAITyping.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    
    var inputText by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()

    // Setup TextToSpeech to read back AI answers natively
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    // Read last message from AI if voice is activated
    LaunchedEffect(isSpeaking) {
        if (isSpeaking && chats.isNotEmpty()) {
            val lastMsg = chats.last()
            if (lastMsg.sender == "mitra") {
                // Remove formatting stars for cleaner TTS speech
                val cleanSpeech = lastMsg.text.replace("**", "").replace("•", "")
                tts?.speak(cleanSpeech, TextToSpeech.QUEUE_FLUSH, null, null)
            }
            // Stop speaking indicator after some time
            kotlinx.coroutines.delay(8000)
            viewModel.setSpeaking(false)
        }
    }

    // Auto scroll chat to bottom when list changes
    LaunchedEffect(chats.size, isAITyping) {
        if (chats.isNotEmpty()) {
            lazyListState.animateScrollToItem(chats.size - 1)
        }
    }

    val primaryGreen = Color(0xFF2E7D32)
    val secondaryGreen = Color(0xFF4CAF50)
    val accentGold = Color(0xFFFFA000)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE8F5E9), Color(0xFFFFFFFF))
                )
            )
            .padding(bottom = 96.dp) // Space for bottom navigation
    ) {
        // --- Chat Custom Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Mitra AI avatar
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("https://lh3.googleusercontent.com/aida-public/AB6AXuBuxoZ-SRKSi-NWBf13YXBseNs1MroL6XvGto2piut4cHKU370do5UTYCV71t-g_2p0KqrthyZb3MEjbvuYX6_iemOJ9BvG4-aaQq9BOK353jXl_Msa2ZqX-R5-8wkuBsG9pkqrDo8Bb_nl_NMwJ4vfPLQKrnx5Ty8RfFFHHL77s1o5fr2C6oulsDriTGfKkTSfih89DHs5TVtH8Cd6qNJsAPkl7gP7hsRDBi_teivbKmsz8wLPMBr9")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Mitra AI Profile Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, primaryGreen, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Mitra AI Chat",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen
                    )
                    Text(
                        text = "Expert Farming Ecosystem",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            // Clear chat button
            IconButton(
                onClick = { viewModel.clearChatHistory() },
                modifier = Modifier.testTag("clear_chat_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ClearAll,
                    contentDescription = "Clear Chat logs",
                    tint = Color.Gray
                )
            }
        }

        // --- "Mitra is speaking..." Dynamic Visualizer Bar ---
        if (isSpeaking) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .clickable {
                        tts?.stop()
                        viewModel.setSpeaking(false)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Mitra is speaking...",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    // Cute pulsating wave visualizer bars (animated rows)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedWaveBar(durationMs = 600)
                        AnimatedWaveBar(durationMs = 900)
                        AnimatedWaveBar(durationMs = 700)
                        AnimatedWaveBar(durationMs = 1100)
                        AnimatedWaveBar(durationMs = 500)
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "(Tap to stop)",
                        fontSize = 10.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // --- Chat Message Stream Canvas ---
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(10.dp)) }

            items(chats) { chat ->
                if (chat.sender == "user") {
                    // User Message Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(primaryGreen, secondaryGreen)
                                    ),
                                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp)
                                )
                                .padding(14.dp)
                        ) {
                            Text(
                                text = chat.text,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                } else {
                    // Mitra AI Message Row
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = "Mitra robot help logo",
                                tint = primaryGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Mitra AI",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(
                                    Color.White,
                                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp)
                                )
                                .border(1.dp, Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = chat.text,
                                color = Color(0xFF33691E),
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }

                        // Special Trigger: If AI answers about Wheat Rust treatment, append an inline recommended Treatment Guide card
                        if (chat.text.contains("Wheat Leaf Rust") || chat.text.contains("Wheat Rust")) {
                            Spacer(modifier = Modifier.height(12.dp))
                            TreatmentGuideCard(
                                title = "Organic Neem-Garlic Formula",
                                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCAKUQGbyDJXGPJo6jNwymnx6N6vMECmerJPCsynWdSWlhXeWUncbmfImYMflu8j7QPJTBStCQAqSudQJlNqm8aKB789bQlep6FAPwMAf3pdMQzdL6w_3CLlNl6D7GjNXUrJfdjprjRMKgG4fGdKNcDT0Y8R-KoY05hMXW01lvyR2BoRuYBNhidZGCjF7YYfj0W_Vwe9y004_qALCODJRXVo9DQR2pNzaEATj0xW9aB5ZavzrqdNffw",
                                points = listOf(
                                    "Mix 5ml Neem oil + 2g Soap per liter.",
                                    "Spray early morning or late evening.",
                                    "Repeat every 7-10 days until clear."
                                ),
                                onAddTask = {
                                    viewModel.addNewTask(1, "Spray Neem-Garlic organic formula", "July 5, 2026")
                                }
                            )
                        }
                    }
                }
            }

            // AI Typing Loading indicator
            if (isAITyping) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = primaryGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Mitra is searching AI databases...",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }
        }

        // --- Preset Questions Horizontal bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PresetPill("Treat Wheat Rust?") { inputText = "How do I treat Wheat Leaf Rust organically?" }
            PresetPill("NPK for Cotton?") { inputText = "What are the NPK recommendations for Cotton fields?" }
            PresetPill("PM Kisan Scheme?") { inputText = "What is the PM Kisan scheme and how do I apply?" }
            PresetPill("Cow Feeding Schedule?") { inputText = "Give me a healthy feeding schedule for a milking cow." }
        }

        // --- Bottom Input Panel Bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(Color.White, RoundedCornerShape(24.dp))
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Camera/Attachment Button
            IconButton(
                onClick = { viewModel.navigateTo(Tab.CAMERA) },
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Attach disease crop image",
                    tint = primaryGreen
                )
            }

            // Text Input Field
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Ask Mitra anything...", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_text_field"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 3
            )

            // Mic Speech Button (Mocks voice recognition typing)
            IconButton(
                onClick = {
                    inputText = "Wheat Leaf Rust detected organically"
                    viewModel.sendChatMessage("My wheat crop Block A has orange spots on leaves. Is this Wheat Leaf Rust? How do I treat it organically?")
                    inputText = ""
                },
                modifier = Modifier
                    .size(44.dp)
                    .background(primaryGreen, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice assistant speaking input",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Send Button
            IconButton(
                onClick = {
                    if (inputText.trim().isNotEmpty()) {
                        viewModel.sendChatMessage(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .background(primaryGreen, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send message to Mitra",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun PresetPill(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFF81C784), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )
    }
}

@Composable
fun AnimatedWaveBar(durationMs: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "WaveBar")
    val heightScale by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scale"
    )

    Box(
        modifier = Modifier
            .width(3.dp)
            .fillMaxHeight(0.8f * heightScale)
            .height(28.dp)
            .background(Color(0xFF2E7D32), RoundedCornerShape(4.dp))
    )
}

@Composable
fun TreatmentGuideCard(
    title: String,
    imageUrl: String,
    points: List<String>,
    onAddTask: () -> Unit
) {
    val context = LocalContext.current
    var addedToTasks by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(320.dp)
            .padding(top = 8.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, Color(0xFFFFA000))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI recommended treatment label",
                    tint = Color(0xFFFFA000),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI RECOMMENDED TREATMENT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFFA000),
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Beautiful treatment macro photo via Coil
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )

                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Application Guide",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Render custom bullet points
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                points.forEach { point ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "•", color = Color(0xFFFFA000), modifier = Modifier.padding(end = 6.dp))
                        Text(text = point, fontSize = 12.sp, color = Color.DarkGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    onAddTask()
                    addedToTasks = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (addedToTasks) Color.Gray else Color(0xFF2E7D32)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (addedToTasks) "ADDED TO FARM TASKS" else "ADD TO FARM TASKS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = Color.White
                )
            }
        }
    }
}
