package com.example.semicolon.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semicolon.sqlite_database.User
import com.example.semicolon.sqlite_database.daos.UserDao
import kotlinx.coroutines.*

class LoginViewModel(private val userDatabase: UserDao): ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user
    private var one: Int = 0

    init {
        getTotal()
    }

    private fun getTotal() {
        uiScope.launch {
            one = getNumber()
        }
    }

    fun checkUser(username: String, password: String) {
        uiScope.launch {
            if (username.trim().length > 1 && password.trim().length > 7) {
                _user.value = getUser(username, password)
            } else
                _user.value = null
        }
    }

    private suspend fun getUser(username: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            userDatabase.getUserByUsernameAndPassword(username, password)
        }
    }

    private suspend fun getNumber(): Int {
        return withContext(Dispatchers.IO) {
            userDatabase.getOne()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}