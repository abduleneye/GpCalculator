package com.engpacalculator.gpcalculator.features.five_grading_system_cgpa_features.presentation.five_cgpa_main_screen_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engpacalculator.gpcalculator.R
import com.engpacalculator.gpcalculator.features.five_grading_system_cgpa_features.presentation.FiveCgpaUiStates
import com.engpacalculator.gpcalculator.features.five_grading_system_sgpa_features.presentation.FiveSgpaUiEvents
import com.engpacalculator.gpcalculator.features.five_grading_system_sgpa_features.presentation.FiveSgpaUiStates
import com.engpacalculator.gpcalculator.ui.theme.AppBars
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FiveCgpaResultBottomSheetContent(

    fiveSgpaUiStates: FiveSgpaUiStates,
    fiveCgpaUiStates: FiveCgpaUiStates,
    sheetState: BottomSheetState,
    onEvent: (FiveSgpaUiEvents) -> Unit
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    Box(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
            .background(AppBars)
            .clickable {
                scope.launch {
                    if (sheetState.isCollapsed) {
                        sheetState.expand()
                    } else {
                        sheetState.collapse()
                    }
                }
            },
        contentAlignment = Alignment.TopCenter
    ) {

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Icon(
                painter = painterResource(id = R.drawable.horizontal_rule),
                contentDescription = "handle",
                modifier = Modifier
                    .padding(top = 0.dp),
                tint = Color.DarkGray

            )

//            Button(onClick = {
//                onEvent(FiveSgpaUiEvents.showSaveResultDBox)
//
//            }) {
//
//                Text(text = "S")
//
//            }

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp)
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.save_icon_foreground),
                        contentDescription = "handle",
                        modifier = Modifier
                            .padding(top = 0.dp)
                            .size(64.dp)
                            .clickable {
                                onEvent(FiveSgpaUiEvents.showSaveResultDBox)
                            },
                        tint = Color.DarkGray

                    )
                    Text(text = "Save as")


                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                thickness = 2.dp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(text = "Your CGPA is:", fontSize = 20.sp)


            }
            Spacer(modifier = Modifier.height(3.dp))

            Text(text = "${fiveCgpaUiStates.cgpa}", fontSize = 30.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "${fiveCgpaUiStates.gpaDescriptor}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "${fiveCgpaUiStates.remark}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 20.dp)
            )


        }
    }

}
