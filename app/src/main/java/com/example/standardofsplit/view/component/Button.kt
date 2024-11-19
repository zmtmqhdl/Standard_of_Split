package com.example.standardofsplit.view.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.view.theme.Shape
import com.example.standardofsplit.view.theme.Typography
import com.example.standardofsplit.viewmodel.Calculator
import com.example.standardofsplit.view.theme.Color

private object CustomButtonDefaults {
    val defaultShape = RoundedCornerShape(10.dp)
    val defaultModifier = Modifier.height(53.dp)
    val smallModifier = Modifier.height(33.dp)
    val defaultFontSize = 26.sp
    val smallFontSize = 13.sp
    val defaultFontWeight = FontWeight.Bold
}

@Composable
private fun basicButtonColor() = ButtonDefaults.buttonColors(
    containerColor = Color.Yellow,
    contentColor = Color.White
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
        text = text,
        style = Typography.SubmitButtonText
    )
}

@Composable
fun SubmitButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .height(53.dp)
            .width(223.dp),
        shape = Shape.RoundedCRectangle,
        colors = ButtonDefaults.buttonColors(Color.Yellow),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick,
    ) {
        Text(
            text = text,
            style = Typography.SubmitButtonText
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
        modifier = modifier
            .size(50.dp),
        shape = Shape.Circle,
        colors = ButtonDefaults.buttonColors(Color.Gray2),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = Typography.CircleButtonText
            )
        }
    }
}

@Preview
@Composable
fun Preview_SubmitButton() {
    SubmitButton(
        text = "확인",
        onClick = {}
    )
}

@Preview
@Composable
fun Preview_CircleButton() {

}


@Composable
fun Basic_Button2(
    content: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = CustomButtonDefaults.smallFontSize,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .then(CustomButtonDefaults.smallModifier)
            .wrapContentSize(),
        shape = CustomButtonDefaults.defaultShape,
        colors = circleButtonColors(),
    ) {
        ButtonText(
            text = content,
            fontSize = fontSize,
        )
    }
}

@Composable
fun Small_Button(
    content: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 13.sp,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(38.dp)
            .width(73.dp),
        shape = CustomButtonDefaults.defaultShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Yellow,
            contentColor = Color.White,
        )
    ) {
        ButtonText(text = content, fontSize = fontSize, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Elevated_Button(
    content1: String,
    content2: String,
    flag: Boolean,
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        shape = CustomButtonDefaults.defaultShape,
        colors = circleButtonColors(),
    ) {
        ButtonText(text = if (flag) content1 else content2, fontSize = 18.sp)
    }
}

@Composable
fun add_Button(
    content: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CustomButtonDefaults.defaultShape,
        colors = circleButtonColors(),
    ) {
        ButtonText(text = content, fontSize = 18.sp)
    }
}

@Composable
fun Toggle_Name_Button(
    viewModel: Calculator,
    modifier: Modifier = Modifier,
    initialText: String = "이름 변경 OFF",
    toggledText: String = "이름 변경 ON",
) {
    val isToggled by viewModel.changeMode.observeAsState()

    Button(
        onClick = { viewModel.toggleChangeMode() },
        shape = CustomButtonDefaults.defaultShape,
        modifier = modifier
            .then(CustomButtonDefaults.defaultModifier)
            .width(353.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isToggled == true) Color.Green else Color.Red,  // ON일 때 초록색, OFF일 때 빨간색
            contentColor = Color.White
        )
    ) {
        ButtonText(text = if (isToggled == true) toggledText else initialText)
    }
}

@Composable
fun Rectangle_Button(
    content: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = CustomButtonDefaults.defaultShape,
        modifier = modifier
            .then(CustomButtonDefaults.defaultModifier)
            .width(353.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray1,
            contentColor = Color.White
        )
    ) {
        ButtonText(text = content)
    }
}

@Composable
fun Toggle_Square_Button(
    result: Boolean,
    content: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 18.sp,
    onClick: () -> Unit,
) {
    val isDisabled = content == "X"

    Button(
        onClick = onClick,
        modifier = modifier.size(105.dp),
        shape = CustomButtonDefaults.defaultShape,
        enabled = !isDisabled,
        colors = ButtonDefaults.run {
            buttonColors(
                containerColor = when {
                    isDisabled -> Color.Black
                    result -> Color.Gray1
                    else -> Color.Gray1
                },
                contentColor = if (isDisabled) Color.Black else Color.White,
                disabledContainerColor = Color.Black,
                disabledContentColor = Color.Black
            )
        }
    ) {
        if (!isDisabled) {
            ButtonText(text = content, fontSize = fontSize)
        }
    }
}

@Composable
fun Square_Button(
    content: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(105.dp)
            .width(216.dp),
        shape = CustomButtonDefaults.defaultShape,
        colors = basicButtonColor()
    ) {
        ButtonText(text = content, fontSize = fontSize)
    }
}