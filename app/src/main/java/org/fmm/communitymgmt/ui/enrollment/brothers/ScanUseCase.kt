package org.fmm.communitymgmt.ui.enrollment.brothers

interface ScanUseCase {
    suspend fun executeAfterScan(code: String)
}