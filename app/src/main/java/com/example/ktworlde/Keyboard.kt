package com.example.ktworlde

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout
import java.util.*

class Keyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener, View.OnTouchListener {
    private var button_й: Button? = null
    private var button_ц: Button? = null
    private var button_у: Button? = null
    private var button_к: Button? = null
    private var button_е: Button? = null
    private var button_н: Button? = null
    private var button_г: Button? = null
    private var button_ш: Button? = null
    private var button_щ: Button? = null
    private var button_з: Button? = null
    private var button_х: Button? = null
    private var button_ъ: Button? = null
    private var button_ф: Button? = null
    private var button_ы: Button? = null
    private var button_в: Button? = null
    private var button_а: Button? = null
    private var button_п: Button? = null
    private var button_р: Button? = null
    private var button_о: Button? = null
    private var button_л: Button? = null
    private var button_д: Button? = null
    private var button_ж: Button? = null
    private var button_э: Button? = null
    private var button_я: Button? = null
    private var button_ч: Button? = null
    private var button_с: Button? = null
    private var button_м: Button? = null
    private var button_и: Button? = null
    private var button_т: Button? = null
    private var button_ь: Button? = null
    private var button_б: Button? = null
    private var button_ю: Button? = null
    private var button_del: Button? = null
    var button_ввод: Button? = null
    private val keyValues = SparseArray<String>()
    private var inputConnection: InputConnection? = null
    private var deque = ArrayDeque<InputConnection>()

