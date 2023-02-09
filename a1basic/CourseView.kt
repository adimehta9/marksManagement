package com.example.a1basic

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Insets
import javafx.scene.control.ScrollPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox

// VBox for all individual Saved Courses
class CourseView(private val model: Model): VBox(), InvalidationListener {
    init {
        model.addListener(this)
        invalidated(null)
    }

    // Clears display and updates it with new courses
    override fun invalidated(observable: Observable?){
        children.clear()
        model.getCourses().forEach {
            // if it should be displayed
            if(it.value.show) {
                children.add(SavedCourse(model, it.value.id, it.value.name, it.value.term, it.value.grade)).apply {
                    padding = Insets(5.0)
                    spacing = 5.0
                }
            }
        }
    }
}
