package ro.alexmamo.roomjetpackcompose.presentation.book_list

import android.content.Context
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ro.alexmamo.roomjetpackcompose.R
import ro.alexmamo.roomjetpackcompose.data.dao.BookDao
import ro.alexmamo.roomjetpackcompose.domain.model.Book
import ro.alexmamo.roomjetpackcompose.presentation.MainActivity
import ro.alexmamo.roomjetpackcompose.presentation.book_list.components.BookCard
import ro.alexmamo.roomjetpackcompose.utils.getBookTest
import javax.inject.Inject

@HiltAndroidTest
class BookListScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val bookTest by lazy { getBookTest(context) }

    @Inject
    lateinit var bookDao: BookDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testBookClickAndNavigationToBookDetailsScreenAndBackToBookListScreen() {
        composeTestRule.apply {
            onNodeWithContentDescription(getString(R.string.open_insert_book_dialog))
                .performClick()
            onNodeWithText(getString(R.string.book_title))
                .performTextInput(bookTest.title)
            onNodeWithText(getString(R.string.book_author))
                .performTextInput(bookTest.author)
            onNodeWithText(getString(R.string.insert_button))
                .performClick()
            onNodeWithText(bookTest.title)
                .performClick()
            onNodeWithText(getString(R.string.book_details_screen_title))
                .assertIsDisplayed()
            onNodeWithText(bookTest.title)
                .assertIsDisplayed()
            onNodeWithText("by ${bookTest.author}")
                .assertIsDisplayed()
            onNodeWithContentDescription(getString(R.string.navigate_back))
                .performClick()
            onNodeWithText(getString(R.string.book_list_screen_title))
                .assertIsDisplayed()
        }
    }

    @Test
    fun testPinIconTogglePinnedStatus() {
        var bookPinnedStatus = false
        var clickedBookId = -1
        val book = Book(
            id = 1,
            title = "Test book",
            author = "Author",
            isPinned = false
        )

        composeTestRule.activity.setContent {
            BookCard(
                book = book.copy(isPinned = bookPinnedStatus),
                onPinBook = {
                    clickedBookId = book.id
                    bookPinnedStatus = !bookPinnedStatus
                },
                onBookCardClick = {},
                onDeleteBook = {},
                onEditBook = {}
            )
        }

        composeTestRule
            .onNodeWithTag(getString(R.string.unpinned), useUnmergedTree = true)
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.pin_icon))
            .performClick()

        assertThat(clickedBookId).isEqualTo(1)
        assertThat(bookPinnedStatus).isTrue()

        composeTestRule.activity.setContent {
            BookCard(
                book = book.copy(isPinned = bookPinnedStatus),
                onPinBook = {
                    clickedBookId = book.id
                    bookPinnedStatus = !bookPinnedStatus
                },
                onBookCardClick = {},
                onDeleteBook = {},
                onEditBook = {}
            )
        }

        composeTestRule
            .onNodeWithTag(getString(R.string.pinned), useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun testPinnedBookMovesToTopOfList() {
        val book1 = Book(id = 1, title = "Book A", author = "Author A", isPinned = false)
        val book2 = Book(id = 2, title = "Book B", author = "Author B", isPinned = false)
        val book3 = Book(id = 3, title = "Book C", author = "Author C", isPinned = false)

        var bookList = listOf(book1, book2, book3)

        val setContent = @Composable {
            LazyColumn(
                modifier = Modifier.testTag("book_list")
            ) {
                items(
                    items = bookList.sortedWith(compareBy<Book> { !it.isPinned }.thenBy { it.title }),
                    key = { it.id }
                ) { book ->
                    BookCard(
                        modifier = Modifier.testTag("book_item_${book.id}"),
                        book = book,
                        onPinBook = {
                            bookList = bookList.map {
                                if (it.id == book.id) it.copy(isPinned = !it.isPinned)
                                else it
                            }
                        },
                        onBookCardClick = {},
                        onDeleteBook = {},
                        onEditBook = {}
                    )
                }
            }
        }

        composeTestRule.activity.setContent { setContent() }

        composeTestRule
            .onNodeWithTag("book_list")
            .onChildren()
            .assertCountEquals(3)

        composeTestRule
            .onNodeWithTag("book_item_2")
            .assertExists()

        composeTestRule
            .onNodeWithTag("book_item_2")
            .onChildren()
            .filterToOne(hasContentDescription(getString(R.string.pin_icon)))
            .performClick()

        composeTestRule.activity.setContent { setContent() }

        composeTestRule
            .onNodeWithTag("book_list")
            .onChildren()
            .onFirst()
            .assert(hasTestTag("book_item_2"))
    }

    @Test
    fun testUnpinBookReturnsToNormalPosition() {
        val book1 = Book(id = 1, title = "Book A", author = "Author A", isPinned = false)
        val book2 = Book(id = 2, title = "Book B", author = "Author B", isPinned = true) // Initially pinned
        val book3 = Book(id = 3, title = "Book C", author = "Author C", isPinned = false)

        var bookList = listOf(book1, book2, book3)

        val setContent = @Composable {
            LazyColumn(
                modifier = Modifier.testTag("book_list")
            ) {
                items(
                    items = bookList.sortedWith(compareBy<Book> { !it.isPinned }.thenBy { it.title }),
                    key = { it.id }
                ) { book ->
                    BookCard(
                        modifier = Modifier.testTag("book_item_${book.id}"),
                        book = book,
                        onPinBook = {
                            bookList = bookList.map {
                                if (it.id == book.id) it.copy(isPinned = !it.isPinned)
                                else it
                            }
                        },
                        onBookCardClick = {},
                        onDeleteBook = {},
                        onEditBook = {}
                    )
                }
            }
        }

        composeTestRule.activity.setContent { setContent() }

        composeTestRule
            .onNodeWithTag("book_list")
            .onChildren()
            .onFirst()
            .assert(hasTestTag("book_item_2"))

        composeTestRule
            .onNodeWithTag("book_item_2")
            .onChildren()
            .filterToOne(hasContentDescription(getString(R.string.pin_icon)))
            .performClick()

        composeTestRule.activity.setContent { setContent() }

        composeTestRule.onNodeWithTag("book_list").onChildren()[1].assert(hasTestTag("book_item_2"))

        composeTestRule
            .onNodeWithTag("book_list")
            .onChildren()
            .onFirst()
            .assert(hasTestTag("book_item_1")) // Book A should be first

        composeTestRule.onNodeWithTag("book_list").onChildren()[2].assert(hasTestTag("book_item_3")) // Book C should be last
    }

    private fun getString(@StringRes resId: Int) = composeTestRule.activity.getString(resId)
}