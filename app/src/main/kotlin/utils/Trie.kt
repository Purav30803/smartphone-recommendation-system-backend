package utils

import repository.SmartphoneRepository

class TrieNode {
    val children = mutableMapOf<Char, TrieNode>()
    var isEndOfWord = false
}

class Trie {
    private val root = TrieNode()

    /**
     * Insert a single word into the Trie.
     */
    fun insert(word: String) {
        var node = root
        for (char in word) {
            node = node.children.computeIfAbsent(char) { TrieNode() }
        }
        node.isEndOfWord = true
    }

    /**
     * Insert a list of words into the Trie.
     */
    fun insertAll(words: List<String>) {
        words.forEach { insert(it) }
    }

    /**
     * Search for words starting with a given prefix.
     */
    fun searchPrefix(prefix: String): List<String> {
        var node = root
        for (char in prefix) {
            node = node.children[char] ?: return emptyList()
        }
        return collectAllWords(node, prefix)
    }

    /**
     * Collect all words from the Trie that start with a given node.
     */
    private fun collectAllWords(node: TrieNode, prefix: String): List<String> {
        val words = mutableListOf<String>()
        if (node.isEndOfWord) words.add(prefix)
        for ((char, childNode) in node.children) {
            words.addAll(collectAllWords(childNode, prefix + char))
        }
        return words
    }

    /**
     * Populate the Trie with data from the database.
     */
    fun populateFromDatabase(repository: SmartphoneRepository) {
        val allSmartphones = repository.getAllSmartphones()
        println(allSmartphones)
        val keywords = allSmartphones.flatMap {
            listOf(it.brand.lowercase(), it.model.lowercase())
        }.distinct()
        println("Populating Trie with keywords: $keywords") // Debugging
        insertAll(keywords)
    }

}
