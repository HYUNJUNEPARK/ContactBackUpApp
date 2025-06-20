package com.canbe.contactbackup.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.canbe.contactbackup.ui.model.ContactUiModel

@Composable
fun CustomDefaultButton(
    text: String,
    buttonColor: Color = AppTheme,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        ),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            style = FixedTextStyle(14.sp)
        )
    }
}

@Composable
fun ContactItem(
    contactUiModel: ContactUiModel,
    onItemClick: (contactUiModel: ContactUiModel) -> Unit
) {
    val color = remember { backColors.random() }

    Box(
        modifier = Modifier.clickable {
            onItemClick(contactUiModel)
        }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (contactUiModel.profileUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(contactUiModel.profileUri),
                    contentDescription = "프로필 이미지",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(38.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "기본 프로필 아이콘",
                        tint = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp)
            ) {
                Text(text = contactUiModel.name, fontSize = 16.sp)
                Text(text = contactUiModel.numbers[0], fontSize = 12.sp)
                HorizontalDivider(thickness = 0.2.dp, color = Color.Gray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomStyledButtonPreview() {
    CustomDefaultButton("Default Button") {}
}

@Preview(showBackground = true)
@Composable
fun ContactItemPreview() {
    ContactItem(
        contactUiModel = ContactUiModel(
            id = "12",
            name = "ABC",
            numbers = listOf("123-123-123"),
            organization = null,
            note = null,
            profileUri = null
        )
    ) {}
}