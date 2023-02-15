package com.example.unscramble

import androidx.compose.runtime.currentRecomposeScope
import com.example.unscramble.data.*
import com.example.unscramble.ui.viewModel.GameViewModel
import org.junit.Test
import org.junit.Assert.*


class GameViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdateAndErrorFlagUnset(){
        var currentGameUiState = viewModel.uiState.value

        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(CORRECT_SCORE, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet(){
        val incorrectAnswer = "and"

        viewModel.updateUserGuess(incorrectAnswer)
        viewModel.checkUserGuess()
        val currentGameUiState = viewModel.uiState.value
        // Assert that score is unchanged
        assertEquals(0, currentGameUiState.score)
        // Assert that checkUserGuess() method updates isGuessedWordWrong correctly
        assertTrue(currentGameUiState.isGuessedWordWrong)
    }

    //boundary case
    @Test
    fun gameViewModel_Initialization_FirstWordLoaded(){
        val gameUiState = viewModel.uiState.value
        val unScrambleWord = getUnscrambledWord(gameUiState.currentScrambledWord)

        assertNotEquals(unScrambleWord, gameUiState.currentScrambledWord)

        assertTrue(gameUiState.currentWordCount == 1)
        assertTrue(gameUiState.score == 0)
        assertFalse(gameUiState.isGuessedWordWrong)
        assertFalse(gameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly(){
        var expectedScore = 0
        var currentGameUiState = viewModel.uiState.value

        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        repeat(MAX_NO_OF_WORDS){
            expectedScore += SCORE_INCREASE

            println("$correctPlayerWord ${currentGameUiState.currentScrambledWord}")
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
    //        getALl()

            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)


            assertEquals(expectedScore, currentGameUiState.score)
        }
        // Assert that after all questions are answered, the current word count is up-to-date.
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)
        // Assert that after 10 questions are answered, the game is over.
        assertTrue(currentGameUiState.isGameOver)
    }

    companion object{
        private const val CORRECT_SCORE = SCORE_INCREASE
    }
    @Test
    fun getAllWord(){
        println(sortCharString("kontol"))
        getAll()
    }
}