package com.app.tmdb.repository

import androidx.lifecycle.ViewModel
import com.app.tmdb.viewmodel.MovieViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @ClassKey(MovieViewModel::class)
    @IntoMap
    abstract fun  movieViewModel(mainViewModel: MovieViewModel): ViewModel
    // what this fun do is , it will bind the mainViewModel object with the  ViewModel
    // when ever there is a request for a viewModel it will return mainViewModel object.

}