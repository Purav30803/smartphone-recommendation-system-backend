package utils

object SpellChecker {
    fun findClosestWord(input: String, wordList: List<String>): String {
        return wordList.minByOrNull { editDistance(input, it) } ?: input
    }

    private fun editDistance(word1: String, word2: String): Int {
        val dp = Array(word1.length + 1) { IntArray(word2.length + 1) }
        for (i in dp.indices) dp[i][0] = i
        for (j in dp[0].indices) dp[0][j] = j

        for (i in 1..word1.length) {
            for (j in 1..word2.length) {
                dp[i][j] = if (word1[i - 1] == word2[j - 1]) {
                    dp[i - 1][j - 1]
                } else {
                    1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
                }
            }
        }
        return dp[word1.length][word2.length]
    }
}
