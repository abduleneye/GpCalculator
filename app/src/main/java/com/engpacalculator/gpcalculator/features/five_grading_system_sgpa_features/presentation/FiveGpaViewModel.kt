package com.engpacalculator.gpcalculator.features.five_grading_system_sgpa_features.presentation

import GpCalculatorPrototype.Data.CourseDataEntries
import GpCalculatorPrototype.Data.CourseMaps
import GpCalculatorPrototype.Data.CoursesUnitPointArrayList
import GpCalculatorPrototype.Data.GpData
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engpacalculator.gpcalculator.features.five_grading_system_cgpa_features.data.ResultTracker
import com.engpacalculator.gpcalculator.features.five_grading_system_cgpa_features.data.SgpaResultDisplayFormatForFiveCgpaCalculation
import com.engpacalculator.gpcalculator.features.five_grading_system_cgpa_features.data.local.entity.FiveCgpaResultEntity
import com.engpacalculator.gpcalculator.features.five_grading_system_cgpa_features.domain.repository.FiveCgpaResultRepository
import com.engpacalculator.gpcalculator.features.five_grading_system_cgpa_features.presentation.FiveCgpaUiStates
import com.engpacalculator.gpcalculator.features.five_grading_system_cgpa_features.presentation.five_cgpa_results_record_screen_component.FiveCgpaResultsRecordState
import com.engpacalculator.gpcalculator.features.five_grading_system_sgpa_features.data.ErrorMessages
import com.engpacalculator.gpcalculator.features.five_grading_system_sgpa_features.data.ErrorPassedValues
import com.engpacalculator.gpcalculator.features.five_grading_system_sgpa_features.data.local.entity.FiveSgpaResultEntity
import com.engpacalculator.gpcalculator.features.five_grading_system_sgpa_features.domain.repository.FiveSgpaResultRepository
import com.engpacalculator.gpcalculator.features.five_grading_system_sgpa_features.presentation.five_sgpa_results_record_screen_component.FiveSgpaResultsRecordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ofPattern
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class FiveGpaViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val myFiveSgpaRepository: FiveSgpaResultRepository,
    private val myFiveCgpaRepository: FiveCgpaResultRepository,


    ) : ViewModel() {

    companion object {
        private const val DB_STATE_KEY = "my_db_state"
        private const val COURSE_ENTRIES_KEY = "my_course_entry_state"
    }

    private val coursePointObj = CoursesUnitPointArrayList()
    private val courseMapObj = CourseMaps()
    private val coursesDataEntryObj = CourseDataEntries()
    private val stateClassObject = FiveSgpaUiStates()
    private var result = ""
    var coursesUnitSubList = ArrayList<Int>()


    private var _courseEntries = MutableStateFlow(
        savedStateHandle.get(COURSE_ENTRIES_KEY) ?: CourseDataEntries().coursesDataEntry
    )
    var courseEntries = _courseEntries.asStateFlow()

    private var _dbState =
        MutableStateFlow(savedStateHandle.get(DB_STATE_KEY) ?: FiveSgpaUiStates())
    var dbState = _dbState.asStateFlow()


    private var _fiveSgparesultIntroDB = MutableStateFlow(FiveSgpaResultsRecordState())
    val fiveSgparesultIntroDB = _fiveSgparesultIntroDB.asStateFlow()

    private var _fiveCgpaResultIntroDB = MutableStateFlow(FiveCgpaResultsRecordState())
    val fiveCgpaResultIntroDB = _fiveCgpaResultIntroDB.asStateFlow()

    private var _fiveCgpaUiState =
        MutableStateFlow(FiveCgpaUiStates())
    var fiveCgpaUiState = _fiveCgpaUiState.asStateFlow()


    init {
        loadFiveSgpaData()
        loadFiveCgpaData()
    }

    private fun loadFiveCgpaData(chkBoxState: Boolean = false, pseudoIndex: Int = 0) {
        viewModelScope.launch {

            myFiveCgpaRepository.GetFiveCgpaResultRecordDao()
                .collect { result ->

                    _fiveCgpaResultIntroDB.update {
                        it.copy(
                            resultItems = result
                        )
                    }
                    //  _resultIntroDB.value.resultItems = result

                }


        }

    }

    private fun loadFiveSgpaData(chkBoxState: Boolean = false, pseudoIndex: Int = 0) {
        viewModelScope.launch {
            _fiveCgpaUiState.value.displayedResultForFiveCgpaCalculation.clear()

            myFiveSgpaRepository.GetFiveSgpaResultRecordDao()
                .collect { result ->

                    _fiveSgparesultIntroDB.update {
                        it.copy(
                            resultItems = result
                        )
                    }

                    for (i in 0 until result.size) {

                        _fiveCgpaUiState.value.displayedResultForFiveCgpaCalculation.add(
                            SgpaResultDisplayFormatForFiveCgpaCalculation(
                                resultSelected = false,
                                resultName = result.get(i).resultName,
                                resultSgpa = result.get(i).gp
                            )
                        )


                    }
                    //  _resultIntroDB.value.resultItems = result

                }


        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: FiveGpaUiEvents) {

        when (event) {
            // var TAG: String = "list"


            is FiveGpaUiEvents.showFiveCgpaSaveResultDB -> {
                _fiveCgpaUiState.update {
                    it.copy(
                        saveResultDBVisibilty = true
                    )
                }


            }

            is FiveGpaUiEvents.hideFiveCgpaSaveResultDB -> {
                _fiveCgpaUiState.update {
                    it.copy(
                        saveResultDBVisibilty = false
                    )
                }
            }

            is FiveGpaUiEvents.helpFiveCgpa -> {

                _fiveCgpaUiState.update {
                    it.copy(
                        newHelperText = "Ahh..."
                    )
                }
            }

            is FiveGpaUiEvents.setFiveCgpaSRA -> {
                _fiveCgpaUiState.update {
                    it.copy(
                        saveResultAs = event.saveResultAs
                    )
                }

            }

            is FiveGpaUiEvents.saveFiveCgpaResult -> {

                if (
                    _fiveCgpaUiState.value.saveResultAs.isEmpty()
                ) {
                    textFieldsErrorCheckSaveFiveCgpaResultAsDataEntry()

                } else {
                    viewModelScope.launch {
                        myFiveCgpaRepository.InsertFiveCgpaResult(
                            FiveCgpaResultEntity(
                                resultName = _fiveCgpaUiState.value.saveResultAs.uppercase(),
                                gp = _fiveCgpaUiState.value.cgpa,
                                remark = _fiveCgpaUiState.value.remark,
                                resultEntries = _fiveCgpaUiState.value.sgpaResultNames
                            )
                        )
                    }

                    Log.d("names", "${_fiveCgpaUiState.value.sgpaResultNames}")


                }


            }


            is FiveGpaUiEvents.executeCgpaCalculation -> {
                if (_fiveCgpaUiState.value.sgpaListToBeCalculated.isNotEmpty()) {
                    for (i in 0.._fiveCgpaUiState.value.sgpaListToBeCalculated.size - 1) {
                        _fiveCgpaUiState.value.cgpaList.add(
                            _fiveCgpaUiState.value.sgpaListToBeCalculated.get(
                                i
                            ).sgpaResult.toDouble().toFloat()
                        )
                    }

                    _fiveCgpaUiState.update {
                        it.copy(
                            cgpa = String.format(
                                "%.2f",
                                _fiveCgpaUiState.value.cgpaList.sum() / _fiveCgpaUiState.value.cgpaList.size
                            )
                        )
                    }

                    GpaDescriptor(_fiveCgpaUiState.value.cgpa.toFloat(), "cgpa")

                }

                Log.d(
                    "CGPA",
                    "Your sum is ${_fiveCgpaUiState.value.cgpaList.sum()} and size is ${_fiveCgpaUiState.value.cgpaList.size}"
                )
                Log.d("CGPA", "Your cgpa is ${_fiveCgpaUiState.value.cgpa}")

                //_fiveCgpaUiState.value.displayedResultForFiveCgpaCalculation.clear()
                _fiveCgpaUiState.value.cgpaList.clear()
                //_fiveCgpaUiState.value.sgpaListToBeCalculated.clear()
                //loadData()
            }

            is FiveGpaUiEvents.onCheckChanged -> {


                _fiveCgpaUiState.value.displayedResultForFiveCgpaCalculation.get(event.index).resultSelected =
                    event.isChecked

                for (i in 0 until _fiveCgpaUiState.value.displayedResultForFiveCgpaCalculation.size) {
                    if (_fiveCgpaUiState.value.displayedResultForFiveCgpaCalculation[i].resultSelected) {
                        _fiveCgpaUiState.update {
                            it.copy(operatorIconState = true)
                        }

                    } else {
                        _fiveCgpaUiState.update {
                            it.copy(operatorIconState = false)
                        }
                    }

                }

                val randomNumber = Random.nextInt(1, 1000)
//                val generatedNumbers = mutableSetOf<Int>()
//                while (generatedNumbers.size < 500) {
//                    if (randomNumber !in generatedNumbers) {
//
//                        generatedNumbers.add(randomNumber)
//
//                    }
//                }
                _fiveCgpaUiState.update {
                    it.copy(
                        helperText = randomNumber.toString()
                    )
                }


                if (event.isChecked == true) {
                    _fiveCgpaUiState.update {
                        it.copy(operatorIconState = true)
                    }

                    _fiveCgpaUiState.value.sgpaListToBeCalculated.add(
                        //index = event.index,
                        ResultTracker(
                            id = event.index,
                            sgpaResult = event.sgpaNeeded,
                            resultName = event.resultNameRef
                        )
                    )
                    _fiveCgpaUiState.value.sgpaResultNames.add(event.resultNameRef)
                } else {
                    _fiveCgpaUiState.value.sgpaListToBeCalculated.removeIf {
                        it.id == event.index
                    }
                    _fiveCgpaUiState.value.sgpaResultNames.remove(event.resultNameRef)

                }

                // println(_fiveCgpaUiState.value.sgpaListToBeCalculated)
                Log.d("List of sgpa", "${_fiveCgpaUiState.value.sgpaListToBeCalculated}")
            }

            is FiveGpaUiEvents.DeleteFiveGpaResultByReference -> {
                _fiveCgpaUiState.update {
                    it.copy(operatorIconState = false)
                }
                viewModelScope.launch {
                    myFiveSgpaRepository.FiveSgpaResultToBeDeleted(event.fiveSgpaResultName)
                    _fiveCgpaUiState.value.displayedResultForFiveCgpaCalculation.clear()
                    _fiveCgpaUiState.value.cgpaList.clear()
                    _fiveCgpaUiState.value.sgpaListToBeCalculated.clear()

                }
            }

            is FiveGpaUiEvents.DeleteFiveCgpaResultByReference -> {
                _fiveCgpaUiState.update {
                    it.copy(operatorIconState = false)
                }
                viewModelScope.launch {
                    myFiveCgpaRepository.FiveCgpaResultToBeDeleted(event.fiveCgpaResultName)
//                    _fiveCgpaUiState.value.displayedResultForFiveCgpaCalculation.clear()
//                    _fiveCgpaUiState.value.cgpaList.clear()
//                    _fiveCgpaUiState.value.sgpaListToBeCalculated.clear()

                }
            }


            is FiveGpaUiEvents.DeleteResult -> {
                _fiveCgpaUiState.update {
                    it.copy(operatorIconState = false)
                }
                viewModelScope.launch {
                    myFiveSgpaRepository.DeleteFiveSgpaResult(event.fiveSgpaResultName)
                    _fiveCgpaUiState.value.displayedResultForFiveCgpaCalculation.clear()
                    _fiveCgpaUiState.value.cgpaList.clear()
                    _fiveCgpaUiState.value.sgpaListToBeCalculated.clear()


                }

            }

            is FiveGpaUiEvents.showResultDBox -> {
                _dbState.update {
                    it.copy(
                        resultDialogBoxVisibility = true
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.hideFiveSgpaSaveResultDB -> {
                _dbState.update {
                    it.copy(
                        saveResultAsDialogBoxVisibility = false
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.setSRA -> {
                _dbState.update {
                    it.copy(
                        saveResultAs = event.savedResultName

                    )
                }

            }

            is FiveGpaUiEvents.saveFiveGpaResult -> {

                if (
                    _dbState.value.saveResultAs.isEmpty()
                ) {
                    textFieldsErrorCheckSaveFiveSgpaResultAsDataEntry()

                } else {
                    viewModelScope.launch {
                        myFiveSgpaRepository.InsertFiveSgpaResult(
                            FiveSgpaResultEntity(
                                resultEntries = _courseEntries.value,
                                gp = _dbState.value.fiveSgpaFinalResult,
                                resultName = _dbState.value.saveResultAs.uppercase(),
                                remark = _dbState.value.remark
                            )
                        )
                        _fiveCgpaUiState.value.displayedResultForFiveCgpaCalculation.clear()
                        _fiveCgpaUiState.value.cgpaList.clear()
                        _fiveCgpaUiState.value.sgpaListToBeCalculated.clear()

                    }

                    resetFiveCgpaSRADBox()


                }

            }

            is FiveGpaUiEvents.resetBackToDefaultValueFromErrorSRA -> {
                resetFiveSgpaSRADBox()

            }

            is FiveGpaUiEvents.replaceEditedInEntriesToArrayList -> {
                textFieldsErrorCheckEditedCourseDataEntry()
            }

            is FiveGpaUiEvents.editItemsEntries -> {

                _dbState.update {
                    it.copy(
                        courseCode = event.courseCodeEdit,
                        selectedCourseUnit = event.unitEdit,
                        selectedCourseGrade = event.gradeEdit

                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.updateCourseIndexEntry -> {
                _dbState.update {
                    it.copy(
                        courseEntryIndex = event.entryIndex
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.showCourseEntryEditDBox -> {
                _dbState.update {
                    it.copy(
                        courseEntryEditDialogBoxVisibility = true,
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.hideCourseEntryEditDBox -> {
                _dbState.update {
                    it.copy(
                        courseEntryEditDialogBoxVisibility = false
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }


            is FiveGpaUiEvents.resetResultField -> {

                _dbState.update {
                    it.copy(
                        fiveSgpaFinalResult = "new val"
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.showDataEntryDBox -> {
                _dbState.update {
                    it.copy(
                        courseEntryDialogBoxVisibility = true
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            }

            is FiveGpaUiEvents.hideDataEntryDBox -> {
                _dbState.update {
                    it.copy(
                        allReadyInList = false,
                        courseEntryDialogBoxVisibility = false
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.showUnitMenuDropDown -> {

                _dbState.update {
                    it.copy(
                        isUnitDropDownMenuExpanded = true
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            }

            is FiveGpaUiEvents.hideUnitMenuDropDown -> {

                _dbState.update {
                    it.copy(
                        isUnitDropDownMenuExpanded = false
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            }

            is FiveGpaUiEvents.showGradeMenuDropDown -> {
                _dbState.update {
                    it.copy(
                        isGradeDropDownMenuExpanded = true
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.hideGradeMenuDropDown -> {
                _dbState.update {
                    it.copy(
                        isGradeDropDownMenuExpanded = false
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.setSelectedCourseGrade -> {

                _dbState.update {
                    it.copy(
                        selectedCourseGrade = event.grade
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            }

            is FiveGpaUiEvents.setSelectedCourseUnit -> {
                _dbState.update {
                    it.copy(
                        selectedCourseUnit = event.unit
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.deleteCourseEntry -> {

                _courseEntries.value.removeAt(event.itemToRemove)
                savedStateHandle.set(COURSE_ENTRIES_KEY, _courseEntries.value)
                _dbState.value.arrayOfAlreadyEnteredCourseslist.removeAt(event.itemToRemove)
                _dbState.update {
                    it.copy(
                        enteredCourses = _courseEntries.value.size.toString()
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.setCourseCode -> {

                if (_dbState.value.arrayOfAlreadyEnteredCourseslist.contains(event.courseCode.uppercase())) {
                    _dbState.update {
                        it.copy(
                            allReadyInList = true,
                            matchAlreadyInCourseEntry = event.courseCode.uppercase()
                        )
                    }
                } else {
                    _dbState.update {
                        it.copy(
                            allReadyInList = false,
                            matchAlreadyInCourseEntry = ""

                        )
                    }

                }
                _dbState.update {
                    it.copy(
                        courseCode = event.courseCode.replace(" ", "")
                    )


                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

//                } else {
//                    _dbState.update {
//                        it.copy(
//                            courseCode = event.courseCode
//                        )
//
//
//                    }
//                }


            }

            is FiveGpaUiEvents.addEntriesToArrayList -> {

                textFieldsErrorCheckCourseDataEntry()


            }

            is FiveGpaUiEvents.setTotalCreditLoad -> {
                _dbState.update {
                    it.copy(
                        totalCreditLoad = event.totalCreditLoad
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.setTotalCourses -> {
//                var use = event.totalCourses
//                if (use[0] == '0') {
//                    use = use.replace("0", "")
//                    _dbState.update {
//                        it.copy(
//                            totalCourses = use
//                        )
//                    }
//                    savedStateHandle.set(DB_STATE_KEY, _dbState.value)
//
//
//                } else if (use[0] == '0' && use[1] == '0') {
//                    use = use.replace("0", "")
//                    _dbState.update {
//                        it.copy(
//                            totalCourses = use
//                        )
//                    }
//                    savedStateHandle.set(DB_STATE_KEY, _dbState.value)
//
//
//                } else {
                _dbState.update {
                    it.copy(
                        totalCourses = event.totalCourses
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


                // }


            }

            is FiveGpaUiEvents.hideBaseEntryDBox -> {
                textFieldsErrorCheckBaseEntryDB()

            }

            is FiveGpaUiEvents.showSaveResultDBox -> {
                _dbState.update {
                    it.copy(
                        saveResultAsDialogBoxVisibility = true
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

//            is FiveGpaUiEvents.hideFiveSgpaSaveResultDB -> {
//                _dbState.update {
//
//                    it.copy(
//                        saveResultAsDialogBoxVisibility = false,
//                    )
//                }
//                savedStateHandle.set(DB_STATE_KEY, _dbState.value)
//
//            }

            is FiveGpaUiEvents.resetAlreadyInList -> {
                _dbState.update {
                    it.copy(
                        allReadyInList = false
                    )

                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            }

            is FiveGpaUiEvents.showCourseDataEntriesContextmenu -> {

                _dbState.update {
                    it.copy(
                        courseItemsDropDownVisibility = true
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            }

            is FiveGpaUiEvents.hideCourseDataEntriesContextmenu -> {
                _dbState.update {
                    it.copy(
                        courseItemsDropDownVisibility = false
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.resetTotalEntries -> {


                if (_dbState.value.totalCourses > 0.toString()) {


                    _dbState.value.arrayOfAlreadyEnteredCourseslist.clear()
                    _courseEntries.value.clear()
                    _dbState.update {
                        it.copy(
                            //totalCourses = "",
                            totalCreditLoad = "",
                            enteredCourses = "0",

                            //baseEntryDialogBoxVisibility = true
                        )
                    }
                    savedStateHandle.set(DB_STATE_KEY, _dbState.value)


                } else {


                }


            }

            is FiveGpaUiEvents.resetBackToDefaultValuesFromErrorsTNOC -> {
                _dbState.update {
                    it.copy(
                        defaultLabelColourTNOC = ErrorPassedValues.errorPassedColour,
                        defaultLabelTNOC = ErrorPassedValues.labelForTNOC

                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.resetBackToDefaultValuesFromErrorsCC -> {
                _dbState.update {
                    it.copy(
                        defaultLabelColourCC = ErrorPassedValues.errorPassedColour,
                        defaultEnteredCourseCodeLabel = ErrorPassedValues.enterCourseCodeLabel

                    )
                }

            }

            is FiveGpaUiEvents.resetBackToDefaultValuesFromErrorsECC -> {
                _dbState.update {
                    it.copy(
                        defaultLabelColourECC = ErrorPassedValues.errorPassedColour,
                        defaultEditCourseCodeLabel = ErrorPassedValues.editCourseCodeLabel

                    )
                }

            }


            is FiveGpaUiEvents.resetDefaultValuesFromErrorsTNOCL -> {
                _dbState.update {
                    it.copy(
                        defaultLabelTNOCL = ErrorPassedValues.labelForTNOCC,
                        //defaultLabelColourTNOCL = ErrorPassedValues.errorPassedColour

                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.showEditBaseEntryDBox -> {
                _dbState.update {
                    it.copy(
                        editBaseEntryDialogBoxVisibility = true,
                        errorToastMessageVisibilityETNOCDB = true

                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.hideEditBaseEntryDBox -> {

                textFieldsErrorEditedCheckBaseEntryDB()

                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.hideEditBaseEntryRegardlessDBox -> {

                if (_dbState.value.enteredCourses == "0") {

                    _dbState.update {
                        it.copy(
                            editBaseEntryDialogBoxVisibility = false,
                            totalCourses = _dbState.value.prevTotalNumberOfCourses
                        )
                    }

                    savedStateHandle.set(DB_STATE_KEY, _dbState.value)

                } else if (_dbState.value.enteredCourses != "0") {

                    _dbState.update {
                        it.copy(
                            editBaseEntryDialogBoxVisibility = false,
                            totalCourses = _dbState.value.enteredCourses

                        )
                    }

                    savedStateHandle.set(DB_STATE_KEY, _dbState.value)


                }


            }


            is FiveGpaUiEvents.showBaseEntryDBox -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    greetings()
                }

                _dbState.update {
                    it.copy(
                        baseEntryDialogBoxVisibility = true
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }


            is FiveGpaUiEvents.executeCalculation -> {

                _dbState.update {
                    it.copy(
                        changeDoneIcon = true
                    )
                }

                var execTotalUnit: Int = 0


                for (i in 1.._dbState.value.totalCourses.toInt()) {
                    coursesUnitSubList.add(_courseEntries.value[i - 1].courseUnit)
                    savedStateHandle.set(COURSE_ENTRIES_KEY, _courseEntries.value)

                }
                execTotalUnit = coursesUnitSubList.sum()
                _dbState.update {
                    it.copy(
                        totalCreditLoad = execTotalUnit.toString()
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)



                courseValueMapper(_courseEntries.value)
                result = operations(_dbState.value.totalCreditLoad.toInt())
                _dbState.update {
                    it.copy(
                        fiveSgpaFinalResult = result
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


                GpaDescriptor(_dbState.value.fiveSgpaFinalResult.toFloat(), "sgpa")

                onReExecuteCalculationClearArrayField()


            }

            is FiveGpaUiEvents.hideBaseEntryRegardlessDBox -> {
                _dbState.update {
                    it.copy(
                        baseEntryDialogBoxVisibility = false,
                        totalCourses = "",
                        prevTotalNumberOfCourses = ""

                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.setPrevTotalCourses -> {
                _dbState.update {
                    it.copy(
                        prevTotalNumberOfCourses = event.text
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.showClearConfirmationDBox -> {
                _dbState.update {
                    it.copy(
                        clearCoursesConfirmationDBoxVisibility = true
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.hideClearConfirmationDBox -> {
                _dbState.update {
                    it.copy(
                        clearCoursesConfirmationDBoxVisibility = false
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.showHomeAdShimmerEffect -> {
                _dbState.update {
                    it.copy(
                        homeAdShimmerEffectVisibility = true
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.hideHomeAdShimmerEffect -> {
                _dbState.update {
                    it.copy(
                        homeAdShimmerEffectVisibility = false
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.showAboutAdShimmerEffect -> {
                _dbState.update {
                    it.copy(
                        aboutAdShimmerEffectVisibility = true
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.hideAboutAdShimmerEffect -> {
                _dbState.update {
                    it.copy(
                        aboutAdShimmerEffectVisibility = true
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            }

            is FiveGpaUiEvents.setTotalNumberOfEditedCourses -> {
                _dbState.update {
                    it.copy(
                        editedNumberOfCourses = event.noOfEditedTotalCourse
                    )
                }
            }

            is FiveGpaUiEvents.resetBackToDefaultValuesFromErrorsCU -> {
                _dbState.update {
                    it.copy(
                        pickedCourseUnitDefaultLabel = ErrorPassedValues.enterCourseUnitLabel,
                        defaultLabelColourCU = ErrorPassedValues.dropDownErrorPassedColour
                    )
                }
            }

            is FiveGpaUiEvents.resetBackToDefaultValuesFromErrorsCG -> {
                _dbState.update {
                    it.copy(
                        pickedCourseGradeDefaultLabel = ErrorPassedValues.enterCourseGradeLabel,
                        defaultLabelColourCG = ErrorPassedValues.dropDownErrorPassedColour
                    )
                }
            }


            else -> {}
        }

    }


    private fun textFieldsErrorCheckBaseEntryDB() {

        if (_dbState.value.totalCourses.isEmpty() || _dbState.value.totalCourses == "0" || _dbState.value.totalCourses == "00" || _dbState.value.totalCourses.get(
                0
            ) == '0'
        ) {

            _dbState.update {
                it.copy(
                    defaultLabelTNOC = ErrorMessages.errorLabelMessageForTNOC,
                    defaultLabelColourTNOC = ErrorMessages.textFieldErrorLabelColorHexCode
                )
            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)


        } else if (_dbState.value.totalCreditLoad.isNotEmpty()) {
            _dbState.update {
                it.copy(
                    baseEntryDialogBoxVisibility = false
                )
            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)


        }


    }

    private fun textFieldsErrorEditedCheckBaseEntryDB() {

        if (_dbState.value.totalCourses == _dbState.value.prevTotalNumberOfCourses && _dbState.value.enteredCourses == "0") {
            _dbState.update {
                it.copy(
                    errorMessageHolderForETNOCDBToastMessage = "No changes made",
                    editBaseEntryDialogBoxVisibility = false,
                    errorToastMessageVisibilityETNOCDB = false,

                    )
            }
        } else if (_dbState.value.editedNumberOfCourses.isEmpty() || _dbState.value.editedNumberOfCourses == "0" || _dbState.value.editedNumberOfCourses == "00" || _dbState.value.editedNumberOfCourses.get(
                0
            ) == '0'
        ) {
            if (_dbState.value.enteredCourses == "0") {
                _dbState.update {
                    it.copy(
                        errorMessageHolderForETNOCDBToastMessage = "entry can't be empty",
                        totalCourses = _dbState.value.prevTotalNumberOfCourses,
                        editedNumberOfCourses = _dbState.value.prevTotalNumberOfCourses,
                        errorToastMessageVisibilityETNOCDB = true,


                        )
                }
            } else if (_dbState.value.enteredCourses != "0") {
                _dbState.update {
                    it.copy(
                        errorToastMessageVisibilityETNOCDB = true,
                        errorMessageHolderForETNOCDBToastMessage = "entry can't be empty",
                        totalCourses = _dbState.value.enteredCourses,
                        editedNumberOfCourses = _dbState.value.enteredCourses

                    )
                }
            }

            savedStateHandle.set(DB_STATE_KEY, _dbState.value)


        } else if (_dbState.value.editedNumberOfCourses == "0") {

            if (_dbState.value.enteredCourses == "0") {
                _dbState.update {
                    it.copy(
                        errorToastMessageVisibilityETNOCDB = true,
                        errorMessageHolderForETNOCDBToastMessage = "entry can't be 0",
                        totalCourses = _dbState.value.prevTotalNumberOfCourses,
                        editedNumberOfCourses = _dbState.value.prevTotalNumberOfCourses

                    )
                }
            } else if (_dbState.value.enteredCourses != "0") {
                _dbState.update {
                    it.copy(
                        errorToastMessageVisibilityETNOCDB = true,
                        errorMessageHolderForETNOCDBToastMessage = "entry can't be 0",
                        totalCourses = _dbState.value.enteredCourses,
                        editedNumberOfCourses = _dbState.value.enteredCourses

                    )
                }
            }

            savedStateHandle.set(DB_STATE_KEY, _dbState.value)


        } else if (_dbState.value.editedNumberOfCourses.isNotEmpty() && _dbState.value.enteredCourses != "0") {

            if (_dbState.value.editedNumberOfCourses.toInt() < dbState.value.enteredCourses.toInt()) {
                _dbState.update {
                    it.copy(
                        errorToastMessageVisibilityETNOCDB = true,
                        errorMessageHolderForETNOCDBToastMessage = "entry can't be less than already entered courses",
                        totalCourses = _dbState.value.enteredCourses,
                        editedNumberOfCourses = _dbState.value.enteredCourses

                    )
                }
            } else if (_dbState.value.editedNumberOfCourses >= _dbState.value.enteredCourses) {
                _dbState.update {
                    it.copy(
                        errorToastMessageVisibilityETNOCDB = false,
                        errorMessageHolderForETNOCDBToastMessage = "successfully updated",
                        totalCourses = _dbState.value.editedNumberOfCourses,
                        editBaseEntryDialogBoxVisibility = false,
                        editedNumberOfCourses = _dbState.value.editedNumberOfCourses


                    )
                }
            }

            savedStateHandle.set(DB_STATE_KEY, _dbState.value)


        } else {
            _dbState.update {
                it.copy(
                    errorToastMessageVisibilityETNOCDB = false,
                    errorMessageHolderForETNOCDBToastMessage = "",
                    editBaseEntryDialogBoxVisibility = false,
                )
            }
        }


    }


    private fun textFieldsErrorCheckEditedCourseDataEntry() {

        if (_dbState.value.courseCode.isEmpty()) {

            _dbState.update {
                it.copy(
                    defaultEditCourseCodeLabel = ErrorMessages.errorMessageForCourseCode,
                    defaultLabelColourECC = ErrorMessages.textFieldErrorLabelColorHexCode


                )
            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)


        } else {

            if (
                _courseEntries.value.contains(
                    GpData(
                        courseCode = _dbState.value.courseCode.uppercase(),
                        courseGrade = _dbState.value.selectedCourseGrade,
                        courseUnit = _dbState.value.selectedCourseUnit.toInt()
                    )
                )
            ) {

                _dbState.update {
                    it.copy(
                        enteredCourses = _courseEntries.value.size.toString(),
                        courseEntryEditDialogBoxVisibility = false,

                        )
                }


            } else {
                _courseEntries.value[_dbState.value.courseEntryIndex.toInt()] = GpData(
                    courseCode = _dbState.value.courseCode.uppercase(),
                    courseGrade = _dbState.value.selectedCourseGrade,
                    courseUnit = _dbState.value.selectedCourseUnit.toInt()
                )
                savedStateHandle.set(COURSE_ENTRIES_KEY, _courseEntries.value)


                _dbState.update {
                    it.copy(
                        enteredCourses = _courseEntries.value.size.toString(),
                        courseEntryEditDialogBoxVisibility = false
//                    courseEntryDialogBoxVisibility = false,

                    )
                }
                clearCourseDataEntry()
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            }


        }


    }


    private fun textFieldsErrorCheckCourseDataEntry() {

        if (_dbState.value.courseCode.isEmpty()) {

            _dbState.update {
                it.copy(
                    defaultEnteredCourseCodeLabel = ErrorMessages.errorMessageForCourseCode,
                    defaultLabelColourCC = ErrorMessages.textFieldErrorLabelColorHexCode
                )
            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)


        } else if (_dbState.value.selectedCourseUnit.isEmpty()) {
            _dbState.update {
                it.copy(
                    pickedCourseUnitDefaultLabel = ErrorMessages.errorMessageForCourseUnit,
                    defaultLabelColourCU = ErrorMessages.textFieldErrorLabelColorHexCode

                )
            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)


        } else if (_dbState.value.selectedCourseGrade.isEmpty()) {

            _dbState.update {
                it.copy(
                    pickedCourseGradeDefaultLabel = ErrorMessages.errorMessageForCourseGrade,
                    defaultLabelColourCG = ErrorMessages.textFieldErrorLabelColorHexCode
                )
            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)


        } else if (


            _dbState.value.arrayOfAlreadyEnteredCourseslist.contains(
                _dbState.value.courseCode.uppercase(Locale.UK)
            )
        ) {


            _dbState.update {
                it.copy(
                    matchAlreadyInCourseEntry = _dbState.value.courseCode.uppercase(Locale.UK),
                    allReadyInList = true,

                    )
            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            savedStateHandle.set(DB_STATE_KEY, _dbState.value)
            _dbState.update {
                it.copy(
                    // allReadyInList = false,
                    //matchAlreadyInCourseEntry = ""
                )
            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)


        } else {

            _courseEntries.value.add(
                GpData(
                    _dbState.value.courseCode.uppercase(Locale.UK),
                    _dbState.value.selectedCourseGrade,
                    _dbState.value.selectedCourseUnit.toInt()
                )
            )
            savedStateHandle.set(COURSE_ENTRIES_KEY, _courseEntries.value)


            _dbState.value.arrayOfAlreadyEnteredCourseslist.add(
                _dbState.value.courseCode.uppercase(
                    Locale.UK
                )
            )
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)



            _dbState.update {
                it.copy(
                    allReadyInList = false,
                    enteredCourses = _courseEntries.value.size.toString(),
                    courseEntryDialogBoxVisibility = false,
                    //matchAlreadyInCourseEntry =

                )
            }

            if (_dbState.value.totalCourses == _dbState.value.enteredCourses) {
                _dbState.update {
                    it.copy(
                        changeDoneIcon = true
                    )
                }


            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            clearCourseDataEntry()


        }


    }


    private fun textFieldsErrorCheckSaveFiveCgpaResultAsDataEntry() {

        _fiveCgpaUiState.update {
            it.copy(
                defaultLabelSRA = ErrorMessages.errorMessageForSRA,
                defaultLabelColourSRA = ErrorMessages.textFieldErrorLabelColorHexCode,
                saveResultDBVisibilty = true
            )
        }
        // savedStateHandle.set(FiveGpaViewModel.DB_STATE_KEY, _dbState.value)


    }

    private fun resetFiveCgpaSRADBox() {
        _fiveCgpaUiState.update {
            it.copy(
                defaultLabelSRA = ErrorPassedValues.labelForSRA,
                defaultLabelColourSRA = ErrorPassedValues.errorPassedColour,
                saveResultDBVisibilty = false,
                saveResultAs = ""


            )
        }
        //savedStateHandle.set(FiveGpaViewModel.DB_STATE_KEY, _dbState.value)
    }


    private fun textFieldsErrorCheckSaveFiveSgpaResultAsDataEntry() {

        _dbState.update {
            it.copy(
                defaultLabelSRA = ErrorMessages.errorMessageForSRA,
                defaultLabelColourSRA = ErrorMessages.textFieldErrorLabelColorHexCode,
                saveResultAsDialogBoxVisibility = true
            )
        }
        savedStateHandle.set(DB_STATE_KEY, _dbState.value)


    }


    private fun resetFiveSgpaSRADBox() {
        _dbState.update {
            it.copy(
                defaultLabelSRA = ErrorPassedValues.labelForSRA,
                defaultLabelColourSRA = ErrorPassedValues.errorPassedColour,
                saveResultAsDialogBoxVisibility = false,
                saveResultAs = ""


            )
        }
        savedStateHandle.set(DB_STATE_KEY, _dbState.value)
    }

    private fun operations(totalCreditLoad: Int): String {

        coursesDataEntryObj.sixUnitCoursesPointSum =
            coursePointObj.sixUnitA.sum() + coursePointObj.sixUnitB.sum() + coursePointObj.sixUnitC.sum() + coursePointObj.sixUnitD.sum() + coursePointObj.sixUnitE.sum() + coursePointObj.sixUnitF.sum()
        coursesDataEntryObj.fourUnitCoursesPointSum =
            coursePointObj.fourUnitA.sum() + coursePointObj.fourUnitB.sum() + coursePointObj.fourUnitC.sum() + coursePointObj.fourUnitD.sum() + coursePointObj.fourUnitE.sum() + coursePointObj.fourUnitF.sum()
        coursesDataEntryObj.threeUnitCoursesPointSum =
            coursePointObj.threeUnitA.sum() + coursePointObj.threeUnitB.sum() + coursePointObj.threeUnitC.sum() + coursePointObj.threeUnitD.sum() + coursePointObj.threeUnitE.sum() + coursePointObj.threeUnitF.sum()
        coursesDataEntryObj.twoUnitCoursesPointSum =
            coursePointObj.twoUnitA.sum() + coursePointObj.twoUnitB.sum() + coursePointObj.twoUnitC.sum() + coursePointObj.twoUnitD.sum() + coursePointObj.twoUnitE.sum() + coursePointObj.twoUnitF.sum()
        coursesDataEntryObj.oneUnitCoursesPointSum =
            coursePointObj.oneUnitA.sum() + coursePointObj.oneUnitB.sum() + coursePointObj.oneUnitC.sum() + coursePointObj.oneUnitD.sum() + coursePointObj.oneUnitE.sum() + coursePointObj.oneUnitF.sum()
        coursesDataEntryObj.totalCoursesPointSum =
            (coursesDataEntryObj.sixUnitCoursesPointSum + coursesDataEntryObj.fourUnitCoursesPointSum + coursesDataEntryObj.threeUnitCoursesPointSum + coursesDataEntryObj.twoUnitCoursesPointSum + coursesDataEntryObj.oneUnitCoursesPointSum).toDouble()


        var finalAns = (coursesDataEntryObj.totalCoursesPointSum / totalCreditLoad)
        //var decimalFormat = DecimalFormat("#.##")
        var final_result = String.format("%.2f", finalAns)


        return ("$final_result")
    }

    private fun courseValueMapper(courseGrade: ArrayList<GpData>) {


        courseGrade.forEach { courseData ->

            when (courseData) {

                //For six unit Courses

                GpData(courseCode = courseData.courseCode, courseGrade = "A", courseUnit = 6) -> {

                    courseMapObj.sixUnit_GradeMap["A"]?.let { coursePointObj.sixUnitA.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "B", courseUnit = 6) -> {

                    courseMapObj.sixUnit_GradeMap["B"]?.let { coursePointObj.sixUnitB.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "C", courseUnit = 6) -> {

                    courseMapObj.sixUnit_GradeMap["C"]?.let { coursePointObj.sixUnitC.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "D", courseUnit = 6) -> {

                    courseMapObj.sixUnit_GradeMap["D"]?.let { coursePointObj.sixUnitD.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "E", courseUnit = 6) -> {

                    courseMapObj.sixUnit_GradeMap["E"]?.let { coursePointObj.sixUnitE.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "F", courseUnit = 6) -> {

                    courseMapObj.sixUnit_GradeMap["F"]?.let { coursePointObj.sixUnitF.add(it) }

                }


                //For four unit Courses

                GpData(courseCode = courseData.courseCode, courseGrade = "A", courseUnit = 4) -> {

                    courseMapObj.fourUnit_GradeMap["A"]?.let { coursePointObj.fourUnitA.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "B", courseUnit = 4) -> {

                    courseMapObj.fourUnit_GradeMap["B"]?.let { coursePointObj.fourUnitB.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "C", courseUnit = 4) -> {

                    courseMapObj.fourUnit_GradeMap["C"]?.let { coursePointObj.fourUnitC.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "D", courseUnit = 4) -> {

                    courseMapObj.fourUnit_GradeMap["D"]?.let { coursePointObj.fourUnitD.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "E", courseUnit = 4) -> {

                    courseMapObj.fourUnit_GradeMap["E"]?.let { coursePointObj.fourUnitE.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "F", courseUnit = 4) -> {

                    courseMapObj.fourUnit_GradeMap["F"]?.let { coursePointObj.fourUnitF.add(it) }

                }


                //For three unit Courses

                GpData(courseCode = courseData.courseCode, courseGrade = "A", courseUnit = 3) -> {

                    courseMapObj.threeUnit_GradeMap["A"]?.let { coursePointObj.threeUnitA.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "B", courseUnit = 3) -> {

                    courseMapObj.threeUnit_GradeMap["B"]?.let { coursePointObj.threeUnitB.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "C", courseUnit = 3) -> {

                    courseMapObj.threeUnit_GradeMap["C"]?.let { coursePointObj.threeUnitC.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "D", courseUnit = 3) -> {

                    courseMapObj.threeUnit_GradeMap["D"]?.let { coursePointObj.threeUnitD.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "E", courseUnit = 3) -> {

                    courseMapObj.threeUnit_GradeMap["E"]?.let { coursePointObj.threeUnitE.add(it) }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "F", courseUnit = 3) -> {

                    courseMapObj.threeUnit_GradeMap["F"]?.let { coursePointObj.threeUnitF.add(it) }

                }


                //For two unit courses:

                GpData(courseCode = courseData.courseCode, "A", 2) -> {

                    courseMapObj.twoUnit_GradeMap["A"]?.let {

                        coursePointObj.twoUnitA.add(it)
                    }

                }

                GpData(courseCode = courseData.courseCode, "B", 2) -> {

                    courseMapObj.twoUnit_GradeMap["B"]?.let {

                        coursePointObj.twoUnitB.add(it)
                    }

                }

                GpData(courseCode = courseData.courseCode, "C", 2) -> {

                    courseMapObj.twoUnit_GradeMap["C"]?.let {

                        coursePointObj.twoUnitC.add(it)
                    }

                }

                GpData(courseCode = courseData.courseCode, "D", 2) -> {

                    courseMapObj.twoUnit_GradeMap["D"]?.let {

                        coursePointObj.twoUnitD.add(it)
                    }

                }

                GpData(courseCode = courseData.courseCode, "E", 2) -> {

                    courseMapObj.twoUnit_GradeMap["E"]?.let {

                        coursePointObj.twoUnitE.add(it)
                    }

                }

                GpData(courseCode = courseData.courseCode, "A", 2) -> {

                    courseMapObj.twoUnit_GradeMap["F"]?.let {

                        coursePointObj.twoUnitF.add(it)
                    }

                }

                //For one unit Courses

                GpData(courseCode = courseData.courseCode, courseGrade = "A", courseUnit = 1) -> {

                    courseMapObj.oneUnit_GradeMap["A"]?.let {
                        coursePointObj.oneUnitA.add(it)
                    }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "B", courseUnit = 1) -> {

                    courseMapObj.oneUnit_GradeMap["B"]?.let {
                        coursePointObj.oneUnitB.add(it)
                    }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "C", courseUnit = 1) -> {

                    courseMapObj.oneUnit_GradeMap["C"]?.let {
                        coursePointObj.oneUnitC.add(it)
                    }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "D", courseUnit = 1) -> {

                    courseMapObj.oneUnit_GradeMap["D"]?.let {
                        coursePointObj.oneUnitD.add(it)
                    }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "E", courseUnit = 1) -> {

                    courseMapObj.oneUnit_GradeMap["E"]?.let {
                        coursePointObj.oneUnitE.add(it)
                    }

                }

                GpData(courseCode = courseData.courseCode, courseGrade = "F", courseUnit = 1) -> {

                    courseMapObj.oneUnit_GradeMap["F"]?.let {
                        coursePointObj.oneUnitF.add(it)
                    }

                }


            }


        }

    }

    private fun clearCourseDataEntry() {
        _dbState.update {
            it.copy(
                courseCode = "",
                selectedCourseUnit = "",
                selectedCourseGrade = "",
                pickedCourseGradeDefaultLabel = ErrorPassedValues.enterCourseGradeLabel,
                pickedCourseUnitDefaultLabel = ErrorPassedValues.enterCourseUnitLabel,
                //defaultEnteredCourseCodeLabel = ErrorPassedValues.enterCourseCodeLabel,

            )

        }
        savedStateHandle.set(DB_STATE_KEY, _dbState.value)

    }

    private fun onReExecuteCalculationClearArrayField() {

        coursePointObj.sixUnitA.clear()
        coursePointObj.sixUnitB.clear()
        coursePointObj.sixUnitC.clear()
        coursePointObj.sixUnitD.clear()
        coursePointObj.sixUnitE.clear()
        coursePointObj.sixUnitF.clear()

        coursePointObj.fourUnitA.clear()
        coursePointObj.fourUnitB.clear()
        coursePointObj.fourUnitC.clear()
        coursePointObj.fourUnitD.clear()
        coursePointObj.fourUnitE.clear()
        coursePointObj.fourUnitF.clear()

        coursePointObj.threeUnitA.clear()
        coursePointObj.threeUnitB.clear()
        coursePointObj.threeUnitC.clear()
        coursePointObj.threeUnitD.clear()
        coursePointObj.threeUnitE.clear()
        coursePointObj.threeUnitF.clear()


        coursePointObj.twoUnitA.clear()
        coursePointObj.twoUnitB.clear()
        coursePointObj.twoUnitC.clear()
        coursePointObj.twoUnitD.clear()
        coursePointObj.twoUnitE.clear()
        coursePointObj.twoUnitF.clear()

        coursePointObj.oneUnitA.clear()
        coursePointObj.oneUnitB.clear()
        coursePointObj.oneUnitC.clear()
        coursePointObj.oneUnitD.clear()
        coursePointObj.oneUnitE.clear()
        coursePointObj.oneUnitF.clear()
        coursesUnitSubList.clear()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun greetings() {


        var myCurrentTime = LocalDateTime.now()
        var timeFormatter = ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        var formattedTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            myCurrentTime.format(timeFormatter).toString()
        } else {
            TODO("VERSION.SDK_INT < O")

        }
        var hour = formattedTime[11].toString()
        var realHour = formattedTime[12].toString()
        var separator = formattedTime[13].toString()
        var min = formattedTime[14].toString()
        var realMin = formattedTime[15].toString()


        var myHour = hour + realHour
        var morning =
            listOf<String>("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11")
        var afternoon = listOf<String>("12", "13", "14", "15")
        var evening = listOf<String>("16", "17", "18")
        var night = listOf<String>("19", "20", "21", "22", "23")



        if (morning.contains(myHour)) {
            _dbState.update {
                it.copy(
                    greeting = "Good Morning"
                )
            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            println("Good Morning")
        } else if (afternoon.contains(myHour)) {
            _dbState.update {
                it.copy(
                    greeting = "Good Afternoon"
                )
            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            println("Good Afternoon")

        } else if (evening.contains(myHour)) {

            _dbState.update {
                it.copy(
                    greeting = "Good Evening"
                )
            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            println("Good Evening")

        } else if (night.contains(myHour)) {

            _dbState.update {
                it.copy(
                    greeting = "Good Evening"
                )
            }
            savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            println("Good Night")

        }
    }

    private fun GpaDescriptor(gpa: Float, desc: String) {
        if (desc == "sgpa") {
            if (gpa in 4.50..5.00) {
                _dbState.update {
                    it.copy(
                        gpaDescriptor = "First Class",
                        remark = "You Performed Brilliantly"
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            } else if (gpa in 3.50..4.49) {

                _dbState.update {
                    it.copy(
                        gpaDescriptor = "Second Class Upper",
                        remark = "You Performed Amazing "
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            } else if (gpa in 2.40..3.49) {

                _dbState.update {
                    it.copy(
                        gpaDescriptor = "Second Class Lower",
                        remark = "You Performed Great"
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            } else if (gpa in 1.50..2.39) {

                _dbState.update {
                    it.copy(
                        gpaDescriptor = "Third Class",
                        remark = "You performed averagely"
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            } else if (gpa in 1.00..1.49) {

                _dbState.update {
                    it.copy(
                        gpaDescriptor = "Pass",
                        remark = "You passed"
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            } else if (gpa in 0.00..1.00) {

                _dbState.update {
                    it.copy(
                        gpaDescriptor = "Failure",
                        remark = "You Failed"
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            }

        } else if (desc == "cgpa") {

            if (gpa in 4.50..5.00) {
                _fiveCgpaUiState.update {
                    it.copy(
                        gpaDescriptor = "First Class",
                        remark = "You Performed Brilliantly"
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)

            } else if (gpa in 3.50..4.49) {

                _fiveCgpaUiState.update {
                    it.copy(
                        gpaDescriptor = "Second Class Upper",
                        remark = "You Performed Amazing "
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            } else if (gpa in 2.40..3.49) {

                _fiveCgpaUiState.update {
                    it.copy(
                        gpaDescriptor = "Second Class Lower",
                        remark = "You Performed Great"
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            } else if (gpa in 1.50..2.39) {

                _fiveCgpaUiState.update {
                    it.copy(
                        gpaDescriptor = "Third Class",
                        remark = "You performed averagely"
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            } else if (gpa in 1.00..1.49) {

                _fiveCgpaUiState.update {
                    it.copy(
                        gpaDescriptor = "Pass",
                        remark = "You passed"
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            } else if (gpa in 0.00..1.00) {

                _fiveCgpaUiState.update {
                    it.copy(
                        gpaDescriptor = "Failure",
                        remark = "You Failed"
                    )
                }
                savedStateHandle.set(DB_STATE_KEY, _dbState.value)


            }


        }

    }


}