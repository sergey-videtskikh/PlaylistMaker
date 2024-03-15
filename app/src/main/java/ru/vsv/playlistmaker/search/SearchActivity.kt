package ru.vsv.playlistmaker.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsv.playlistmaker.R
import ru.vsv.playlistmaker.dto.SearchTracksResponseDto
import ru.vsv.playlistmaker.dto.TrackDto
import ru.vsv.playlistmaker.retrofit.AppleMusicApi


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val EDIT_TEXT_VIEW_KEY = "EDIT_TEXT_VIEW_KEY"
        private const val BASE_URL = "https://itunes.apple.com/"
    }

    private lateinit var queryInput: EditText
    private lateinit var backImage: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var textWatcherEditText: TextWatcher
    private lateinit var placeholderEmpty: LinearLayout
    private lateinit var placeholderError: LinearLayout
    private lateinit var updateButton: Button

    private var savedQuery: String = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val appleMusicService = retrofit.create(AppleMusicApi::class.java)
    private val tracks = mutableListOf<TrackDto>()
    private val adapter = SearchAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        window.statusBarColor = MaterialColors.getColor(
            findViewById<View>(android.R.id.content).rootView, R.attr.YP_White_to_YP_Black
        )

        recyclerView = findViewById(R.id.search_rv)
        recyclerView.adapter = adapter

        savedQuery = savedInstanceState?.getString(EDIT_TEXT_VIEW_KEY) ?: ""

        backImage = findViewById(R.id.back_image)
        backImage.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        queryInput = findViewById(R.id.search_edit_text)
        queryInput.setText(savedQuery)

        clearButton = findViewById(R.id.ic_clear_image_view)

        clearButton.setOnClickListener {
            savedQuery = ""
            queryInput.setText(savedQuery)
            queryInput.clearFocus()
            hideDefaultKeyboard(queryInput)
            tracks.clear()
            adapter.notifyDataSetChanged()
        }

        textWatcherEditText = getTextWatcher(clearButton)
        queryInput.addTextChangedListener(textWatcherEditText)

        placeholderEmpty = findViewById(R.id.placeholder_empty)
        placeholderError = findViewById(R.id.placeholder_error)

        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (queryInput.text.isNotBlank()) {
                    savedQuery = queryInput.text.toString()
                    handleSearchQuery(savedQuery)
                }
                true
            }
            false
        }

        updateButton = findViewById(R.id.update_button)
        updateButton.setOnClickListener {
            if (savedQuery.isNotBlank()) {
                handleSearchQuery(savedQuery)
            }
        }
    }

    private fun handleSearchQuery(query: String) {
        appleMusicService.search(query)
            .enqueue(object : Callback<SearchTracksResponseDto> {
                override fun onResponse(
                    call: Call<SearchTracksResponseDto>,
                    response: Response<SearchTracksResponseDto>
                ) {
                    if (response.isSuccessful) {
                        placeholderError.visibility = View.GONE
                        tracks.clear()
                        val results = response.body()?.results ?: emptyList()

                        if (results.isEmpty()) {
                            placeholderEmpty.visibility = View.VISIBLE
                        } else {
                            tracks.addAll(results)
                            placeholderEmpty.visibility = View.GONE
                        }

                        adapter.notifyDataSetChanged()
                    } else {
                        handleErrorPlaceholder()
                    }
                }

                override fun onFailure(call: Call<SearchTracksResponseDto>, t: Throwable) {
                    handleErrorPlaceholder()
                }
            })
    }

    private fun handleErrorPlaceholder() {
        tracks.clear()
        adapter.notifyDataSetChanged()
        placeholderEmpty.visibility = View.GONE
        placeholderError.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EDIT_TEXT_VIEW_KEY, savedQuery)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savedQuery = savedInstanceState.getString(EDIT_TEXT_VIEW_KEY) ?: ""
        queryInput.setText(savedQuery)
    }

    private fun isClearButtonVisible(s: CharSequence?): Boolean = !s.isNullOrEmpty()

    private fun hideDefaultKeyboard(editText: EditText) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    private fun getTextWatcher(clearButton: ImageView) = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            savedQuery = s.toString()
            clearButton.isVisible = isClearButtonVisible(s)
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}