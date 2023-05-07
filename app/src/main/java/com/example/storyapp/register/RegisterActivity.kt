package com.example .storyapp.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.login.LoginActivity
import com.example.storyapp.register.RegisterViewModel


class RegisterActivity : AppCompatActivity() {
    private  val binding by lazy(LazyThreadSafetyMode.NONE){
        ActivityRegisterBinding.inflate(layoutInflater)}

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupRegisButton()
        regisAction()
        playAnim()
        setupViewModel()

    }


    private fun setupViewModel(){
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        registerViewModel.isLoading.observe(this){showLoading(it)}
    }

    private fun register(){
        val name = binding.edTextName.text.toString().trim()
        val email = binding.edTextEmail.text.toString().trim()
        val password = binding.edTextPassword.text.toString().trim()

        when{

            name.isEmpty() -> {
                binding.edTextLayoutName.error = getString(R.string.name_alert)
            }
            email.isEmpty() -> {
                binding.edLayoutEmail.error = getString(R.string.email_alert)
            }
            password.isEmpty() -> {
                binding.edLayoutPassword.error = getString(R.string.password_alert)
            }

            else ->{
                if (binding.edLayoutPassword.error.isNullOrEmpty()) {
                    registerViewModel.register(name,email,password)

                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }


        }


    }

    private fun  setupRegisButton(){
        val edPassword = binding.edTextPassword
        val regisButton = binding.btnRegister
        regisButton.isEnabled = false

        edPassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
               val password = s.toString().trim()
                regisButton.isEnabled = password.length >= 8
            }

        })
    }
    private fun regisAction(){
        binding.btnRegister.setOnClickListener {
            register()
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.pbRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnim(){
        ObjectAnimator.ofFloat(binding.ivRegister, View.TRANSLATION_X,-50f, 50f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode =  ObjectAnimator.REVERSE
        }.start()

        val image = ObjectAnimator.ofFloat(binding.ivRegister,View.ALPHA,1f).setDuration(500)
        val titleTextView = ObjectAnimator.ofFloat(binding.tvTitle,View.ALPHA,1f).setDuration(500)
        val txtViewName = ObjectAnimator.ofFloat(binding.tvName,View.ALPHA,1f).setDuration(500)
        val edTextNameLayout = ObjectAnimator.ofFloat(binding.edTextLayoutName,View.ALPHA,1f).setDuration(500)
        val txtViewEmail = ObjectAnimator.ofFloat(binding.tvEmail,View.ALPHA,1f).setDuration(500)
        val edTextEmailLayout = ObjectAnimator.ofFloat(binding.edLayoutEmail,View.ALPHA,1f).setDuration(500)
        val txtViewPassword = ObjectAnimator.ofFloat(binding.tvPassword,View.ALPHA,1f).setDuration(500)
        val edTextPasswordLayout = ObjectAnimator.ofFloat(binding.edLayoutPassword,View.ALPHA,1f).setDuration(500)
        val registerButton = ObjectAnimator.ofFloat(binding.btnRegister,View.ALPHA,1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                image,titleTextView,txtViewName,edTextNameLayout,txtViewEmail,edTextEmailLayout,txtViewPassword,
                edTextPasswordLayout, registerButton
            )
        }.start()
    }

}