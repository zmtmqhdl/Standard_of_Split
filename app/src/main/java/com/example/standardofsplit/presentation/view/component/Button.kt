package com.example.standardofsplit.presentation.view.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.presentation.view.theme.Color
import com.example.standardofsplit.presentation.view.theme.Shape
import com.example.standardofsplit.presentation.view.theme.Typography
import com.example.standardofsplit.presentation.view.viewmodel.Calculator

private object CustomButtonDefaults {
    val defaultFontSize = 26.sp
    val defaultFontWeight = FontWeight.Bold
}

@Composable
private fun basicButtonColor() = ButtonDefaults.buttonColors(
    containerColor = Color.Yellow, contentColor = Color.White
)

@Composable
private fun ButtonText(
    text: String,
    fontSize: TextUnit = CustomButtonDefaults.defaultFontSize,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    fontWeight: FontWeight = CustomButtonDefaults.defaultFontWeight
) {
    Text(
        text = text, style = Typography.SubmitButtonText
    )
}

// Button

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
        colors = ButtonDefaults.buttonColors(Color.Yellow),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = text, style = Typography.SubmitButtonText
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
            .width(20.dp)
            .height(10.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(Color.Yellow),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = text, style = Typography.ProductAddButtonText
        )
    }
}

// 글씨 중앙 맞추기 고쳐야함
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
        colors = ButtonDefaults.buttonColors(Color.Gray1),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = text,
            style = Typography.CircleButtonText,
        )
    }
}

@Composable
fun NameChangeToggleButton(
    text1: String,
    text2: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: Calculator,
) {
    val isToggled by viewModel.changeMode.observeAsState()
//    viewModel.toggleChangeMode()
    Button(
        onClick = onClick,
        modifier = modifier
            .width(353.dp)
            .height(50.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(
            if (isToggled == true) Color.Green else Color.Red
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = if (isToggled == true) text2 else text1,
            style = Typography.NameChangeToggleButton,
        )
    }
}

@Composable
fun PersonSelectButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    flag: Boolean,
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
                    flag -> Color.Gray1
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
                style = Typography.PersonSelectButton,
            )
        }
    }
}

@Composable
fun ReceiptAddButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(50.dp)
            .height(25.dp),
        shape = Shape.RoundedCRectangle,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            style = Typography.ReceiptAddButtonText,
        )
    }
}

@Composable
fun ReceiptOpenCloseButton(
    text1: String, text2: String, onClick: () -> Unit, modifier: Modifier = Modifier, flag: Boolean
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier,
        shape = Shape.RoundedCRectangle,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = if (flag) text1 else text2, style = Typography.ReceiptOpenCloseButtonText
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
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray1, contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, style = Typography.FunctionButtonText)
    }
}

@Composable
fun CalculateButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(216.dp)
            .height(105.dp),
        shape = Shape.RoundedCRectangle,
        colors = basicButtonColor(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, style = Typography.CalculatorButtonText)
    }
}

@Composable
fun DialogButton(
    text: String, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(38.dp)
            .width(73.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Yellow,
            contentColor = Color.White,
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, style = Typography.DialogButtonText)
    }
}

// Preview

@Preview
@Composable
fun Preview_SubmitButton() {
    SubmitButton(text = "제출", onClick = {})
}

@Preview
@Composable
fun Preview_ProductAddButton() {
    ProductAddButton(text = "제출", onClick = {})
}

@Preview
@Composable
fun Preview_CircleButton() {
    CircleButton(text = "+", onClick = {})
}

//@Preview
//@Composable
//fun Preview_NameChangeToggleButton() {
//    ChangeButton(text1 = "변화 전", text2 = "변화 후", onClick = {})
//}

@Preview
@Composable
fun Preview_PersonSelectButton() {
    PersonSelectButton(text = "인원", onClick = {}, flag = false)
}

@Preview
@Composable
fun Preview_ReceiptAddButton() {
    ReceiptAddButton(text = "영수증 추가", onClick = {})
}

@Preview
@Composable
fun Preview_ReceiptOpenCloseButton() {
    ReceiptOpenCloseButton(text1 = "열기", text2 = "접기", onClick = {}, flag = false)
}

@Preview
@Composable
fun Preview_FunctionButton() {
    FunctionButton(text = "기능", onClick = {})
}

@Preview
@Composable
fun Preview_CalculateButton() {
    CalculateButton(text = "정산 완료", onClick = {})
}

@Preview
@Composable
fun Preview_DialogButton() {
    DialogButton(text = "확인", onClick = {})
}