package com.example.a1basic

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.layout.HBox
import javafx.scene.text.Font
import kotlin.math.round

class StatusBarView(private val model: Model): HBox(), InvalidationListener {
    override fun invalidated(observable: Observable?) {
        var courseCount = 0
        var gradeTotal = 0.0
        var failedCount = 0
        var numOfWDs = 0


        model.getCourses().forEach{
            if(it.value.show) {
                if (it.value.grade.uppercase() != "WD") {
                    if (it.value.grade.toInt() < 50) {
                        failedCount++
                    }
                    courseCount++
                    gradeTotal += it.value.grade.toDouble()
                } else {
                    numOfWDs++
                }
            }
        }

        children.clear()
        children.addAll(Label("Course Average: ${if(courseCount == 0) 
            "n/a" else (round(gradeTotal/courseCount * 100.0)/100.0).toString()}").apply{font = Font.font(12.0) },
            Separator().apply { orientation = Orientation.VERTICAL },
            Label("Courses Taken: $courseCount").apply{font = Font.font(12.0)},
            Separator().apply{orientation = Orientation.VERTICAL},
            Label("Courses Failed: $failedCount").apply{font = Font.font(12.0)},
        )

        if(model.getCurWD()) {
            children.addAll(Separator().apply { orientation = Orientation.VERTICAL },
                Label("WD Courses: $numOfWDs").apply{font = Font.font(12.0) })
        }
    }


    init {
        model.addListener(this)
        invalidated(null)
        padding = Insets(5.0, 5.0, 5.0, 15.0)
        spacing = 10.0
    }


}
