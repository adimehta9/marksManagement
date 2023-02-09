package com.example.a1basic

import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.layout.HBox

class TopToolBar(model: Model): HBox() {

    private val sortBox: ChoiceBox<String> = ChoiceBox<String>().apply {
        value = "Course Code"
        items.addAll("Course Code", "Term", "Grade Ascending", "Grade Descending" )
        valueProperty().addListener{_, _, _ ->
            model.sortCourse(value, filterBox.value, wd.isSelected)
        }
    }

    private val filterBox: ChoiceBox<String> = ChoiceBox<String>().apply{
        value = "All Courses"
        items.addAll( "All Courses", "CS Courses Only", "Math Courses Only", "Other Courses")
        valueProperty().addListener { _, _, _ ->
            model.sortCourse(sortBox.value, value, wd.isSelected)
        }
    }




    private val sort: HBox = HBox(
        Label("Sort by:").apply {
            padding = Insets(12.0)
        },
        sortBox,
        Separator().apply{
            orientation = Orientation.VERTICAL
            padding = Insets(5.0)
        }).apply {
        alignment = Pos.CENTER
    }

    private val filter = HBox(
        Label("Filter by:").apply {
            padding = Insets(5.0)
        },
        filterBox,
        Separator().apply{
            orientation = Orientation.VERTICAL
            padding = Insets(10.0)
        }).apply {
        alignment = Pos.CENTER
    }

    private val wd = CheckBox("Include WD").apply{
        isSelected = true
        padding = Insets(10.0)
        alignment = Pos.CENTER
        selectedProperty().addListener { _, _, newValue ->
            model.sortCourse(sortBox.value, filterBox.value, newValue)
        }

    }

    init {
        children.addAll(sort, filter, wd)
        Course.curSort = sortBox.value
        model.setCurFilter(filterBox.value)
        model.setCurWD(wd.isSelected)
    }

}