    init {
        init(context)
    }
    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.keyboard, this, true)
        button_й = findViewById<View>(R.id.button_й) as Button
        button_ц = findViewById<View>(R.id.button_ц) as Button
        button_у = findViewById<View>(R.id.button_у) as Button
        button_к = findViewById<View>(R.id.button_к) as Button
        button_е = findViewById<View>(R.id.button_е) as Button
        button_н = findViewById<View>(R.id.button_н) as Button
        button_г = findViewById<View>(R.id.button_г) as Button
        button_ш = findViewById<View>(R.id.button_ш) as Button
        button_щ = findViewById<View>(R.id.button_щ) as Button
        button_з = findViewById<View>(R.id.button_з) as Button
        button_х = findViewById<View>(R.id.button_х) as Button
        button_ъ = findViewById<View>(R.id.button_ъ) as Button
        button_ф = findViewById<View>(R.id.button_ф) as Button
        button_ы = findViewById<View>(R.id.button_ы) as Button
        button_в = findViewById<View>(R.id.button_в) as Button
        button_а = findViewById<View>(R.id.button_а) as Button
        button_п = findViewById<View>(R.id.button_п) as Button
        button_р = findViewById<View>(R.id.button_р) as Button
        button_о = findViewById<View>(R.id.button_о) as Button
        button_л = findViewById<View>(R.id.button_л) as Button
        button_д = findViewById<View>(R.id.button_д) as Button
        button_ж = findViewById<View>(R.id.button_ж) as Button
        button_э = findViewById<View>(R.id.button_э) as Button
        button_я = findViewById<View>(R.id.button_я) as Button
        button_ч = findViewById<View>(R.id.button_ч) as Button
        button_с = findViewById<View>(R.id.button_с) as Button
        button_м = findViewById<View>(R.id.button_м) as Button
        button_и = findViewById<View>(R.id.button_и) as Button
        button_т = findViewById<View>(R.id.button_т) as Button
        button_ь = findViewById<View>(R.id.button_ь) as Button
        button_б = findViewById<View>(R.id.button_б) as Button
        button_ю = findViewById<View>(R.id.button_ю) as Button
        button_del = findViewById<View>(R.id.button_del) as Button
        button_ввод = findViewById<View>(R.id.button_ввод) as Button

        button_й!!.setOnClickListener(this)
        button_ц!!.setOnClickListener(this)
        button_у!!.setOnClickListener(this)
        button_к!!.setOnClickListener(this)
        button_е!!.setOnClickListener(this)
        button_н!!.setOnClickListener(this)
        button_г!!.setOnClickListener(this)
        button_ш!!.setOnClickListener(this)
        button_щ!!.setOnClickListener(this)
        button_з!!.setOnClickListener(this)
        button_х!!.setOnClickListener(this)
        button_ф!!.setOnClickListener(this)
        button_ы!!.setOnClickListener(this)
        button_в!!.setOnClickListener(this)
        button_а!!.setOnClickListener(this)
        button_п!!.setOnClickListener(this)
        button_р!!.setOnClickListener(this)
        button_о!!.setOnClickListener(this)
        button_л!!.setOnClickListener(this)
        button_д!!.setOnClickListener(this)
        button_ж!!.setOnClickListener(this)
        button_э!!.setOnClickListener(this)
        button_я!!.setOnClickListener(this)
        button_ч!!.setOnClickListener(this)
        button_с!!.setOnClickListener(this)
        button_м!!.setOnClickListener(this)
        button_и!!.setOnClickListener(this)
        button_т!!.setOnClickListener(this)
        button_б!!.setOnClickListener(this)
        button_ю!!.setOnClickListener(this)
        button_del!!.setOnClickListener(this)
        button_ввод!!.setOnClickListener(this)

        button_ь!!.setOnTouchListener(this)

        keyValues.put(R.id.button_й, "Й"); keyValues.put(R.id.button_ц, "Ц"); keyValues.put(R.id.button_у, "У")
        keyValues.put(R.id.button_к, "К"); keyValues.put(R.id.button_е, "Е"); keyValues.put(R.id.button_н, "Н")
        keyValues.put(R.id.button_г, "Г"); keyValues.put(R.id.button_ш, "Ш"); keyValues.put(R.id.button_щ, "Щ")
        keyValues.put(R.id.button_з, "З"); keyValues.put(R.id.button_х, "Х"); keyValues.put(R.id.button_ю, "Ю")
        keyValues.put(R.id.button_ф, "Ф"); keyValues.put(R.id.button_ы, "Ы"); keyValues.put(R.id.button_в, "В")
        keyValues.put(R.id.button_а, "А"); keyValues.put(R.id.button_п, "П"); keyValues.put(R.id.button_р, "Р")
        keyValues.put(R.id.button_о, "О"); keyValues.put(R.id.button_л, "Л"); keyValues.put(R.id.button_д, "Д")
        keyValues.put(R.id.button_ж, "Ж"); keyValues.put(R.id.button_э, "Э"); keyValues.put(R.id.button_я, "Я")
        keyValues.put(R.id.button_ч, "Ч"); keyValues.put(R.id.button_с, "С"); keyValues.put(R.id.button_м, "М")
        keyValues.put(R.id.button_и, "И"); keyValues.put(R.id.button_т, "Т"); keyValues.put(R.id.button_ь, "Ь")
        keyValues.put(R.id.button_ъ, "Ъ"); keyValues.put(R.id.button_б, "Б");
    }

    fun enterHandler(){
        if (inputConnection != null && inputConnection!!.getTextBeforeCursor(5,0)!!.length == 5 &&
            deque.isNotEmpty()) inputConnection = deque.pop()
    }

    override fun onClick(view: View) {
        if (inputConnection == null) return
        val selectedText = inputConnection!!.getSelectedText(0)
        if (view.id == R.id.button_del) {
            if (TextUtils.isEmpty(selectedText)) {
                inputConnection!!.deleteSurroundingText(1, 0)
            } else {
                inputConnection!!.commitText("", 1)
            }
        } else {
            val value = keyValues[view.id]
            inputConnection!!.commitText(value, 1)
        }
    }

    fun setInputConnection(ic1: InputConnection?,ic2: InputConnection?,
    ic3: InputConnection?, ic4: InputConnection?, ic5: InputConnection?) {
        deque.clear()
        inputConnection = ic1
        deque.push(ic5)
        deque.push(ic4)
        deque.push(ic3)
        deque.push(ic2)
    }

    fun clearInputConnection(){
        deque.clear()
        inputConnection = null
    }

    private var millis: Long = 0L
    private var startFlag = false
    private var moveFlag = false
    override fun onTouch(view: View, e: MotionEvent): Boolean {
        if (inputConnection == null) return false
        if (view.id == R.id.button_ь) {
            var value = keyValues[R.id.button_ь, "Ь"]
            when(e.action){
                MotionEvent.ACTION_DOWN -> if (!startFlag){
                    startFlag = true
                        millis = e.eventTime
                    }
                MotionEvent.ACTION_MOVE -> {
                    if (!moveFlag) moveFlag = true
                    else if (e.eventTime - millis >= 300)
                        button_ъ!!.visibility = VISIBLE
                }
                MotionEvent.ACTION_UP -> {
                    if (moveFlag && e.eventTime - millis >= 300)
                        value = keyValues[R.id.button_ъ, "Ъ"]
                    button_ъ!!.visibility = GONE
                    inputConnection!!.commitText(value, 1)
                    millis = 0L
                    moveFlag = false
                    startFlag = false
                }
            }
        }
        return false
    }
}