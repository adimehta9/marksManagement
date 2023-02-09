package com.example.a1basic

import javafx.geometry.Insets
import javafx.scene.control.Separator
import javafx.scene.layout.VBox

class ToolBarView(model: Model): VBox() {
    init {
        children.addAll(TopToolBar(model), Separator(), BottomToolBar(model)).apply {
            padding = Insets(5.0)
        }
    }



}