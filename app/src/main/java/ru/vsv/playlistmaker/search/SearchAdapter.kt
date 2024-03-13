package ru.vsv.playlistmaker.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.vsv.playlistmaker.R
import ru.vsv.playlistmaker.dto.TrackDto

class SearchAdapter(private val dataSet: List<TrackDto>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val albumCoverView: ImageView = view.findViewById(R.id.album_cover)
        private val trackNameView: TextView = view.findViewById(R.id.track_name)
        private val artistNameView: TextView = view.findViewById(R.id.artist_name)
        private val trackTime: TextView = view.findViewById(R.id.track_time)

        fun bind(track: TrackDto) {
            Glide.with(view)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.search_placeholder)
                .transform(CenterCrop(), RoundedCorners(2))
                .into(albumCoverView)

            trackNameView.text = track.trackName
            artistNameView.text = track.artistName
            trackTime.text = track.trackTimeMillis.toString()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_item, viewGroup, false)

        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(searchViewHolder: SearchViewHolder, position: Int) {
        searchViewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}