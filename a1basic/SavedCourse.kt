package com.example.a1basic

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.paint.Color


// Display for each course within the Course list
class SavedCourse(model: Model, id: String, name: String, t: String, g: String): HBox() {

    // ID of created course (cannot be edited)
    private val courseID: TextField = TextField().apply{
        text = id
        prefWidth = 80.0
        prefHeight = 5.0
        alignment = Pos.CENTER
        isEditable = false
    }

    // name of the course
     private val courseName: TextField = TextField().apply{
        text = name
        prefWidth = 300.0
        prefHeight = 5.0
        HBox.setHgrow(this, Priority.ALWAYS)
        onKeyTyped = EventHandler {
            // upon change
            delOrUndo.text = "Undo"
            update.isDisable = false
        }
    }

    // term selected as choice box
    private val term: ChoiceBox<String> = ChoiceBox<String>().apply{
        value = t
        items.addAll("F20", "W20", "S20", "F21", "W21", "S21", "F22", "W22", "S22", "F23", "W23", "S23")
        onAction = EventHandler {
            // upon change
            update.isDisable = false
            delOrUndo.text = "Undo"
        }
    }

    // grade of the course
    private val grade: TextField = TextField().apply {
        text = g
        prefWidth = 40.0
        prefHeight = 5.0
        onKeyTyped = EventHandler {
            // upon change
            update.isDisable = false
            delOrUndo.text = "Undo"
        }
    }

    // delete button or undo
    private val delOrUndo: Button = Button("Delete").apply{
        maxWidth = 70.5
        prefHeight = 5.0


        onAction = EventHandler {
            if(text == "Delete") model.removeCourse(courseID.text)
            else {
                // must be undo -> revert values back
                // make button delete again, and disable update button
                courseName.text = model.getCourses()[courseID.text]?.name
                term.value = model.getCourses()[courseID.text]?.term
                grade.text = model.getCourses()[courseID.text]?.grade
                text = "Delete"
                update.isDisable = true
            }
        }
    }


    private val update: Button = Button("Update").apply{
        maxWidth = 70.5
        prefHeight = 5.0
        isDisable = true

        onAction = EventHandler {
           try{
               model.updateCourse(courseID.text, courseName.text, term.value, grade.text)
               delOrUndo.text = "Delete"
               isDisable = true
           } catch(nfe:NumberFormatException) {
               println("Invalid Input")
           }
        }
    }



    private val c = if(g.uppercase() == "WD") Color.DARKSLATEGRAY
    else if (g.toInt() < 50) Color.LIGHTCORAL
    else if (g.toInt() < 60) Color.LIGHTBLUE
    else if (g.toInt() < 91) Color.LIGHTGREEN
    else if (g.toInt() < 96) Color.SILVER
    else Color.GOLD


    init{
        children.addAll(courseID, courseName, term, grade, update, delOrUndo).apply {
            background = Background(BackgroundFill(c, null, null))
            spacing = 10.0
            padding = Insets(10.0)
            HBox.setHgrow(courseName, Priority.ALWAYS)
        }



    }

}