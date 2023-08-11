package com.example.gpcalculator.ScreenElements

import GpCalculatorPrototype.Data.GpData
import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.gpcalculator.Data.CourseItemsModifierDropDownItems
import com.example.gpcalculator.ui.theme.Cream


@Composable
fun TotalCoursesListCardViewToDisplay(
    data: ArrayList<GpData>,
    onClickEvent: (DialogBoxUiEvents) -> Unit,
) {
    val state = rememberLazyListState()

    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        // contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(items = data, key = { id, listItem ->
            listItem.hashCode()
        }) { index, item ->

            val context = LocalContext.current

            MyCardView(
                info = item,
                index = index,
                onEvent = onClickEvent,
                state = DialogBoxState(),
                dropDownItems = listOf(

                    CourseItemsModifierDropDownItems("Edit"),
                    CourseItemsModifierDropDownItems("Delete")

                ),
                onMenuItemClick = {
                    Toast.makeText(
                        context,
                        it.text,
                        Toast.LENGTH_LONG
                    ).show()

                    if (it.text == "Delete") {
                        onClickEvent(DialogBoxUiEvents.deleteCourseEntry(index))
                    }
                    if (it.text == "Edit") {
                        Toast.makeText(
                            context,
                            item.courseCode,
                            Toast.LENGTH_LONG
                        ).show()
                        onClickEvent(DialogBoxUiEvents.updateCourseIndexEntry(index.toString()))
                        onClickEvent(
                            DialogBoxUiEvents.editItemsEntries(
                                item.courseCode,
                                item.courseGrade,
                                item.courseUnit.toString()
                            )
                        )
                        onClickEvent(DialogBoxUiEvents.showCourseEntryEditDBox)
                    }
                },
                onItemClick = onClickEvent


            )

        }


    }


}


@Composable
fun MyCardView(
    info: GpData,
    index: Int,
    onEvent: (DialogBoxUiEvents) -> Unit,

    dropDownItems: List<CourseItemsModifierDropDownItems>,
    modifier: Modifier = Modifier,
    state: DialogBoxState,
    onItemClick: (DialogBoxUiEvents) -> Unit,
    onMenuItemClick: (CourseItemsModifierDropDownItems) -> Unit

) {


    val context = LocalContext.current
    var pressOffset by remember {
        mutableStateOf(DpOffset(0.dp, 100.dp))
    }

    val density = LocalDensity.current
    val interactionSource = remember {
        MutableInteractionSource()
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

    var isContextMenuVisible by remember {
        mutableStateOf(false)
    }
    val myContext = LocalContext.current



    androidx.compose.material3.Card(
        colors = CardDefaults.cardColors(containerColor = Cream),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
            // .fillMaxWidth()
            .height(70.dp)
            .background(color = Cream)

    ) {
        Box(

            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .indication(interactionSource = interactionSource, LocalIndication.current)
                .pointerInput(true) {
                    detectTapGestures(
                        onLongPress = {
                            //onItemClick(DialogBoxUiEvents.showCourseDataEntriesContextmenu)
                            isContextMenuVisible = true
                            Toast
                                .makeText(myContext, "A  ahead", Toast.LENGTH_SHORT)
                                .show()
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                            onItemClick(DialogBoxUiEvents.showCourseDataEntriesContextmenu)


                        },
                        onPress = {
                            //Toast.makeText(myContext, "A step ahead", Toast.LENGTH_SHORT).show()
                            val press = PressInteraction.Press(it)
                            interactionSource.emit(press)
                            tryAwaitRelease()
                            interactionSource.emit(PressInteraction.Release(press))
                        }
                    )

                }
                .padding(top = 8.dp)


        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = info.courseCode, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(text = info.courseGrade, fontWeight = FontWeight.Bold)
                    Text(text = info.courseUnit.toString(), fontWeight = FontWeight.Bold)

                }

            }

        }

        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = {
                isContextMenuVisible = false
                onEvent(DialogBoxUiEvents.hideCourseDataEntriesContextmenu)
            },
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight

            ),
            modifier = Modifier.background(Cream)
        ) {
            dropDownItems.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onMenuItemClick(item)
                        isContextMenuVisible = false
                        onEvent(DialogBoxUiEvents.hideCourseDataEntriesContextmenu)
                    }) {

                    Text(text = item.text)

                }

            }
        }
    }
}