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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListScreenModel
@Inject
constructor(
    private val repository: HelloRepository,
    @Named("key") private val key: String,
    val state: SavedStateHandle,
) : ViewModel() {

    val hellos: MutableState<List<HelloEntity>> = mutableStateOf(ArrayList())

    val loading = mutableStateOf(false)

    init {
        viewModelScope.launch {
            try {
                repository.loadMore(key = key, lastId = -1).onEach { dataState ->
                    loading.value = dataState.loading

                    dataState.data?.let { list ->
                        hellos.value = list
                    }

                    dataState.error?.let { error ->
                        Log.e(TAG, "newSearch: $error")
                    }
                }.launchIn(viewModelScope)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadMore(lastId: Int) {
        repository.loadMore(key = key, lastId = lastId).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                val current = ArrayList(this.hellos.value)
                current.addAll(list)
                this.hellos.value = current
            }

            dataState.error?.let { error ->
                Log.e(TAG, "nextPage: $error")
            }
        }.launchIn(viewModelScope)
    }

}