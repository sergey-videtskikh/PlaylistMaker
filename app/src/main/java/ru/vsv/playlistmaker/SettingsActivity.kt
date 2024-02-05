package ru.vsv.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val MIME_TYPE_TEXT_PLAIN = "text/plain"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backImage = findViewById<ImageView>(R.id.back_image)
        backImage.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val shareImage = findViewById<ImageView>(R.id.share_image)
        shareImage.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_practicum_course_link))
                type = MIME_TYPE_TEXT_PLAIN
            }

            startActivity(sendIntent)
        }

        val supportImage = findViewById<ImageView>(R.id.support_image)
        supportImage.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject_to_support))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message_to_support))
                type = MIME_TYPE_TEXT_PLAIN;
            }
            startActivity(sendIntent)
        }

        val agreementImage = findViewById<ImageView>(R.id.agreement_image)
        agreementImage.setOnClickListener {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.legal_practicum_offer_link))
                type = MIME_TYPE_TEXT_PLAIN;
            }
            startActivity(sendIntent)
        }
    }
}