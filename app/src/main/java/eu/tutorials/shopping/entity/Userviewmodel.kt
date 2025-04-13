package eu.tutorials.shopping.entity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val userRepo: UserRepository): ViewModel() {

    private val _sItems = mutableStateOf<List<User>>(emptyList())
    val sItems: State<List<User>> = _sItems

    fun updateItem(updatedItem: User) {
        viewModelScope.launch {
            val updatedUser = updatedItem.copy(isEdit = true)
            userRepo.update(updatedUser)
            _sItems.value = userRepo.getUsersList() // Fetch updated list
        }
    }
}