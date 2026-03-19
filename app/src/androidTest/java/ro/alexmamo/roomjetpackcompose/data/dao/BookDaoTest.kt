package ro.alexmamo.roomjetpackcompose.data.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ro.alexmamo.roomjetpackcompose.data.network.BookDb
import ro.alexmamo.roomjetpackcompose.utils.getBookTest
import ro.alexmamo.roomjetpackcompose.utils.getUpdatedBookTest
import java.io.IOException
import javax.inject.Inject

@HiltAndroidTest
class BookDaoTest() {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var bookDao: BookDao
    @Inject
    lateinit var bookDb: BookDb

    val context = ApplicationProvider.getApplicationContext<Context>()
    private val bookTest = getBookTest(context)
    private val updatedBookTest = getUpdatedBookTest(context)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndGetBookById() = runTest {
        bookDao.insertBook(bookTest)
        val book = bookDao.getBookById(bookTest.id)
        Truth.assertThat(book).isEqualTo(bookTest)
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndCheckIfBookExistsInBookList() = runTest {
        bookDao.insertBook(bookTest)
        val bookList = bookDao.getBookList().first()
        Truth.assertThat(bookTest).isIn(bookList)
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndCheckTheSizeOfBookList() = runTest {
        bookDao.insertBook(bookTest)
        val bookList = bookDao.getBookList().first()
        Truth.assertThat(bookList.size).isEqualTo(1)
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateBookPinnedStatusUsingUpdateBook() = runTest {
        bookDao.insertBook(bookTest.copy(isPinned = false))
        val pinnedBook = bookTest.copy(isPinned = true)
        bookDao.updateBook(pinnedBook)
        val updatedBook = bookDao.getBookById(bookTest.id)
        Truth.assertThat(updatedBook.isPinned).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun testPinnedBooksAppearTopAsperPinnedStatus() = runTest {
        val book1 = bookTest.copy(id = 1, title = "A", isPinned = false)
        val book2 = bookTest.copy(id = 2, title = "B", isPinned = true)
        val book3 = bookTest.copy(id = 3, title = "C", isPinned = false)

        bookDao.insertBook(book1)
        bookDao.insertBook(book2)
        bookDao.insertBook(book3)

        val sortedList = bookDao.getBookList().first()
        Truth.assertThat(sortedList.first().id).isEqualTo(book2.id)
    }

    @Test
    @Throws(Exception::class)
    fun testIsPinnedIsAddedByUpdatingIsPinnedToTheBookItemUsingBookDao() = runTest {
        val initialBook = bookTest.copy(isPinned = false)
        bookDao.insertBook(initialBook)

        val updatedBook = initialBook.copy(isPinned = true)
        bookDao.updateBook(updatedBook)

        val result = bookDao.getBookById(initialBook.id)

        Truth.assertThat(result.isPinned).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateAndGetBookById() = runTest {
        bookDao.insertBook(bookTest)
        bookDao.updateBook(updatedBookTest)
        val book = bookDao.getBookById(bookTest.id)
        Truth.assertThat(book.title).isEqualTo(updatedBookTest.title)
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndDeleteAndCheckTheSizeOfBookList() = runTest {
        bookDao.insertBook(bookTest)
        bookDao.deleteBook(bookTest)
        val bookList = bookDao.getBookList().first()
        Truth.assertThat(bookList).isEmpty()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        bookDb.close()
    }
}