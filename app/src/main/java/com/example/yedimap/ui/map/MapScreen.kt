package com.example.yedimap.ui.map

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.yedimap.R

data class CampusBuilding(
    val name: String,
    val center: LatLng,
    val polygon: List<LatLng>,
    val fillColor: Color,
    val strokeColor: Color,
    val classes: List<String>
)


@Composable
fun MapScreen() {

    val context = LocalContext.current

    // Kampüs merkezi
    val yeditepe = LatLng(40.97201588044807, 29.152204246141045)

    // Kampüs sınırları (polygon)
    val campusBoundary = listOf(
        LatLng(40.9747870, 29.1535306),
        LatLng(40.9744711, 29.1540885),
        LatLng(40.9744630, 29.1540831),
        LatLng(40.9732560, 29.1532141),
        LatLng(40.9685493, 29.1559124),
        LatLng(40.9684948, 29.1557407),
        LatLng(40.9697645, 29.1541100),
        LatLng(40.9721137, 29.1502261),
        LatLng(40.9721483, 29.1498613),
        LatLng(40.9723103, 29.1493946),
        LatLng(40.9726019, 29.1495448),
        LatLng(40.9730474, 29.1501161),
        LatLng(40.9735578, 29.1508162),
        LatLng(40.9736064, 29.1505694),
        LatLng(40.9738008, 29.1506687),
        LatLng(40.9738737, 29.1503844),
        LatLng(40.9741390, 29.1504782),
        LatLng(40.9741106, 29.1506016),
        LatLng(40.9744448, 29.1506901),
        LatLng(40.9743820, 29.1510361),
        LatLng(40.9743334, 29.1511622),
        LatLng(40.9741208, 29.1516960),
        LatLng(40.9743354, 29.1518810),
        LatLng(40.9748154, 29.1520339),
        LatLng(40.9754229, 29.1522861),
        LatLng(40.9753216, 29.1526991),
        LatLng(40.9747911, 29.1535279)
    )

    // ==== ÖRNEK BİNALAR ====
    val buildings = listOf(
        CampusBuilding(
            name = "Engineering Faculty (örnek)",
            center = LatLng(40.97200, 29.15220),
            polygon = listOf(
                LatLng(40.97240, 29.15190),
                LatLng(40.97240, 29.15260),
                LatLng(40.97160, 29.15260),
                LatLng(40.97160, 29.15190),
            ),
            fillColor = Color(0x55FF9800),
            strokeColor = Color(0xFFFF9800),
            classes = listOf(
                "E101 - Algorithms",
                "E102 - Data Structures",
                "E201 - Operating Systems"
            )
        ),
        CampusBuilding(
            name = "Otopark",
            center = LatLng(40.9750, 29.1529),   // ortalama bir merkez
            polygon = listOf(
                LatLng(40.9753966, 29.1523048),
                LatLng(40.9752832, 29.1526884),
                LatLng(40.9749409, 29.1532275),
                LatLng(40.9747992, 29.1531122),
                LatLng(40.9744650, 29.1525891),
                LatLng(40.9748073, 29.1520527),
                LatLng(40.9754006, 29.1523021)
            ),
            fillColor = Color(0x55999999),      // gri, yarı şeffaf
            strokeColor = Color(0xFF757575),    // koyu gri çerçeve
            classes = listOf("Otopark Alanı")
        ),
        CampusBuilding(
            name = "Blue Building (örnek)",
            center = LatLng(40.97240, 29.15250),
            polygon = listOf(
                LatLng(40.97270, 29.15220),
                LatLng(40.97270, 29.15280),
                LatLng(40.97210, 29.15280),
                LatLng(40.97210, 29.15220),
            ),
            fillColor = Color(0x553F51B5),
            strokeColor = Color(0xFF3F51B5),
            classes = listOf("B101", "B102", "B201")
        ),
        CampusBuilding(
            name = "Green Building (örnek)",
            center = LatLng(40.97160, 29.15190),
            polygon = listOf(
                LatLng(40.97190, 29.15170),
                LatLng(40.97190, 29.15230),
                LatLng(40.97130, 29.15230),
                LatLng(40.97130, 29.15170),
            ),
            fillColor = Color(0x554CAF50),
            strokeColor = Color(0xFF4CAF50),
            classes = listOf("G001", "G002", "G003")
        )
    )

    var selectedBuilding by remember { mutableStateOf<CampusBuilding?>(null) }
    // Kamera
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(40.97205, 29.15250),
            16.5f
        )
    }

    // Harita stili + zoom limitleri
    val mapProperties = MapProperties(
        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
            context,
            R.raw.`yeditepe_map_style`
        ),
        minZoomPreference = 16.0f,
        maxZoomPreference = 18.5f
    )

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true
            ),
            onMapClick = {
                // Haritanın boş yerine tıklayınca kart kapansın
                selectedBuilding = null
            }
        ) {

            // 1) Şehir maskesi – kampüs dışı koyu yeşil, iç delik
            val outerBounds = listOf(
                LatLng(41.2, 28.8),
                LatLng(41.2, 29.4),
                LatLng(40.8, 29.4),
                LatLng(40.8, 28.8)
            )

            Polygon(
                points = outerBounds,
                holes = listOf(campusBoundary),
                fillColor = Color(0xCC1B3A28),
                strokeColor = Color.Transparent,
                strokeWidth = 0f
            )

            // 2) Kampüs sınırı (içi şeffaf – yollar gözüksün)
            Polygon(
                points = campusBoundary,
                fillColor = Color.Transparent,
                strokeColor = Color(0xFF4CAF50),
                strokeWidth = 3f
            )

            // 3) Binalar – tıklanabilir polygonlar
            buildings.forEach { building ->
                Polygon(
                    points = building.polygon,
                    fillColor = building.fillColor,
                    strokeColor = building.strokeColor,
                    strokeWidth = 2f,
                    clickable = true,
                    onClick = {
                        selectedBuilding = building
                    }
                )
            }

            // 4) Merkez pin
            Marker(
                state = MarkerState(position = yeditepe),
                title = "Yeditepe University",
                snippet = "Main Campus"
            )
        }

        // === ANİMASYONLU ALT KART ===
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = selectedBuilding != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            selectedBuilding?.let { building ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .padding(20.dp)
                ) {

                    Column {

                        // Başlık
                        Text(
                            text = building.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // ========= Sadece OTOPARK =========
                        if (building.name.lowercase() == "otopark") {

                            Button(
                                onClick = {
                                    openGoogleMapsDirections(
                                        context = context,
                                        destination = building.center,
                                        label = building.name
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )  {
                                Text("Yol Tarifi")
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Kapatmak için haritanın başka bir yerine dokunun.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )

                        } else {
                            // ========= Diğer binalar =========

                            Button(
                                onClick = {
                                    openGoogleMapsDirections(
                                        context = context,
                                        destination = building.center,
                                        label = building.name
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Yol Tarifi")
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Search bar
                            OutlinedTextField(
                                value = "",
                                onValueChange = { },
                                placeholder = { Text("Aramak istediğiniz sınıfı yazın") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Bu binadaki sınıflar:",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            building.classes.forEach { cls ->
                                Text(
                                    text = "- $cls",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Kapatmak için haritanın başka bir yerine dokunun.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

fun openGoogleMapsDirections(
    context: Context,
    destination: LatLng,
    label: String
) {
    // Yürüyüş rotası linki
    val uri = Uri.parse(
        "https://www.google.com/maps/dir/?api=1" +
                "&destination=${destination.latitude},${destination.longitude}" +
                "&travelmode=walking"
    )

    // Önce Google Maps app ile dene
    val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }

    try {
        context.startActivity(mapIntent)
    } catch (e: ActivityNotFoundException) {
        // Telefonda Google Maps app yoksa, tarayıcıda aç
        val browserIntent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(browserIntent)
    }
}