package org.hyperskill.secretdiary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private val pin = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val editPin = findViewById<EditText>(R.id.etPin)
        val logButton = findViewById<Button>(R.id.btnLogin)

        logButton.setOnClickListener {
            val enteredPin = editPin.text
            if (enteredPin.isNotEmpty() && enteredPin.toString().toInt() == pin) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                editPin.error = "Wrong PIN!"
            }
        }
    }
}