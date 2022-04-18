package com.example.ktworlde

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import java.util.ArrayDeque

class MainActivity : AppCompatActivity() {
    private var analyzer: WordAnalyzer? = null
    private var enter: Button? = null
    private var counter = 1
    private var dequeIc = ArrayDeque<InputConnection>()
    private var dequeTb = ArrayDeque<Textbar>()
    private var kbd: Keyboard? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()
        setContentView(R.layout.activity_main)
        val editText1 = findViewById<EditText>(R.id.editText1)
        val editText2 = findViewById<EditText>(R.id.editText2)
        val editText3 = findViewById<EditText>(R.id.editText3)
        val editText4 = findViewById<EditText>(R.id.editText4)
        val editText5 = findViewById<EditText>(R.id.editText5)
        val tb1 = findViewById<Textbar>(R.id.textbar1)
        val tb2 = findViewById<Textbar>(R.id.textbar2)
        val tb3 = findViewById<Textbar>(R.id.textbar3)
        val tb4 = findViewById<Textbar>(R.id.textbar4)
        val tb5 = findViewById<Textbar>(R.id.textbar5)
        dequeTb.push(tb5); dequeTb.push(tb4); dequeTb.push(tb3)
        dequeTb.push(tb2); dequeTb.push(tb1)

        val keyboard = findViewById<Keyboard>(R.id.keyboard)
        enter = keyboard.button_ввод


        editText1.showSoftInputOnFocus = false
        editText2.showSoftInputOnFocus = false
        editText3.showSoftInputOnFocus = false
        editText4.showSoftInputOnFocus = false
        editText5.showSoftInputOnFocus = false

        val ic1 = editText1.onCreateInputConnection(EditorInfo())
        val ic2 = editText2.onCreateInputConnection(EditorInfo())
        val ic3 = editText3.onCreateInputConnection(EditorInfo())
        val ic4 = editText4.onCreateInputConnection(EditorInfo())
        val ic5 = editText5.onCreateInputConnection(EditorInfo())
        dequeIc.push(ic5); dequeIc.push(ic4); dequeIc.push(ic3)
        dequeIc.push(ic2); dequeIc.push(ic1)

        analyzer = WordAnalyzer()
        enter?.setOnClickListener(Click())
        keyboard.setInputConnection(ic1,ic2,ic3,ic4,ic5)
        kbd = keyboard

    }

    inner class Click: View.OnClickListener{
        override fun onClick(view: View?) {
            if (view?.id == R.id.button_ввод){
                if (dequeIc.isEmpty()) {
                    //Lose
                    return
                }
                if (dequeIc.first!!.getTextBeforeCursor(5,0)!!.length == 5) {
                    val word = dequeIc.first!!.getTextBeforeCursor(5,0).toString().lowercase()
                    val arr: IntArray = analyzer!!.analyzeWord(word,counter)
                    counter++
                    dequeIc.pop()
                    dequeTb.first.showResult(arr,word)
                    dequeTb.first.visibility = View.VISIBLE
                    dequeTb.pop()
                    if (arr.contentEquals(IntArray(5){2})) {
                        // Win
                        // GreenScreen
                    } else {
                        // NextStep
                    }
                    kbd!!.enterHandler()
                }
            }
        }
    }

}