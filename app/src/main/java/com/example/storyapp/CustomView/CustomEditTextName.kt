package com.example.storyapp.CustomView

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R
import android.util.Patterns.EMAIL_ADDRESS

class CustomEditTextName: AppCompatEditText {
    var inputType=""

    private fun init() {
        addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.isEmpty()){
                        error = context.getString(R.string.name_alert)
                    }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.name)
    }
    constructor(context:Context):super(context){
        init()
    }

    constructor(context:Context, attrs:AttributeSet): super(context, attrs){
        init()
    }

    constructor(context:Context, attrs:AttributeSet, defStyleattr: Int): super (context, attrs,defStyleattr){
        init()
    }

}