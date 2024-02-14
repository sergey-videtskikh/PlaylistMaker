package ru.vsv.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.MaterialColors

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val MIME_TYPE_TEXT_PLAIN = "text/plain"
        const val MIME_TYPE_MESSAGE_RFC822 = "message/rfc822"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        window.statusBarColor = MaterialColors.getColor(
            findViewById<View>(android.R.id.content).rootView, R.attr.YP_White_to_YP_Black
        )

        val backImage = findViewById<ImageView>(R.id.back_image)
        backImage.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val shareImage = findViewById<ImageView>(R.id.share_image)
        shareImage.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_practicum_course_link))
                type = MIME_TYPE_TEXT_PLAIN
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }

        val supportImage = findViewById<ImageView>(R.id.support_image)
        supportImage.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject_to_support))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message_to_support))
                type = MIME_TYPE_MESSAGE_RFC822
            }
            startActivity(Intent.createChooser(supportIntent, null))
        }

        val agreementImage = findViewById<ImageView>(R.id.agreement_image)
        agreementImage.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.legal_practicum_offer_link))
                type = MIME_TYPE_TEXT_PLAIN
            }
            startActivity(agreementIntent)
        }
    }
}