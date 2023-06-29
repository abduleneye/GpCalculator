package com.example.gpcalculator.ScreenElements

import GpCalculatorPrototype.Data.CourseDataEntries
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.material.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.gpcalculator.ui.theme.Cream

@Composable
fun DropDownMenu(
    labelTextOne: String,
    labelTextTwo: String,
    dBState: DialogBoxState,
    onEvent: (DialogBoxUiEvents) -> Unit,


){

    val context = LocalContext.current

    var dataEntryObj = CourseDataEntries()

    var textFilledSize  by remember {
        mutableStateOf(Size.Zero)
    }

    var iconOne = if(dBState.isUnitDropDownMenuExpanded){
        Icons.Filled.KeyboardArrowUp
    }else{
        Icons.Filled.KeyboardArrowDown
    }

    var iconTwo = if(dBState.isGradeDropDownMenuExpanded){
        Icons.Filled.KeyboardArrowUp
    }else{
        Icons.Filled.KeyboardArrowDown
    }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {


        Row {

           Column() {

               OutlinedTextField(


                   value = dBState.selectedCourseUnit,
                   textStyle = TextStyle(baselineShift = BaselineShift(-0.37f)),
                   onValueChange = {},
                   enabled = false,
                   modifier = Modifier
                       .onGloballyPositioned { co_ordinates ->

                           textFilledSize = co_ordinates.size.toSize()

                       }
                       .width(130.dp)
                       .height(70.dp)
                       ,
                   label = {
                       Text(text = labelTextOne, style = TextStyle(baselineShift = BaselineShift(-0.37f)))
                   },
                   trailingIcon = {

                       Icon(
                           imageVector = iconOne,
                           contentDescription = "DropDownIcon",
                           modifier = Modifier
                               .clickable {

                                   if(dBState.isUnitDropDownMenuExpanded){
                                       onEvent(DialogBoxUiEvents.hideUnitMenuDropDown)

                                   }else{
                                       onEvent(DialogBoxUiEvents.showUnitMenuDropDown)
                                       Toast.makeText(context, "${dBState.dialogDefaultHeight}", Toast.LENGTH_SHORT).show()



                                   }

                               }
                       )

                   }
               )
               
               DropdownMenu(
                   expanded = dBState.isUnitDropDownMenuExpanded,
                   onDismissRequest = { onEvent(DialogBoxUiEvents.showUnitMenuDropDown) },
                   modifier = Modifier
                       .width(with(LocalDensity.current){
                          textFilledSize.width.toDp()
                       })
                       .height(120.dp)
                       .background(color = Cream)
               ) {

                   dataEntryObj.unitList.forEach{courseUnit ->
                       
                       DropdownMenuItem(onClick = {
                           onEvent(DialogBoxUiEvents.setSelectedCourseUnit(courseUnit))
                           onEvent(DialogBoxUiEvents.hideUnitMenuDropDown)
                       }) {

                           Text(text = courseUnit)
                           
                       }



                   }



               }

           }

            Spacer(modifier = Modifier
                .width(20.dp))

            Column() {

                OutlinedTextField(
                    value = dBState.selectedCourseGrade,
                    textStyle = TextStyle(baselineShift = BaselineShift(-0.37f)),
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier
                        .onGloballyPositioned { co_ordinates ->

                            textFilledSize = co_ordinates.size.toSize()

                        }
                        .width(130.dp)
                        .height(70.dp),
                    label = {
                        Text(text = labelTextTwo, style = TextStyle(baselineShift = BaselineShift(-0.37f)))
                    },
                    trailingIcon = {

                        Icon(
                            imageVector = iconTwo,
                            contentDescription = "DropDownIcon",
                            modifier = Modifier
                                .clickable {

                                    if (dBState.isGradeDropDownMenuExpanded){
                                        onEvent(DialogBoxUiEvents.hideGradeMenuDropDown)

                                    }else{
                                        onEvent(DialogBoxUiEvents.showGradeMenuDropDown)
                                    }

                                }
                        )

                    }
                )
                
                DropdownMenu(
                    expanded = dBState.isGradeDropDownMenuExpanded,
                    onDismissRequest = { onEvent(DialogBoxUiEvents.showGradeMenuDropDown) },
                    modifier = Modifier
                        .width(with(LocalDensity.current){
                            textFilledSize.width.toDp()
                        })
                        .height(120.dp)
                        .background(color = Cream)
                ) {

                    dataEntryObj.gradeList.forEach{courseGrade ->

                        DropdownMenuItem(onClick = {
                            onEvent(DialogBoxUiEvents.setSelectedCourseGrade(courseGrade))
                            onEvent(DialogBoxUiEvents.hideGradeMenuDropDown)
                        }) {

                            Text(text = courseGrade)

                        }

                    }
                    
                }

            }





        }

        
    }



}