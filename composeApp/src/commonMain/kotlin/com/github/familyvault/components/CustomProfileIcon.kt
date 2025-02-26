import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun CustomProfileIcon(
    icon: ImageVector,
    contentDescription: String? = "",
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = rememberVectorPainter(icon),
            contentDescription = contentDescription,
            modifier = Modifier.size(125.dp),
            tint = AdditionalTheme.colors.firstOptionPrimaryColor
        )
    }
}