package com.example.standardofsplit.View.Components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.standardofsplit.ui.theme.Gray
import com.example.standardofsplit.ui.theme.Red
import com.example.standardofsplit.ui.theme.White
import com.example.standardofsplit.ui.theme.Yellow
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private object DialogDefaults {
    val shape = RoundedCornerShape(10.dp)
    val backgroundColor = Color.DarkGray
    val dialogSize = Modifier.wrapContentSize()
    val contentPadding = 20.dp
    val buttonSpacing = 8.dp
    val topPadding = 0.dp
}

@Composable
private fun getTextFieldColors() = TextFieldDefaults.colors(
    unfocusedIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent
)

@Composable
fun Receipt_New_Dialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var newName by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isToastShowing by remember { mutableStateOf(false) }

    DialogContainer(onDismiss = onDismiss) {
        InputField("영수증 이름", newName) { newName = it }
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = {
                if (newName.length != 0) {
                    onConfirm(newName)
                } else if (!isToastShowing) {
                    isToastShowing = true
                    showCustomToast(
                        context = context,
                        message = "영수증 이름을 작성해주세요."
                    )
                    MainScope().launch {
                        kotlinx.coroutines.delay(2000)
                        isToastShowing = false
                    }
                }
            }
        )
    }
}

@Composable
private fun DialogContainer(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = DialogDefaults.shape,
            color = DialogDefaults.backgroundColor,
            modifier = DialogDefaults.dialogSize
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(DialogDefaults.contentPadding)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun DialogButtons(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = DialogDefaults.topPadding),
        horizontalArrangement = Arrangement.End
    ) {
        if (onDelete != null) {
            Small_Button(
                content = "삭제",
                onClick = onDelete,
                color = Red
            )
            Spacer(modifier = Modifier.width(50.dp))
        }
        Small_Button("취소", onClick = onDismiss)
        Spacer(modifier = Modifier.width(DialogDefaults.buttonSpacing))
        Small_Button(
            content = "확인",
            onClick = onConfirm,
        )
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Text(
        text = label,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 18.sp
    )
    TextField(
        value = value,
        onValueChange = onValueChange,
        shape = DialogDefaults.shape,
        modifier = Modifier
            .height(60.dp)
            .fillMaxHeight(),
        colors = getTextFieldColors(),
        textStyle = TextStyle(fontSize = 18.sp)
    )
    Spacer(modifier = Modifier.padding(0.5.dp))
}

@SuppressLint("DefaultLocale")
fun formatNumberWithCommas(number: String): String {
    if (number.isEmpty()) return ""
    return try {
        val longNumber = number.toLong()
        String.format("%,d", longNumber)
    } catch (e: NumberFormatException) {
        number
    }
}

@Composable
fun Reset_Confirm_Dialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = DialogDefaults.shape,
            color = DialogDefaults.backgroundColor,
            modifier = DialogDefaults.dialogSize
        ) {
            Column(
                modifier = Modifier.width(280.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "정산을 다시 하시겠습니까?\n정산 내역이 초기화됩니다.",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 20.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .drawBehind {
                                drawLine(
                                    color = Color.White,
                                    start = Offset(size.width, 0f),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 7f
                                )
                                drawLine(
                                    color = Color.White,
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 7f
                                )
                            },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray
                        ),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text("아니요", color = Color.White)
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .drawBehind {
                                drawLine(
                                    color = Color.White,
                                    start = Offset(0f, 0f),
                                    end = Offset(0f, size.height),
                                    strokeWidth = 7f
                                )
                                drawLine(
                                    color = Color.White,
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 7f
                                )
                            },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray
                        ),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text("네", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun Receipt_Add_Dialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    var newproductname by remember { mutableStateOf("") }
    var newprice by remember { mutableStateOf("") }
    var newquantity by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isToastShowing by remember { mutableStateOf(false) }

    DialogContainer(onDismiss = onDismiss) {
        InputField("상품명", newproductname) { newproductname = it }
        InputField("단가", formatNumberWithCommas(newprice)) { input ->
            newprice = input.replace(",", "").filter { it.isDigit() }
        }
        InputField("수량", newquantity) { input ->
            newquantity = input.filter { it.isDigit() }
        }
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = {
                if (newproductname.isNotEmpty() && newprice.isNotEmpty() && newquantity.isNotEmpty()) {
                    onConfirm(newproductname, newprice, newquantity)
                } else if (!isToastShowing) {
                    isToastShowing = true
                    showCustomToast(
                        context = context,
                        message = "모든 항목을 작성해주세요."
                    )
                    MainScope().launch {
                        kotlinx.coroutines.delay(2000)
                        isToastShowing = false
                    }
                }
            }
        )
    }
}

