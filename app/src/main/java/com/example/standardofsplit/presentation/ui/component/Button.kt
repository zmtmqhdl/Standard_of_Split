package com.example.standardofsplit.presentation.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.standardofsplit.presentation.ui.theme.Color
import com.example.standardofsplit.presentation.ui.theme.Shape
import com.example.standardofsplit.presentation.ui.theme.Typography

@Composable
fun SubmitButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(223.dp)
            .height(53.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(Color.Orange),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = text, style = Typography.submitButtonTextStyle
        )
    }
}

@Composable
fun ProductAddButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(80.dp)
            .height(30.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(Color.Gray2),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = text, style = Typography.productAddButtonTextStyle
        )
    }
}

@Composable
fun CircleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(50.dp),
        shape = Shape.Circle,
        colors = ButtonDefaults.buttonColors(Color.Gray2),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = text,
            style = Typography.circleButtonTextStyle,
        )
    }
}

@Composable
fun NameChangeToggleButton(
    text1: String,
    text2: String,
    onClick: () -> Unit,
    changeMode: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(353.dp)
            .height(50.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(
            if (changeMode) Color.Green else Color.Red
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = if (changeMode) text2 else text1,
            style = Typography.nameChangeToggleButtonStyle,
        )
    }
}

@Composable
fun PersonSelectButton(
    text: String,
    state: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDisabled = text == "X"
    Button(
        onClick = onClick,
        modifier = modifier.size(105.dp),
        shape = Shape.RoundedCRectangle,
        enabled = !isDisabled,
        colors = ButtonDefaults.run {
            buttonColors(
                containerColor = when {
                    isDisabled -> Color.Black
                    state -> Color.Gray1
                    else -> Color.Gray1
                },
                contentColor = if (isDisabled) Color.Black else Color.White,
                disabledContainerColor = Color.Black,
                disabledContentColor = Color.Black
            )
        },
        contentPadding = PaddingValues(0.dp)
    ) {
        if (!isDisabled) {
            Text(
                text = text,
                style = Typography.personSelectButtonStyle,
            )
        }
    }
}

@Composable
fun AddButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(110.dp)
            .height(30.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(Color.Gray2),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            style = Typography.addButtonTextStyle,
        )
    }
}

@Composable
fun ReceiptOpenCloseButton(
    text1: String, text2: String, onClick: () -> Unit, modifier: Modifier = Modifier, flag: Boolean
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier
            .width(130.dp)
            .height(40.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(Color.Gray2),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = if (flag) text1 else text2, style = Typography.receiptOpenCloseButtonTextStyle
        )
    }
}

@Composable
fun FunctionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(353.dp)
            .height(53.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(Color.Gray1),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, style = Typography.functionButtonTextStyle)
    }
}

@Composable
fun CalculateButton(
    onClick: () -> Unit,
    check: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(216.dp)
            .height(105.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray1, contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = if (check) "정산 완료" else "적용",
            style = Typography.calculatorButtonTextStyle
        )
    }
}

@Composable
fun DialogButton(
    text: String, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(35.dp)
            .width(63.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Orange,
            contentColor = Color.White,
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, style = Typography.dialogButtonTextStyle)
    }
}

@Composable
fun DialogDeleteButton(
    text: String, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(35.dp)
            .width(63.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,
            contentColor = Color.White,
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, style = Typography.dialogButtonTextStyle)
    }
}