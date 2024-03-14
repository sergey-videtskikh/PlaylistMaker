package ru.vsv.playlistmaker.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
        private const val TAG = "SearchActivity"
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

    private var savedValue: String? = null

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val appleMusicService = retrofit.create(AppleMusicApi::class.java)
    private val tracks = ArrayList<TrackDto>()
    private val adapter = SearchAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        window.statusBarColor = MaterialColors.getColor(
            findViewById<View>(android.R.id.content).rootView, R.attr.YP_White_to_YP_Black
        )

        recyclerView = findViewById(R.id.search_rv)
        recyclerView.adapter = adapter

        savedValue = savedInstanceState?.getString(EDIT_TEXT_VIEW_KEY)

        backImage = findViewById(R.id.back_image)
        backImage.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        queryInput = findViewById(R.id.search_edit_text)
        queryInput.setText(savedValue)

        clearButton = findViewById(R.id.ic_clear_image_view)

        clearButton.setOnClickListener {
            savedValue = ""
            queryInput.setText(savedValue)
            queryInput.clearFocus()
            hideDefaultKeyboard(queryInput)
        }

        textWatcherEditText = getTextWatcher(clearButton)
        queryInput.addTextChangedListener(textWatcherEditText)

        placeholderEmpty = findViewById(R.id.placeholder_empty)
        placeholderError = findViewById(R.id.placeholder_error)

        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (queryInput.text.isNotBlank()) {
                    val query = queryInput.text.toString()

                    appleMusicService.search(query)
                        .enqueue(object : Callback<SearchTracksResponseDto> {
                            override fun onResponse(
                                call: Call<SearchTracksResponseDto>,
                                response: Response<SearchTracksResponseDto>
                            ) {
                                if (response.code() == 200) {
                                    tracks.clear()
                                    placeholderError.visibility = View.GONE
                                    if (response.body()?.results?.isNotEmpty() == true) {
                                        tracks.addAll(response.body()?.results!!)
                                        adapter.notifyDataSetChanged()
                                    }
                                    if (tracks.isEmpty()) {
                                        adapter.notifyDataSetChanged()
                                        placeholderEmpty.visibility = View.VISIBLE
                                    } else {
                                        placeholderEmpty.visibility = View.GONE
                                    }
                                } else {
                                    placeholderError.visibility = View.VISIBLE
                                }
                            }

                            override fun onFailure(
                                call: Call<SearchTracksResponseDto>,
                                t: Throwable
                            ) {
                                Log.e(
                                    TAG,
                                    "Ошибка при запросе списка песен: ${t.message.toString()}"
                                )
                                placeholderError.visibility = View.VISIBLE
                            }
                        })
                    queryInput.setText("")
                }
                true
            }
            false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EDIT_TEXT_VIEW_KEY, savedValue)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savedValue = savedInstanceState.getString(EDIT_TEXT_VIEW_KEY)
        queryInput.setText(savedValue)
    }

    private fun isClearButtonVisible(s: CharSequence?): Boolean = !s.isNullOrEmpty()

    private fun hideDefaultKeyboard(editText: EditText) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    private fun getTextWatcher(clearButton: ImageView) = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            savedValue = s.toString()
            clearButton.isVisible = isClearButtonVisible(s)
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}