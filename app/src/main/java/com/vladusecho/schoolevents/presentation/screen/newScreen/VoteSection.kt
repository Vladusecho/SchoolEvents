package com.vladusecho.schoolevents.presentation.screen.newScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vladusecho.schoolevents.R
import com.vladusecho.schoolevents.domain.entity.Vote
import com.vladusecho.schoolevents.presentation.ui.theme.EventsFontFamily

@Composable
fun VoteSection(
    likes: Int,
    dislikes: Int,
    userVote: Vote,
    onVoteClick: (Vote) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onVoteClick(if (userVote == Vote.LIKE) Vote.NONE else Vote.LIKE) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_heart),
                    contentDescription = "Like",
                    tint = if (userVote == Vote.LIKE) Color.Red else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = likes.toString(),
                fontFamily = EventsFontFamily,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onVoteClick(if (userVote == Vote.DISLIKE) Vote.NONE else Vote.DISLIKE) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_broken_heart),
                    contentDescription = "Dislike",
                    tint = if (userVote == Vote.DISLIKE) Color.DarkGray else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = dislikes.toString(),
                fontFamily = EventsFontFamily,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}
