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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun Receipt_Add_Dialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    openDialog: Boolean
//    openDialog: MutableState<Boolean>
) {
    if (openDialog) {
        Dialog(onDismissRequest = { onDismiss }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                modifier = Modifier
                    .width(500.dp)
                    .height(400.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(text = "상품명", fontWeight = FontWeight.Bold)
                    TextField(
                        value = "",
                        onValueChange = {},
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxHeight(),
                        placeholder = {
                            Text(
                                text = "상품명 입력",
                                fontSize = 12.sp,
                                color = Color.Gray,
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.padding(0.5.dp))
                    Text(text = "단가", fontWeight = FontWeight.Bold)
                    TextField(
                        value = "",
                        onValueChange = {},
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(50.dp),
                        placeholder = {
                            Text(
                                text = "상품명 입력",
                                fontSize = 12.sp,  // 원하는 크기로 설정
                                color = Color.Gray  // 색상 설정 (선택적)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.padding(0.5.dp))
                    Text(text = "수량", fontWeight = FontWeight.Bold)
                    TextField(
                        value = "",
                        onValueChange = {},
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(50.dp),
                        placeholder = {
                            Text(
                                text = "상품명 입력",
                                fontSize = 12.sp,  // 원하는 크기로 설정
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Small_Button(
                            content = "취소",
                            onClick = onDismiss
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Small_Button(
                            content = "확인",
                            onClick = onConfirm
                        )
                    }
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
        openDialog = true // 직접 Boolean 값으로 전달
    )
}