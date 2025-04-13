package eu.tutorials.shopping.entity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class], version = 2,exportSchema = false)
    abstract class Userdatabase: RoomDatabase() {
    abstract fun Userdao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: Userdatabase? = null
        fun getdatabase(context: Context): Userdatabase {
            return INSTANCE ?: synchronized(this) {  // ✅ Corrected synchronization
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Userdatabase::class.java,
                    "user_database"
                ).fallbackToDestructiveMigration()  // ✅ Prevents data loss
                    .build()
                INSTANCE = instance
                instance

            }
        }
    }
}