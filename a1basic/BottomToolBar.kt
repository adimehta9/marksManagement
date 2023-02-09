package com.example.a1basic

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import java.lang.Exception
import java.lang.NullPointerException


class BottomToolBar(model: Model): HBox() {

    // Text field for the Course's ID input
    private val courseID: TextField = TextField().apply{
        prefWidth = 80.0
        prefHeight = 5.0
        setHgrow(this, Priority.SOMETIMES)
    }

    // Text field for Course Name
    private val courseName:TextField = TextField().apply{
        prefWidth = 300.0
        prefHeight = 5.0
        setHgrow(this, Priority.ALWAYS)
    }


    // All possible terms that can be used
    private val term:ChoiceBox<String> = ChoiceBox<String>().apply{
        items.addAll("F20", "W20", "S20", "F21", "W21", "S21", "F22", "W22", "S22", "F23", "W23", "S23")
    }


    // Grade of the course about to be added (can be WD)
    private val grade: TextField = TextField().apply{
        prefWidth = 40.0
        prefHeight = 5.0
    }


    // Adds the course to the course list
    private val create: Button = Button("Create").apply{
        maxWidth = 70.5
        prefHeight = 5.0
        onAction = EventHandler {
            // makes sure id, value and grade aren't empty, and grade either WD or 0 ≤..≤ 100
            try {
                model.addCourse(courseID.text, courseName.text, term.value, grade.text)
                courseID.text = ""
                courseName.text = ""
                term.value = null
                grade.text = ""
            } catch(nfe:NumberFormatException) {
                grade.text = "!"
                println("Grade wasn't WD or number")
            } catch(npe: NullPointerException) {
                println("Term not selected")
            } catch(e: Exception) {
                grade.text = "!"
                println(e.message)
            }
        }
    }

    init{
        children.addAll(courseID, courseName, term, grade, create)
        background = Background(BackgroundFill(Color.LIGHTGRAY, null, null))
        padding = Insets(10.0)
        spacing = 10.0
    }



}