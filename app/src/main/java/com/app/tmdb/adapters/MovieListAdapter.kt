package com.app.tmdb.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.tmdb.databinding.ItemMovieBinding
import com.app.tmdb.model.Movie
import com.app.tmdb.utils.Constants
import com.app.tmdb.utils.getProgressDrawable
import com.app.tmdb.utils.getScreenHeight
import com.app.tmdb.utils.loadImage

@SuppressLint("NotifyDataSetChanged")
class MovieListAdapter(
    private var activity: AppCompatActivity,
    private var movies: ArrayList<Movie?>?,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {


    fun updateMovies(newMovies: ArrayList<Movie?>?) {
        movies?.clear()
        movies?.addAll(newMovies ?: ArrayList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }


    override fun getItemCount() = movies?.size ?: 0

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        movies?.get(position)?.let { holder.bind(activity, it) }
        holder.itemView.setOnClickListener { onClickListener.onClick(
            movies?.get(position) ?: Movie()
        ) }
    }

    fun filterList(filterList: ArrayList<Movie?>?) {
        movies = filterList
        notifyDataSetChanged()
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val progressDrawable = getProgressDrawable(binding.root.context)

        fun bind(activity: AppCompatActivity, movie: Movie) {
            binding.ivMovie.layoutParams.height = (60*getScreenHeight(activity)/100)
            binding.ivMovie.loadImage(Constants.IMAGE_URL + movie.posterPath, progressDrawable)
        }
    }
}

class OnClickListener(val clickListener: (movie: Movie?) -> Unit) {
    fun onClick(movie: Movie?) = clickListener(movie)
}