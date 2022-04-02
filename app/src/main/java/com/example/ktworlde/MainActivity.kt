package com.example.ktworlde

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RelativeLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editText = findViewById<EditText>(R.id.editText)
        val textBar = findViewById<RelativeLayout>(R.id.textBar)
        val keyboard = findViewById<Keyboard>(R.id.keyboard)
        editText.setShowSoftInputOnFocus(false)
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT)
        editText.setTextIsSelectable(true)

        //val ic = editText.onCreateInputConnection(EditorInfo())
        val ic = textBar.onCreateInputConnection(EditorInfo())
        keyboard.setInputConnection(ic)
    }
}