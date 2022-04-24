package com.example.ktworlde

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.ActionBar
import java.util.ArrayDeque

class MainActivity : AppCompatActivity() {
    private var analyzer: WordAnalyzer? = null
    private var enter: Button? = null
    private var restart: ImageButton? = null
    private var counter = 1
    private var dequeIc = ArrayDeque<InputConnection>()
    private var dequeTb = ArrayDeque<Textbar>()
    private var kbd: Keyboard? = null
    private var winFlag = false

    private var editText1: EditText? = null
    private var editText2: EditText? = null
    private var editText3: EditText? = null
    private var editText4: EditText? = null
    private var editText5: EditText? = null
    private var tb1: Textbar? = null
    private var tb2: Textbar? = null
    private var tb3: Textbar? = null
    private var tb4: Textbar? = null
    private var tb5: Textbar? = null
    private var ic1: InputConnection? = null
    private var ic2: InputConnection? = null
    private var ic3: InputConnection? = null
    private var ic4: InputConnection? = null
    private var ic5: InputConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inputStream = assets.open("singular.txt")
        analyzer = WordAnalyzer(inputStream)
        // Hiding upper actionbar
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        // Setting editText and resText
        setContentView(R.layout.activity_main)
        editText1 = findViewById(R.id.editText1)
        editText2 = findViewById(R.id.editText2)
        editText3 = findViewById(R.id.editText3)
        editText4 = findViewById(R.id.editText4)
        editText5 = findViewById(R.id.editText5)
        tb1 = findViewById(R.id.textbar1)
        tb2 = findViewById(R.id.textbar2)
        tb3 = findViewById(R.id.textbar3)
        tb4 = findViewById(R.id.textbar4)
        tb5 = findViewById(R.id.textbar5)

        // Setting keyboard and buttons
        val keyboard = findViewById<Keyboard>(R.id.keyboard)
        enter = keyboard.button_ввод
        restart = findViewById(R.id.restart)

        // Disabling writing with systemKeyboard
        editText1!!.showSoftInputOnFocus = false
        editText2!!.showSoftInputOnFocus = false
        editText3!!.showSoftInputOnFocus = false
        editText4!!.showSoftInputOnFocus = false
        editText5!!.showSoftInputOnFocus = false

        // Final steps
        enter?.setOnClickListener(Click())
        restart?.setOnClickListener(Click())
        kbd = keyboard
        setupNew()
    }

    private fun setupNew(){
        winFlag = false
        dequeIc.clear()
        dequeTb.clear()
        analyzer!!.clearAnalyzer()
        counter = 1
        editText1!!.text.clear(); editText2!!.text.clear(); editText3!!.text.clear()
        editText4!!.text.clear(); editText5!!.text.clear();
        tb1!!.restart(); tb2!!.restart(); tb3!!.restart()
        tb4!!.restart(); tb5!!.restart()
        ic1 = editText1!!.onCreateInputConnection(EditorInfo())
        ic2 = editText2!!.onCreateInputConnection(EditorInfo())
        ic3 = editText3!!.onCreateInputConnection(EditorInfo())
        ic4 = editText4!!.onCreateInputConnection(EditorInfo())
        ic5 = editText5!!.onCreateInputConnection(EditorInfo())
        dequeIc.push(ic5); dequeIc.push(ic4); dequeIc.push(ic3)
        dequeIc.push(ic2); dequeIc.push(ic1)
        dequeTb.push(tb5); dequeTb.push(tb4); dequeTb.push(tb3)
        dequeTb.push(tb2); dequeTb.push(tb1)
        kbd!!.setInputConnection(ic1,ic2,ic3,ic4,ic5)
    }

    inner class Click: View.OnClickListener{
        override fun onClick(view: View?) {
            if (view?.id == R.id.button_ввод){
                if (dequeIc.isEmpty()) return
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
                        winFlag = true
                        kbd!!.clearInputConnection()
                    } else {
                        // NextStep
                    }
                    kbd!!.enterHandler()
                }
                if (dequeIc.isEmpty()) {
                    //Lose
                    kbd!!.clearInputConnection()
                    return
                }
            }
            if (view?.id == R.id.restart)
                if (dequeIc.isEmpty() || winFlag) setupNew()
        }
    }

}