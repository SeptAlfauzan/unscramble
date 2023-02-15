package com.example.unscramble.ui.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import com.example.unscramble.ui.model.GameUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    var userGuess by mutableStateOf("")

    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf<String>()

    private fun shuffleCurrentWord(word: String): String {

        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    private fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random()
        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    fun checkUserGuess(){
//        Log.d("viewmodel", "checkUserGuess: $userGuess $currentWord $usedWords")

        if(userGuess.equals(currentWord, ignoreCase = true)){
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        }else{
            _uiState.update { currentState -> currentState.copy(isGuessedWordWrong = true) }
        }
        updateUserGuess("")
    }

    fun updateGameState(updatedScore: Int){

        if(usedWords.size == MAX_NO_OF_WORDS){
            _uiState.update { currentState -> currentState.copy(
                score = updatedScore,
                isGuessedWordWrong = false,
                isGameOver = true,
            ) }
        }else{
            _uiState.update { currentState -> currentState.copy(
                isGuessedWordWrong = false,
                currentScrambledWord = pickRandomWordAndShuffle(),
                score = updatedScore,

                currentWordCount = currentState.currentWordCount.inc()
            ) }
        }
    }

    fun skip(){
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }


    init {
        resetGame()
    }
}


