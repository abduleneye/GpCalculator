package com.example.gpcalculator.presentation.course_list_screen_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gpcalculator.ui.theme.Cream
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopAppBarDropDownMenu(
    onEvent: (DialogBoxUiEvents) -> Unit
) {
    var optionsMenuState by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )


    TopAppBar(

        title = {

            Text(text = "GpCalculator")
        },
        actions = {
            IconButton(onClick = {

                optionsMenuState = !optionsMenuState

            }) {
                Icon(Icons.Default.MoreVert, contentDescription = "more")

            }

            DropdownMenu(
                expanded = optionsMenuState, onDismissRequest = {
                    optionsMenuState = false
                },
                modifier = Modifier
                    .background(Cream),
                offset = DpOffset(0.0.dp, 2.0.dp)
            ) {
//Reset Option Menu Item
                DropdownMenuItem(
                    onClick = {


                        onEvent(DialogBoxUiEvents.resetTotalEntries)
                        optionsMenuState = !optionsMenuState
                        scope.launch {
                            if (sheetState.isExpanded) {
                                sheetState.collapse()
                            }
                        }

                    },
                    modifier = Modifier
                        .background(Cream)
                        .height(35.dp),


                    ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = "Reset",
                            fontSize = 16.sp,
                            style = TextStyle(baselineShift = BaselineShift(0.199f))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(Icons.Default.Refresh, contentDescription = "settings")
                    }
                }
//Edit Base entry DialogBox Item
                DropdownMenuItem(onClick = {
                    onEvent(DialogBoxUiEvents.showEditBaseEntryDBox)
                    optionsMenuState = !optionsMenuState
                    scope.launch {
                        if (sheetState.isExpanded) {
                            sheetState.collapse()
                        }
                    }
                }) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = "Edit",
                            fontSize = 16.sp,
                            style = TextStyle(baselineShift = BaselineShift(0.199f))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }


                }
//About Item

                DropdownMenuItem(onClick = {
                    optionsMenuState = !optionsMenuState
                    scope.launch {
                        if (sheetState.isExpanded) {
                            sheetState.collapse()
                        }
                    }
                }) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = "About",
                            fontSize = 16.sp,
                            style = TextStyle(baselineShift = BaselineShift(0.199f))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(Icons.Default.Info, contentDescription = "Edit")
                    }


                }


            }

        }

    )


}