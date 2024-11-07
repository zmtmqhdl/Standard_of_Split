import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standardofsplit.View.Components.Basic_Button
import com.example.standardofsplit.View.Components.Circle_Button
import com.example.standardofsplit.ViewModel.Start
import com.example.standardofsplit.ui.theme.StandardOfSplitTheme

@Composable
fun StartScreen(
    start: Start,
    intentToReceiptActivity: () -> Unit
) {
    val personcount by start.personCount.observeAsState()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .offset(y = 250.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Circle_Button(
                content = "-",
                onClick = { start.decrement() }
            )
            Text(
                text = "$personcount",
                modifier = Modifier.padding(horizontal = 40.dp),
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDCD0FF)
            )
            Circle_Button(
                content = "+",
                onClick = { start.increment() }
            )
        }

        // 0xFFDCD0FF 연한색
        // 0xFF9B87CB 진한색

        
        Text(text = "※ 인원 수를 선택해주세요 ※", fontSize = 20.sp, color = Color(0xFF9B87CB))
        Spacer(modifier = Modifier.height(50.dp))

        Basic_Button(
            content = "시작하기",
            onClick = {
                intentToReceiptActivity()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StartPreview() {
    val dummyStart = Start()
    StandardOfSplitTheme {
        StartScreen(
            start = dummyStart,
            intentToReceiptActivity = {}
        )
    }
}