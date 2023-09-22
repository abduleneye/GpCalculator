package com.example.gpcalculator.presentation.myViewModels.course_list_screen_component

import GpCalculatorPrototype.Data.GpData
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gpcalculator.presentation.course_list_screen_components.BaseEntryDialogBox
import com.example.gpcalculator.presentation.course_list_screen_components.CourseEntryDialogBox
import com.example.gpcalculator.presentation.course_list_screen_components.DialogBoxState
import com.example.gpcalculator.presentation.course_list_screen_components.DialogBoxUiEvents
import com.example.gpcalculator.presentation.course_list_screen_components.EditBaseEntryDialogBox
import com.example.gpcalculator.presentation.course_list_screen_components.ResultBottomSheetContent
import com.example.gpcalculator.presentation.course_list_screen_components.TopAppBarDropDownMenu
import com.example.gpcalculator.ui.theme.Cream
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    onEvent: (DialogBoxUiEvents) -> Unit,
    state: DialogBoxState,
    stateTwo: ArrayList<GpData>,
) {


    val context = LocalContext.current

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )

    val scaffoldState = rememberBottomSheetScaffoldState(

        bottomSheetState = sheetState
    )


    val scope = rememberCoroutineScope()
    val sheetWidth = remember {
        mutableStateOf(60.dp)
    }
    val statusIcon = if (state.baseEntryDialogBoxVisibility) {
        Icons.Filled.Add
    } else if (state.enteredCourses < state.totalCourses) {
        Icons.Filled.Add
    } else {
        Icons.Filled.Check

    }

    var initStatusIcon = Icons.Filled.KeyboardArrowUp



    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = { ResultBottomSheetContent(state) },
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .width(sheetWidth.value)
            .background(color = Cream)

        // .height(10.dp)
        ,
        sheetPeekHeight = 0.dp,
        drawerGesturesEnabled = true,
        sheetElevation = 100.dp,
        backgroundColor = Cream
    ) {


        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {


                    if (stateTwo.size < state.totalCourses.toInt()) {

                        onEvent(DialogBoxUiEvents.showDataEntryDBox)


//                    } else if (state.totalCreditLoad == "" || state.totalCourses == "") {
//                        onEvent(DialogBoxUiEvents.showBaseEntryDBox)

                    } else {
                        onEvent(DialogBoxUiEvents.executeCalculation)
                        //onEvent(DialogBoxUiEvents.showResultDBox)
                        scope.launch {
                            if (sheetState.isCollapsed) {
                                sheetState.expand()
                            } else {
                                sheetState.collapse()
                            }
                        }
                    }
                }) {

                    androidx.compose.material.Icon(
                        imageVector = statusIcon,
                        contentDescription = "Add Course details",


                        )


                }
            },
            topBar = {

                TopAppBarDropDownMenu(onEvent = onEvent)

            },

            backgroundColor = Cream


        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (state.enteredCourses == "0") {
                        "Click the Plus button!!!"
                    } else {
                        ""
                    },
                    fontSize = 35.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    lineHeight = 1000.sp,


                    )
            }


            if (state.courseEntryDialogBoxVisibility) {
                //CardViewToDisplay(data = CourseDataEntries().coursesDataEntry)

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,

                        ) {

                        CourseEntryDialogBox(
                            onEvent = onEvent,
                            dbState = state,
                            title = "Enter your course details"
                        )

                    }

                }
            } else if (state.baseEntryDialogBoxVisibility) {


                BaseEntryDialogBox(
                    Desc = "Welcome please make your entries",
                    state = state,
                    events = onEvent
                )

            } else if (state.editBaseEntryDialogBoxVisibility) {
                EditBaseEntryDialogBox(
                    Desc = "Edit Entries",
                    state = state,
                    events = onEvent
                )

            } else if (state.courseEntryEditDialogBoxVisibility) {

                EditCourseEntryDialogBox(onEvent = onEvent, dbState = state, title = "Edit Entries")

            } else {

                TotalCoursesListCardViewToDisplay(data = stateTwo, onClickEvent = onEvent)

            }


        }


    }


}