package com.m1x.gymmer.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.m1x.gymmer.ui.screens.state.AttentionTrainee
import com.m1x.gymmer.ui.screens.state.MyTraineesUiState
import com.m1x.gymmer.ui.screens.state.RosterTrainee
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyTraineesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        MyTraineesUiState(
            needsAttention = listOf(
                AttentionTrainee("1", "Isha Gupta", "INACTIVE: 4 DAYS"),
                AttentionTrainee("2", "Karan Malhotra", "MISSED: MAX PR TEST")
            ),
            activeRoster = listOf(
                RosterTrainee("3", "Aditya Reddy", "ELITE MEMBERSHIP • HYPERTROPHY", "92%", "Leg Day Focus • 2h ago"),
                RosterTrainee("4", "Sanjana Rao", "STANDARD • MOBILITY & YOGA", "75%", "Sun Salutation • 5h ago"),
                RosterTrainee("5", "Devansh Singh", "ELITE MEMBERSHIP • STRENGTH", "100%", "Deadlift Max • 1d ago")
            )
        )
    )
    val uiState: StateFlow<MyTraineesUiState> = _uiState.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}
