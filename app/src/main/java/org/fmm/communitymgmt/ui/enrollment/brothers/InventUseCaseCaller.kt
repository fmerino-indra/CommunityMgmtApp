package org.fmm.communitymgmt.ui.enrollment.brothers

import org.fmm.communitymgmt.domainlogic.usecase.InventUseCase
import javax.inject.Inject

class InventUseCaseCaller @Inject constructor(
    private val useCase: InventUseCase
): ScanUseCase {
    override suspend fun executeAfterScan(code: String) {
        useCase.executeAfterScan(code)
    }
}