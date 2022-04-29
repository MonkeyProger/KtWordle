package com.example.ktworlde

import java.io.InputStream
import java.io.InputStreamReader
import kotlin.math.log
import kotlin.random.Random

class WordAnalyzer(inputStream: InputStream, inputStream1: InputStream) {
    private val words: List<String> = InputStreamReader(inputStream).readLines()
    private val answers: List<String> = InputStreamReader(inputStream1).readLines()
    private var matchList: List<String> = answers
    var key = "ответ"
    private var guess = IntArray(5)
    private var curWord = ""

    fun clearAnalyzer(){
        guess = IntArray(5)
        matchList = answers
        key = answers[Random.nextInt(answers.size)]
    }

    fun contains(word: String): Boolean = words.contains(word)

    fun analyzeWord(word: String): IntArray {
        val letterMap = createLetterMap(key).toMutableMap()
        curWord = word
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
            guess = intArr
        }
        else guess = IntArray(5){2}
        return guess
    }

    fun analyzeEntropy(word: String): Array<Pair<String, Double>> {
        val top = Array(5){"" to 0.0}.toMutableList()
        matchList = buildMatches(word,guess)
        if (matchList.size == 1) {
            top.add(0,matchList.firstOrNull()!! to 100.0)
            top.removeAt(5)
        } else
        for (i in matchList){
            val curE = getE(i)
            for (j in 0..4){
                if (curE >= top[j].second){
                    top.add(j, i to curE)
                    top.removeAt(5)
                    break
                }
            }
        }
        return top.toTypedArray()
    }

    //  for building a new matchDeque
    private fun buildMatches(word: String, comb: IntArray):List<String>{
        val deque = ArrayDeque<String>()
        val contained = mutableMapOf<Char,Int>()
        for (h in 0..4){
            if (comb[h] == 1)
                if (contained.containsKey(word[h])) contained[word[h]] = contained[word[h]]!! + 1
                else contained[word[h]] = 1
        }
        for (i in matchList){
            val letters = createLetterMap(i).toMutableMap()
            val cont = contained
            var flag = true
            if (i == word) continue
            for (j in 0..4)
                if (comb[j] == 2)
                    if (i[j] != word[j]) {
                        flag = false
                        break
                    } else letters[i[j]] = letters[i[j]]!! -1
            if (!flag) continue
            for (j in cont)
                for (h in 1..j.value)
                    if (letters.containsKey(j.key) && letters[j.key] != 0)
                        letters[j.key] = letters[j.key]!! -1 else {
                        flag = false
                        break
                    }
            if (!flag) continue
            for (j in 0..4)
                if (comb[j]==0)
                    if (letters.containsKey(word[j]) && letters[word[j]]!=0){
                        flag = false
                        break
                    }
            if (flag) deque.addFirst(i)
        }
        return deque.toList()
    }

    private fun buildPos(word: String, comb: IntArray): Double{
        var res = 0.0
        val contained = mutableMapOf<Char,Int>()
        for (h in 0..4){
            if (comb[h] == 1)
                if (contained.containsKey(word[h])) contained[word[h]] = contained[word[h]]!! + 1
                else contained[word[h]] = 1
        }
        for (i in matchList){
            val letters = createLetterMap(i).toMutableMap()
            val cont = contained
            var flag = true
            if (i == word) continue
            for (j in 0..4)
                if (comb[j] == 2)
                    if (i[j] != word[j]) {
                        flag = false
                        break
                    } else letters[i[j]] = letters[i[j]]!! -1
            if (!flag) continue
            for (j in cont)
                for (h in 1..j.value)
                    if (letters.containsKey(j.key) && letters[j.key] != 0)
                        letters[j.key] = letters[j.key]!! -1 else {
                        flag = false
                        break
                    }
            if (!flag) continue
            for (j in 0..4)
                if (comb[j]==0)
                    if (letters.containsKey(word[j]) && letters[word[j]]!=0){
                        flag = false
                        break
                    }
            if (flag) res++
        }
        return res
    }

    //  getting entropy of certain word in matchList
    private fun getE(word: String): Double{
        var entropy = 0.0
        val illegalVal = arrayOf(242,241,239,233,215,161)
        for (i in 0 until 243) {
            if (illegalVal.contains(i)) continue
            val pos = buildPos(word, conv3(i)) / matchList.size
            if (pos != 0.0)
            entropy += pos*(log(1/pos,2.0))
        }
        return entropy
    }

    //  getting combination from number (converting a decimal number to a ternary number)
    private fun conv3(num: Int): IntArray {
        var r = num
        val q = IntArray(5){0}
        var digit = 4
        while (r>0){
            q[digit] = (r%3)
            r /= 3
            digit--
        }
        return q
    }

    private fun createLetterMap(word: String): Map<Char,Int>{
        val res = mutableMapOf<Char,Int>()
        for (i in word) {
            if (res.containsKey(i)) res[i] = res[i]!! + 1
            else res[i] = 1
        }
        return res
    }
}