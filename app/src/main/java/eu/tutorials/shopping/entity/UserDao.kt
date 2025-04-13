package eu.tutorials.shopping.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
    interface UserDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(item: User):Long

        @Delete
        suspend fun delete(item: User)

        @Query("SELECT * FROM grocery_items")
        suspend fun getAllUsersList(): List<User>

        @Query("SELECT * FROM grocery_items")
        fun getAllUsers(): LiveData<List<User>>

        @Update
        suspend fun update(item: User)

    }


