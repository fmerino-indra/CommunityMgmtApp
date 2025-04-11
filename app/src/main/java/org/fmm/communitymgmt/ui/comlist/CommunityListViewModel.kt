package org.fmm.communitymgmt.ui.comlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hilt_aggregated_deps._dagger_hilt_android_internal_managers_HiltWrapper_SavedStateHandleModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.domainlogic.usecase.GetCommunityList
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import javax.inject.Inject

/**
 * @HiltViewModel -> Para conectarse con DaggerHilt
 * @Inject -> Que le inyecten algo
 */
@HiltViewModel
class CommunityListViewModel @Inject constructor(private val getCommunityList: GetCommunityList):ViewModel(){

    private var _state =
        MutableStateFlow<CommunityListState>(CommunityListState.Loading)
    val state:StateFlow<CommunityListState> = _state

    private lateinit var communityList : List<AbstractRelationship>
    fun getData() {
        viewModelScope.launch {
            _state.value = CommunityListState.Loading
            val result = withContext(Dispatchers.IO) {
                getCommunityList()
            }
            if (result!=null) {
                _state.value = CommunityListState.Success(result)
            } else {
                _state.value = CommunityListState.Error("[FMMP] Ha ocurrido un error leyendo los " +
                        "datos")
            }
        }
    }
}