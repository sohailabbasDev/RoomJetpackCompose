package ro.alexmamo.roomjetpackcompose.presentation.book_details

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ro.alexmamo.roomjetpackcompose.domain.model.Book
import ro.alexmamo.roomjetpackcompose.presentation.book_details.components.BookDetailsContent
import ro.alexmamo.roomjetpackcompose.presentation.book_details.components.BookDetailsTopBar
import ro.alexmamo.roomjetpackcompose.presentation.book_list.BookListViewModel

@Composable
fun BookDetailsScreen(
    viewModel: BookListViewModel = hiltViewModel(),
    book: Book,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            BookDetailsTopBar(
                onArrowBackIconClick = navigateBack
            )
        },
        content = { innerPadding ->
            BookDetailsContent(
                innerPadding = innerPadding,
                book = book,
                onPinBook = {
                    viewModel.pinBook(book)
                }
            )
        }
    )
}