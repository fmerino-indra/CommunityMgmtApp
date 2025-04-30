package org.fmm.communitymgmt.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class ImageDTO (val id:Int?, val tinyPhoto:String?, val mimeType:String?) {
}