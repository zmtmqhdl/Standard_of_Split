package com.example.standardofsplit.View.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.ViewModel.Calculator
import com.example.standardofsplit.ui.theme.LightPurple
import com.example.standardofsplit.ui.theme.DarkPurple

private object CustomButtonDefaults {
    val defaultShape = RoundedCornerShape(10.dp)
    val defaultColor = LightPurple
    val selectedColor = DarkPurple
    val defaultModifier = Modifier.height(53.dp)
    val defaultFontSize = 26.sp
    val defaultFontWeight = FontWeight.Bold
}

@Composable
private fun getDefaultButtonColors() = ButtonDefaults.buttonColors(
    containerColor = CustomButtonDefaults.defaultColor,
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
        fontSize = fontSize,
        textAlign = textAlign,
        color = Color.White,
        fontWeight = fontWeight,
        modifier = modifier
    )
}

@Composable
fun Basic_Button(
    content: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = CustomButtonDefaults.defaultFontSize,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .then(CustomButtonDefaults.defaultModifier)
            .width(353.dp),
        shape = CustomButtonDefaults.defaultShape,
        colors = getDefaultButtonColors()
    ) {
        ButtonText(
            text = content,
            fontSize = fontSize,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun Small_Button(
    content: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 13.sp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(33.dp)
            .width(73.dp),
        shape = CustomButtonDefaults.defaultShape,
        colors = getDefaultButtonColors()
    ) {
        ButtonText(text = content, fontSize = fontSize)
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
        colors = getDefaultButtonColors()
    ) {
        ButtonText(text = if (flag) content1 else content2)
    }
}

@Composable
fun Circle_Button(
    content: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    fontSize: TextUnit = 36.sp,
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier.size(50.dp),
        colors = getDefaultButtonColors(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ButtonText(
                text = content,
                fontSize = fontSize,
                modifier = Modifier.wrapContentHeight()
            )
        }
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
        colors = getDefaultButtonColors()
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
        colors = getDefaultButtonColors()
    ) {
        ButtonText(text = content)
    }
}

@Composable
fun Toggle_Square_Button(
    result: Boolean,
    content: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    onClick: () -> Unit,
) {
    Button(
        onClick = { if (content != "X") onClick() },
        modifier = modifier
            .height(105.dp)
            .width(105.dp),
        shape = CustomButtonDefaults.defaultShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (result) CustomButtonDefaults.selectedColor else CustomButtonDefaults.defaultColor,
            contentColor = Color.White
        )
    ) {
        ButtonText(text = content, fontSize = fontSize)
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
        colors = getDefaultButtonColors()
    ) {
        ButtonText(text = content, fontSize = fontSize)
    }
}