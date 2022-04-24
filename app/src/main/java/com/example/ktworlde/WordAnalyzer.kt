package com.example.ktworlde

import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.random.Random

class WordAnalyzer(inputStream: InputStream) {
    private val words: List<String> = InputStreamReader(inputStream).readLines()
    private var key = "ответ"
    private var map = mutableMapOf<Int,IntArray>()

    fun clearAnalyzer(){
        map.clear()
        key = words[Random.nextInt(words.size)]
    }

    fun analyzeWord(word: String, num: Int): IntArray {
        val letterMap = createLetterMap().toMutableMap()
        if (word != key) {
            val intArr = IntArray(5)
            for (i in 0..4){
                if (word[i] == key[i]) {
                    intArr[i] = 2
                    letterMap[word[i]] = letterMap[word[i]]!! - 1
                }
            }
            for (i in 0..4) {
                if (intArr[i] != 0) continue
                else if (letterMap.containsKey(word[i]) && letterMap[word[i]] != 0) {
                    intArr[i] = 1
                    letterMap[word[i]] = letterMap[word[i]]!! - 1
                }
                else intArr[i] = 0
            }
            map[num] = intArr
        }
        else map[num] = IntArray(5){2}
        return map[num]!!
    }

    private fun createLetterMap(): Map<Char,Int>{
        val res = mutableMapOf<Char,Int>()
        for (i in key) {
            if (res.containsKey(i)) res[i] = res[i]!! + 1
            else res[i] = 1
        }
        return res
    }
}