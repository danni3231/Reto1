package com.dannir.reto1.activities

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dannir.reto1.databinding.ActivityLoginBinding
import com.dannir.reto1.model.Reto1App.Companion.prefs
import com.dannir.reto1.model.utils.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ),1)

        if (prefs.getCurrentUser() != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {

            val email: String = binding.loginEmailTF.editText?.text!!.toString()
            val password: String = binding.loginPassordTF.editText?.text!!.toString()

            if(validateInputs(email, password)){
                login(email, password)
            }
        }
    }

    private fun login(email: String, password: String) {
        if ((email.contentEquals(EMAIL1) || email.contentEquals(EMAIL2)) && password.contentEquals(
                USERPASS
            )) {

            val intent = Intent(this, MainActivity::class.java)
            if (email.contentEquals(EMAIL1)) {
                prefs.saveCurrentUser("USER_1")
            } else {
                prefs.saveCurrentUser("USER_2")
            }
            startActivity(intent)
            finish()

        }else{
            Toast.makeText(this,"Credenciales incorrectas",Toast.LENGTH_SHORT).show()
        }

    }

    private fun validateInputs(email: String, password: String): Boolean {
        if(email.isEmpty()){
            Toast.makeText(this,"Por favor escribe tu correo electrónico",Toast.LENGTH_SHORT).show()
            return false
        }else if(password.isEmpty()){
            Toast.makeText(this,"Por favor escribe tu contraseña",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}