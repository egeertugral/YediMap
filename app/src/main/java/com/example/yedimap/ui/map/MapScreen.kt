package com.example.yedimap.ui.map

import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.yedimap.R
import androidx.compose.ui.text.style.TextOverflow
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*

/* =========================
   1) TYPES + DATA MODEL
   ========================= */

enum class MapItemType {
    FACULTY,

    RING_STOP,
    SPORT_BASKET,
    SPORT_TENNIS,
    SPORT_FOOTBALL,
    SPORT_POOL,
    DORM_MALE,
    DORM_FEMALE,
    CAFE,
    CARPARK,
    SERA,
    PREP_CENTER,
    PREP_ANNEX,
    RECTORATE,
    SOCIAL,
    FUTURE_CENTER
}

data class MapItem(
    val id: String,
    val name: String,
    val type: MapItemType,
    val center: LatLng,
    val polygon: List<LatLng>? = null,   // polygon alan çizer
    val classes: List<String>? = null
)

/* =========================
   2) COLORS (YOUR SCHEME)
   ========================= */

object MapColors {

    // FACULTY -> turuncu tonları
    val facultyFill = Color(0x55FF9800)
    val facultyStroke = Color(0xFFFF9800)

    // RING_STOP -> siyah tonları
    val ringFill = Color(0xFF111111)

    // SPORT
    val basketFill = Color(0x553F51B5)
    val basketStroke = Color(0xFF3F51B5)

    val tennisFill = Color(0x552ECC71)
    val tennisStroke = Color(0xFF2ECC71)

    val footballFill = Color(0x554CAF50)
    val footballStroke = Color(0xFF4CAF50)

    // POOL
    val poolFill = Color(0x554FC3F7)
    val poolStroke = Color(0xFF0288D1)

    // DORM -> erkek açık mavi, kızlar pembe
    val dormMaleFill = Color(0x554FC3F7)
    val dormMaleStroke = Color(0xFF4FC3F7)

    val dormFemaleFill = Color(0x55F48FB1)
    val dormFemaleStroke = Color(0xFFF48FB1)

    // CAFE -> slightly lighter dark blue (Kubbe Cafe)
    val cafeFill = Color(0x55426BD6)
    val cafeStroke = Color(0xFF3559C7)

    // CARPARK -> gri
    val carparkFill = Color(0x55999999)
    val carparkStroke = Color(0xFF757575)

    // SERA -> yeşil
    val seraFill = Color(0x554CAF50)
    val seraStroke = Color(0xFF2E7D32)

    // PREP -> kırmızı
    val prepFill = Color(0x55E53935)
    val prepStroke = Color(0xFFE53935)

    // REKTÖRLÜK -> daha koyu kahverengi
    val rectorateFill = Color(0x556D4C41)
    val rectorateStroke = Color(0xFF4E342E)

    // SOSYAL TESİSLER -> mor
    val socialFill = Color(0x558E24AA)
    val socialStroke = Color(0xFF8E24AA)

    // UI accent purple (requested)
    val uiPurple = Color(0xFF614184)

    // Campus mask
    val outsideMask = Color(0xCC1B3A28)
    val campusBorder = Color(0xFF2E7D32)

    //
    val futureCenterFill = Color(0x556D5A4A)
    val futureCenterStroke = Color(0xFF5D4037)
}

/* =========================
   3) HELPERS
   ========================= */

private fun colorsFor(type: MapItemType): Pair<Color, Color> {
    return when (type) {
        MapItemType.FACULTY -> MapColors.facultyFill to MapColors.facultyStroke
        MapItemType.RING_STOP -> MapColors.ringFill to MapColors.ringFill

        MapItemType.SPORT_BASKET -> MapColors.basketFill to MapColors.basketStroke
        MapItemType.SPORT_TENNIS -> MapColors.tennisFill to MapColors.tennisStroke
        MapItemType.SPORT_FOOTBALL -> MapColors.footballFill to MapColors.footballStroke
        MapItemType.SPORT_POOL -> MapColors.poolFill to MapColors.poolStroke

        MapItemType.DORM_MALE -> MapColors.dormMaleFill to MapColors.dormMaleStroke
        MapItemType.DORM_FEMALE -> MapColors.dormFemaleFill to MapColors.dormFemaleStroke

        MapItemType.CAFE -> MapColors.cafeFill to MapColors.cafeStroke
        MapItemType.CARPARK -> MapColors.carparkFill to MapColors.carparkStroke

        MapItemType.SERA -> MapColors.seraFill to MapColors.seraStroke

        MapItemType.PREP_CENTER, MapItemType.PREP_ANNEX -> MapColors.prepFill to MapColors.prepStroke

        MapItemType.RECTORATE -> MapColors.rectorateFill to MapColors.rectorateStroke
        MapItemType.SOCIAL -> MapColors.socialFill to MapColors.socialStroke
        MapItemType.FUTURE_CENTER -> MapColors.futureCenterFill to MapColors.futureCenterStroke
    }
}

private fun isClassFaculty(item: MapItem): Boolean {

    return item.type == MapItemType.FACULTY && !item.classes.isNullOrEmpty()
}

private fun isNoClassesItem(item: MapItem): Boolean {
    // Otopark, ring durakları, kapı vs sınıfsız
    return item.type in setOf(
        MapItemType.CARPARK,
        MapItemType.RING_STOP,
        MapItemType.CAFE,
        MapItemType.DORM_MALE,
        MapItemType.DORM_FEMALE,
        MapItemType.SERA,
        MapItemType.PREP_CENTER,
        MapItemType.PREP_ANNEX,
        MapItemType.RECTORATE,
        MapItemType.SOCIAL,
        MapItemType.SPORT_BASKET,
        MapItemType.SPORT_TENNIS,
        MapItemType.SPORT_FOOTBALL,
        MapItemType.SPORT_POOL
    ) && item.classes.isNullOrEmpty()
}

private fun emojiFor(type: MapItemType): String {
    return when (type) {
        MapItemType.FACULTY -> "🏛️"
        MapItemType.PREP_CENTER, MapItemType.PREP_ANNEX -> "📘"
        MapItemType.RECTORATE -> "🏢"
        MapItemType.CAFE -> "☕"
        MapItemType.SERA -> "🌱"
        MapItemType.SOCIAL -> "🧩"
        MapItemType.DORM_FEMALE -> "👩‍🎓"
        MapItemType.DORM_MALE -> "👨‍🎓"
        MapItemType.CARPARK -> "🅿️"
        MapItemType.RING_STOP -> "🚏"
        MapItemType.SPORT_BASKET -> "🏀"
        MapItemType.SPORT_FOOTBALL -> "⚽"
        MapItemType.SPORT_TENNIS -> "🎾"
        MapItemType.SPORT_POOL -> "🏊"
        MapItemType.FUTURE_CENTER -> "🛖"
    }
}


