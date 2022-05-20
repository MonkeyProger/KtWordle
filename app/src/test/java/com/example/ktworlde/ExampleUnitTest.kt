package com.example.ktworlde

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest: AbstractFunctionTest() {

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
}