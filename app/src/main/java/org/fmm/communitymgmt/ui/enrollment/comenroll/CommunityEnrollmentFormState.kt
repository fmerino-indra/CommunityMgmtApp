package org.fmm.communitymgmt.ui.enrollment.comenroll

data class CommunityEnrollmentFormState (
    val isResponsible: Boolean = false,
    val isMarried: Boolean = false,
    val communityNumber:String="",
    val parish: String="",
    val parishAddress: String="",
    val parishAddressNumber: String="",
    val parishAddressPostalCode: String="",
    val parishAddressCity: String="",

    val nError:String? = null,
    val pError:String? = null,
    val pAError:String? = null,
    val pANError:String? = null,
    val pAPCError:String? = null,
    val pACError:String? = null,

    val isValid:Boolean = false
)