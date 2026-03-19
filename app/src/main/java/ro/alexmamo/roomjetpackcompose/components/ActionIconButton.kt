package ro.alexmamo.roomjetpackcompose.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun ActionIconButton(
    onActionIconButtonClick: () -> Unit,
    imageVector: ImageVector,
    resourceId: Int,
    tint : Color = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onActionIconButtonClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(
                id = resourceId
            ),
            tint = tint,
            modifier = modifier
        )
    }
}