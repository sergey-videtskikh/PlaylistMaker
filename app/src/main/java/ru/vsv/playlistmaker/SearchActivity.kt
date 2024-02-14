package ru.vsv.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.MaterialColors


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "SearchActivity"
        private const val EDIT_TEXT_VIEW_KEY = "EDIT_TEXT_VIEW_KEY"
    }

    private lateinit var searchEditText: EditText

    private var savedValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        window.statusBarColor = MaterialColors.getColor(
            findViewById<View>(android.R.id.content).rootView, R.attr.YP_White_to_YP_Black
        )

        savedValue = savedInstanceState?.getString(EDIT_TEXT_VIEW_KEY)

        val backImage = findViewById<ImageView>(R.id.back_image)
        backImage.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        searchEditText = findViewById(R.id.search_edit_text)
        searchEditText.setText(savedValue)

        val clearButton = findViewById<ImageView>(R.id.ic_clear_image_view)

        clearButton.setOnClickListener {
            savedValue = ""
            searchEditText.setText(savedValue)
            searchEditText.clearFocus()
            hideDefaultKeyboard(searchEditText)
        }

        val textWatcherEditText = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                savedValue = s.toString()
                Log.i(LOG_TAG, "Введеное значение: $savedValue")
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        searchEditText.addTextChangedListener(textWatcherEditText)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EDIT_TEXT_VIEW_KEY, savedValue)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savedValue = savedInstanceState.getString(EDIT_TEXT_VIEW_KEY)
        searchEditText.setText(savedValue)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideDefaultKeyboard(editText: EditText) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}