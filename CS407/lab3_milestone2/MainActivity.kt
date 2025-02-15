package com.cs407.lab3_milestone2

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager

        findViewById<Button>(R.id.firstFragBtn).setOnClickListener{
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, FirstFragment::class.java, null)
                .setReorderingAllowed(true)
                .addToBackStack("showing First")
                .commit()
        }
        findViewById<Button>(R.id.SecFragBtn).setOnClickListener{
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, SecondFragment::class.java, null)
                .setReorderingAllowed(true)
                .addToBackStack("showing Second")
                .commit()
        }
    }
}