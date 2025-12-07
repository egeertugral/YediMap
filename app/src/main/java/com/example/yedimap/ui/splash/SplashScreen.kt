package com.example.yedimap.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yedimap.R
import com.example.yedimap.ui.theme.OffWhite
import com.example.yedimap.ui.theme.PurpleSplash

// Logo ve yazı için gradient fırçası
private val LogoGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF574EA0), // mor
        Color(0xFF2F9E64)  // yeşil
    )
)

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleSplash),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .size(160.dp)
                .shadow(8.dp, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(
                containerColor = OffWhite
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // GRADIENT LOGO (ic_yedimap_logo vektörünü kullanıyoruz)
                val logoPainter = painterResource(id = R.drawable.ic_yedimap_logo)

                Icon(
                    painter = logoPainter,
                    contentDescription = "YediMap Logo",
                    tint = Color.White, // alt renk
                    modifier = Modifier
                        .size(64.dp)
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(
                                    brush = LogoGradient,
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        }
                )

                // GRADIENT YAZI
                Text(
                    text = "YediMap",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(
                                    brush = LogoGradient,
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        }
                )
            }
        }
    }
}
