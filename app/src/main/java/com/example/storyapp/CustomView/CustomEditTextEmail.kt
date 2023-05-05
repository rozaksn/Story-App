package com.example.storyapp.CustomView

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import android.util.Patterns.EMAIL_ADDRESS
import com.example.storyapp.R

class CustomEditTextEmail:AppCompatEditText {

    constructor(context:Context): super(context){
        init()
    }

    constructor(context: Context,attrs:AttributeSet):super(context,attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet,defStyleAttr:Int):super (context, attrs, defStyleAttr){
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint=context.getString(R.string.email)
    }
    private fun init(){
        addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!EMAIL_ADDRESS.matcher(s).matches()){
                    error = context.getString(R.string.email_alert)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }
}