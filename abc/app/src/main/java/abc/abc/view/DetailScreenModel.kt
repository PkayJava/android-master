package abc.abc.view

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import abc.abc.common.TAG
import abc.abc.entity.HelloEntity
import abc.abc.repository.HelloRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltViewModel
class DetailScreenModel
@Inject
constructor(
    private val repository: HelloRepository,
    @Named("key") private val key: String,
    private val state: SavedStateHandle,
) : ViewModel() {

    val hello: MutableState<HelloEntity?> = mutableStateOf(null)

    val loading = mutableStateOf(false)

    val onLoad: MutableState<Boolean> = mutableStateOf(false)

    init {

    }

    fun loadDetail(entityId: Int) {
        viewModelScope.launch {
            try {
                if (hello.value == null) {
                    repository.loadDetail(entityId = entityId).onEach { dataState ->
                        loading.value = dataState.loading

                        dataState.data?.let { entity ->
                            hello.value = entity
                        }

                        dataState.error?.let { error ->
                            Log.e(TAG, "newSearch: $error")
                        }
                    }.launchIn(viewModelScope)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
