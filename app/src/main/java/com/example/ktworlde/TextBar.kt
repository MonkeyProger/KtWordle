package com.example.ktworlde

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment


class TextBar (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
):  RelativeLayout(context, attrs, defStyleAttr), TextWatcher, View.OnKeyListener, View.OnFocusChangeListener {
    private var et1letter: EditText? = null
    private var et2letter: EditText? = null
    private var et3letter: EditText? = null
    private var et4letter: EditText? = null
    private var et5letter: EditText? = null
    private var whoHasFocus = 0
    private var curText = ""
    init {
        initializeView(context)
        //et1letter!!.requestFocus()
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        return super.onCreateInputConnection(outAttrs)
    }
    private fun initializeView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.edittext, this, true)
        et1letter = findViewById<View>(R.id.et1letter) as EditText
        et2letter = findViewById<View>(R.id.et2letter) as EditText
        et3letter = findViewById<View>(R.id.et3letter) as EditText
        et4letter = findViewById<View>(R.id.et4letter) as EditText
        et5letter = findViewById<View>(R.id.et5letter) as EditText
        et1letter!!.setShowSoftInputOnFocus(false)
        et2letter!!.setShowSoftInputOnFocus(false)
        et3letter!!.setShowSoftInputOnFocus(false)
        et4letter!!.setShowSoftInputOnFocus(false)
        et5letter!!.setShowSoftInputOnFocus(false)
        et1letter!!.setRawInputType(InputType.TYPE_CLASS_TEXT)
        et2letter!!.setRawInputType(InputType.TYPE_CLASS_TEXT)
        et3letter!!.setRawInputType(InputType.TYPE_CLASS_TEXT)
        et4letter!!.setRawInputType(InputType.TYPE_CLASS_TEXT)
        et5letter!!.setRawInputType(InputType.TYPE_CLASS_TEXT)
        setListeners()
    }
    private fun setListeners() {
        et1letter!!.addTextChangedListener(this)
        et2letter!!.addTextChangedListener(this)
        et3letter!!.addTextChangedListener(this)
        et4letter!!.addTextChangedListener(this)
        et5letter!!.addTextChangedListener(this)
        et1letter!!.setOnKeyListener(this)
        et2letter!!.setOnKeyListener(this)
        et3letter!!.setOnKeyListener(this)
        et4letter!!.setOnKeyListener(this)
        et5letter!!.setOnKeyListener(this)
        et1letter!!.onFocusChangeListener = this
        et2letter!!.onFocusChangeListener = this
        et3letter!!.onFocusChangeListener = this
        et4letter!!.onFocusChangeListener = this
        et5letter!!.onFocusChangeListener = this
    }
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
            when (v!!.id) {
                R.id.et1letter -> whoHasFocus = 1
                R.id.et2letter -> whoHasFocus = 2
                R.id.et3letter -> whoHasFocus = 3
                R.id.et4letter -> whoHasFocus = 4
                R.id.et5letter -> whoHasFocus = 5
                else -> {}
            }
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }

    override fun afterTextChanged(s: Editable) {
        when(whoHasFocus) {
            1 -> if (et1letter!!.text.toString().isNotEmpty()) {
                    curText + et1letter!!.text
                    et2letter!!.requestFocus()
                }
            2 -> if (et2letter!!.text.toString().isNotEmpty()) {
                curText + et2letter!!.text
                et3letter!!.requestFocus()
            }
            3 -> if (et3letter!!.text.toString().isNotEmpty()) {
                curText + et3letter!!.text
                et4letter!!.requestFocus()
            }
            4 -> if (et4letter!!.text.toString().isNotEmpty()) {
                curText + et4letter!!.text
                et5letter!!.requestFocus()
            }
            5 -> if (et5letter!!.text.toString().isNotEmpty()) {
                curText + et5letter!!.text
            }
        }
    }

}