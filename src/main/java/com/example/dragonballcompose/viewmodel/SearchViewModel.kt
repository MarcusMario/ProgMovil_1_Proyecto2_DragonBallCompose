package com.example.dragonballcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dragonballcompose.models.Character
import com.example.dragonballcompose.repository.DragonBallRepository
import com.example.dragonballcompose.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val repository = DragonBallRepository()

    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun searchCharacter(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _characters.value = emptyList()
            _error.value = null

            when (val result = repository.searchCharacter(name)) {
                is Result.Success -> _characters.value = result.data
                is Result.Error -> _error.value = result.message
            }
            _isLoading.value = false
        }
    }
}
