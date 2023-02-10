package com.example.a1enhanced
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


        children.clear()
        children.addAll(
            Label(
                "Course Average: ${
                    if (model.getCourseCount() == 0)
                        "n/a" else (round(model.getGradeTotal() / model.getCourseCount() * 100.0) / 100.0).toString()
                }"
            ).apply { font = Font.font(12.0) },
            Separator().apply { orientation = Orientation.VERTICAL },
            Label("GPA:  ${if(model.getCourseCount() == 0) "n/a" else (model.getCumGPA()/model.getCourseCount()).toString()}"),
            Separator().apply { orientation = Orientation.VERTICAL },
            Label("Courses Taken: ${model.getCourseCount()}").apply { font = Font.font(12.0) },
            Separator().apply { orientation = Orientation.VERTICAL },
            Label("Courses Failed: ${model.getFailedCount()}").apply { font = Font.font(12.0) },
        )

        if(model.getCurWD()) {
            children.addAll(Separator().apply { orientation = Orientation.VERTICAL },
                Label("WD Courses: ${model.getNumOfWDs()}").apply{font = Font.font(12.0) })
        }
    }


    init {
        model.addListener(this)
        invalidated(null)
        padding = Insets(5.0, 5.0, 5.0, 15.0)
        spacing = 10.0
    }


}