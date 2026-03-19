package ro.alexmamo.roomjetpackcompose.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import ro.alexmamo.roomjetpackcompose.data.dao.BookDao
import ro.alexmamo.roomjetpackcompose.domain.model.Book

@Database(
    entities = [Book::class],
    version = 2,
    exportSchema = false
)
abstract class BookDb : RoomDatabase() {
    abstract val bookDao: BookDao
}