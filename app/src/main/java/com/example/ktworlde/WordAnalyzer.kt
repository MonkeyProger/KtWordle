package com.example.ktworlde

import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.log
import kotlin.random.Random

class WordAnalyzer(inputStream: InputStream, inputStream1: InputStream) {
    private val words: List<String> = InputStreamReader(inputStream).readLines()
    private val answers: List<String> = InputStreamReader(inputStream1).readLines()
    private val answersSet: LinkedList<String> = LinkedList(answers)
    private var matchSet: LinkedList<String> = answersSet
    private val wordsMap = mutableMapOf<String,MutableMap<Char,Int>>()
    var key = "ответ"
    private var guess = IntArray(5)
    private var curWord = ""

    init{
        for (i in matchSet){
            wordsMap[i]=createLetterMap(i)
        }
    }

    fun clearAnalyzer(){
        guess = IntArray(5)
        matchSet = answersSet
        key = answers[Random.nextInt(answers.size)]
    }

    fun contains(word: String): Boolean = words.contains(word)


    // * Функция сравнивает слово с ответом и составляет комбинацию(guess:IntArray(5)), которая
    //   отражает положение и наличие букв в введенном слове по отношению к ответу:
    // * Значение 2 соответствует положению буквы на своем месте
    //   1 - наличию буквы в слове, но не на своем месте
    //   0 - отсутствие буквы в слове
    // * Например {2,0,1,0,0}
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

