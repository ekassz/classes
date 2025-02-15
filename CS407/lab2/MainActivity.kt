package com.cs407.lab2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val editText = findViewById<EditText>(R.id.enterText)
        val submitButton = findViewById<Button>(R.id.button)
        submitButton.setOnClickListener{
            //Log.i("INFO", "Button Clicked!")
            val userInput = editText.text.toString()
            //Toast.makeText(this, userInput, Toast.LENGTH_SHORT).show()
            val intent = Intent(this,CalculatorActivity::class.java)
            intent.putExtra("EXTRA_MESSAGE", userInput)
            startActivity(intent)
        }


    }
}