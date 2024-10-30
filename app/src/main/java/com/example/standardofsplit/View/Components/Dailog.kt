package com.example.standardofsplit.View.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun Receipt_Add_Dialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    index: Int
) {
    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            modifier = Modifier
                .width(500.dp)
                .height(400.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(20.dp)
            ) {
                InputField("상품명", productName) { productName = it }
                InputField("단가", price) { price = it }
                InputField("수량", quantity) { quantity = it }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Small_Button("취소", onClick = onDismiss)
                    Spacer(modifier = Modifier.width(8.dp))
                    Small_Button("확인", onClick = onConfirm)
                }
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Text(text = label, fontWeight = FontWeight.Bold)
    TextField(
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(50.dp)
            .fillMaxHeight(),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(fontSize = 16.sp)
    )
    Spacer(modifier = Modifier.padding(0.5.dp))
}

@Composable
fun Receipt_Name_Dialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    name: String
) {
    var newName by remember { mutableStateOf(name) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            modifier = Modifier
                .width(500.dp)
                .height(400.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(20.dp)
            ) {
                InputField("영수증 이름", newName) { newName = it }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Small_Button("취소", onClick = onDismiss)
                    Spacer(modifier = Modifier.width(8.dp))
                    Small_Button("확인", onClick = onConfirm)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewMyApp() {
    // Dialog를 열기 위한 상태를 직접 설정
    Receipt_Add_Dialog(
        onDismiss = {},
        onConfirm = {},
        index = 0// 직접 Boolean 값으로 전달
    )
}