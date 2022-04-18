package com.example.ktworlde

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

class Textbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr){
    private var deque = ArrayDeque<TextView>()
    private var let1: TextView? = null
    private var let2: TextView? = null
    private var let3: TextView? = null
    private var let4: TextView? = null
    private var let5: TextView? = null
    init {
        LayoutInflater.from(context).inflate(R.layout.textbar, this, true)
        let1 = findViewById<View>(R.id.letter1) as TextView
        let2 = findViewById<View>(R.id.letter2) as TextView
        let3 = findViewById<View>(R.id.letter3) as TextView
        let4 = findViewById<View>(R.id.letter4) as TextView
        let5 = findViewById<View>(R.id.letter5) as TextView
        deque.addFirst(let5!!); deque.addFirst(let4!!)
        deque.addFirst(let3!!); deque.addFirst(let2!!)
        deque.addFirst(let1!!)
    }
    fun showResult(arr: IntArray,word: String){
        for (i in 0..4){
            val el = deque.first()
            el.text = word[i].toString().uppercase()
            when (arr[i]) {
                1 -> el.setBackgroundColor(Color.argb(255,255,235,0))
                2 -> el.setBackgroundColor(Color.argb(255,106,227,150))
                else -> {}
            }
            deque.removeFirst()
        }
        this.visibility = View.VISIBLE
    }
}