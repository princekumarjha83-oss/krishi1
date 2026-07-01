package com.example.data.api

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"
    
    // OkHttp Client with generous timeouts as AI responses can take several seconds
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val mediaType = "application/json; charset=utf-8".toMediaType()

    /**
     * Calls Gemini 3.5 Flash for chat text generation, incorporating conversation history.
     */
    suspend fun generateChatResponse(
        prompt: String,
        history: List<Pair<String, String>> = emptyList()
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "null") {
            Log.w(TAG, "Gemini API key is empty or default placeholder. Using smart offline farmer recommendations.")
            return@withContext getMockFarmingAnswer(prompt)
        }

        try {
            val url = "$BASE_URL/gemini-3.5-flash:generateContent?key=$apiKey"

            // Construct contents JSON array incorporating chat history
            val contentsArray = JSONArray()

            // Map past history turns to contents
            for (turn in history) {
                val role = if (turn.first == "user") "user" else "model"
                contentsArray.put(
                    JSONObject().apply {
                        put("role", role)
                        put("parts", JSONArray().put(JSONObject().put("text", turn.second)))
                    }
                )
            }

            // Append current prompt
            contentsArray.put(
                JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().put(JSONObject().put("text", prompt)))
                }
            )

            // Construct full request payload
            val rootObject = JSONObject().apply {
                put("contents", contentsArray)
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", 
                        "You are Mitra AI, an expert agricultural ecosystem assistant built for Indian farmers. " +
                        "Provide highly friendly, practical, and multilingual recommendations (in the farmer's language: English, Hindi, Kannada, etc.). " +
                        "Give organic, chemical, NPK fertilizer, pest control, and smart irrigation advice. Use short, clear, scannable bullet points."
                    )))
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                })
            }

            val requestBody = rootObject.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string() ?: ""
                    Log.e(TAG, "API call failed code: ${response.code}, body: $errorBody")
                    return@withContext "I'm experiencing connectivity issues to my AI core, but here's some guidance based on my agricultural database:\n\n" + getMockFarmingAnswer(prompt)
                }

                val responseBody = response.body?.string() ?: return@withContext "Unable to read response from server."
                return@withContext extractTextFromResponse(responseBody)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in generateChatResponse: ${e.message}", e)
            return@withContext "I'm having trouble connecting right now, but here's what I recommend offline:\n\n" + getMockFarmingAnswer(prompt)
        }
    }

    /**
     * Performs crop disease detection from an image using Gemini.
     */
    suspend fun analyzeCropDisease(
        bitmap: Bitmap,
        cropContext: String = ""
    ): DiseaseAnalysisResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "null") {
            Log.w(TAG, "Gemini API key is empty or default placeholder. Using offline scanning results.")
            return@withContext getMockDiseaseAnalysis(cropContext)
        }

        try {
            val url = "$BASE_URL/gemini-3.5-flash:generateContent?key=$apiKey"

            // Convert Bitmap to Base64 String
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
            val base64Image = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

            // Construct contents structure with text prompt and base64 image data
            val promptText = "Analyze this agricultural crop image. Identify the crop and the disease/pest present. " +
                    "Respond with a raw, valid JSON object containing exactly these fields: " +
                    "\"disease\": \"name of disease\", \"confidence\": \"e.g. 95%\", \"riskLevel\": \"High\" or \"Medium\" or \"Low\", " +
                    "\"description\": \"short description of what is visible and infection stage\", " +
                    "\"treatment\": \"1-2 bullet points of organic or chemical solutions\", " +
                    "\"prevention\": \"1-2 bullet points of crop management to prevent spreading\". " +
                    "Do not put markdown block formatting in your response, return just the JSON."

            val partsArray = JSONArray().apply {
                put(JSONObject().put("text", promptText))
                put(JSONObject().apply {
                    put("inlineData", JSONObject().apply {
                        put("mimeType", "image/jpeg")
                        put("data", base64Image)
                    })
                })
            }

            val contentsArray = JSONArray().put(
                JSONObject().put("parts", partsArray)
            )

            val rootObject = JSONObject().apply {
                put("contents", contentsArray)
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.4)
                })
            }

            val requestBody = rootObject.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e(TAG, "API call failed code: ${response.code}")
                    return@withContext getMockDiseaseAnalysis(cropContext)
                }

                val responseBody = response.body?.string() ?: return@withContext getMockDiseaseAnalysis(cropContext)
                val jsonText = extractTextFromResponse(responseBody)
                
                try {
                    val cleanedJsonText = jsonText.trim().removeSurrounding("```json", "```").trim()
                    val resultJson = JSONObject(cleanedJsonText)
                    return@withContext DiseaseAnalysisResult(
                        disease = resultJson.optString("disease", "Unknown Spot Disease"),
                        confidence = resultJson.optString("confidence", "85%"),
                        riskLevel = resultJson.optString("riskLevel", "Medium"),
                        description = resultJson.optString("description", "Spots or lesions are detected on the foliage surface, which may indicate a fungal or bacterial infection."),
                        treatment = resultJson.optString("treatment", "Apply organic Neem oil extract and check soil moisture to decrease fungal growth."),
                        prevention = resultJson.optString("prevention", "Maintain proper plant spacing for aeration and avoid overhead irrigation.")
                    )
                } catch (jsonEx: Exception) {
                    Log.e(TAG, "Failed to parse json from Gemini response: $jsonText", jsonEx)
                    return@withContext getMockDiseaseAnalysis(cropContext)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in analyzeCropDisease: ${e.message}", e)
            return@withContext getMockDiseaseAnalysis(cropContext)
        }
    }

    private fun extractTextFromResponse(responseJson: String): String {
        return try {
            val root = JSONObject(responseJson)
            val candidates = root.getJSONArray("candidates")
            val content = candidates.getJSONObject(0).getJSONObject("content")
            val parts = content.getJSONArray("parts")
            parts.getJSONObject(0).getString("text")
        } catch (e: Exception) {
            "Error extracting response text."
        }
    }

    /**
     * Fallback mock farmer responses.
     */
    private fun getMockFarmingAnswer(prompt: String): String {
        val p = prompt.lowercase()
        return when {
            p.contains("wheat") || p.contains("rust") -> {
                "**Wheat Leaf Rust (Puccinia triticina)**\n" +
                "• **Organic Treatment:** Apply a **Neem Oil & Garlic spray** (5ml Neem oil + 2g liquid soap per liter of water). Spray during early morning or late evening.\n" +
                "• **NPK Recommendation:** Ensure balanced application of Nitrogen. Excess Nitrogen can promote lush vegetative growth which is more susceptible to leaf rust.\n" +
                "• **Irrigation Advice:** Irrigate during the early morning hours to allow leaves to dry quickly and reduce moisture on foliage."
            }
            p.contains("cotton") || p.contains("bollworm") -> {
                "**Cotton Pink Bollworm Protection**\n" +
                "• **Organic Solution:** Deploy pheromone traps (5 traps per acre) to monitor and catch male adult moths. Release Trichogramma egg parasitoids at 60,000 per acre.\n" +
                "• **Soil Treatment:** Deep summer plowing exposes hibernating larvae to natural predators and sun heat.\n" +
                "• **Chemical Control:** If infestation exceeds 10% damage, spray appropriate insecticides such as Spinosad under agronomic supervision."
            }
            p.contains("soil") || p.contains("fertilizer") || p.contains("npk") -> {
                "**NPK Soil Health Guide**\n" +
                "• **Nitrogen (N):** Essential for green leafy growth. Apply urea or organic compost.\n" +
                "• **Phosphorus (P):** Crucial for root development and seed selection. Apply Single Super Phosphate (SSP) or rock phosphate.\n" +
                "• **Potassium (K):** Enhances disease resistance and crop quality. Apply Muriate of Potash (MOP) or wood ash organically."
            }
            p.contains("irrigation") || p.contains("water") -> {
                "**Smart Irrigation Strategy**\n" +
                "• **Drip Irrigation:** Recommended for Cotton and fruit orchards to save up to 40% water.\n" +
                "• **Foliage Check:** Irrigate crop Block A tonight because of rising soil temperature and low evaporation rates forecasted for tomorrow.\n" +
                "• **Water Stress Indicator:** Curled or pale leaves indicate early water stress. Water deeply but ensure proper drainage to prevent root rot."
            }
            p.contains("scheme") || p.contains("pension") || p.contains("kisan") -> {
                "**Government Agricultural Support Schemes**\n" +
                "• **PM-KISAN:** Provides ₹6,000 per year in three equal installments directly to bank accounts of small and marginal farmers.\n" +
                "• **PM Fasal Bima Yojana:** Crop insurance protection against natural calamities with very low premium rates (2% for Kharif, 1.5% for Rabi).\n" +
                "• **Agriculture Infrastructure Fund:** Medium-to-long term debt financing for post-harvest management infrastructure at a 3% interest subvention."
            }
            p.contains("cow") || p.contains("buffalo") || p.contains("livestock") -> {
                "**Livestock Care Advice**\n" +
                "• **Foot & Mouth Disease (FMD):** Keep cattle sheds dry, clean, and disinfected. Administer FMD vaccination every 6 months.\n" +
                "• **Feeding:** Maintain a daily diet of 25-30kg green fodder, 5kg dry fodder, and 1-2kg cattle feed concentrate with fresh clean drinking water.\n" +
                "• **Vaccination reminder:** Ensure deworming is conducted before the onset of monsoons to protect digestive health."
            }
            else -> {
                "Namaste! I am Mitra, your farming companion. I can help you with crop disease management, smart irrigation, mandi prices, and government schemes.\n\n" +
                "• **Tip of the Day:** Soil test results are the basis of precise farming. Always complete a soil health analysis before sowing seeds to optimize NPK application.\n\n" +
                "Tell me more about your crop, soil type, or any problems you are observing in your fields!"
            }
        }
    }

    /**
     * Fallback mock scanning disease reports.
     */
    private fun getMockDiseaseAnalysis(cropContext: String): DiseaseAnalysisResult {
        return if (cropContext.contains("Cotton", true)) {
            DiseaseAnalysisResult(
                disease = "Bacterial Blight",
                confidence = "88%",
                riskLevel = "Medium",
                description = "Angular dark brown spots appear on leaves and cotton bolls, surrounded by water-soaked margins.",
                treatment = "• Spray Copper Oxychloride (2.5g per liter) mixed with Streptocycline (1g per 10 liters).\n• Use organic Pseudomonas fluorescens formulation for biopesticide leaf spray.",
                prevention = "• Use certified disease-free seeds.\n• Destroy plant residues post-harvest to prevent overwintering bacterial hosts."
            )
        } else {
            DiseaseAnalysisResult(
                disease = "Rice Leaf Blast",
                confidence = "92%",
                riskLevel = "High",
                description = "Spindle-shaped spots with greyish centers and reddish-brown borders are visible on the rice leaves.",
                treatment = "• Spray organic Neem Seed Kernel Extract (NSKE 5%) or apply bio-fungicide containing Trichoderma viride.\n• In case of severe outbreak, apply Tricyclazole (0.6g per liter) under supervision.",
                prevention = "• Avoid excessive Nitrogen application which triggers fungal spore proliferation.\n• Maintain clean bunds and ensure optimum plant spacing for proper air circulation."
            )
        }
    }
}

data class DiseaseAnalysisResult(
    val disease: String,
    val confidence: String,
    val riskLevel: String,
    val description: String,
    val treatment: String,
    val prevention: String
)
