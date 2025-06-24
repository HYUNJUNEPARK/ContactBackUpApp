package com.canbe.contactbackup.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.canbe.contactbackup.ui.theme.AppTheme
import com.canbe.contactbackup.ui.theme.CustomDefaultButton
import com.canbe.contactbackup.ui.theme.Mint

@Composable
fun CustomDefaultDialog(
    title: String? = null,
    content: String,
    buttonColor: Color = AppTheme,
    isRightButtonVisible: Boolean = true,
    leftButtonText: String,
    onLeftButtonRequest: () -> Unit,
    rightButtonText: String? = null,
    onRightButtonRequest: (() -> Unit)? = null,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
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
                    CustomDefaultButton(leftButtonText, buttonColor) {
                        onLeftButtonRequest()
                    }

                    if(isRightButtonVisible) {
                        CustomDefaultButton(rightButtonText, buttonColor) {
                            onRightButtonRequest?.invoke()
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
    CustomDefaultDialog(
        title = "Title",
        content = "Content",
        buttonColor = AppTheme,
        isRightButtonVisible = true,
        leftButtonText = "Left Button",
        rightButtonText = "Right Button",
        onLeftButtonRequest = {},
        onRightButtonRequest = {},
        onDismissRequest = {}
    )
}