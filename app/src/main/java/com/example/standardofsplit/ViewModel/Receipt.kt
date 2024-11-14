package com.example.standardofsplit.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.standardofsplit.Model.ReceiptClass

class Receipt : ViewModel() {
    private val _receipts = MutableLiveData<MutableList<ReceiptClass>>(mutableListOf())
    val receipts: LiveData<MutableList<ReceiptClass>> = _receipts

    companion object {
        private val DEFAULT_RECEIPT = ReceiptClass(
            ReceiptNumber = 0,
            PlaceName = "영수증",
            ProductName = mutableListOf("상품"),
            ProductPrice = mutableListOf("0"),
            ProductQuantity = mutableListOf("0")
        )
    }

    init {
        _receipts.value?.add(DEFAULT_RECEIPT)
    }

    fun addReceipt(receipt: ReceiptClass) {
        _receipts.value = (_receipts.value?.toMutableList() ?: mutableListOf()).apply {
            add(receipt)
        }
    }

    fun updateReceiptName(index: Int, newName: String) {
        _receipts.value = _receipts.value?.toMutableList()?.apply {
            this[index].PlaceName = newName
        }
    }

    fun updateReceiptDetail(
        index: Int,
        iindex: Int,
        productName: String,
        productQuantity: String,
        productPrice: String
    ) {
        _receipts.value = _receipts.value?.toMutableList()?.apply {
            this[index].apply {
                ProductName[iindex] = productName
                ProductQuantity[iindex] = productQuantity
                ProductPrice[iindex] = productPrice
            }
        }
    }

    fun updateAddReceipt(
        index: Int,
        productName: String,
        productQuantity: String,
        productPrice: String
    ) {
        _receipts.value = _receipts.value?.toMutableList()?.apply {
            this[index].apply {
                ProductName.add(productName)
                ProductQuantity.add(productQuantity)
                ProductPrice.add(productPrice)
            }
        }
    }
}