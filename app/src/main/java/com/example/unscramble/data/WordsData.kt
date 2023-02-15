package com.example.unscramble.data

const val MAX_NO_OF_WORDS = 10
const val SCORE_INCREASE = 20

// Set with all the words for the Game
val allWords: Set<String> =
    setOf(
        "animal",
        "auto",
        "anecdote",
        "alphabet",
        "all",
        "awesome",
        "arise",
        "balloon",
        "basket",
        "bench",
        // ...
        "zoology",
        "zone",
        "zeal"
    )

 val sortCharString: (String) -> String = {
    it.toCharArray().sorted().joinToString("")
}
private val sortedCharWordLengthMap = allWords.groupBy({ it.length }, {
    sortCharString(it) to it
}).map { (key, values) -> key to values.toMap() }.toMap()

val getAll: () -> Unit = {println(sortedCharWordLengthMap)}

internal fun getUnscrambledWord(scrambledWord: String) = sortedCharWordLengthMap[scrambledWord.length]?.get(sortCharString(scrambledWord))
    ?: ""