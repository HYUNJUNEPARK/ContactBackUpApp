package com.canbe.contactbackup.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.canbe.contactbackup.ui.theme.CustomDefaultButton

@Composable
fun CustomCloseButtonDialog(
    title: String? = null,
    content: String,
    buttonText: String,
    onButtonRequest: () -> Unit,
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
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (title != null) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = title,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        modifier = Modifier.size(24.dp).align(Alignment.CenterEnd),
                        onClick = onDismissRequest
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "뒤로가기",
                        )
                    }
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
                    CustomDefaultButton(buttonText) {
                        onButtonRequest()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomCloseButtonDialogPreview() {
    CustomCloseButtonDialog(
        title = "Title",
        content = "Content",
        buttonText = "Button",
        onButtonRequest = {},
        onDismissRequest = {}
    )
}