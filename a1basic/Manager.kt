package com.example.a1basic

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.control.Separator
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage

class Manager: Application() {
    override fun start(stage: Stage){
        val model = Model()
        val tbv = ToolBarView(model)
        val cv = ScrollPane(CourseView(model)).apply{
            isFitToWidth = true
        }
        val sbv = StatusBarView(model)
        val root = BorderPane(cv, tbv, null, sbv, null)

        stage.apply {
            title = "CS349 - A1 My Mark Management - a56mehta"
            scene = Scene(root, 800.0, 600.0)
        }.show()

    }
}