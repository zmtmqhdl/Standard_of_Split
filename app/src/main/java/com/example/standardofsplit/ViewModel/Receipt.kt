package com.example.standardofsplit.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.standardofsplit.Model.ReceiptClass

class Receipt : ViewModel() {

    private val _receipts = MutableLiveData<MutableList<ReceiptClass>>(mutableListOf())
    val receipts: LiveData<MutableList<ReceiptClass>> = _receipts

    init {
        val defaultReceipt = ReceiptClass(
            ReceiptNumber = 0,
            PlaceName = "영수증",
            ProductName = mutableListOf("상품"),
            ProductQuantity = mutableListOf("0"),
            ProductPrice = mutableListOf("0")
        )
        _receipts.value?.add(defaultReceipt)
    }

    fun addReceipt(receipt: ReceiptClass) {
        val currentList = _receipts.value?.toMutableList() ?: mutableListOf()
        currentList.add(receipt)
        _receipts.value = currentList
    }

    fun updateReceiptName(index: Int, newName: String) {
        val currentList = _receipts.value?.toMutableList() ?: mutableListOf()
        currentList[index].PlaceName = newName
        _receipts.value = currentList
    }

    fun updateReceiptDetail(index: Int, iindex: Int, productName: String, productQuantity: String, productPrice: String) {
        val currentList = _receipts.value?.toMutableList() ?: mutableListOf()
        currentList[index].ProductName[iindex] = productName
        currentList[index].ProductQuantity[iindex] = productQuantity
        currentList[index].ProductPrice[iindex] = productPrice
        _receipts.value = currentList
    }

    fun updateAddReceipt(index: Int, productName: String, productQuantity: String, productPrice: String) {
        val currentList = _receipts.value?.toMutableList() ?: mutableListOf()
        currentList[index].ProductName.add(productName)
        currentList[index].ProductQuantity.add(productQuantity)
        currentList[index].ProductPrice.add(productPrice)
    }
}