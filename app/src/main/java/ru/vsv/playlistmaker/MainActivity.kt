package ru.vsv.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val searchButtonCLickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(
                    this@MainActivity, "Нажата кнопка Поиск", Toast.LENGTH_LONG
                ).show()
            }
        }
        searchButton.setOnClickListener(searchButtonCLickListener)

        val mediaButton = findViewById<Button>(R.id.media_library_button)
        mediaButton.setOnClickListener {
            Toast.makeText(
                this@MainActivity, "Нажата кнопка Медиатека", Toast.LENGTH_SHORT
            ).show()
        }

        val settingButton = findViewById<Button>(R.id.settings_button)
        settingButton.setOnClickListener {
            Toast.makeText(
                this@MainActivity, "Нажата кнопка Настройки", Toast.LENGTH_SHORT
            ).show()
        }
    }
}