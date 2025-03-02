import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.github.familyvault.ui.theme.AdditionalTheme


@Composable
fun CustomIcon(
    icon: ImageVector,
    contentDescription: String? = "",
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(AdditionalTheme.colors.firstOptionSecondaryColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = rememberVectorPainter(icon),
                contentDescription = contentDescription,
                modifier = Modifier.size(85.dp),
                tint = AdditionalTheme.colors.firstOptionPrimaryColor
            )
        }
    }
}