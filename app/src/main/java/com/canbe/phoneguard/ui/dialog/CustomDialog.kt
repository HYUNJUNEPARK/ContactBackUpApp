package com.canbe.phoneguard.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.canbe.phoneguard.ui.theme.CustomStyledButton

@Composable
fun CustomDialog(
    title: String? = null,
    content: String,
    isSingleButton: Boolean = true,
    leftButtonText: String,
    onLeftButtonRequest: () -> Unit,
    rightButtonText: String,
    onRightButtonRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (title != null) {
                    Text(text = title, fontWeight = FontWeight.Bold)
                }

                Text(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 22.dp),
                    textAlign = TextAlign.Center,
                    text = content
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                ) {
                    CustomStyledButton(leftButtonText) {
                        onLeftButtonRequest()
                    }

                    if(!isSingleButton) {
                        CustomStyledButton(rightButtonText) {
                            onRightButtonRequest()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomDialogPreview() {
    CustomDialog(
        title = "Title",
        content = "Content",
        isSingleButton = false,
        leftButtonText = "Left Button",
        rightButtonText = "Right Button",
        onLeftButtonRequest = {},
        onRightButtonRequest = {},
        onDismissRequest = {}
    )
}