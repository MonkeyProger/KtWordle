package com.example.ktworlde.model

import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.math.log
import kotlin.random.Random
/*
    * Класс в котором реализованы функции сравнения слов, их фильтрации, нахождения энтропии,
      составления списка 5 слов, имеющих наибольшую энтропию.
    * Порядок вызова функций при вводе игроком очередного слова:
           0                  1                  1.1            2          2.1
      analyzeWord() ||| analyzeEntropy() -> buildMatches() -> getE() -> buildPos()

      0) Сравнивая введенное слово с ответом, получает комбинацию догадки.

      1) Фильтрует список слов по введенному слову и его комбинации, составляет топ 5 слов
         1.1) Выполняет фильтрацию списка

      2) Получает энтропию по всем ВОЗМОЖНЫМ в игре комбинациям для слова
         2.1) Возвращает количество подошедших слов под конкретную комбинацию для слова
 */

class WordAnalyzer(inputStream: InputStream, inputStream1: InputStream) {
    private val words: List<String> = InputStreamReader(inputStream).readLines()
    private val answers: List<String> = InputStreamReader(inputStream1).readLines()
    private val answersSet: LinkedList<String> = LinkedList(answers)
    private var matchList: LinkedList<String> = answersSet
    public val wordsMap = mutableMapOf<String,MutableMap<Char,Int>>()
    var key = "ответ"
    private var guess = IntArray(5)
    private var curWord = ""

    init{
        // Составляю для каждого слова Map, содержащий информацию о кол-ве букв в слове
        for (i in words){
            wordsMap[i]=createLetterMap(i)
        }
    }
    fun clearAnalyzer(){
        guess = IntArray(5)
        matchList = answersSet
        key = answers[Random.nextInt(answers.size)]
    }
    fun contains(word: String): Boolean = words.contains(word)


// -------------------------------------------------------------------------------------------------
// |                                    ОСНОВНЫЕ ФУНКЦИИ                                           |
// -------------------------------------------------------------------------------------------------


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


    // * Функция возвращает топ 5 слов дающих наибольшую энтропию ПОСЛЕ введения слова.
    //   Вызывает функции buildMatches() для фильтрации по слову и его комбинации, затем getE (см.ниже)
    fun analyzeEntropy(word: String): Array<Pair<String, Double>> {
        val top = Array(5){"" to 0.0}.toMutableList() // Конечный топ

        matchList = buildMatches(word,guess) // Вызов функции фильтрации списка (см. ниже)

        if (matchList.size == 1) { // Если подходит одно слово сразу оно и выводится
            top.add(0,matchList.firstOrNull()!! to 100.0)
            top.removeAt(5)
        } else
        /* Проход по всем словам в отфильтрованном списке и составление энтропии для каждого,
           (функция getE() по слову получает энтропию см.ниже) */
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


    // * Функция составляет топ 5 слов дающих наибольшую энтропию ДО момента введения первого слова.
    //   Вызывает функцию getE (см.ниже)
    fun analyzeFullEntropy(): Array<Pair<String, Double>>{
        val top = Array(5){"" to 0.0}.toMutableList()
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


    /*
        * Получение энтропии для слова по списку
        * Для нахождения энтропии для слова необходимо проверить каждую возможную комбинацию из игры
        * Исходя из того, что некоторые комбинации по смыслу игры невозможны, и проверять их не имеет
          смысла, такие запрещенные комбинации введены в массив illegalVal, комбинации для удобства
          представленны в виде 10ных чисел, каждое из которых переводится функцией conv3(см. ниже)
        * Комбинации соответственно: 22222 22221 22212 22122 21222 12222
    */
    public fun getE(word: String): Double{
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


    // * Функция для фильтрации списка по конкретной комбинации к слову
    //   Для сравнения очередного слова с введенным используется isValid() см. ниже
    // * Возвращает отфильтрованный по вероятным словам LinkedList
    private fun buildMatches(word: String, comb: IntArray): LinkedList<String> {
        val list = LinkedList<String>() // Искомый отфильтрованный список
        /*
        contained содержит информацию о кол-ве имеющихся букв в слове в сравнении с ключевым словом (Зелёные, Желтые)
        notContained содержит информацию о буквах, имеющихся в текущем, но не в ключевом слове (Серые)
        Логика распределения представлена в функции buildContainers см. ниже
         */
        val contained = mutableMapOf<Char,Int>()
        val notContained = mutableMapOf<Char,Int>()
        contained.putAll(wordsMap[word]!!)
        notContained.putAll(contained)
        buildContainers(comb,word,contained,notContained)

        // Проход по всем элементам списка и сравнение элемента на вероятное слово
        for (i in matchList)
            if (isValid(word,comb,contained,notContained,i)) list.add(i)
        return list
    }


    // * Нахождение числа подошедших слов к догадке word с комбинацией comb
    //   Для сравнения очередного слова с введенным используется isValid() см. ниже
    //   Требуется для нахождения вероятности в функции getE()
    //   Аналогичен buildMatches
    // * Возвращает число подошедших слов
    public fun buildPos(word: String, comb: IntArray): Double{
        var res = 0.0
        val contained = mutableMapOf<Char,Int>()
        val notContained = mutableMapOf<Char,Int>()
        contained.putAll(wordsMap[word]!!)
        notContained.putAll(contained)
        buildContainers(comb,word,contained,notContained)
        // Проход по всем элементам списка и сравнение элемента на вероятное слово
        for (i in matchList)
            if (isValid(word,comb,contained,notContained,i)) res++
        return res
    }


    /*
        Функция реализует сравнение текущего слова i с введенным предположенным словом word.
        Возвращает true, если слово подходит.
    */
    public fun isValid(
        word: String, comb: IntArray,
        contained: MutableMap<Char,Int>,
        notContained: MutableMap<Char,Int>,
        i: String
    ): Boolean{
        if (i == word) return false
        val letters = wordsMap[i]
        var flag = true

        // Проход по текущему слову i и проверка места буквы, т.е проверка значений 2 и 1
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
        if (!flag) return false

        // Проход по содержащимся в слове буквам и проверка их количества/отсутствия в текущем i
        // Пример: xTTTx имеет комбинацию xЖЗСx
        // ТxTxx подходит | ТxTxT не подходит
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
        if (!flag) return false
        return true
    }


    
// -------------------------------------------------------------------------------------------------
// |                                   ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ                                     |
// -------------------------------------------------------------------------------------------------

    // Получении комбинации из числа (перевод десятичного числа в троичное)
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


    // Составляет mutMaps содержащихся и не содержащихся букв в слове по комбинации
    public fun buildContainers(
        comb: IntArray, word: String,
        contained: MutableMap<Char,Int>,
        notContained: MutableMap<Char,Int>){
        for (h in 0..4){
            when(comb[h]){
                2 -> notContained[word[h]] = notContained[word[h]]!! - 1
                1 -> notContained[word[h]] = notContained[word[h]]!! - 1
                0 -> contained[word[h]] = contained[word[h]]!! - 1
            }
        }
    }


    /*
    * Функция возвращает map с информацией о количестве содержимых букв в слове
      Например: "аббат" и "окунь" представляются соответственно в виде:
                 a - 2     о - 1
                 б - 2     к - 1
                 т - 1     у - 1
                           н - 1
                           ь - 1
    */
    private fun createLetterMap(word: String): MutableMap<Char,Int>{
        val res = mutableMapOf<Char,Int>()
        for (i in word) {
            if (res.containsKey(i)) res[i] = res[i]!! + 1
            else res[i] = 1
        }
        return res
    }
}