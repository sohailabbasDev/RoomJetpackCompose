package ro.alexmamo.roomjetpackcompose.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ro.alexmamo.roomjetpackcompose.R
import ro.alexmamo.roomjetpackcompose.data.dao.BookDao
import ro.alexmamo.roomjetpackcompose.data.network.BookDb
import ro.alexmamo.roomjetpackcompose.data.repository.BookRepositoryImpl
import ro.alexmamo.roomjetpackcompose.domain.repository.BookRepository
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideBookDb(
        @ApplicationContext
        context: Context
    ): BookDb {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE books ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0")
            }
        }

        return Room.databaseBuilder(
            context,
            BookDb::class.java,
            context.resources.getString(R.string.db_name)
        ).addMigrations(MIGRATION_1_2).build()
    }

    @Provides
    fun provideBookDao(
        bookDb: BookDb
    ) = bookDb.bookDao

    @Provides
    fun provideBookRepository(
        bookDao: BookDao
    ): BookRepository = BookRepositoryImpl(
        bookDao = bookDao
    )
}