@Composable
fun Receipt_Name_Dialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    onDelete: () -> Unit,
    name: String
) {
    var newName by remember { mutableStateOf(name) }
    val isConfirmEnabled = newName.isNotBlank()
    val context = LocalContext.current
    var isToastShowing by remember { mutableStateOf(false) }

    DialogContainer(onDismiss = onDismiss) {
        InputField("영수증 이름", newName) { newName = it }
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = {
                if (newName.length != 0) {
                    onConfirm(newName)
                } else if (!isToastShowing) {
                    isToastShowing = true
                    showCustomToast(
                        context = context,
                        message = "영수증 이름을 작성해주세요."
                    )
                    MainScope().launch {
                        kotlinx.coroutines.delay(2000)
                        isToastShowing = false
                    }
                }
            },
            onDelete = onDelete
        )
    }
}

@Composable
fun Receipt_Change_Dialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
    onDelete: () -> Unit,
    productName: String,
    price: String,
    quantity: String,
) {
    var newproductname by remember { mutableStateOf(productName) }
    var newprice by remember { mutableStateOf(price) }
    var newquantity by remember { mutableStateOf(quantity) }
    val context = LocalContext.current
    var isToastShowing by remember { mutableStateOf(false) }

    DialogContainer(onDismiss = onDismiss) {
        InputField("상품명", newproductname) { newproductname = it }
        InputField("단가", formatNumberWithCommas(newprice)) { input ->
            newprice = input.replace(",", "").filter { it.isDigit() }
        }
        InputField("수량", newquantity) { input ->
            newquantity = input.filter { it.isDigit() }
        }
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = {
                if (newproductname.isNotEmpty() && newprice.isNotEmpty() && newquantity.isNotEmpty()) {
                    onConfirm(newproductname, newprice, newquantity)
                } else if (!isToastShowing) {
                    isToastShowing = true
                    showCustomToast(
                        context = context,
                        message = "모든 항목을 작성해주세요."
                    )
                    MainScope().launch {
                        kotlinx.coroutines.delay(2000)
                        isToastShowing = false
                    }
                }
            },
            onDelete = onDelete
        )
    }
}

@Composable
fun Button_Name_Dialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, String) -> Unit,
    name: String,
    index: Int
) {
    var newName by remember { mutableStateOf(name) }

    DialogContainer(onDismiss = onDismiss) {
        InputField("버튼 이름", newName) { newName = it }
        DialogButtons(
            onDismiss = onDismiss,
            onConfirm = { onConfirm(index, newName) }
        )
    }
}

@Composable
fun Receipt_Detail_Dialog(
    onDismiss: () -> Unit,
    name: String,
    receiptDetails: Map<String, Map<String, Int>>
) {
    val total = receiptDetails.values.sumOf { products -> 
        products.values.sum() 
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = DialogDefaults.shape,
            color = DialogDefaults.backgroundColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Yellow,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .width(500.dp)
                        .background(Gray, shape = RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    val entries = receiptDetails.entries.toList()
                    entries.forEachIndexed { index, (placeName, products) ->
                        item {
                            Text(
                                text = placeName,
                                fontSize = 24.sp,
                                color = White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            products.forEach { (productName, amount) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, bottom = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = productName,
                                        fontSize = 16.sp,
                                        color = White
                                    )
                                    Text(
                                        text = "${formatNumberWithCommas(amount.toString())}원",
                                        fontSize = 16.sp,
                                        color = White
                                    )
                                }
                            }
                            
                            if (index < entries.size - 1) {
                                Divider(
                                    color = White,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "총합: ${formatNumberWithCommas(total.toString())}원",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }

                Small_Button(
                    content = "확인",
                    onClick = onDismiss,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(240.dp)
                        .height(60.dp),  // 높이 증가
                    fontSize = 20.sp  // 글씨 크기 증가
                )
            }
        }
    }
}