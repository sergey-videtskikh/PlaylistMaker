package ru.vsv.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.MaterialColors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor = MaterialColors.getColor(
            findViewById<View>(android.R.id.content).rootView, R.attr.YP_Blue_to_YP_Black
        )

        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        val mediaButton = findViewById<Button>(R.id.media_library_button)
        mediaButton.setOnClickListener {
            val mediaIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(mediaIntent)
        }

        val settingButton = findViewById<Button>(R.id.settings_button)
        settingButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}