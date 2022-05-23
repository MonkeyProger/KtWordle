package com.example.ktworlde

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
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
}