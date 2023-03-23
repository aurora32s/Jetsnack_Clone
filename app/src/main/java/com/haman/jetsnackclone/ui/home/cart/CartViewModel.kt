package com.haman.jetsnackclone.ui.home.cart

import androidx.lifecycle.ViewModel
import com.haman.jetsnackclone.R
import com.haman.jetsnackclone.model.OrderLine
import com.haman.jetsnackclone.model.SnackRepo
import com.haman.jetsnackclone.model.SnackbarManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel(
    private val snackbarManager: SnackbarManager,
    snackRepository: SnackRepo
) : ViewModel() {

    private val _orderLines: MutableStateFlow<List<OrderLine>> =
        MutableStateFlow(snackRepository.getCart())
    val orderLines: StateFlow<List<OrderLine>> = _orderLines.asStateFlow()

    private var requestCount = 0
    private fun shouldRandomlyFail(): Boolean = requestCount % 5 == 0

    fun increaseSnackCount(snackId: Long) {
        if (shouldRandomlyFail().not()) {
            val currentCount = _orderLines.value.first { it.snack.id == snackId }.count
            updateSnackCount(snackId, currentCount + 1)
        } else {
            snackbarManager.showMessage(R.string.cart_increase_error)
        }
    }

    fun decreaseSnackCount(snackId: Long) {
        if (shouldRandomlyFail().not()) {
            val currentCount = _orderLines.value.first { it.snack.id == snackId }.count
            if (currentCount == 1) {
                removeSnack(snackId)
            } else {
                updateSnackCount(snackId, currentCount - 1)
            }
        } else {
            snackbarManager.showMessage(R.string.cart_decrease_error)
        }
    }

    fun removeSnack(snackId: Long) {
        _orderLines.value = _orderLines.value.filter { it.snack.id != snackId }
    }

    private fun updateSnackCount(snackId: Long, count: Int) {
        _orderLines.value = _orderLines.value.map {
            if (it.snack.id == snackId) it.copy(count = count)
            else it
        }
    }

    companion object {
    }
}