    // * Функция возвращает топ 5 слов дающих наибольшую энтропию после введения слова
    fun analyzeEntropy(word: String): Array<Pair<String, Double>> {
        val top = Array(5){"" to 0.0}.toMutableList() // Конечный топ
        matchSet = buildMatches(word,guess) // Вызов функции фильтрации списка (см. ниже)

        if (matchSet.size == 1) { // Если подходит одно слово сразу оно и выводится
            top.add(0,matchSet.firstOrNull()!! to 100.0)
            top.removeAt(5)
        } else
        // Проход по всем словам в отфильтрованном списке и составление энтропии для каждого,
        // (функция getE() по слову получает энтропию см.ниже)
        // по результату составляется топ слов с наибольшей энтропией
        for (i in matchSet){
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

    // * Функция составляет топ 5 слов дающих наибольшую энтропию до момента введения слова
    fun analyzeFullEntropy(): Array<Pair<String, Double>>{
        val top = Array(5){"" to 0.0}.toMutableList()
        for (i in matchSet){
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

    // * Функция для фильтрации списка по конкретной к слову
    private fun buildMatches(word: String, comb: IntArray): LinkedList<String> {
        val set = LinkedList<String>() // Искомый отфильтрованный список
        // * Map содержащий буквы, которые имеются, но не на месте, и буквы не содержащиеся в слове
        //   Если буква содержится в слове, но стоит не на месте, то contained[буква] += 1
        //   Если буква отсутствует в слове, то contained[буква] = 0
        val contained = mutableMapOf<Char,Int>()
        val notContained = mutableMapOf<Char,Int>()
        contained.putAll(wordsMap[word]!!)
        notContained.putAll(contained)
        for (h in 0..4){
            when(comb[h]){
                2 -> notContained[word[h]] = notContained[word[h]]!! - 1
                1 -> notContained[word[h]] = notContained[word[h]]!! - 1
                0 -> contained[word[h]] = contained[word[h]]!! - 1
            }
        }

        // Проход по всем элементам списка и сравнение элемента на вероятное слово
        for (i in matchSet) {
            if (i == word) continue
            val letters = wordsMap[i]// см. функцию createLetterMap() ниже
            var flag = true

            // Проход по текущему слову и проверка места буквы, т.е проверка значений 2 и 1
            // Пример: ОТЧЁТ имеет комбинацию ЗЖССЖ
            // ООТТО подходит | ОКТЕТ не подходит
            for (j in 0..4)
                when (comb[j]){
                    2 -> if (i[j] != word[j]) {
                        flag = false
                        break
                        }
                    1 -> if (i[j] == word[j]) {
                        flag = false
                        break
                        }
                    0 -> if (i[j] == word[j]) {
                        flag = false
                        break
                        }
                    }
            if (!flag) continue

            for (j in contained) {
                if (letters!!.containsKey(j.key)){
                    if (j.value == 0){
                        flag = false
                        break
                    }
                    val notC = notContained[j.key]!!
                    val letter = letters[j.key]!!
                    if (notC == 0 && letter >= j.value) continue else
                        if (notC != 0 && (letter - j.value) < notC) continue else {
                            flag = false
                            break
                        }
                } else if (j.value > 0){
                    flag = false
                    break
                }
            }
            if (!flag) continue
            // Проход по текущему слову и проверка наличия буквы, но не на своем месте, и проверка
            // на отсутствие буквы, т.е проверка значений 0, 1
//            for (j in contained) {
//                if (!flag) break
//                if (j.value == 0 && letters.containsKey(j.key) && letters[j.key] != 0) {
//                    flag = false
//                    break
//                }
//                // Если рассматриваемая буква есть в слове в нескольких экземплярах, проверяются
//                // все экземпляры
//                for (h in 1..j.value)
//                    if (letters.containsKey(j.key) && letters[j.key] != 0)
//                        letters[j.key] = letters[j.key]!! - 1
//                    else {
//                        flag = false
//                        break
//                    }
//            }
            set.add(i)
        }
        return set
    }

    private fun buildPos(word: String, comb: IntArray): Double{
        var res = 0.0
        val contained = mutableMapOf<Char,Int>()
        val notContained = mutableMapOf<Char,Int>()
        contained.putAll(wordsMap[word]!!)
        notContained.putAll(contained)
        for (h in 0..4){
            when(comb[h]){
                2 -> notContained[word[h]] = notContained[word[h]]!! - 1
                1 -> notContained[word[h]] = notContained[word[h]]!! - 1
                0 -> contained[word[h]] = contained[word[h]]!! - 1
            }
        }

        // Проход по всем элементам списка и сравнение элемента на вероятное слово
        for (i in matchSet) {
            if (i == word) continue
            val letters = wordsMap[i]// см. функцию createLetterMap() ниже
            var flag = true

            // Проход по текущему слову и проверка места буквы, т.е проверка значений 2 и 1
            // Пример: ОТЧЁТ имеет комбинацию ЗЖССЖ
            // ООТТО подходит | ОКТЕТ не подходит
            for (j in 0..4)
                when (comb[j]){
                    2 -> if (i[j] != word[j]) {
                        flag = false
                        break
                    }
                    1 -> if (i[j] == word[j]) {
                        flag = false
                        break
                    }
                    0 -> if (i[j] == word[j]) {
                        flag = false
                        break
                    }
                }
            if (!flag) continue

            for (j in contained) {
                if (letters!!.containsKey(j.key)){
                    if (j.value == 0){
                        flag = false
                        break
                    }
                    val notC = notContained[j.key]!!
                    val letter = letters[j.key]!!
                    if (notC == 0 && letter >= j.value) continue else
                        if (notC != 0 && (letter - j.value) < notC) continue else {
                            flag = false
                            break
                        }
                } else if (j.value > 0){
                    flag = false
                    break
                }
            }
            if (!flag) continue
//            for (j in contained) {
//                if (!flag) break
//                if (j.value == 0 && letters.containsKey(j.key) && letters[j.key] != 0) {
//                    flag = false
//                    break
//                }
//                for (h in 1..j.value)
//                    if (letters.containsKey(j.key) && letters[j.key] != 0)
//                        letters[j.key] = letters[j.key]!! - 1
//                    else {
//                        flag = false
//                        break
//                    }
//            }
            res++
        }
        return res
    }

    // * Получение энтропии для слова по списку
    // * Для нахождения энтропии для слова необходимо проверить каждую возможную комбинацию из игры
    // * Исходя из того, что некоторые комбинации по сути игры невозможны, и проверять их не имеет
    //   смысла, запрещенные комбинации введены в массив illegalVal, комбинации для удобства
    //   представленны в виде 10ных чисел, каждое из которых переводится функцией conv3(см. ниже)
    // * Комбинации: 22222 22221 22212 22122 21222 12222
    private fun getE(word: String): Double{
        var entropy = 0.0
        val illegalVal = arrayOf(242,241,239,233,215,161)
        for (i in 0 until 243) {
            if (illegalVal.contains(i)) continue
            val pos = buildPos(word, conv3(i)) / matchSet.size
            if (pos != 0.0)
            entropy += pos*(log(1/pos,2.0))
        }
        return entropy
    }

    // * Получении комбинации из числа (перевод десятичного числа в троичное)
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

    // * Функция возвращает map с информацией о количество содержимых букв в слове
    // * Например: "аббат" и "окунь" представляются соответственно в виде:
    //              a - 2     о - 1
    //              б - 2     к - 1
    //              т - 1     у - 1
    //                        н - 1
    //                        ь - 1
    private fun createLetterMap(word: String): MutableMap<Char,Int>{
        val res = mutableMapOf<Char,Int>()
        for (i in word) {
            if (res.containsKey(i)) res[i] = res[i]!! + 1
            else res[i] = 1
        }
        return res
    }
}