package com.example .storyapp.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.login.LoginActivity


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

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
        val password = binding.edTextPassword.toString().trim()
        val button = binding.btnRegister
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
            button.isEnabled ->{
                if (password.isEmpty() && password.length < 8 && name.isEmpty()) false else true
            }
            button.isEnabled ->{
                if(password.length > 8) true
            }
            else ->{
                registerViewModel.register(name,email,password)
            }
        }
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity as Activity).toBundle())
        finish()
    }

    private fun regisAction(){
        val password = binding.edTextPassword.text
        val button = binding.btnRegister
       // button.isEnabled = if (password.isEmpty() && password?.length!! < 8) false else true
        //button.isEnabled = if ( password.isNotEmpty()) true else false
        binding.btnRegister.setOnClickListener {
            register()
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.pbRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnim(){
        ObjectAnimator.ofFloat(binding.ivRegister, View.TRANSLATION_X,-30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode =  ObjectAnimator.REVERSE
        }.start()

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
                titleTextView,txtViewName,edTextNameLayout,txtViewEmail,edTextEmailLayout,txtViewPassword,
                edTextPasswordLayout, registerButton
            )
        }.start()
    }

}