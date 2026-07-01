package com.example.ui.screens
 
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.viewmodel.KrishimitraViewModel
import com.example.ui.viewmodel.Tab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: KrishimitraViewModel) {
    val context = LocalContext.current
    val currentTab by viewModel.currentTab.collectAsState()
    val farms by viewModel.farms.collectAsState()
    val weather by viewModel.weatherState.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val selectedFarmId by viewModel.selectedFarmId.collectAsState()

    var showAddCropDialog by remember { mutableStateOf(false) }
    var showLanguageDropdown by remember { mutableStateOf(false) }
    var showFarmSwitcherDropdown by remember { mutableStateOf(false) }

    val languages = listOf("English", "Hindi", "Kannada", "Telugu", "Tamil", "Bengali", "Marathi", "Gujarati", "Punjabi", "Malayalam", "Odia")

    // Dynamic color helper
    val primaryGreen = Color(0xFF2E7D32)
    val accentGold = Color(0xFFFFA000)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE8F5E9), Color(0xFFFFFFFF))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp) // Bottom padding for navigation
    ) {
        // --- Custom Premium Header App Bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showFarmSwitcherDropdown = true }
            ) {
                // Farmer Avatar Profile
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("https://lh3.googleusercontent.com/aida-public/AB6AXuBI-AETwgFrBxEQD7F2cHMRfQo9NpARWvhxoxkAgAVywXk0rN0l_3xPN-lmR_A_7tX30PqDCvh6tXs8zFP6M_q0MJFgwCuvGbCxlM2HJC01D3aVnVyV_h4A3_hMyXAy87B1CxLE1JGsGf6t9iq1OXKt7gIRFfrv4Q_O2pxi8DDV3lrcNhqcnlwkifMIA37S8A79sXlik0eIHRsDgOYgS9fiyRYUMBu6-oXrtrUIrSZBYq1hpg9aC3NO")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Farmer profile avatar",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(2.dp, primaryGreen, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(modifier = Modifier.size(6.dp).background(primaryGreen, CircleShape))
                        Text(
                            text = (farms.find { it.id == selectedFarmId }?.location ?: "Nashik, MH").uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            letterSpacing = 1.2.sp
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "नमस्ते, Rajesh! 👋",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Switch Farm Profiles",
                            tint = primaryGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Farm Profiles Selector Dropdown
                DropdownMenu(
                    expanded = showFarmSwitcherDropdown,
                    onDismissRequest = { showFarmSwitcherDropdown = false }
                ) {
                    farms.forEach { farm ->
                        DropdownMenuItem(
                            text = { Text(text = "${farm.name} (${farm.cropType})") },
                            onClick = {
                                viewModel.selectFarm(farm.id)
                                showFarmSwitcherDropdown = false
                            }
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Notification Bell icon with badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color(0xFFF1F5F9), CircleShape)
                        .clickable { /* trigger notification preview */ }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔔", fontSize = 16.sp)
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color.Red, CircleShape)
                            .align(Alignment.TopEnd)
                            .border(1.dp, Color.White, CircleShape)
                    )
                }

                // Language Selector Button
                Box {
                    Button(
                        onClick = { showLanguageDropdown = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = primaryGreen
                        ),
                        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(40.dp)
                            .testTag("language_selector_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Translate Language",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when (selectedLanguage) {
                                "English" -> "EN"
                                "Hindi" -> "HI"
                                "Kannada" -> "KN"
                                "Telugu" -> "TE"
                                "Tamil" -> "TA"
                                else -> selectedLanguage.take(2).uppercase()
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Language selection dropdown
                    DropdownMenu(
                        expanded = showLanguageDropdown,
                        onDismissRequest = { showLanguageDropdown = false }
                    ) {
                        languages.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(text = lang) },
                                onClick = {
                                    viewModel.changeLanguage(lang)
                                    showLanguageDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // --- Content Canvas ---
        Column(modifier = Modifier.padding(16.dp)) {

            // --- Fungal Disease Alert Warning Card ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE)
                ),
                border = BorderStroke(1.dp, Color(0xFFFFCDD2))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFFFCDD2), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Disease outbreak warning",
                            tint = Color(0xFFD32F2F)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Outbreak Warning",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828)
                        )
                        Text(
                            text = "Wheat Leaf Rust spores reported nearby. Monitor Block A closely.",
                            fontSize = 12.sp,
                            color = Color(0xFF5D4037)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Scan Crop",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen,
                        modifier = Modifier
                            .clickable { viewModel.navigateTo(Tab.CAMERA) }
                            .padding(8.dp)
                    )
                }
            }

            // --- Weather & Recommendation Glass Card ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.7f)
                ),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Weather Today",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = weather.temperature,
                                    fontSize = 38.sp,
                                    fontWeight = FontWeight.Light,
                                    color = Color(0xFF1E293B)
                                )
                                Text(
                                    text = " / 22°C",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.LightGray,
                                    modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "🌤️",
                                fontSize = 32.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFF8E1))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = weather.condition.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF9A825)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f))
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💡", fontSize = 18.sp)
                        }

                        Column {
                            Text(
                                text = "AI RECOMMENDATION",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryGreen
                            )
                            Text(
                                text = weather.irrigationAdvice,
                                fontSize = 13.sp,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }
            }

            // --- Action Grid ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Disease Detection (Scan Crop) card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(140.dp)
                        .clickable { viewModel.navigateTo(Tab.CAMERA) },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = primaryGreen)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📸", fontSize = 18.sp)
                        }

                        Column {
                            Text(
                                text = "SCAN CROP",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.8f),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Disease\nDetection",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // Market Prices (Mandi Live Prices link)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(140.dp)
                        .clickable { viewModel.navigateTo(Tab.MARKET) },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFFFF8E1), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📈", fontSize = 18.sp)
                        }

                        Column {
                            Text(
                                text = "MARKET LIVE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Wheat\n₹2,450/qtl",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            // --- AI Assistant Banner ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .background(Color(0xFF1A1A1A), RoundedCornerShape(24.dp))
                    .clickable { viewModel.navigateTo(Tab.CHAT) }
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ask KrishiMitra AI",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Instant answers to your farming doubts in 12 languages.",
                            fontSize = 11.sp,
                            color = Color.LightGray.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(Color(0xFFFFC107), CircleShape)
                            .shadow(4.dp, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎙️", fontSize = 20.sp)
                    }
                }
            }

            // --- Soil Health Progress Widget ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Circular progress representation
                    Box(
                        modifier = Modifier.size(54.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Drawing custom progress arc using Canvas
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Background track
                            drawArc(
                                color = Color(0xFFF1F5F9),
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 4.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                            )
                            // Foreground progress (82%)
                            drawArc(
                                color = primaryGreen,
                                startAngle = -90f,
                                sweepAngle = 360f * 0.82f,
                                useCenter = false,
                                style = Stroke(width = 4.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                            )
                        }
                        Text(
                            text = "82%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Soil Health: Optimal",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = "NPK balance is perfect for your Wheat crop.",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Text(
                        text = "Details ›",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen,
                        modifier = Modifier
                            .clickable { viewModel.navigateTo(Tab.PROFILE) }
                            .padding(8.dp)
                    )
                }
            }

            // --- Crop Health Segment ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Crop Health Index",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* Simulate map launch */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "View Field Map",
                        tint = primaryGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "View Field Map",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Horizontal Scroll list of crops
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                farms.forEach { farm ->
                    Card(
                        modifier = Modifier
                            .width(260.dp)
                            .shadow(2.dp, RoundedCornerShape(20.dp))
                            .clickable { viewModel.selectFarm(farm.id) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedFarmId == farm.id) Color(0xFFE8F5E9) else Color.White
                        ),
                        border = BorderStroke(
                            width = if (selectedFarmId == farm.id) 2.dp else 1.dp,
                            color = if (selectedFarmId == farm.id) primaryGreen else Color.LightGray.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Text(
                                        text = farm.cropType,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B5E20)
                                    )
                                    Text(
                                        text = "${farm.name} • ${farm.sizeAcres} Acres",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }

                                // Status Indicator
                                val statusColor = if (farm.cropType.lowercase() == "cotton") Color(0xFFD84315) else primaryGreen
                                val statusBg = if (farm.cropType.lowercase() == "cotton") Color(0xFFFFCCBC) else Color(0xFFC8E6C9)
                                val statusText = if (farm.cropType.lowercase() == "cotton") "ACTION" else "STABLE"

                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(statusBg)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = statusText,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = statusColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Crop visual image (Coil hotlinked from prompt)
                            val cropImgUrl = if (farm.cropType.lowercase() == "cotton") {
                                "https://lh3.googleusercontent.com/aida-public/AB6AXuB89OHA_4eXxnZ-7d4JD_4QrpWhFGWZbivnSMboV0ZyAtaaZCvs6X0hQhPJPFjdtJib62awNX69iq6IYmRbig4N6FlrQMA1Vu6MpdNTsu5Z9a_ix8tCNp44AKlyR8f-F3sRnrY6vT7dT2qsCELemrQu9g8WhgDhcXH9n5HSPewzuQCT0swKruukpdHp9LwiCIm6ydHv16GqU4DscuPHTswWNESpkl34SSS8tibe-PacoZMnsLcLdJTx"
                            } else {
                                "https://lh3.googleusercontent.com/aida-public/AB6AXuCDX1W51lASKASB7P1xCzDGskpp1ECvK6nUvS5URYLC0lJH2Ax6Tr26ay-CpgQq21WUqvAu6RLER4mPz9vZmSXiLNTeVFpuG1SNcaASsmLoO87sTs7F6oj0VvgNNHufilZvPYvN-1bua-ZViZJB-KN4uploXO8VboqJqw4SUv_cumCks6W3nKqdIr-xm8O150DpSYFY-pmm3H7PtOWxl5kFp7yw8mfgcPAnZeogkLdtxkS0ze5wbqqu"
                            }

                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(cropImgUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "${farm.cropType} growth monitoring",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Status Details
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (farm.cropType.lowercase() == "cotton") Icons.Default.WaterDrop else Icons.Default.Verified,
                                    contentDescription = "Status detail icon",
                                    tint = if (farm.cropType.lowercase() == "cotton") Color(0xFFD84315) else primaryGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (farm.cropType.lowercase() == "cotton") "Low Soil Moisture detected" else "Optimal Nitrogen Levels",
                                    fontSize = 12.sp,
                                    color = Color(0xFF33691E),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Add Crop Button
                Card(
                    modifier = Modifier
                        .size(width = 160.dp, height = 210.dp)
                        .clickable { showAddCropDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, primaryGreen)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add crop profile",
                            tint = primaryGreen,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Add Farm Crop",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- AI Recommendations Section (Sparkle Border Card) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.5.dp, accentGold.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFFFF8E1), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI Sparkle",
                                tint = accentGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Mitra AI Recommendations",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Personalised recommendations
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Text(text = "•", color = primaryGreen, fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
                            Text(
                                text = "Irrigate Wheat Block A tonight to capitalize on tomorrow's low evaporation rates and optimize hydration.",
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                        }
                        Row(verticalAlignment = Alignment.Top) {
                            Text(text = "•", color = primaryGreen, fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
                            Text(
                                text = "Apply organic mulch or neem spray to Cotton rows to protect against rising ground temperatures & prevent pests.",
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.navigateTo(Tab.CHAT) },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "ASK MITRA FOR DETAILED INSIGHTS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Market Live Mandi Prices ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Market Mandi Prices",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                        Text(
                            text = "Live Mandi",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Commodity rows
                    CommodityRow("Wheat", "Grade A", "₹2,450 / q", "+2.4%", true)
                    CommodityRow("Cotton", "Long Staple", "₹6,800 / q", "-0.8%", false)
                    CommodityRow("Soybean", "Yellow", "₹4,200 / q", "0.0%", null)
                }
            }
        }
    }

    // --- Add Crop Dialog ---
    if (showAddCropDialog) {
        var cropName by remember { mutableStateOf("") }
        var cropType by remember { mutableStateOf("") }
        var cropSize by remember { mutableStateOf("") }
        var farmLocation by remember { mutableStateOf("") }
        var sowingDate by remember { mutableStateOf("") }
        var soilType by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddCropDialog = false },
            title = { Text(text = "Add Farm Crop Profile") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = cropName,
                        onValueChange = { cropName = it },
                        label = { Text("Farm Block Name (e.g. Block D)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cropType,
                        onValueChange = { cropType = it },
                        label = { Text("Crop Name (e.g. Rice)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cropSize,
                        onValueChange = { cropSize = it },
                        label = { Text("Size (Acres)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = farmLocation,
                        onValueChange = { farmLocation = it },
                        label = { Text("Location (e.g. Nagpur, MH)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = sowingDate,
                        onValueChange = { sowingDate = it },
                        label = { Text("Sowing Date (e.g. Jun 10)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = soilType,
                        onValueChange = { soilType = it },
                        label = { Text("Soil Type (e.g. Clayey Soil)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val sizeVal = cropSize.toDoubleOrNull() ?: 1.0
                        viewModel.addFarm(
                            name = cropName.ifEmpty { "Block D" },
                            location = farmLocation.ifEmpty { "Nagpur, MH" },
                            crop = cropType.ifEmpty { "Rice" },
                            sowingDate = sowingDate.ifEmpty { "Jun 15" },
                            soilType = soilType.ifEmpty { "Loamy Soil" },
                            size = sizeVal
                        )
                        showAddCropDialog = false
                    }
                ) {
                    Text("Add Crop")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCropDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CommodityRow(name: String, grade: String, price: String, trend: String, upTrend: Boolean?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFF1F8E9).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
            Text(text = grade, fontSize = 11.sp, color = Color.Gray)
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(text = price, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (upTrend != null) {
                    Icon(
                        imageVector = if (upTrend) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                        contentDescription = "Trend direction",
                        tint = if (upTrend) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
                Text(
                    text = trend,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (upTrend) {
                        true -> Color(0xFF2E7D32)
                        false -> Color(0xFFD32F2F)
                        null -> Color.Gray
                    }
                )
            }
        }
    }
}
