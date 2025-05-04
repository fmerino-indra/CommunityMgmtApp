package org.fmm.communitymgmt

import android.app.Application
import android.content.Context
import coil.Coil
import coil.util.CoilUtils
import dagger.hilt.android.HiltAndroidApp
import kotlinx.datetime.LocalDate
import kotlin.reflect.typeOf

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
//        kotlinx.serialization.serializer(typeOf<LocalDate>())
//        LocalDate.serializer()

    }
    companion object { lateinit var appContext: Context }
}