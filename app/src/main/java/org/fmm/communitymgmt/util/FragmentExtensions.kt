package org.fmm.communitymgmt.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.fragment.app.Fragment

fun Fragment.vibrate() {
    // 📳 Vibración
    // 📳 Vibración moderna
    val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        val vibratorManager = requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        requireContext().getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
    }

//    var vibrationEffect = VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
//    vibrator.vibrate(vibrationEffect)

    val vibrationEffect = VibrationEffect.createWaveform(longArrayOf(0,300,200,300), VibrationEffect
        .DEFAULT_AMPLITUDE)
    vibrator.vibrate(vibrationEffect)

    /*
    val vibratorOld = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        vibratorOld.vibrate(
            VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    } else {
        vibratorOld.vibrate(150) // deprecated, pero válido en API < 26
    }
 */
}
fun Fragment.playBeep() {
    val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
    toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 150) // 150 ms
}

fun Fragment.playBeepAndVibrate() {
    playBeep()
    vibrate()
}
