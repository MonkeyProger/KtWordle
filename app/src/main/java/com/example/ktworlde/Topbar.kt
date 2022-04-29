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
import androidx.core.text.trimmedLength

class Topbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr){
    private var word1: TextView? = null
    private var en1: TextView? = null
    init {
        LayoutInflater.from(context).inflate(R.layout.topbar, this, true)
        word1 = findViewById<View>(R.id.word1) as TextView
        en1 = findViewById<View>(R.id.e1) as TextView
    }
    fun set(pair: Pair<String, Double>){
            word1!!.text = pair.first
            en1!!.text = "%.2f".format(pair.second)
    }
    fun clear(){
            word1!!.text = ""
            en1!!.text = ""
    }
}