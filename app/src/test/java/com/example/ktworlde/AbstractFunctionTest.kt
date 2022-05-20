package com.example.ktworlde
import com.example.ktworlde.model.WordAnalyzer
import junit.framework.Assert.assertTrue
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import java.io.FileInputStream


abstract class AbstractFunctionTest {
    val words = FileInputStream("D:\\KtWorlde\\app\\src\\main\\assets\\singular.txt")
    val probWords = FileInputStream("D:\\KtWorlde\\app\\src\\main\\assets\\res.txt")
    val analyzer = WordAnalyzer(words,probWords)

    // Проверка корректности вывода комбинации заданного слова при определенном ключевом слове
    fun analyzeWordTest(){
        analyzer.key = "АБВГД"
        assertArrayEquals(intArrayOf(2,2,2,2,2),analyzer.analyzeWord("АБВГД"))
        assertArrayEquals(intArrayOf(1,1,1,1,1),analyzer.analyzeWord("ДГАБВ"))
        assertArrayEquals(intArrayOf(0,0,0,0,0),analyzer.analyzeWord("ООООО"))

        analyzer.key = "АБАВВ"
        assertArrayEquals(intArrayOf(1,1,2,0,0),analyzer.analyzeWord("ВАААА"))
        assertArrayEquals(intArrayOf(2,2,2,0,0),analyzer.analyzeWord("АБААБ"))
        assertArrayEquals(intArrayOf(0,0,1,1,1),analyzer.analyzeWord("ГГБАА"))

        analyzer.key = "БАВАГ"
        assertArrayEquals(intArrayOf(1,2,1,2,1),analyzer.analyzeWord("ВАГАБ"))
        assertArrayEquals(intArrayOf(0,2,0,2,0),analyzer.analyzeWord("ОАОАО"))
        assertArrayEquals(intArrayOf(1,2,1,0,1),analyzer.analyzeWord("ВАБОА"))
        analyzer.key = "ОАВВО"
        assertArrayEquals(intArrayOf(1,0,2,2,0),analyzer.analyzeWord("АВВВА"))
        assertArrayEquals(intArrayOf(1,1,2,2,2),analyzer.analyzeWord("АОВВО"))
        assertArrayEquals(intArrayOf(1,1,0,2,2),analyzer.analyzeWord("АООВО"))
    }

    // Проверка того, подходит ли заданое слово введённому при определенной комбинации
    fun validationTest(){
        var comb = intArrayOf(0,0,0,0,0)
        val cont = mutableMapOf<Char,Int>()
        val notCont = mutableMapOf<Char,Int>()
        var word = "октет" // Введенное слово
        var i = "октет"    // Предположительное слово
        cont.putAll(analyzer.wordsMap[word]!!)
        notCont.putAll(cont)
        analyzer.buildContainers(comb,word,cont,notCont)
        assertFalse(analyzer.isValid(word,comb,cont,notCont,i))

        comb = intArrayOf(0,1,1,0,1)
        cont.clear()
        notCont.clear()
        word = "ветка"
        i = "накат"
        cont.putAll(analyzer.wordsMap[word]!!)
        notCont.putAll(cont)
        analyzer.buildContainers(comb,word,cont,notCont)
        assertFalse(analyzer.isValid(word,comb,cont,notCont,i))

        comb = intArrayOf(0,0,1,0,1)
        cont.clear()
        notCont.clear()
        word = "октет"
        i = "тиара"
        cont.putAll(analyzer.wordsMap[word]!!)
        notCont.putAll(cont)
        analyzer.buildContainers(comb,word,cont,notCont)
        assertFalse(analyzer.isValid(word,comb,cont,notCont,i))

        comb = intArrayOf(0,2,0,0,0)
        cont.clear()
        notCont.clear()
        word = "кокор"
        i = "посад"
        cont.putAll(analyzer.wordsMap[word]!!)
        notCont.putAll(cont)
        analyzer.buildContainers(comb,word,cont,notCont)
        assertTrue(analyzer.isValid(word,comb,cont,notCont,i))

        comb = intArrayOf(0,2,0,0,0)
        cont.clear()
        notCont.clear()
        word = "кокор"
        i = "посол"
        cont.putAll(analyzer.wordsMap[word]!!)
        notCont.putAll(cont)
        analyzer.buildContainers(comb,word,cont,notCont)
        assertFalse(analyzer.isValid(word,comb,cont,notCont,i))

        comb = intArrayOf(1,1,1,0,1)
        cont.clear()
        notCont.clear()
        word = "лассо"
        i = "оскал"
        cont.putAll(analyzer.wordsMap[word]!!)
        notCont.putAll(cont)
        analyzer.buildContainers(comb,word,cont,notCont)
        assertTrue(analyzer.isValid(word,comb,cont,notCont,i))

        comb = intArrayOf(1,0,0,1,0)
        cont.clear()
        notCont.clear()
        word = "атака"
        i = "капкан"
        cont.putAll(analyzer.wordsMap[word]!!)
        notCont.putAll(cont)
        analyzer.buildContainers(comb,word,cont,notCont)
        assertFalse(analyzer.isValid(word,comb,cont,notCont,i))

        comb = intArrayOf(0,0,0,1,2)
        cont.clear()
        notCont.clear()
        word = "атака"
        i = "кукла"
        cont.putAll(analyzer.wordsMap[word]!!)
        notCont.putAll(cont)
        analyzer.buildContainers(comb,word,cont,notCont)
        assertTrue(analyzer.isValid(word,comb,cont,notCont,i))
    }
}