package eu.tutorials.shopping.entity

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserRepository(context: Context) {
    private val userDao: UserDao


    init {
        val database = Userdatabase.getdatabase(context)
        userDao = database.Userdao()
    }
    val allUsers: LiveData<List<User>> = userDao.getAllUsers()
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    suspend fun getUsersList(): List<User> {
        return withContext(Dispatchers.IO){
         userDao.getAllUsersList()}
    }
    suspend fun update(user: User) {
        userDao.update(user)
    }

}

