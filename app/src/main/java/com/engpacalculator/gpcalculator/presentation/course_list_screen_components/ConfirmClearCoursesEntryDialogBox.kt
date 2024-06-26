package com.engpacalculator.gpcalculator.presentation.course_list_screen_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.engpacalculator.gpcalculator.ui.theme.AppBars
import com.engpacalculator.gpcalculator.ui.theme.Cream

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConfirmClearCoursesEntryConfirmationDialogBox(
    onEvent: (DialogBoxUiEvents) -> Unit,
    dbState: DialogBoxState,
    sheetState: BottomSheetState


) {

    val scope = rememberCoroutineScope()


    Dialog(
        onDismissRequest = {
            onEvent(DialogBoxUiEvents.hideClearConfirmationDBox)

        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            //securePolicy = SecureFlagPolicy.SecureOn,
        )
    ) {


        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .height(
                    160.dp //final

                ),

            backgroundColor = Cream

        ) {
            Column(

            ) {


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Column {
                        Text(
                            text = "Are you sure you want to clear all" +
                                    "\nentered Courses?" +
                                    "\nthis action can't be undone",
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 20.dp),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))



                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp),
                    contentAlignment = Alignment.BottomCenter,

                    ) {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 15.dp, bottom = 2.dp),
                        horizontalArrangement = Arrangement.End

                    ) {

                        Button(
                            onClick = {
                                onEvent(DialogBoxUiEvents.hideClearConfirmationDBox)

                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppBars
                            ),
                        ) {

                            Text(text = "No")


                        }

                        Spacer(
                            modifier = Modifier
                                .width(16.dp)
                        )

                        Button(
                            onClick = {

//                                scope.launch {
//                                    if (sheetState.isExpanded) {
//                                        sheetState.collapse()
//                                    }
//                                }
                                onEvent(DialogBoxUiEvents.resetTotalEntries)
                                onEvent(DialogBoxUiEvents.hideClearConfirmationDBox)


                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppBars
                            ),
                        ) {

                            Text(text = "Yes")

                        }


                    }


                }

            }


        }

    }


}