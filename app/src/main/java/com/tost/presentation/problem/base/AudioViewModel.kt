package com.tost.presentation.problem.base

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tost.data.repository.RecordsRepository
import com.tost.presentation.problem.TostRecorder
import com.tost.presentation.problem.widget.AudioStateButton
import com.tost.presentation.problem.widget.TostProgressBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created By Malibin
 * on 12월 12, 2020
 */

abstract class AudioViewModel constructor(
    private val recordsRepository: RecordsRepository,
) : ViewModel() {

    abstract val part: String

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int>
        get() = _progress

    private val _audioState = MutableLiveData(AudioStateButton.State.RECORDING)
    val audioState: LiveData<AudioStateButton.State>
        get() = _audioState

    var isTest = false

    private val recordPlayer = MediaPlayer()
    private val tostRecorder = TostRecorder()

    private var onProgressFinishListener: OnProgressFinishListener? = null

    fun setOnProgressFinishListener(l: OnProgressFinishListener?) {
        this.onProgressFinishListener = l
    }

    fun startCountDown(time: Int) = viewModelScope.launch {
        resetProgress()
        val tick = time.calculateTick()
        while (time > getCurrentProgress()) {
            delay(tick)
            _progress.postValue(getCurrentProgress() + tick.toInt())
        }
        onProgressFinishListener?.onFinish()
    }

    fun prepareRecorder(baseFilePath: String) {
        val fileName = "$baseFilePath$part"
        tostRecorder.prepare(fileName)
    }

    fun startRecord() {
        tostRecorder.start()
        _audioState.value = AudioStateButton.State.RECORDING
    }

    fun finishRecord() {
        tostRecorder.stop()
        tostRecorder.release()
        recordPlayer.setDataSource(tostRecorder.fileName)
    }

    fun cancelRecord() {
        tostRecorder.stop()
        tostRecorder.release()
        _progress.value = 0
        _audioState.value = AudioStateButton.State.STOP
        recordsRepository.deleteRecord(tostRecorder.fileName)
    }

    @JvmOverloads
    fun playRecord(duration: Int = getCurrentProgress()) {
        viewModelScope.launch {
            recordPlayer.seekTo(duration)
            recordPlayer.start()
            _audioState.value = AudioStateButton.State.PLAYING
            val tick = recordPlayer.duration.calculateTick()
            while (recordPlayer.isPlaying) {
                delay(tick)
                _progress.postValue(recordPlayer.currentPosition)
            }
        }
    }

    private fun getCurrentProgress() = progress.value
        ?: throw IllegalStateException("progress value cannot be null")

    private fun Int.calculateTick(): Long = 20L + this shr 10

    private fun resetProgress() = _progress.postValue(0)

    fun pausePlayRecord() {
        recordPlayer.pause()
        _audioState.value = AudioStateButton.State.PAUSE
    }

    override fun onCleared() {
        super.onCleared()
        recordPlayer.release()
        tostRecorder.release()
    }

    fun interface OnProgressFinishListener {
        fun onFinish()
    }
}