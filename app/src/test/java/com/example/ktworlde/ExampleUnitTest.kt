package com.example.ktworlde

import org.junit.Test

class ExampleUnitTest: AbstractFunctionTest() {

    // ===================================Тесты функций=============================================
    @Test
    fun computingCombTest() {
        // Проверка корректности вывода комбинации заданного слова при определенном ключевом слове
        analyzeWordTest()
    }

    @Test
    fun checkingWordTest(){
        // Проверка того, подходит ли заданое слово введённому при определенной комбинации
        validationTest()
    }


    // ======================Тесты времени выполнения затратных функций=============================
    // P.S. Не означают, что на android будут выполняться столько же

    @Test
    fun buildPos1Runtime(){
        // Тест времени выполнения функции buildPos() для
        // "ХУДШЕГО" случая |С|С|С|С|С| ЛУЧШЕГО слова-догадки (атака)
        buildPosTimeTest("атака")
    }

    @Test
    fun buildPos2Runtime(){
        // Тест времени выполнения функции buildPos() для
        // "ХУДШЕГО" случая |С|С|С|С|С| слова-догадки (ответ) : что-то среднее
        buildPosTimeTest("ответ")
    }

    @Test
    fun getEntropy1Runtime(){
        // Тест времени выполнения функции getE() для
        // ЛУЧШЕГО слова-догадки (атака)
        getEntropyTimeTest("атака")
    }

    @Test
    fun getEntropy2Runtime(){
        // Тест времени выполнения функции getE() для
        // слова (ответ) : что-то среднее
        getEntropyTimeTest("ответ")
    }

    @Test
    fun analyzerFullEntropyRuntime(){
        // Тест времени выполнения функции analyzeFullEntropy(), т.е получения
        // списка лучших слов ДО момента введения первого слова
        analyzeFullEntrTimeTest()
    }

    @Test
    fun iterationsTest(){
        val key = getRandomAnswerExcept("")
        val random = getRandomGuessExcept(key)
        println("Количество итераций для ответа '$key' при догадке 'олово':${iterationsOnGuessTest(key,"олово")}")
        println("Количество итераций для ответа '$key' при догадке '$random':${iterationsOnGuessTest(key,random)}")
    }

    @Test
    fun lotsOfIterationsTest(){
        var mean1 = 0
        var mean2 = 0
        val random = getRandomGuessExcept("олово")
        for (j in 0 until 80){
            val key = getRandomAnswerExcept(random)
            mean2+=iterationsOnGuessTest(key,random)
            mean1+=iterationsOnGuessTest(key,"олово")
        }
        println("Количество итераций в среднем для догадки 'олово':${mean1/80}")
        println("Количество итераций в среднем для случайно выбранной догадки '$random':${mean2/80}")
    }
}