package hr.algebra.personmanager.dao

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [Person::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class PeopleDatabase : RoomDatabase(){

    abstract fun personDao() : PersonDao;

    companion object {
        @Volatile private var INSTANCE: PeopleDatabase? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(PeopleDatabase::class.java) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it}
            }


        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PeopleDatabase::class.java,
            "people.db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE people "
                    + " ADD COLUMN title TEXT"
        )
    }
}


