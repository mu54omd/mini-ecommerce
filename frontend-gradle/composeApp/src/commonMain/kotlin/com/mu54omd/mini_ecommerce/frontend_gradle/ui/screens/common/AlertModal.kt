package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertModal(
    message: String,
    onConfirmClick: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onConfirmClick,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RectangleShape,
        dragHandle = null,
        scrimColor = Color.Black.copy(alpha = .5f),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceBright,
                        shape = RoundedCornerShape(5)
                    )
                    .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            ) {
                Text(
                    text = message,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(0.8f).basicMarquee()
                )
                TextButton(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            onConfirmClick()
                        }
                    },
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand).weight(0.2f)
                ) {
                    Text("Ok", maxLines = 1)
                }
            }
        }
    }
}
