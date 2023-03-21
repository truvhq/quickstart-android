package com.example.truvpoc

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.truvpoc.api.data.Employment
import com.example.truvpoc.api.NetworkModule
import com.truv.TruvEventsListener
import com.truv.models.TruvEventPayload
import com.truv.models.TruvSuccessPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {

    private val api = NetworkModule.api

    private val productType = BuildConfig.truvProductType

    private val _userId = MutableStateFlow<String>("")
    private val userId = _userId.asStateFlow()

    private val _bridgeToken = MutableStateFlow<String?>(null)
    val bridgeToken = _bridgeToken.asStateFlow()

    private val _employmentInfo = MutableStateFlow<Employment?>(null)
    val employmentInfo = _employmentInfo.asStateFlow()

    private val _incomeInfo = MutableStateFlow<Employment?>(null)
    val incomeInfo = _incomeInfo.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    init {
        getBridgeToken()
    }

    private fun getBridgeToken() {
        Log.d(TAG, "getBridgeToken")

        viewModelScope.launch {
            flowOf(api.createUser(UUID.randomUUID().toString()))
                .flowOn(Dispatchers.IO)
                .catch {
                    _error.emit("Can't create a user")
                }
                .collect {
                    _userId.emit(it.id)
                }

            Log.d(TAG, "created user with id: ${userId.value}")

            flowOf(api.getBridgeToken(userId = userId.value, productType = BuildConfig.truvProductType))
                .flowOn(Dispatchers.IO)
                .catch {
                    _error.emit("Issue with Bridge Token")
                }
                .collect {
                    _bridgeToken.emit(it.bridgeToken)
                }

        }
    }

    fun getAccessToken(publicToken: String) {
        Log.d(TAG, "getAccessToken")

        viewModelScope.launch {
            flowOf(api.getAccessToken(publicToken))
                .flowOn(Dispatchers.IO)
                .catch {
                    _error.emit("Issue with Access Token")
                }
                .collect {
                    viewModelScope.launch {
                        if (productType == "employment") {
                            getEmploymentVerification(it.accessToken)
                        } else {
                            getIncomeVerification(it.accessToken)
                        }
                    }
                }
        }
    }

    private fun getEmploymentVerification(accessToken: String) {
        Log.d(TAG, "getEmploymentVerification")

        viewModelScope.launch {
            flowOf(api.getEmploymentInfoByToken(accessToken))
                .flowOn(Dispatchers.IO)
                .catch {
                    _error.emit("Issue with Employment verification")
                }
                .collect {
                    Log.d(TAG, "getEmploymentVerification: $it")
                    _employmentInfo.emit(it.getEmployment())
                }
        }
    }

    private fun getIncomeVerification(accessToken: String) {
        Log.d(TAG, "getIncomeVerification")

        viewModelScope.launch {
            flowOf(api.getIncomeInfoByToken(accessToken))
                .flowOn(Dispatchers.IO)
                .catch {
                    _error.emit("Issue with Income verification")
                }
                .collect {
                    _incomeInfo.emit(it.getEmployment())
                }
        }
    }

    val truvEventsListener = object : TruvEventsListener {

        override fun onClose() {
            Log.d(TAG, "Bridge Closed")
        }

        override fun onError() {
            Log.e(TAG, "Bridge Error")
        }

        override fun onEvent(event: TruvEventPayload.EventType) {
            Log.d(TAG, "Event: $event")
        }

        override fun onLoad() {
            Log.d(TAG, "Bridge Loaded")
        }

        override fun onSuccess(payload: TruvSuccessPayload) {
            Log.d(TAG, "Bridge Success")
            getAccessToken(payload.publicToken)
        }

    }

    companion object {
        private const val TAG = "Truv"
    }

}