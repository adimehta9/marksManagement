package com.example.a1enhanced


import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint


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
        prefWidth = 45.0
        prefHeight = 5.0
    }


    // Adds the course to the course list
    private val create: Button = Button("Create").apply{
        maxWidth = 70.5
        prefHeight = 5.0
        onAction = EventHandler {
            // makes sure id, value and grade aren't empty, and grade either WD or 0 ≤..≤ 100
            try {
                println("term.value is " + courseID.text)
                model.addCourse(courseID.text, courseName.text, term.value ?: "", grade.text)
                courseID.text = ""
                courseName.text = ""
                term.value = null
                grade.text = ""

                courseID.border = Border(BorderStroke(null, null, null, null))
                term.border = Border(BorderStroke(null, null, null, null))
                grade.border = Border(BorderStroke(null, null, null, null))


            } catch(nfe:NumberFormatException) {
                // Grade was not a number or WD
                println("Number format")
                grade.border = Border(BorderStroke(Paint.valueOf("ff0000"), BorderStrokeStyle.SOLID, CornerRadii(0.0), BorderWidths(2.0)))
                if(courseID.text == "") courseID.border = Border(BorderStroke(Paint.valueOf("ff0000"), BorderStrokeStyle.SOLID, CornerRadii(0.0), BorderWidths(2.0)))
                else courseID.border = Border(BorderStroke(null, null, null, null))
                if(term.value == null) term.border = Border(BorderStroke(Paint.valueOf("ff0000"), BorderStrokeStyle.SOLID, CornerRadii(0.0), BorderWidths(2.0)))
                else term.border = Border(BorderStroke(null, null, null, null))

            } catch(e: CourseException) {

                if(!e.idValid) {
                    courseID.border = Border(
                        BorderStroke(
                            Paint.valueOf("ff0000"),
                            BorderStrokeStyle.SOLID,
                            CornerRadii(0.0),
                            BorderWidths(2.0)
                        )
                    )
                } else {
                    courseID.border = Border(BorderStroke(null, null, null, null))
                }

                if(!e.termValid) {
                    term.border = Border(
                        BorderStroke(
                            Paint.valueOf("ff0000"),
                            BorderStrokeStyle.SOLID,
                            CornerRadii(0.0),
                            BorderWidths(2.0)
                        )
                    )
                } else {
                    term.border = Border(BorderStroke(null, null, null, null))
                }

                if(!e.gradeValid) {
                    grade.border = Border(
                        BorderStroke(
                            Paint.valueOf("ff0000"),
                            BorderStrokeStyle.SOLID,
                            CornerRadii(0.0),
                            BorderWidths(2.0)
                        )
                    )
                } else {
                    grade.border = Border(BorderStroke(null, null, null, null))
                }
            }
        }
    }

    init{
        children.addAll(courseID, courseName, term, grade, create)
        background = Background(BackgroundFill(Color.LIGHTGRAY, CornerRadii(7.0), null))
        padding = Insets(10.0)
        spacing = 10.0
    }



}