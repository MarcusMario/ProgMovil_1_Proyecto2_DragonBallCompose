package com.example.dragonballcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dragonballcompose.models.Character
import com.example.dragonballcompose.repository.DragonBallRepository
import com.example.dragonballcompose.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val characterId: Int) : ViewModel() {

    private val repository = DragonBallRepository()

    private val _character = MutableStateFlow<Character?>(null)
    val character: StateFlow<Character?> = _character.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { loadCharacter() }

    private fun loadCharacter() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getCharacterById(characterId)) {
                is Result.Success -> _character.value = result.data
                is Result.Error -> _error.value = result.message
            }
            _isLoading.value = false
        }
    }
}

class DetailViewModelFactory(private val characterId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(characterId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
