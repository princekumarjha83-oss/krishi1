package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.viewmodel.KrishimitraViewModel
import com.example.ui.viewmodel.ScanningState
import com.example.ui.viewmodel.Tab

@Composable
fun CameraScreen(viewModel: KrishimitraViewModel) {
    val context = LocalContext.current
    val scanningState by viewModel.scanningState.collectAsState()
    val scannedImage by viewModel.scannedImage.collectAsState()
    val activeFarmId by viewModel.selectedFarmId.collectAsState()
    val farms by viewModel.farms.collectAsState()

    var isFlashOn by remember { mutableStateOf(false) }

    // Resolve active farm crop for mock fallback
    val activeCrop = farms.find { it.id == activeFarmId }?.cropType ?: "Wheat"

    // Set up a real gallery uploader picker launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.isMutableRequired = true
                    }
                }
                
                // Scale bitmap for faster API upload
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 640, 480, true)
                viewModel.setScannedImageBitmap(scaledBitmap)
                
                // Trigger actual Gemini analysis
                viewModel.scanCropDisease(scaledBitmap, activeCrop)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val primaryGreen = Color(0xFF2E7D32)
    val secondaryGreen = Color(0xFF66BB6A)
    val accentGold = Color(0xFFFFA000)

    // Viewfinder laser scanner line animation
    val infiniteTransition = rememberInfiniteTransition(label = "ViewfinderLaser")
    val laserPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LaserOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(bottom = 96.dp)
    ) {
        // --- Viewfinder Camera Feed (Live Mock Rice Leaf or Selected Image) ---
        Box(modifier = Modifier.fillMaxSize()) {
            if (scannedImage != null) {
                // Show uploaded user image in frame
                Image(
                    bitmap = scannedImage!!.asImageBitmap(),
                    contentDescription = "Selected plant foliage image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Background rice leaf live analysis image via Coil
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("https://lh3.googleusercontent.com/aida-public/AB6AXuBqD2RaCuCkW3jA6uhSWU5GyAu9zpcc-pIBRzgP705nuxwTGSjvu3l0oQ4NF5ZCskZfPzJBRNQ8KMXozcY4QaHvegZEZh1uCpdOIfnj1pn9zMilGxiuC2MW3qO79gwkZIP5-jW9bsoaKr2bRMyJigDPNGdhSoeVNhbXjb07deksb-Wlh5way01dnSJwIi1UnNk6Ub-6-f6Sm7U5Y4tzdo7FBTgelfHvFXthtBTEXFcpKqfmCHEiSYJA")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Rice leaf viewport",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.85f
                )
            }

            // Darkening overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.15f))
            )
        }

        // --- Top Scanning Notification Overlay ---
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp)
                .shadow(6.dp, CircleShape)
                .background(Color.White.copy(alpha = 0.8f), CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            if (scanningState is ScanningState.Scanning) accentGold else primaryGreen,
                            CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when (scanningState) {
                        is ScanningState.Scanning -> "Analyzing crop with Gemini AI..."
                        is ScanningState.Success -> "Scan Complete"
                        is ScanningState.Error -> "Scanning Error"
                        else -> "Hold steady... scanning for pests"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryGreen
                )
            }
        }

        // --- Viewfinder Camera Frame Corners Overlay ---
        if (scanningState !is ScanningState.Success) {
            Box(
                modifier = Modifier
                    .size(width = 280.dp, height = 280.dp)
                    .align(Alignment.Center)
                    .border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            ) {
                // Render animated green scanning laser bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .offset(y = 280.dp * laserPosition)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color(0xFF81C784), Color.Transparent)
                            )
                        )
                )

                // Beautiful glowing AI point trackers
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(x = 60.dp, y = 50.dp)
                        .background(Color(0xFFFFA000), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(x = 200.dp, y = 180.dp)
                        .background(Color(0xFFFFA000), CircleShape)
                )
            }
        }

        // --- Camera Action Controls (At Bottom above Nav) ---
        if (scanningState is ScanningState.Idle) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Photo Gallery Upload
                IconButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(4.dp, CircleShape)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Upload from Photo Library",
                        tint = primaryGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Main capture trigger button (Simulates capture and forces scan)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .border(4.dp, Color.White, CircleShape)
                        .padding(4.dp)
                        .background(Color.White, CircleShape)
                        .clickable {
                            // Create dummy bitmap or use active screen trigger for simulation
                            val dummyBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                            viewModel.scanCropDisease(dummyBitmap, activeCrop)
                        }
                )

                // Flash toggle button
                IconButton(
                    onClick = { isFlashOn = !isFlashOn },
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(4.dp, CircleShape)
                        .background(
                            if (isFlashOn) accentGold.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.8f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = "Camera Flash Toggle",
                        tint = if (isFlashOn) accentGold else primaryGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // --- AI Analysis Results Card overlay ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            when (val state = scanningState) {
                is ScanningState.Scanning -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = primaryGreen)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Mitra AI is running YOLOv8 & Gemini neural vision diagnosis...",
                                fontSize = 13.sp,
                                color = primaryGreen,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                is ScanningState.Success -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(12.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(2.dp, accentGold)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color(0xFFFFEBEE), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = "Warning disease flag",
                                            tint = Color.Red,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Disease: ${state.result.disease}",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1B5E20)
                                        )
                                        Text(
                                            text = "${state.result.confidence} confidence score",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = primaryGreen
                                        )
                                    }
                                }

                                // Risk tag badge
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFCDD2))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = state.result.riskLevel,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFFC62828)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = state.result.description,
                                fontSize = 13.sp,
                                color = Color.DarkGray,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.setSpeaking(false)
                                        // Auto load diagnostic question in AI Chat screen
                                        viewModel.sendChatMessage("How do I treat ${state.result.disease} organically? Give me NPK details.")
                                        viewModel.navigateTo(Tab.CHAT)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Medication,
                                        contentDescription = "Medication view treatment",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "VIEW TREATMENT",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                // Reset scanning button
                                IconButton(
                                    onClick = { viewModel.resetScanning() },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .shadow(1.dp, RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF1F8E9), RoundedCornerShape(12.dp))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Scan New Foliage Crop",
                                        tint = primaryGreen
                                    )
                                }
                            }
                        }
                    }
                }

                is ScanningState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Scanning Failed", fontWeight = FontWeight.Bold, color = Color.Red)
                            Text(text = state.message, fontSize = 12.sp, color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { viewModel.resetScanning() },
                                colors = ButtonDefaults.buttonColors(containerColor = primaryGreen)
                            ) {
                                Text("Try Again")
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }
}
