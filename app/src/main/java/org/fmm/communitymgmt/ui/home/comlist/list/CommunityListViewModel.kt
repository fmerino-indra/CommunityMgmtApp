package org.fmm.communitymgmt.ui.home.comlist.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fmm.communitymgmt.domainlogic.usecase.GetCommunityList
import org.fmm.communitymgmt.domainmodels.model.AbstractRelationship
import org.fmm.communitymgmt.domainmodels.model.CommunityModel
import org.fmm.communitymgmt.ui.security.model.UserSession
import javax.inject.Inject

/**
 * @HiltViewModel -> Para conectarse con DaggerHilt
 * @Inject -> Que le inyecten algo
 */
@HiltViewModel
class CommunityListViewModel @Inject constructor(
    private val getCommunityList: GetCommunityList,
    private val _userSession: UserSession
):ViewModel(){
    val userSession: UserSession get() = _userSession

    private var _state =
        MutableStateFlow<CommunityListState>(CommunityListState.Loading)
    val state:StateFlow<CommunityListState> = _state

    private var _selectedCommunity = MutableStateFlow<CommunitySelectState>(CommunitySelectState.NotSelected)
    val selectedCommunity: StateFlow<CommunitySelectState> = _selectedCommunity

    private lateinit var communityList : List<AbstractRelationship>

    fun selectCommunity(community: CommunityModel) {
        _selectedCommunity.value = CommunitySelectState.Selected(community)
    }

    fun getData() {
        if (selectedCommunity.value !is CommunitySelectState.Selected) {
            return
        }
//        if (userSession.userInfo?.community == null)
//            return
//        require(userSession.isLoggedIn() && userSession.isCommunitySelected())

        viewModelScope.launch {
            _state.value = CommunityListState.Loading
            val result = withContext(Dispatchers.IO) {
                runCatching {
//                    getCommunityList(userSession.userInfo?.community?.id!!)
                    getCommunityList((selectedCommunity.value as CommunitySelectState.Selected).community.id!!)
                }.onSuccess {
                    _state.value = CommunityListState.Success(it)
                }.onFailure {
                    Log.e("CommunityListViewModel", "Error while Community List", it)
                    _state.value = CommunityListState.Error(
                        "Communication Error"
                    )

                }
            }
        }


        /*
        viewModelScope.launch {
            _state.value = CommunityListState.Loading
            val result = withContext(Dispatchers.IO) {
                getCommunityList()
            }
            if (result!=null) {
                _state.value = CommunityListState.Success(result)
            } else {
                _state.value = CommunityListState.Error(
                    "[FMMP] Ha ocurrido un error leyendo los " +
                            "datos"
                )
            }
        }

         */
    }
}