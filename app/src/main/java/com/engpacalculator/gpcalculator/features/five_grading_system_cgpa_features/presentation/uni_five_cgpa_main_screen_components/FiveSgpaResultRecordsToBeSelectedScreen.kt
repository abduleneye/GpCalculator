package com.engpacalculator.gpcalculator.features.five_grading_system_cgpa_features.presentation.uni_five_cgpa_main_screen_components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.engpacalculator.gpcalculator.features.five_grading_system_cgpa_features.data.SgpaResultDisplayFormatForFiveCgpaCalculation
import com.engpacalculator.gpcalculator.features.five_grading_system_cgpa_features.presentation.FiveCgpaUiStates
import com.engpacalculator.gpcalculator.features.five_grading_system_sgpa_features.presentation.FiveSgpaUiEvents
import com.engpacalculator.gpcalculator.features.five_grading_system_sgpa_features.presentation.FiveSgpaViewModel
import com.engpacalculator.gpcalculator.features.five_grading_system_sgpa_features.presentation.five_sgpa_results_record_screen_component.FiveSgpaResultsRecordState
import com.engpacalculator.gpcalculator.ui.theme.Cream


@Composable
fun Init() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(text = "No saved record(s) yet")

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UniFiveSgpaRecordedResultToBeSelectedFrom(
    navController: NavController,
    fiveCgpaUiStates: FiveCgpaUiStates,
    fiveSgpaRecordsState: FiveSgpaResultsRecordState,
    fiveSgpaViewModel: FiveSgpaViewModel,
    //uniFiveCgpaViewModel: FiveCgpaViewModel,
    onEventFiveSgpa: (FiveSgpaUiEvents) -> Unit,
    //onEventFiveCgpa: (FiveCgpaUiEvents) -> Unit


) {


//    if (fiveSgpaRecordsState.resultItems.isEmpty()) {
//
//        Init()
//
//
//    } else {

//        Checkbox(
//            checked = fiveCgpaUiStates.checkBoxStatus,
//            onCheckedChange = {
//                onEventFiveCgpa(UniFiveCgpaUiEvents.onCheckChanged(isChecked = it))
//            },
//        )
    ResultRecordToDisplay(
        data = fiveCgpaUiStates,
        navController = navController,
        onEventFiveSgpa = onEventFiveSgpa,
        fiveSgpaViewModel = fiveSgpaViewModel,
        //uniFiveCgpaViewModel = uniFiveCgpaViewModel,
        //onEventFiveCgpa = onEventFiveCgpa,
        uniFiveCgpaUiState = fiveCgpaUiStates

    )

}
//
//}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyCardView(
    info: SgpaResultDisplayFormatForFiveCgpaCalculation,
    navController: NavController,
    index: Int,
    modifier: Modifier = Modifier,
    uniFiveCgpaUiState: FiveCgpaUiStates,
    onEventFiveSgpaUiEvents: (FiveSgpaUiEvents) -> Unit,
    fiveSgpaViewModel: FiveSgpaViewModel,
    // uniFiveCgpaViewModel: FiveCgpaViewModel,


) {

    //val json = Gson().toJson(info.resultEntries)

    val myContext = LocalContext.current

    val scope = rememberCoroutineScope()


    Card(
        ///
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                Toast
                    .makeText(myContext, "Clicked from card view!!!", Toast.LENGTH_SHORT)
                    .show()
            },
        colors = CardDefaults.cardColors(
            containerColor = Cream
        )


    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(100.dp)
                .clickable {
//                    navController.navigate(
//                        Screen.Five_Sgpa_Full_Records_Screen.withArgs(
//                            info.resultName,
//                            json,
//                            info.gp,
//                            info.remark
//                        )
//                    )
                    Toast
                        .makeText(myContext, "Clicked from column!!!", Toast.LENGTH_SHORT)
                        .show()


                },
        ) {
            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Checkbox(
                    checked = info.resultSelected,
                    onCheckedChange = {
                        onEventFiveSgpaUiEvents(
                            FiveSgpaUiEvents.onCheckChanged(
                                isChecked = it,
                                index = index
                            )
                        )
                        Toast.makeText(context, "${index}", Toast.LENGTH_SHORT).show()
                        //uniFiveCgpaUiState.checkBoxStatus = isChecked
                    })
            }



            Text(text = info.resultName, fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = info.resultEntries.toString(), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = info.resultSgpa, fontWeight = FontWeight.SemiBold)


            //}

        }

    }


}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResultRecordToDisplay(
    //data: List<UniFiveSgpaResultEntity>,
    data: FiveCgpaUiStates,
    uniFiveCgpaUiState: FiveCgpaUiStates,
    navController: NavController,
    onEventFiveSgpa: (FiveSgpaUiEvents) -> Unit,
    //onEventFiveCgpa: (FiveCgpaUiEvents) -> Unit,
    fiveSgpaViewModel: FiveSgpaViewModel,
    // uniFiveCgpaViewModel: FiveCgpaViewModel

) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()


    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .height(1024.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        // contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(items = data.displayedResultForFiveCgpaCalculation, key = { id, listItem ->
            id.hashCode()
        }) { index, item ->

            val context = LocalContext.current

            MyCardView(

                info = item,
                index = index,
                uniFiveCgpaUiState = uniFiveCgpaUiState,
                navController = navController,
                onEventFiveSgpaUiEvents = onEventFiveSgpa,
                fiveSgpaViewModel = fiveSgpaViewModel,
                // uniFiveCgpaViewModel = uniFiveCgpaViewModel

            )

        }


    }


}