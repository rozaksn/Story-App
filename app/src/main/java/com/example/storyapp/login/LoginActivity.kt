package com.example.storyapp.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.main.MainActivity
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.register.RegisterActivity
import com.example.storyapp.user.UserModel
import com.example.storyapp.user.UserPreference



class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel:LoginViewModel
    private lateinit var preferences:SharedPreferences
    lateinit var userPref:UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupPref()
        buttonAction()
        setupViewModel()
        playAnim()

    }


    private fun setupViewModel(){
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.isLoading.observe(this){ showLoading(it) }
        loginViewModel.toastMessage.observe(this) { toast(it) }
    }

    private fun setupPref(){
        preferences = getSharedPreferences(EXTRA_PREF,Context.MODE_PRIVATE)
        userPref= UserPreference(this)
    }
    private fun login(){
        val email = binding.edTextEmail.text.toString().trim()
        val password = binding.edTextPassword.text.toString().trim()
        val button = binding.bntLogin
        //button.isEnabled = if (password.isEmpty() && password.length < 8) false else true
        //button.isEnabled = if (password.length > 8) true else false


        when{
            email.isEmpty() ->{
                binding.edLayoutEmail.error = getString(R.string.errorEmail)
            }
            password.isEmpty()->{
                binding.edLayoutPassword.error = getString(R.string.errorPassword)
            }
            else ->{
                loginViewModel.login(email, password)
                loginViewModel.login.observe(this@LoginActivity) {
                    binding.pbLogin.visibility = View.VISIBLE
                    if (it != null) {
                        AlertDialog.Builder(this).apply {
                            setTitle(getString(R.string.login_success))
                            setMessage(getString(R.string.welcome))
                            setPositiveButton(getString(R.string.next_button)) { _, _ ->
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(
                                    intent, ActivityOptionsCompat
                                        .makeSceneTransitionAnimation(this@LoginActivity as Activity)
                                        .toBundle()
                                )
                                finish()
                            }
                            create()
                            show()
                        }
                        saveUserModel(UserModel(it.name, it.token, it.userId,true))
                    }
                }
            }
        }


    }

    private fun buttonAction(){
        binding.bntLogin.setOnClickListener {
            login()
            val methodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            methodManager.hideSoftInputFromWindow(it.windowToken,0)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity as Activity).toBundle())
        }

    }
    private fun saveUserModel(user:UserModel){
        userPref.setUser(user)
    }
    private fun showLoading(isLoading: Boolean) {
        binding.pbLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun playAnim(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X,-30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode =  ObjectAnimator.REVERSE
        }.start()
        val title = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.tvTitleLogin, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val edEmailLayout = ObjectAnimator.ofFloat(binding.edLayoutEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val edPasswordLayout = ObjectAnimator.ofFloat(binding.edLayoutPassword, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.bntLogin, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,message,tvEmail,edEmailLayout,tvPassword,edPasswordLayout,btnLogin,btnRegister
            )
            startDelay = 500
        }.start()
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val EXTRA_PREF="extra_pref"
    }
}