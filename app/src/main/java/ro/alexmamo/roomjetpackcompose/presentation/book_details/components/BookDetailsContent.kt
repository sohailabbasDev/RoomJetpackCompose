package ro.alexmamo.roomjetpackcompose.presentation.book_details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import ro.alexmamo.roomjetpackcompose.R
import ro.alexmamo.roomjetpackcompose.components.ActionIconButton
import ro.alexmamo.roomjetpackcompose.domain.model.Book
import ro.alexmamo.roomjetpackcompose.presentation.book_list.components.AuthorText
import ro.alexmamo.roomjetpackcompose.presentation.book_list.components.TitleText

@Composable
fun BookDetailsContent(
    innerPadding: PaddingValues,
    book: Book,
    onPinBook: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleText(
                title = book.title
            )
        }
        AuthorText(
            author = book.author
        )
        ActionIconButton(
            onActionIconButtonClick = onPinBook,
            imageVector = Icons.Default.PushPin,
            resourceId = R.string.pin_icon,
            tint = if (book.isPinned) Color.Red else Color.Unspecified,
            modifier = Modifier.testTag(
                if (book.isPinned) stringResource(R.string.pinned) else stringResource(R.string.unpinned)
            )
        )
    }
}