package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.KrishimitraViewModel
import com.example.ui.viewmodel.Tab

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: KrishimitraViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()

    if (currentTab == Tab.SPLASH) {
        SplashScreen(
            onLoadingComplete = {
                viewModel.navigateTo(Tab.HOME)
            }
        )
    } else {
        // Frosted primary colors
        val primaryGreen = Color(0xFF2E7D32)
        val containerGreen = Color(0xFFC8E6C9)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0), // Full Edge to Edge
            bottomBar = {
                // Glassmorphism Floating Bottom Navigation bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .shadow(16.dp, RoundedCornerShape(24.dp), clip = false)
                        .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(24.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    NavigationBar(
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .height(72.dp)
                            .testTag("bottom_navigation_bar")
                    ) {
                        // Home Nav Item
                        NavigationBarItem(
                            selected = currentTab == Tab.HOME,
                            onClick = { viewModel.navigateTo(Tab.HOME) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home Agriculture Dashboard"
                                )
                            },
                            label = {
                                Text(
                                    text = "Home",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.Gray,
                                selectedTextColor = primaryGreen,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = primaryGreen
                            ),
                            modifier = Modifier.testTag("nav_home_tab")
                        )

                        // AI Chat Nav Item
                        NavigationBarItem(
                            selected = currentTab == Tab.CHAT,
                            onClick = { viewModel.navigateTo(Tab.CHAT) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = "Mitra AI Chat assistant"
                                )
                            },
                            label = {
                                Text(
                                    text = "Mitra Chat",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.Gray,
                                selectedTextColor = primaryGreen,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = primaryGreen
                            ),
                            modifier = Modifier.testTag("nav_chat_tab")
                        )

                        // Camera Scan Nav Item
                        NavigationBarItem(
                            selected = currentTab == Tab.CAMERA,
                            onClick = { viewModel.navigateTo(Tab.CAMERA) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = "Pest Disease scanner"
                                )
                            },
                            label = {
                                Text(
                                    text = "Scan",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.Gray,
                                selectedTextColor = primaryGreen,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = primaryGreen
                            ),
                            modifier = Modifier.testTag("nav_scan_tab")
                        )

                        // Market Nav Item
                        NavigationBarItem(
                            selected = currentTab == Tab.MARKET,
                            onClick = { viewModel.navigateTo(Tab.MARKET) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = "Mandi price marketplace"
                                )
                            },
                            label = {
                                Text(
                                    text = "Market",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.Gray,
                                selectedTextColor = primaryGreen,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = primaryGreen
                            ),
                            modifier = Modifier.testTag("nav_market_tab")
                        )

                        // Profile Nav Item
                        NavigationBarItem(
                            selected = currentTab == Tab.PROFILE,
                            onClick = { viewModel.navigateTo(Tab.PROFILE) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Landscape,
                                    contentDescription = "Farms profiles task ledger"
                                )
                            },
                            label = {
                                Text(
                                    text = "Profile",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.Gray,
                                selectedTextColor = primaryGreen,
                                unselectedTextColor = Color.Gray,
                                indicatorColor = primaryGreen
                            ),
                            modifier = Modifier.testTag("nav_profile_tab")
                        )
                    }
                }
            }
        ) { paddingValues ->
            // Dynamic smooth screen crossfades
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
                    }
                ) { targetTab ->
                    when (targetTab) {
                        Tab.HOME -> HomeScreen(viewModel)
                        Tab.CHAT -> ChatScreen(viewModel)
                        Tab.CAMERA -> CameraScreen(viewModel)
                        Tab.MARKET -> MarketScreen(viewModel)
                        Tab.PROFILE -> ProfileScreen(viewModel)
                        else -> HomeScreen(viewModel)
                    }
                }
            }
        }
    }
}