// =========================
// Tooltip helpers (long-press polygon hit test)
// =========================
private fun pointInPolygon(point: LatLng, polygon: List<LatLng>): Boolean {
    // Simple ray-casting on lon/lat treated as X/Y (small area -> OK)
    val x = point.longitude
    val y = point.latitude

    var inside = false
    var j = polygon.size - 1
    for (i in polygon.indices) {
        val xi = polygon[i].longitude
        val yi = polygon[i].latitude
        val xj = polygon[j].longitude
        val yj = polygon[j].latitude

        val intersect = ((yi > y) != (yj > y)) &&
                (x < (xj - xi) * (y - yi) / ((yj - yi).takeIf { it != 0.0 } ?: 1e-12) + xi)
        if (intersect) inside = !inside
        j = i
    }
    return inside
}

private fun findPolygonItemAt(latLng: LatLng, items: List<MapItem>): MapItem? {
    // Prefer polygon items only (buildings/sports/dorms etc.)
    // We exclude point-only items (ring stops) by requiring polygon != null.
    return items.firstOrNull { it.polygon != null && pointInPolygon(latLng, it.polygon) }
}

@Composable
private fun LegendRow(
    emoji: String,
    label: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = emoji, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, RoundedCornerShape(999.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}

/* =========================
   4) SCREEN
   ========================= */

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()

    // Always start the map from the same camera position
    val initialCameraPosition = remember {
        CameraPosition.fromLatLngZoom(
            LatLng(40.97205, 29.15250),
            16.5f
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = initialCameraPosition
    }

    // Kampüs merkezi
    val campusCenter = LatLng(40.97201588044807, 29.152204246141045)

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

    // Restrict camera panning so the user cannot move outside campus
    val campusBounds = remember {
        val builder = LatLngBounds.Builder()
        campusBoundary.forEach { builder.include(it) }
        builder.build()
    }

    // ===== MAP ITEMS !!!! =====
    val mapItems = listOf(
        // FACULTY OF ENGINEERING & NATURAL SCIENCES (polygon)
        MapItem(
            id = "faculty_eng",
            name = "FACULTY OF ENGINEERING & NATURAL SCIENCES",
            type = MapItemType.FACULTY,
            // Ortalama bir merkez (polygonun içinden)
            center = LatLng(40.9724254, 29.1512482),
            polygon = listOf(
                LatLng(40.9728632, 29.1514117),
                LatLng(40.9725412, 29.1516477),
                LatLng(40.9724237, 29.1514868),
                LatLng(40.9723670, 29.1516128),
                LatLng(40.9722253, 29.1515350),
                LatLng(40.9720997, 29.1515511),
                LatLng(40.9720713, 29.1513526),
                LatLng(40.9720592, 29.1513339),
                LatLng(40.9720025, 29.1511139),
                LatLng(40.9721159, 29.1510496),
                LatLng(40.9721929, 29.1510442),
                LatLng(40.9722678, 29.1510603),
                LatLng(40.9723063, 29.1510710),
                LatLng(40.9723387, 29.1510817),
                LatLng(40.9723711, 29.1510952),
                LatLng(40.9723974, 29.1511112),
                LatLng(40.9724581, 29.1508484),
                LatLng(40.9726141, 29.1509798),
                LatLng(40.9726708, 29.1510281),
                LatLng(40.9727822, 29.1511542),
                LatLng(40.9728996, 29.1513580),
                LatLng(40.9728490, 29.1513714),
                LatLng(40.9728672, 29.1514090)
            ),
            // Classes will be added later.
            classes = null
        ),

        //  FACULTY OF MEDICINE (polygon)
        MapItem(
            id = "faculty_med",
            name = "FACULTY OF MEDICINE",
            type = MapItemType.FACULTY,
            // Ortalama bir merkez (polygonun içinden)
            center = LatLng(40.97275, 29.15171),
            polygon = listOf(
                LatLng(40.9729847, 29.1517657),
                LatLng(40.9728692, 29.1514117),
                LatLng(40.9725250, 29.1516745),
                LatLng(40.9726283, 29.1520017)
            ),
            // Sınıflar sadece 1 fakültede gösterilecek; bu fakülte sınıflı olmayacaksa null bırak.
            classes = null
        ),

        // FACULTY OF LAW / ECONOMICS & ADMINISTRATIVE (polygon)
        MapItem(
            id = "faculty_law",
            name = "FACULTY OF LAW / ECONOMICS & ADMINISTRATIVE",
            type = MapItemType.FACULTY,
            center = LatLng(40.97186, 29.15303), // yaklaşık merkez (istersen sonra ince ayar yaparız)
            polygon = listOf(
                LatLng(40.9721564, 29.1527769),
                LatLng(40.9721058, 29.1530988),
                LatLng(40.9720005, 29.1530076),
                LatLng(40.9719843, 29.1530424),
                LatLng(40.9719741, 29.1530290),
                LatLng(40.9719357, 29.1530988),
                LatLng(40.9719093, 29.1531551),
                LatLng(40.9718790, 29.1532114),
                LatLng(40.9718547, 29.1532516),
                LatLng(40.9718263, 29.1532838),
                LatLng(40.9718000, 29.1533107),
                LatLng(40.9718141, 29.1533348),
                LatLng(40.9718060, 29.1533482),
                LatLng(40.9718648, 29.1534340),
                LatLng(40.9717048, 29.1536513),
                LatLng(40.9715306, 29.1533267),
                LatLng(40.9716562, 29.1531765),
                LatLng(40.9717088, 29.1532356),
                LatLng(40.9717352, 29.1532034),
                LatLng(40.9717149, 29.1531765),
                LatLng(40.9718162, 29.1529727),
                LatLng(40.9718364, 29.1529781),
                LatLng(40.9718466, 29.1529378),
                LatLng(40.9717716, 29.1528815),
                LatLng(40.9718060, 29.1526937),
                LatLng(40.9721564, 29.1527796)
            ),
            classes = null
        ),

        // FACULTY OF FINE ARTS (polygon)
        MapItem(
            id = "faculty_fine_arts",
            name = "FACULTY OF FINE ARTS",
            type = MapItemType.FACULTY,
            center = LatLng(40.97063, 29.15393),
            polygon = listOf(
                LatLng(40.9710426, 29.1541797),
                LatLng(40.9704067, 29.1545606),
                LatLng(40.9703702, 29.1545042),
                LatLng(40.9702689, 29.1545069),
                LatLng(40.9702710, 29.1543674),
                LatLng(40.9702244, 29.1543728),
                LatLng(40.9702203, 29.1542414),
                LatLng(40.9702082, 29.1542414),
                LatLng(40.9702062, 29.1541019),
                LatLng(40.9702953, 29.1541046),
                LatLng(40.9702892, 29.1538203),
                LatLng(40.9704249, 29.1538122),
                LatLng(40.9705039, 29.1537827),
                LatLng(40.9706071, 29.1537237),
                LatLng(40.9706132, 29.1537532),
                LatLng(40.9706760, 29.1537210),
                LatLng(40.9706598, 29.1536781),
                LatLng(40.9707246, 29.1536245),
                LatLng(40.9707388, 29.1536647),
                LatLng(40.9708016, 29.1536137),
                LatLng(40.9706983, 29.1534770),
                LatLng(40.9708056, 29.1533455),
                LatLng(40.9708623, 29.1532382),
                LatLng(40.9708866, 29.1531819),
                LatLng(40.9710162, 29.1533241),
                LatLng(40.9708805, 29.1535494),
                LatLng(40.9709433, 29.1536513),
                LatLng(40.9708765, 29.1537264),
                LatLng(40.9708117, 29.1537881),
                LatLng(40.9707104, 29.1538632),
                LatLng(40.9707509, 29.1540402),
                LatLng(40.9709656, 29.1539061),
                LatLng(40.9710466, 29.1541770)
            ),
            classes = listOf(
               // "FA101 - Drawing Studio",
               // "FA102 - Basic Design",
               // "FA201 - Digital Illustration",
               // "FA205 - Photography Studio",
               // "FA301 - Art History",
               // "FA310 - Sculpture Workshop"
            )
        ),

        // FACULTY OF COMPUTER & INFORMATION SCIENCES (polygon)
        MapItem(
            id = "faculty_cis",
            name = "FACULTY OF COMPUTER & INFORMATION SCIENCES",
            type = MapItemType.FACULTY,
            center = LatLng(40.97418, 29.15208),
            polygon = listOf(
                LatLng(40.9743982, 29.1520366),
                LatLng(40.9743678, 29.1520742),
                LatLng(40.9743881, 29.1520983),
                LatLng(40.9741633, 29.1524014),
                LatLng(40.9741532, 29.1523880),
                LatLng(40.9741329, 29.1524202),
                LatLng(40.9740600, 29.1523236),
                LatLng(40.9740377, 29.1523531),
                LatLng(40.9739102, 29.1521949),
                LatLng(40.9740803, 29.1519105),
                LatLng(40.9739972, 29.1517603),
                LatLng(40.9740965, 29.1516718),
                LatLng(40.9741451, 29.1518247),
                LatLng(40.9741653, 29.1517952),
                LatLng(40.9742301, 29.1518998),
                LatLng(40.9742018, 29.1519293),
                LatLng(40.9742423, 29.1519883),
                LatLng(40.9742888, 29.1519213),
                LatLng(40.9743050, 29.1519400),
                LatLng(40.9743314, 29.1519052),
                LatLng(40.9744184, 29.1520125),
                LatLng(40.9743987, 29.1520366)
            ),
            classes = null
        ),

        // --- RING STOPS (POINTS) ---
        MapItem(
            id = "ring_stop_1",
            name = "RING STOP 1",
            type = MapItemType.RING_STOP,
            center = LatLng(40.974619, 29.152893),
            polygon = null,
            classes = null
        ),
        MapItem(
            id = "ring_stop_2",
            name = "RING STOP 2",
            type = MapItemType.RING_STOP,
            center = LatLng(40.9732864, 29.1516450),
            polygon = null,
            classes = null
        ),
        MapItem(
            id = "ring_stop_3",
            name = "RING STOP 3",
            type = MapItemType.RING_STOP,
            center = LatLng(40.9728470, 29.1510576),
            polygon = null,
            classes = null
        ),
        MapItem(
            id = "ring_stop_4",
            name = "RING STOP 4",
            type = MapItemType.RING_STOP,
            center = LatLng(40.9721848, 29.1505775),
            polygon = null,
            classes = null
        ),
        MapItem(
            id = "ring_stop_5",
            name = "RING STOP 5",
            type = MapItemType.RING_STOP,
            center = LatLng(40.9707307, 29.1527715),
            polygon = null,
            classes = null
        ),
        MapItem(
            id = "ring_stop_6",
            name = "RING STOP 6",
            type = MapItemType.RING_STOP,
            center = LatLng(40.9694730, 29.1549119),
            polygon = null,
            classes = null
        ),
        MapItem(
            id = "ring_stop_7",
            name = "RING STOP 7",
            type = MapItemType.RING_STOP,
            center = LatLng(40.9702953, 29.1546035),
            polygon = null,
            classes = null
        ),
        MapItem(
            id = "ring_stop_8",
            name = "RING STOP 8",
            type = MapItemType.RING_STOP,
            center = LatLng(40.9717048, 29.1537586),
            polygon = null,
            classes = null
        ),
        MapItem(
            id = "ring_stop_9",
            name = "RING STOP 9",
            type = MapItemType.RING_STOP,
            center = LatLng(40.9729665, 29.1520473),
            polygon = null,
            classes = null
        ),
        MapItem(
            id = "ring_stop_10",
            name = "RING STOP 10",
            type = MapItemType.RING_STOP,
            center = LatLng(40.974474, 29.153142),
            polygon = null,
            classes = null
        ),

        // --- SPORTS AREAS ---
        MapItem(
            id = "sport_basket_1",
            name = "BASKETBALL COURT",
            type = MapItemType.SPORT_BASKET,
            center = LatLng(40.97163, 29.15175),
            polygon = listOf(
                LatLng(40.9718688, 29.1517094),
                LatLng(40.9716724, 29.1514586),
                LatLng(40.9714552, 29.1517932),
                LatLng(40.9716582, 29.1520312),
                LatLng(40.9718688, 29.1517094)
            ),
            classes = null
        ),
        MapItem(
            id = "sport_tennis_1",
            name = "TENNIS COURT",
            type = MapItemType.SPORT_TENNIS,
            center = LatLng(40.9714266, 29.1520041),
            polygon = listOf(
                LatLng(40.9714547, 29.1517939),
                LatLng(40.9712734, 29.1520715),
                LatLng(40.9713990, 29.1522136),
                LatLng(40.9715792, 29.1519374),
                LatLng(40.9714547, 29.1517939)
            ),
            classes = null
        ),

        // MEYDAN FEMALE DORM (polygon)
        MapItem(
            id = "dorm_female_meydan",
            name = "MEYDAN FEMALE DORM",
            type = MapItemType.DORM_FEMALE,
            center = LatLng(40.97374, 29.15155),
            polygon = listOf(
                LatLng(40.9739081, 29.1521949),
                LatLng(40.9739972, 29.1520393),
                LatLng(40.9738291, 29.1515297),
                LatLng(40.9738474, 29.1515163),
                LatLng(40.9738231, 29.1514573),
                LatLng(40.9737927, 29.1514814),
                LatLng(40.9736226, 29.1510898),
                LatLng(40.9736469, 29.1510764),
                LatLng(40.9736064, 29.1509905),
                LatLng(40.9735801, 29.1510013),
                LatLng(40.9734707, 29.1511890),
                LatLng(40.9734829, 29.1511998),
                LatLng(40.9734991, 29.1511890),
                LatLng(40.9736919, 29.1516101),
                LatLng(40.9736773, 29.1516182),
                LatLng(40.9736894, 29.1516665),
                LatLng(40.9737076, 29.1516584),
                LatLng(40.9739081, 29.1522002)
            ),
            classes = null
        ),
        // 2) Otopark (polygon)
        MapItem(
            id = "carpark_1",
            name = "CARPARK",
            type = MapItemType.CARPARK,
            center = LatLng(40.9750, 29.1529),
            polygon = listOf(
                LatLng(40.9753966, 29.1523048),
                LatLng(40.9752832, 29.1526884),
                LatLng(40.9749409, 29.1532275),
                LatLng(40.9747992, 29.1531122),
                LatLng(40.9744650, 29.1525891),
                LatLng(40.9748073, 29.1520527),
                LatLng(40.9754006, 29.1523021)
            )
        ),
        // SOUTH LOWER FEMALE DORM (polygon)
        MapItem(
            id = "dorm_female_south_lower",
            name = "SOUTH LOWER FEMALE DORM",
            type = MapItemType.DORM_FEMALE,
            center = LatLng(40.97305, 29.15055),
            polygon = listOf(
                LatLng(40.9734707, 29.1511863),
                LatLng(40.9735861, 29.1509959),
                LatLng(40.9732702, 29.1505989),
                LatLng(40.9732905, 29.1505748),
                LatLng(40.9732702, 29.1505426),
                LatLng(40.9732439, 29.1505694),
                LatLng(40.9729847, 29.1502315),
                LatLng(40.9730029, 29.1502047),
                LatLng(40.9729705, 29.1501671),
                LatLng(40.9729523, 29.1501966),
                LatLng(40.9726708, 29.1498613),
                LatLng(40.9725655, 29.1500196),
                LatLng(40.9728449, 29.1503817),
                LatLng(40.9728389, 29.1503924),
                LatLng(40.9728611, 29.1504300),
                LatLng(40.9728773, 29.1504166),
                LatLng(40.9731426, 29.1507760),
                LatLng(40.9731386, 29.1507840),
                LatLng(40.9731730, 29.1508162),
                LatLng(40.9734707, 29.1511890)
            ),
            classes = null
        ),

        // NORTH 1 FEMALE DORM (polygon)
        MapItem(
            id = "dorm_female_north_1",
            name = "NORTH 1 FEMALE DORM",
            type = MapItemType.DORM_FEMALE,
            center = LatLng(40.97195, 29.15380),
            polygon = listOf(
                LatLng(40.9725371, 29.1535547),
                LatLng(40.9717595, 29.1539758),
                LatLng(40.9717676, 29.1539973),
                LatLng(40.9714162, 29.1541777),
                LatLng(40.9714213, 29.1541985),
                LatLng(40.9713808, 29.1542172),
                LatLng(40.9713382, 29.1540617),
                LatLng(40.9713686, 29.1540483),
                LatLng(40.9713747, 29.1540751),
                LatLng(40.9717169, 29.1538873),
                LatLng(40.9717088, 29.1538551),
                LatLng(40.9717493, 29.1538337),
                LatLng(40.9717574, 29.1538605),
                LatLng(40.9721139, 29.1536540),
                LatLng(40.9720977, 29.1536164),
                LatLng(40.9721361, 29.1535896),
                LatLng(40.9721503, 29.1536245),
                LatLng(40.9724885, 29.1534314),
                LatLng(40.9724865, 29.1534099),
                LatLng(40.9725057, 29.1533978),
                LatLng(40.9725381, 29.1535547)
            ),
            classes = null
        ),

        // NORTH 2 FEMALE DORM (polygon)
        MapItem(
            id = "dorm_female_north_2",
            name = "NORTH 2 FEMALE DORM",
            type = MapItemType.DORM_FEMALE,
            center = LatLng(40.97078, 29.15450),
            polygon = listOf(
                LatLng(40.9713767, 29.1542226),
                LatLng(40.9713322, 29.1540778),
                LatLng(40.9709879, 29.1542870),
                LatLng(40.9709838, 29.1542628),
                LatLng(40.9709474, 29.1542870),
                LatLng(40.9709595, 29.1543353),
                LatLng(40.9706102, 29.1545284),
                LatLng(40.9705990, 29.1544908),
                LatLng(40.9705565, 29.1545177),
                LatLng(40.9705747, 29.1545713),
                LatLng(40.9701879, 29.1547725),
                LatLng(40.9702203, 29.1548824),
                LatLng(40.9706355, 29.1546759),
                LatLng(40.9706274, 29.1546384),
                LatLng(40.9710122, 29.1544265),
                LatLng(40.9713772, 29.1542226)
            ),
            classes = null
        ),
        // MALE DORM (polygon)
        MapItem(
            id = "dorm_male_1",
            name = "MALE DORM",
            type = MapItemType.DORM_MALE,
            center = LatLng(40.97402, 29.15095),
            polygon = listOf(
                LatLng(40.9738889, 29.1504058),
                LatLng(40.9741451, 29.1504756),
                LatLng(40.9741390, 29.1505051),
                LatLng(40.9741167, 29.1504970),
                LatLng(40.9740944, 29.1506070),
                LatLng(40.9741775, 29.1506499),
                LatLng(40.9741835, 29.1506070),
                LatLng(40.9743435, 29.1506794),
                LatLng(40.9743435, 29.1506955),
                LatLng(40.9743719, 29.1507170),
                LatLng(40.9743860, 29.1506901),
                LatLng(40.9744164, 29.1507009),
                LatLng(40.9743962, 29.1508216),
                LatLng(40.9743212, 29.1510415),
                LatLng(40.9743415, 29.1510549),
                LatLng(40.9742159, 29.1514063),
                LatLng(40.9741957, 29.1513795),
                LatLng(40.9741127, 29.1516396),
                LatLng(40.9740398, 29.1516611),
                LatLng(40.9740195, 29.1516316),
                LatLng(40.9739912, 29.1516477),
                LatLng(40.9739588, 29.1515458),
                LatLng(40.9739405, 29.1515538),
                LatLng(40.9738291, 29.1512936),
                LatLng(40.9738534, 29.1512775),
                LatLng(40.9738231, 29.1512132),
                LatLng(40.9738367, 29.1512024),
                LatLng(40.9738190, 29.1511649),
                LatLng(40.9738028, 29.1511756),
                LatLng(40.9737841, 29.1511012),
                LatLng(40.9737730, 29.1510945),
                LatLng(40.9737643, 29.1510791),
                LatLng(40.9737623, 29.1510656),
                LatLng(40.9737583, 29.1510469),
                LatLng(40.9737603, 29.1510254),
                LatLng(40.9737623, 29.1510013),
                LatLng(40.9737684, 29.1509798),
                LatLng(40.9738008, 29.1507679),
                LatLng(40.9737846, 29.1507478),
                LatLng(40.9738879, 29.1504072),
                LatLng(40.9741461, 29.1504769)
            ),
            classes = null
        ),
        // NORTH 2 FEMALE DORM (PART 2) (polygon)
        MapItem(
            id = "dorm_female_north_2_part_2",
            name = "NORTH 2 FEMALE DORM",
            type = MapItemType.DORM_FEMALE,
            center = LatLng(40.96932, 29.15533),
            polygon = listOf(
                LatLng(40.9695257, 29.1550997),
                LatLng(40.9695864, 29.1552767),
                LatLng(40.9689181, 29.1556925),
                LatLng(40.9688533, 29.1554993),
                LatLng(40.9695237, 29.1550970)
            ),
            classes = null
        ),
        // RECTORATE BUILDING (polygon)
        MapItem(
            id = "rectorate",
            name = "RECTORATE BUILDING",
            type = MapItemType.RECTORATE,
            center = LatLng(40.97285, 29.15280),
            polygon = listOf(
                LatLng(40.9732905, 29.1526052),
                LatLng(40.9730049, 29.1524497),
                LatLng(40.9729928, 29.1524872),
                LatLng(40.9729847, 29.1525114),
                LatLng(40.9729685, 29.1525382),
                LatLng(40.9729543, 29.1525570),
                LatLng(40.9729259, 29.1525784),
                LatLng(40.9728976, 29.1525945),
                LatLng(40.9728692, 29.1525999),
                LatLng(40.9728308, 29.1526026),
                LatLng(40.9727943, 29.1525999),
                LatLng(40.9727660, 29.1525891),
                LatLng(40.9727376, 29.1525704),
                LatLng(40.9727194, 29.1525570),
                LatLng(40.9726890, 29.1525275),
                LatLng(40.9724521, 29.1528198),
                LatLng(40.9724885, 29.1528708),
                LatLng(40.9725351, 29.1529271),
                LatLng(40.9725898, 29.1529727),
                LatLng(40.9726445, 29.1530049),
                LatLng(40.9727012, 29.1530290),
                LatLng(40.9727498, 29.1530398),
                LatLng(40.9728024, 29.1530451),
                LatLng(40.9728490, 29.1530532),
                LatLng(40.9728895, 29.1530505),
                LatLng(40.9729239, 29.1530424),
                LatLng(40.9729543, 29.1530290),
                LatLng(40.9729867, 29.1530183),
                LatLng(40.9730130, 29.1530022),
                LatLng(40.9730414, 29.1529888),
                LatLng(40.9730647, 29.1529754),
                LatLng(40.9730859, 29.1529606),
                LatLng(40.9730961, 29.1529519),
                LatLng(40.9731062, 29.1529432),
                LatLng(40.9731285, 29.1529244),
                LatLng(40.9731487, 29.1529056),
                LatLng(40.9731690, 29.1528842),
                LatLng(40.9731912, 29.1528520),
                LatLng(40.9732095, 29.1528171),
                LatLng(40.9732277, 29.1527823),
                LatLng(40.9732439, 29.1527474),
                LatLng(40.9732560, 29.1527098),
                LatLng(40.9732692, 29.1526696),
                LatLng(40.9732900, 29.1526052)
            ),
            classes = null
        ),

        // FOOTBALL FIELD (polygon)
        MapItem(
            id = "sport_football_field",
            name = "FOOTBALL FIELD",
            type = MapItemType.SPORT_FOOTBALL,
            center = LatLng(40.972505, 29.150465),
            polygon = listOf(
                LatLng(40.9725326, 29.1502094),
                LatLng(40.9726850, 29.1504139),
                LatLng(40.9724703, 29.1507116),
                LatLng(40.9723225, 29.1505158),
                LatLng(40.9725356, 29.1502080)
            ),
            classes = null
        ),
        // SWIMMING POOL (polygon)
        MapItem(
            id = "sport_pool_1",
            name = "SWIMMING POOL",
            type = MapItemType.SPORT_POOL,
            center = LatLng(40.971255, 29.15265),
            polygon = listOf(
                LatLng(40.9713565, 29.1525516),
                LatLng(40.9712512, 29.1528091),
                LatLng(40.9712491, 29.1528144),
                LatLng(40.9711782, 29.1527621),
                LatLng(40.9711965, 29.1527179),
                LatLng(40.9712005, 29.1526750),
                LatLng(40.9712107, 29.1526294),
                LatLng(40.9712248, 29.1525972),
                LatLng(40.9712451, 29.1525730),
                LatLng(40.9712653, 29.1525516),
                LatLng(40.9712876, 29.1524953),
                LatLng(40.9713565, 29.1525516)
            ),
            classes = null
        ),

        // ENGLISH PREPARATORY SCHOOL - MAIN BUILDING (polygon)
        MapItem(
            id = "prep_center_main",
            name = "ENGLISH PREPARATORY SCHOOL - MAIN",
            type = MapItemType.PREP_CENTER,
            center = LatLng(40.97315, 29.15225),
            // ENGLISH PREPARATORY SCHOOL - MAIN BUILDING (polygon)
            polygon = listOf(
                LatLng(40.9732864, 29.1525985),
                LatLng(40.9730034, 29.1524430),
                LatLng(40.9730125, 29.1524047),
                LatLng(40.9730166, 29.1523907),
                LatLng(40.9730176, 29.1523806),
                LatLng(40.9730191, 29.1523719),
                LatLng(40.9730191, 29.1523424),
                LatLng(40.9730151, 29.1523129),
                LatLng(40.9730090, 29.1522753),
                LatLng(40.9730009, 29.1522458),
                LatLng(40.9729928, 29.1522217),
                LatLng(40.9729847, 29.1521949),
                LatLng(40.9729766, 29.1521707),
                LatLng(40.9731953, 29.1519025),
                LatLng(40.9732176, 29.1519293),
                LatLng(40.9732398, 29.1519696),
                LatLng(40.9732601, 29.1520151),
                LatLng(40.9732763, 29.1520688),
                LatLng(40.9732864, 29.1521117),
                LatLng(40.9732986, 29.1521546),
                LatLng(40.9733067, 29.1522029),
                LatLng(40.9733107, 29.1522458),
                LatLng(40.9733127, 29.1522914),
                LatLng(40.9733148, 29.1523317),
                LatLng(40.9733148, 29.1523772),
                LatLng(40.9733127, 29.1524255),
                LatLng(40.9733087, 29.1524658),
                LatLng(40.9733036, 29.1525060),
                LatLng(40.9733001, 29.1525268),
                LatLng(40.9732981, 29.1525375),
                LatLng(40.9732971, 29.1525456),
                LatLng(40.9732935, 29.1525590),
                LatLng(40.9732905, 29.1525744),
                LatLng(40.9732844, 29.1525985)
            ),
            classes = null
        ),
        // ENGLISH PREPARATORY SCHOOL - ANNEX BUILDING (polygon)
        MapItem(
            id = "prep_annex",
            name = "ENGLISH PREPARATORY SCHOOL - ANNEX",
            type = MapItemType.PREP_ANNEX,
            center = LatLng(40.96982, 29.15498),
            polygon = listOf(
                LatLng(40.9700381, 29.1549676),
                LatLng(40.9696310, 29.1551962),
                LatLng(40.9696188, 29.1551574),
                LatLng(40.9696067, 29.1551641),
                LatLng(40.9695642, 29.1550246),
                LatLng(40.9696133, 29.1549978),
                LatLng(40.9696026, 29.1549602),
                LatLng(40.9699824, 29.1547604),
                LatLng(40.9700401, 29.1549663)
            ),
            classes = null
        ),
        // SOCIAL FACILITIES (polygon)
        MapItem(
            id = "social_facilities",
            name = "SOCIAL FACILITIES",
            type = MapItemType.SOCIAL,
            center = LatLng(40.97152, 29.15280),
            polygon = listOf(
                LatLng(40.9715246, 29.1522130),
                LatLng(40.9716501, 29.1522941),
                LatLng(40.9717028, 29.1522780),
                LatLng(40.9717149, 29.1523317),
                LatLng(40.9717655, 29.1523665),
                LatLng(40.9717291, 29.1524470),
                LatLng(40.9717433, 29.1524845),
                LatLng(40.9717007, 29.1525060),
                LatLng(40.9716663, 29.1525838),
                LatLng(40.9716562, 29.1526884),
                LatLng(40.9716288, 29.1528104),
                LatLng(40.9716420, 29.1528198),
                LatLng(40.9716056, 29.1529191),
                LatLng(40.9715929, 29.1529103),
                LatLng(40.9715570, 29.1529807),
                LatLng(40.9715104, 29.1530324),
                LatLng(40.9715033, 29.1530364),
                LatLng(40.9714972, 29.1530357),
                LatLng(40.9714476, 29.1531631),
                LatLng(40.9714618, 29.1532114),
                LatLng(40.9714152, 29.1532463),
                LatLng(40.9713909, 29.1533509),
                LatLng(40.9713423, 29.1533160),
                LatLng(40.9712977, 29.1533187),
                LatLng(40.9712755, 29.1532785),
                LatLng(40.9711438, 29.1531926),
                LatLng(40.9714334, 29.1524470),
                LatLng(40.9714334, 29.1524470),
                LatLng(40.9715246, 29.1522130)
            ),
            classes = null
        ),
        // SERA (GREENHOUSE)
        MapItem(
            id = "sera_1",
            name = "GREENHOUSE",
            type = MapItemType.SERA,
            center = LatLng(40.97365, 29.15327),
            polygon = listOf(
                LatLng(40.9737826, 29.1532114),
                LatLng(40.9737502, 29.1534206),
                LatLng(40.9735031, 29.1533536),
                LatLng(40.9735375, 29.1531336),
                LatLng(40.9737846, 29.1532061)
            ),
            classes = null
        ),
        // KUBBE CAFE
        MapItem(
            id = "cafe_kubbe",
            name = "KUBBE CAFE",
            type = MapItemType.CAFE,
            // approximate center of polygon
            center = LatLng(40.973435, 29.152395),
            polygon = listOf(
                LatLng(40.9735112, 29.1523618),
                LatLng(40.9735107, 29.1523618),
                LatLng(40.9734940, 29.1523296),
                LatLng(40.9734727, 29.1523021),
                LatLng(40.9734479, 29.1522881),
                LatLng(40.9734191, 29.1522847),
                LatLng(40.9733938, 29.1523021),
                LatLng(40.9733735, 29.1523343),
                LatLng(40.9733639, 29.1523692),
                LatLng(40.9733578, 29.1524068),
                LatLng(40.9733720, 29.1524423),
                LatLng(40.9733912, 29.1524751),
                LatLng(40.9734226, 29.1524872),
                LatLng(40.9734545, 29.1524926),
                LatLng(40.9734798, 29.1524751),
                LatLng(40.9735021, 29.1524510),
                LatLng(40.9735102, 29.1524068),
                LatLng(40.9735112, 29.1523618)
            ),
            classes = null
        ),

        // GELECEK MERKEZİ (FUTURE CENTER)
        MapItem(
            id = "future_center",
            name = "FUTURE CENTER",
            type = MapItemType.FUTURE_CENTER,
            center = LatLng(40.9740000, 29.1534300),
            polygon = listOf(
                LatLng(40.9740641, 29.1533938),
                LatLng(40.9740322, 29.1533509),
                LatLng(40.9739876, 29.1533435),
                LatLng(40.9739491, 29.1533817),
                LatLng(40.9739405, 29.1534501),
                LatLng(40.9739714, 29.1534991),
                LatLng(40.9740205, 29.1535085),
                LatLng(40.9740590, 29.1534595),
                LatLng(40.9740641, 29.1533938)
            ),
            classes = null
        )
    )

    var selectedItem by remember { mutableStateOf<MapItem?>(null) }
    var pendingSelection by remember { mutableStateOf<MapItem?>(null) }
    var classQuery by remember { mutableStateOf("") }
    var showLegend by remember { mutableStateOf(false) }
    var tooltipItem by remember { mutableStateOf<MapItem?>(null) }
    var showTooltip by remember { mutableStateOf(false) }

    // Reset map/camera when user returns to Map screen (tab change / back navigation)
    var mapLoaded by remember { mutableStateOf(false) }
    var needsCameraReset by remember { mutableStateOf(true) }

    fun resetMapToInitial() {
        // Close UI
        pendingSelection = null
        selectedItem = null
        showLegend = false
        showTooltip = false
        tooltipItem = null
        classQuery = ""

        // Reset camera instantly (no animation)
        cameraPositionState.move(
            CameraUpdateFactory.newCameraPosition(initialCameraPosition)
        )
    }

    // When this composable is first shown, mark that we want a reset
    LaunchedEffect(Unit) {
        needsCameraReset = true
        if (mapLoaded) {
            resetMapToInitial()
            needsCameraReset = false
        }
    }

    // When coming back to this screen (tab/back), reset again
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                needsCameraReset = true
                if (mapLoaded) {
                    resetMapToInitial()
                    needsCameraReset = false
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Empty-state hint: appears after 2s, stays until user interacts
    var showEmptyHint by remember { mutableStateOf(false) }

    LaunchedEffect(selectedItem, showLegend) {
        showEmptyHint = false

        if (selectedItem == null && !showLegend) {
            delay(2000)
            if (selectedItem == null && !showLegend) {
                showEmptyHint = true
            }
        }
    }


    fun selectItem(newItem: MapItem) {
        // Micro feedback: very subtle system "tick" sound
        view.playSoundEffect(SoundEffectConstants.CLICK)
        showLegend = false
        // Same item -> just refresh content (no need to restart animation)
        if (selectedItem?.id == newItem.id) {
            selectedItem = newItem
            classQuery = ""
        } else {
            // If sheet is already open with another item, close first, then open again
            if (selectedItem != null) {
                pendingSelection = newItem
                selectedItem = null
                classQuery = ""
            } else {
                selectedItem = newItem
                classQuery = ""
            }
        }

        // Subtle camera nudge to give “tap detected” feedback (no zoom change)
        scope.launch {
            val currentZoom = cameraPositionState.position.zoom

            // Nudge target slightly upward (north) so the building appears a bit higher than center
            val nudgedTarget = LatLng(
                newItem.center.latitude - 0.00060,
                newItem.center.longitude
            )

            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(nudgedTarget, currentZoom),
                durationMs = 520
            )
        }
    }

    LaunchedEffect(pendingSelection) {
        pendingSelection?.let {
            // Let the exit animation start/finish, then re-open to trigger enter animation from scratch
            delay(180)
            selectedItem = it
            pendingSelection = null
        }
    }

    val mapProperties = MapProperties(
        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
            context,
            R.raw.yeditepe_map_style
        ),
        // Prevent user from zooming out too far and seeing outside campus
        minZoomPreference = 16.0f,
        maxZoomPreference = 18.5f,
        // Prevent user from panning outside campus
        latLngBoundsForCameraTarget = campusBounds
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
            onMapLoaded = {
                mapLoaded = true
                if (needsCameraReset) {
                    resetMapToInitial()
                    needsCameraReset = false
                }
            },
            onMapClick = {
                pendingSelection = null
                selectedItem = null
                showLegend = false
                showTooltip = false
                tooltipItem = null
                showEmptyHint = false
            },
            onMapLongClick = { latLng ->
                // Long-press: show a small tooltip (emoji + name). Do NOT open the bottom sheet.
                val hit = findPolygonItemAt(latLng, mapItems)
                if (hit != null) {
                    tooltipItem = hit
                    showTooltip = true

                    // Auto-hide after a short time
                    scope.launch {
                        delay(1400)
                        // Only hide if still showing the same tooltip
                        showTooltip = false
                    }
                } else {
                    showTooltip = false
                    tooltipItem = null
                }
            }
        ) {

            // 1) Mask
            val outerBounds = listOf(
                LatLng(41.2, 28.8),
                LatLng(41.2, 29.4),
                LatLng(40.8, 29.4),
                LatLng(40.8, 28.8)
            )

            Polygon(
                points = outerBounds,
                holes = listOf(campusBoundary),
                fillColor = MapColors.outsideMask,
                strokeColor = Color.Transparent,
                strokeWidth = 0f
            )

            // 2) Campus border (transparent inside)
            Polygon(
                points = campusBoundary,
                fillColor = Color.Transparent,
                strokeColor = MapColors.campusBorder,
                strokeWidth = 3f
            )

            // 3) Draw items
            mapItems.forEach { item ->
                val isSelected = selectedItem?.id == item.id
                val (baseFill, baseStroke) = colorsFor(item.type)

                val fillColor = when {
                    isSelected -> baseFill.copy(alpha = 0.75f) // stronger fill
                    selectedItem != null -> baseFill.copy(alpha = 0.20f) // dim others
                    else -> baseFill
                }

                val strokeColor = if (isSelected) baseStroke else baseStroke.copy(alpha = 0.6f)
                val strokeWidth = if (isSelected) 4f else 2f

                if (item.polygon != null) {
                    // Main polygon (draw once)
                    Polygon(
                        points = item.polygon,
                        fillColor = fillColor,
                        strokeColor = strokeColor,
                        strokeWidth = strokeWidth,
                        clickable = true,
                        onClick = {
                            selectItem(item)
                        }
                    )
                } else {
                    if (item.type == MapItemType.RING_STOP) {
                        Circle(
                            center = item.center,
                            radius = 4.0,
                            fillColor = MapColors.ringFill,
                            strokeColor = MapColors.ringFill,
                            strokeWidth = 1f,
                            clickable = true,
                            onClick = {
                                selectItem(item)
                            }
                        )
                    } else {
                        Marker(
                            state = MarkerState(position = item.center),
                            title = item.name,
                            onClick = {
                                selectItem(item)
                                true
                            }
                        )
                    }
                }

            }

        }

        // If BOTH the guide (info) panel and the building sheet are open
        // a tap anywhere should close both immediately.
        if (showLegend && selectedItem != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable {
                        pendingSelection = null
                        selectedItem = null
                        showLegend = false
                    }
            )
        }

        // =========================
        // Top-right Legend Toggle
        // =========================
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.End
        ) {

            // Toggle button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(MapColors.uiPurple, RoundedCornerShape(12.dp))
            ) {
                IconButton(
                    onClick = {
                        val willOpen = !showLegend
                        showLegend = willOpen
                        if (willOpen) {
                            // Close bottom sheet if user opens the guide
                            pendingSelection = null
                            selectedItem = null
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Legend",
                        tint = Color.White
                    )
                }
            }

            AnimatedVisibility(
                visible = showLegend,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .background(Color.White.copy(alpha = 0.95f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                        .clickable { /* prevent map clicks from closing immediately */ },
                ) {
                    Text(
                        text = "🗺️ Campus Map Guide",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // make legend scrollable as it grows
                    val scrollState = rememberScrollState()
                    Column(modifier = Modifier.verticalScroll(scrollState)) {

                        // --- Buildings ---
                        Text(
                            text = "Buildings",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LegendRow("🏛️", "Faculty Buildings", MapColors.facultyStroke)
                        Spacer(modifier = Modifier.height(6.dp))
                        LegendRow("📘", "English Prep (Main/Annex)", MapColors.prepStroke)
                        Spacer(modifier = Modifier.height(6.dp))
                        LegendRow("🏢", "Rectorate Building", MapColors.rectorateStroke)
                        Spacer(modifier = Modifier.height(6.dp))
                        LegendRow("🧩", "Social Facilities", MapColors.socialStroke)
                        Spacer(modifier = Modifier.height(6.dp))
                        LegendRow("☕", "Cafes", MapColors.cafeStroke)
                        Spacer(modifier = Modifier.height(6.dp))
                        LegendRow("🌱", "Greenhouse", MapColors.seraStroke)
                        Spacer(modifier = Modifier.height(6.dp))
                        LegendRow("🛖", "Future Center", MapColors.futureCenterStroke)

                        Spacer(modifier = Modifier.height(12.dp))

                        // --- Sports ---
                        Text(
                            text = "Sports",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LegendRow("🏀", "Basketball Court", MapColors.basketStroke)
                        Spacer(modifier = Modifier.height(6.dp))
                        LegendRow("⚽", "Football Field", MapColors.footballStroke)
                        Spacer(modifier = Modifier.height(6.dp))
                        LegendRow("🎾", "Tennis Court", MapColors.tennisStroke)
                        Spacer(modifier = Modifier.height(6.dp))
                        LegendRow("🏊", "Swimming Pool", MapColors.poolStroke)

                        Spacer(modifier = Modifier.height(12.dp))

                        // --- Dorms ---
                        Text(
                            text = "Dormitories",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LegendRow("👩‍🎓", "Female Dorms", MapColors.dormFemaleStroke)
                        Spacer(modifier = Modifier.height(6.dp))
                        LegendRow("👨‍🎓", "Male Dorm", MapColors.dormMaleStroke)

                        Spacer(modifier = Modifier.height(12.dp))

                        // --- Other ---
                        Text(
                            text = "Other",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LegendRow("🅿️", "Carpark", MapColors.carparkStroke)
                        Spacer(modifier = Modifier.height(6.dp))
                        LegendRow("🚏", "Ring Stop", MapColors.ringFill)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }

        // =========================
        // Long-press Tooltip (no navigation)
        // =========================
        AnimatedVisibility(
            visible = showTooltip && tooltipItem != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 76.dp)
        ) {
            val item = tooltipItem
            if (item != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.92f), RoundedCornerShape(999.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(text = emojiFor(item.type), style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
            }
        }

        // =========================
        // Empty-state one-time hint
        // =========================
        AnimatedVisibility(
            visible = showEmptyHint && selectedItem == null && !showLegend,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(999.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Tap a building to see details",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black.copy(alpha = 0.70f)
                )
            }
        }

        // =========================
        // Bottom Sheet (Animated)
        // =========================
        AnimatedVisibility(
            // Keep the sheet anchored near the bottom like the original.
            // Fixed position: it should not climb up as content grows.
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 0.dp),
            visible = selectedItem != null,
            // Slight slide (subtle) from bottom
            enter = slideInVertically(initialOffsetY = { it / 8 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it / 8 }) + fadeOut()
        ) {
            selectedItem?.let { item ->

                val (itemFill, itemStroke) = colorsFor(item.type)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        //  fixed height so the sheet NEVER pushes upward as content grows
                        // user requested: stays at the same vertical stop
                        .height(if (isClassFaculty(item)) 240.dp else 170.dp)
                        .background(
                            Color.White,
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Header (fixed) — add subtle identity strip + emoji badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val badgeSize = 32.dp

                            // Emoji badge (subtle)
                            Box(
                                modifier = Modifier
                                    .size(badgeSize)
                                    .background(
                                        color = itemStroke.copy(alpha = 0.18f),
                                        shape = RoundedCornerShape(999.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = emojiFor(item.type),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Name strip (no background, only padding)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = item.name.uppercase(),
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis

                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(badgeSize)
                                    .background(
                                        color = Color.Black.copy(alpha = 0.04f),
                                        shape = RoundedCornerShape(999.dp)
                                    )
                                    .clickable {
                                        pendingSelection = null
                                        selectedItem = null
                                        showEmptyHint = false
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.Black.copy(alpha = 0.60f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Directions button (fixed)
                        Button(
                            onClick = {
                                openGoogleMapsDirections(
                                    context = context,
                                    destination = item.center
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MapColors.uiPurple,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Directions")
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        //Text(
                          //  text = "Tap anywhere on the map to close.",
                          //  style = MaterialTheme.typography.bodySmall,
                          //  color = Color.Gray,
                           // modifier = Modifier.fillMaxWidth(),
                      //  )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Scrollable content area (so the sheet height never grows)
                        // Content area:
// - If this is the faculty with classes -> scrollable list
// - Otherwise -> keep it empty and reserve space so the footer is always visible
                        if (isClassFaculty(item)) {
                            val sheetScroll = rememberScrollState()
                            Column(
                                modifier = Modifier
                                    .weight(1f, fill = true)
                                    .verticalScroll(sheetScroll)
                            ) {
                                Text(
                                    "Classrooms in this building:",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                val filtered = item.classes.orEmpty()

                                if (filtered.isEmpty()) {
                                    Text(
                                        text = "No results found.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                } else {
                                    filtered.forEach { cls ->
                                        Text(text = "• $cls", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        } else {
                            // No extra content for other buildings -> keep layout stable
                            Spacer(modifier = Modifier.weight(1f, fill = true))
                        }

                    }
                }
            }
        }
    }
}

/* =========================
   5) NAVIGATION TO GOOGLE MAPS
   ========================= */

fun openGoogleMapsDirections(
    context: Context,
    destination: LatLng
) {

    val uri = Uri.parse(
        "https://www.google.com/maps/dir/?api=1" +
                "&destination=${destination.latitude},${destination.longitude}" +
                "&travelmode=walking"
    )

    val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }

    try {
        context.startActivity(mapIntent)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}