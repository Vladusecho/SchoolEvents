package com.vladusecho.schoolevents.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.vladusecho.schoolevents.domain.usecase.GetEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase
) : ViewModel() {


}