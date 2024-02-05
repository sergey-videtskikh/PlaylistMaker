package ru.vsv.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backImage = findViewById<ImageView>(R.id.back_image)

        backImage.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val shareImage = findViewById<ImageView>(R.id.share_image)

        shareImage.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_practicum_course_link))
                type = "text/plain"
            }

            startActivity(sendIntent)
        }
    }
}