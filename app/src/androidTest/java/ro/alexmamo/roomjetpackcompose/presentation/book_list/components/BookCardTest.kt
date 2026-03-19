package ro.alexmamo.roomjetpackcompose.presentation.book_list.components

import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import ro.alexmamo.roomjetpackcompose.R
import ro.alexmamo.roomjetpackcompose.domain.model.Book
import ro.alexmamo.roomjetpackcompose.presentation.MainActivity

@HiltAndroidTest
class BookCardTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testBookCardPinExists() {
        composeTestRule.activity.setContent {
            BookCard(
                book = Book(
                    id = 0,
                    title = "Test book",
                    author = "Author",
                    isPinned = false,
                ),
                onPinBook = {},
                onBookCardClick = {},
                onDeleteBook = {},
                onEditBook = {}
            )
        }
        composeTestRule.apply {
            onNodeWithContentDescription(getString(R.string.pin_icon))
                .assertIsDisplayed()
        }
    }

    

    @Test
    fun testPinnedBookDisplaysPinIcon() {
        composeTestRule.activity.setContent {
            BookCard(
                book = Book(
                    id = 0,
                    title = "Test book",
                    author = "Author",
                    isPinned = true,
                ),
                onPinBook = {},
                onBookCardClick = {},
                onDeleteBook = {},
                onEditBook = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.pin_icon), useUnmergedTree = true)
            .assertExists()
            .assert(hasTestTag(getString(R.string.pinned)))
    }

    @Test
    fun testUnpinnedBookDisplaysNoIcon() {
        composeTestRule.activity.setContent {
            BookCard(
                book = Book(
                    id = 0,
                    title = "Test book",
                    author = "Author",
                    isPinned = false,
                ),
                onPinBook = {},
                onBookCardClick = {},
                onDeleteBook = {},
                onEditBook = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(getString(R.string.pin_icon), useUnmergedTree = true)
            .assertExists()
            .assert(hasTestTag(getString(R.string.unpinned)))
    }

    private fun getString(@StringRes resId: Int) = composeTestRule.activity.getString(resId)

}
