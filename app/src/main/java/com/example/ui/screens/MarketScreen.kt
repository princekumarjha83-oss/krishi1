package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.KrishimitraViewModel

@Composable
fun MarketScreen(viewModel: KrishimitraViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var showSellCropDialog by remember { mutableStateOf(false) }

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
            .padding(bottom = 96.dp)
    ) {
        // --- Header Search Bar panel ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = "Market Ecosystem icon",
                tint = primaryGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Krishimitra Market",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = primaryGreen,
                modifier = Modifier.weight(1f)
            )

            // Sell Crop Action Button
            Button(
                onClick = { showSellCropDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = primaryGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Sell,
                    contentDescription = "Sell crops button",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("SELL CROP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // --- LSTM AI Price Predictor card ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .shadow(4.dp, RoundedCornerShape(20.dp)),
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
                                contentDescription = "AI intelligence forecast",
                                tint = accentGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "AI Market Price Forecasting",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "LSTM Neural models forecast a steady **+12% spike** in Wheat mandi rates over the next 4 weeks due to export demands. Postpone immediate sales if storage allows.",
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated coordinate prediction graph built with Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(Color(0xFFE8F5E9).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFFC8E6C9), RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw baseline grid lines
                            drawLine(
                                color = Color.LightGray.copy(alpha = 0.5f),
                                start = androidx.compose.ui.geometry.Offset(0f, h * 0.7f),
                                end = androidx.compose.ui.geometry.Offset(w, h * 0.7f)
                            )
                            drawLine(
                                color = Color.LightGray.copy(alpha = 0.5f),
                                start = androidx.compose.ui.geometry.Offset(0f, h * 0.3f),
                                end = androidx.compose.ui.geometry.Offset(w, h * 0.3f)
                            )

                            // Forecast Trend Line (glowing green rise)
                            val path = androidx.compose.ui.graphics.Path().apply {
                                moveTo(0f, h * 0.8f)
                                cubicTo(w * 0.3f, h * 0.75f, w * 0.6f, h * 0.4f, w, h * 0.2f)
                            }

                            drawPath(
                                path = path,
                                color = primaryGreen,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
                            )

                            // Glow area
                            val glowPath = androidx.compose.ui.graphics.Path().apply {
                                moveTo(0f, h * 0.8f)
                                cubicTo(w * 0.3f, h * 0.75f, w * 0.6f, h * 0.4f, w, h * 0.2f)
                                lineTo(w, h)
                                lineTo(0f, h)
                                close()
                            }
                            drawPath(
                                path = glowPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(primaryGreen.copy(alpha = 0.15f), Color.Transparent)
                                )
                            )

                            // Point highlights
                            drawCircle(
                                color = accentGold,
                                radius = 6f,
                                center = androidx.compose.ui.geometry.Offset(w * 0.6f, h * 0.45f)
                            )
                            drawCircle(
                                color = primaryGreen,
                                radius = 8f,
                                center = androidx.compose.ui.geometry.Offset(w, h * 0.2f)
                            )
                        }

                        // Labels
                        Text(
                            text = "Now: ₹2,450",
                            fontSize = 9.sp,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.BottomStart)
                        )
                        Text(
                            text = "Forecast: ₹2,740 (Aug)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryGreen,
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }
                }
            }

            // --- Mandi Live Rates ---
            Text(
                text = "Live Mandi Prices",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .shadow(1.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    MandiItem("Wheat (Kanak)", "Grade A", "₹2,450 / q", "+2.4%", true)
                    MandiItem("Cotton (Kapas)", "Long Staple", "₹6,800 / q", "-0.8%", false)
                    MandiItem("Soybean", "Yellow", "₹4,200 / q", "0.0%", null)
                    MandiItem("Paddy (Basmati)", "Grade A+", "₹3,900 / q", "+1.8%", true)
                }
            }

            // --- Buy Agro Goods Marketplace ---
            Text(
                text = "Buy Seeds & Organic Fertilizers",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MarketProductItem("Certified Wheat HD-3226", "Seeds • 40Kg Bag", "₹1,850", "98% germination rate")
                MarketProductItem("Organic Vermicompost", "Nutrients • 50Kg Bag", "₹650", "Enriched with Trichoderma")
                MarketProductItem("Knapsack Power Sprayer", "Equipment • 16L", "₹3,200", "2-Stroke heavy engine")
            }

            // --- Rent Heavy Equipments (Rental Economy) ---
            Text(
                text = "Equipment Rental Center",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RentalProductItem("Mahindra Jivo 245 DI", "Tractor • 24HP", "₹650 / hr", "Rotavator attached")
                RentalProductItem("John Deere Combiner", "Harvester", "₹1,800 / hr", "Operator included")
                RentalProductItem("Heavy Disk Harrow", "Tillage equipment", "₹300 / hr", "Tractor tow hook")
            }
        }
    }

    // --- Sell Crop Dialog ---
    if (showSellCropDialog) {
        var cropName by remember { mutableStateOf("") }
        var quantityQuintals by remember { mutableStateOf("") }
        var expectedPrice by remember { mutableStateOf("") }
        var cellNumber by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showSellCropDialog = false },
            title = { Text(text = "Post Crop Harvest for Sale") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Your harvest offer will be shared with verified wholesale buyers in Maharashtra & MP.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    OutlinedTextField(
                        value = cropName,
                        onValueChange = { cropName = it },
                        label = { Text("Crop Name (e.g. Basmati Rice)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = quantityQuintals,
                        onValueChange = { quantityQuintals = it },
                        label = { Text("Quantity (Quintals)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = expectedPrice,
                        onValueChange = { expectedPrice = it },
                        label = { Text("Expected Price (per Quintal)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cellNumber,
                        onValueChange = { cellNumber = it },
                        label = { Text("Contact Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Action complete
                        showSellCropDialog = false
                    }
                ) {
                    Text("Submit Post")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSellCropDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun MandiItem(name: String, variety: String, price: String, trend: String, upTrend: Boolean?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
            Text(text = variety, fontSize = 11.sp, color = Color.Gray)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = price, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF33691E))
            Spacer(modifier = Modifier.width(10.dp))

            val chipColor = when (upTrend) {
                true -> Color(0xFFE8F5E9)
                false -> Color(0xFFFFEBEE)
                null -> Color(0xFFF5F5F5)
            }
            val textColor = when (upTrend) {
                true -> Color(0xFF2E7D32)
                false -> Color(0xFFC62828)
                null -> Color.Gray
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(chipColor)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = trend,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun MarketProductItem(title: String, desc: String, price: String, badge: String) {
    Card(
        modifier = Modifier
            .width(190.dp)
            .shadow(1.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(width = 166.dp, height = 90.dp)
                    .background(Color(0xFFE8F5E9), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Product bag placeholder",
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = desc, fontSize = 10.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = badge,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFFA000)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = price, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                Button(
                    onClick = { /* Buy item */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("BUY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun RentalProductItem(title: String, desc: String, price: String, badge: String) {
    Card(
        modifier = Modifier
            .width(190.dp)
            .shadow(1.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(width = 166.dp, height = 90.dp)
                    .background(Color(0xFFFFF3E0), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Machinery placeholder",
                    tint = Color(0xFFE65100),
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = desc, fontSize = 10.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = badge,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = price, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                Button(
                    onClick = { /* Rent item */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Text("RENT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
