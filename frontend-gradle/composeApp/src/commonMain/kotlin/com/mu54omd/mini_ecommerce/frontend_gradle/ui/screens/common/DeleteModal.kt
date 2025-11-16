package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import frontend_gradle.composeapp.generated.resources.Res
import frontend_gradle.composeapp.generated.resources.delete_warning_alert
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteModal(
    id: Long,
    onCancelClick: () -> Unit,
    onConfirmClick: (Long) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onCancelClick,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RectangleShape,
        dragHandle = null,
        scrimColor = Color.Black.copy(alpha = 0.5f),
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
                    .padding(start = 10.dp, end = 10.dp)
                    .width(350.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceBright,
                        shape = RoundedCornerShape(20)
                    )
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning Delete Icon",
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.errorContainer
                )
                Spacer(Modifier.width(30.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.delete_warning_alert),
                        textAlign = TextAlign.Center
                    )
                    Row() {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    sheetState.hide()
                                    onCancelClick()
                                }
                            },
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = {
                                onConfirmClick(id)
                            },
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
