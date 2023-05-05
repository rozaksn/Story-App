package com.example.storyapp.CustomView

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R
import com.google.android.material.internal.TextWatcherAdapter

class CustomEditTextPassword:AppCompatEditText {
    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet):super(context,attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr:Int):super (context, attrs, defStyleAttr){
        init()
    }

    private fun init(){
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
               if (s.length < 8){
                   error = context.getString(R.string.password_alert)
               }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


}