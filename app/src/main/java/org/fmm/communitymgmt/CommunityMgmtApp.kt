package org.fmm.communitymgmt

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

/**
 * Este fichero tiene las configuraciones generales
 */
@HiltAndroidApp
class CommunityMgmtApp: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext=this

        // GPT Dice que aquí para precargar la configuración (y cifrarla) pero realmente lo hace
    // sola

    }
    companion object { lateinit var appContext: Context }
}