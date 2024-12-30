package com.example.standardofsplit.presentation.viewModel

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.View
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.ViewModel
import com.example.standardofsplit.presentation.ui.component.showCustomToast
import com.example.standardofsplit.presentation.ui.theme.Typography
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor() : ViewModel() {

    private val _showDetailDialog = MutableStateFlow(false)
    val showDetailDialog: StateFlow<Boolean> = _showDetailDialog

    private val _showResetDialog = MutableStateFlow(false)
    val showResetDialog: StateFlow<Boolean> = _showResetDialog

    private val _accountText = MutableStateFlow("계좌 입력")
    val accountText: StateFlow<String> = _accountText

    private val _showAccountDialog = MutableStateFlow(false)
    val showAccountDialog: StateFlow<Boolean> = _showAccountDialog

    fun changeAccountTextStyle(): TextStyle {
        return if (_accountText.value == "계좌 입력") {
            Typography.accountBeforeStyle
        } else {
            Typography.accountAfterStyle
        }
    }

    fun changeResetDialog() {
        _showResetDialog.value = !_showResetDialog.value
    }

    fun changeAccountDialog() {
        _showAccountDialog.value = !_showAccountDialog.value
    }

    fun changeDetailDialog() {
        _showDetailDialog.value = !_showDetailDialog.value
    }

    fun accountTextUpdate(newName: String) {
        _accountText.value = newName
    }

    fun capture(context: Context, rootView: View) {
        val bitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        rootView.draw(canvas)

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "${System.currentTimeMillis()}_정산내역.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/StandardOfSplit")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val contentResolver = context.contentResolver
        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (imageUri != null) {
            try {
                val outputStream: OutputStream? = contentResolver.openOutputStream(imageUri)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()
                    values.clear()
                    values.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(imageUri, values, null, null)

                    showCustomToast(message = "정산 내역이 갤러리에 저장되었습니다.", context = context)

                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, imageUri)
                        type = "image/png"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "이미지 공유"))
                } else {
                    showCustomToast(message = "파일을 열 수 없습니다.", context = context)
                }
            } catch (e: IOException) {
                showCustomToast(message = "정산 내역 저장에 실패하였습니다.", context = context)
            }
        } else {
            showCustomToast(message = "정산 내역 저장에 실패하였습니다.", context = context)
        }
    }
}
