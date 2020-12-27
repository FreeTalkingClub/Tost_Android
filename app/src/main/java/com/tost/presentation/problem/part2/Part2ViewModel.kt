package com.tost.presentation.problem.part2

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tost.data.repository.RecordsRepository
import com.tost.data.entity.ProblemState
import com.tost.presentation.problem.base.AudioViewModel

/**
 * Created By Malibin
 * on 12월 06, 2020
 */

class Part2ViewModel @ViewModelInject constructor(
    recordsRepository: RecordsRepository,
) : AudioViewModel(recordsRepository) {

    override val part: String = "part2"

    private val _problemState = MutableLiveData(ProblemState.PREPARE)
    val problemState: LiveData<ProblemState>
        get() = _problemState

    fun changeState(state: ProblemState) {
        _problemState.value = state
    }
}
