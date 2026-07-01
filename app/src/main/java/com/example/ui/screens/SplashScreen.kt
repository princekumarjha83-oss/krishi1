package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onLoadingComplete: () -> Unit) {
    // Languages cycling
    val greetings = listOf(
        "Welcome Farmers",
        "किसानों का स्वागत है", // Hindi
        "ರೈತರಿಗೆ ಸ್ವಾಗತ",       // Kannada
        "రైతులకు స్వాగతం",      // Telugu
        "உழவர்களை வரவேற்கிறோம்", // Tamil
        "কৃষকদের স্বাগত",       // Bengali
        "शेतकऱ्यांचे स्वागत",   // Marathi
        "ખેડૂતોનું સ્વાગત છે",  // Gujarati
        "ਕਿਸਾਨਾਂ ਦਾ ਸੁਆਗਤ ਹੈ",  // Punjabi
        "കർഷകർക്ക് സ്വാഗതം",    // Malayalam
        "କୃଷକମାନଙ୍କୁ ସ୍ୱାଗତ"    // Odia
    )

    var currentGreetingIndex by remember { mutableIntStateOf(0) }
    var greetingText by remember { mutableIntStateOf(0) } // For smooth triggers
    var progress by remember { mutableFloatStateOf(0f) }

    // Cycle languages
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            currentGreetingIndex = (currentGreetingIndex + 1) % greetings.size
        }
    }

    // Progress bar loader
    LaunchedEffect(Unit) {
        val duration = 4000
        val steps = 100
        val stepDelay = (duration / steps).toLong()
        for (i in 0..steps) {
            progress = i / 100f
            delay(stepDelay)
        }
        onLoadingComplete()
    }

    // Dynamic color rotations for our mesh background simulation
    val infiniteTransition = rememberInfiniteTransition(label = "BackgroundRotation")
    val rotAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("splash_screen_container")
    ) {
        // --- Animated Dynamic Green Mesh Background ---
        Canvas(modifier = Modifier.fillMaxSize().rotate(rotAngle)) {
            val width = size.width
            val height = size.height
            
            // Draw a subtle multi-colored agricultural organic mesh
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF81C784).copy(alpha = 0.4f), Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(width * 0.2f, height * 0.3f),
                    radius = width * 0.8f
                )
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF2E7D32).copy(alpha = 0.3f), Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(width * 0.8f, height * 0.7f),
                    radius = width * 0.9f
                )
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFA000).copy(alpha = 0.15f), Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.5f),
                    radius = width * 0.7f
                )
            )
        }

        // Soft white ambient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.15f))
        )

        // --- Center Glassmorphic Card ---
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
                .fillMaxWidth(0.88f)
                .shadow(16.dp, RoundedCornerShape(28.dp), clip = false)
                .background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(28.dp))
                .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(28.dp))
                .padding(vertical = 36.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Leaf Logo Icon Container
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .shadow(8.dp, RoundedCornerShape(24.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF2E7D32), Color(0xFF81C784))
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = "App Logo Eco Leaf",
                        tint = Color.White,
                        modifier = Modifier.size(54.dp)
                    )

                    // Sparkle / AI Accent Badge
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 6.dp, y = (-6).dp)
                            .background(Color(0xFFFFA000), shape = CircleShape)
                            .border(2.dp, Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Sparkle",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Brand Heading
                Text(
                    text = "Krishimitra AI",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "PRECISION AGRICULTURE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF558B2F),
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Cycling regional language greets
                    Text(
                        text = greetings[currentGreetingIndex],
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Modern visual micro-indicator progress bar
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(6.dp)
                        .clip(CircleShape),
                    color = Color(0xFF2E7D32),
                    trackColor = Color(0xFFC8E6C9)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Empowering fields with intelligence...",
                    fontSize = 12.sp,
                    color = Color(0xFF558B2F).copy(alpha = 0.8f)
                )
            }
        }

        // Bottom attribution text
        Text(
            text = "© 2026 Krishimitra Technologies",
            fontSize = 12.sp,
            color = Color(0xFF1B5E20).copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}
