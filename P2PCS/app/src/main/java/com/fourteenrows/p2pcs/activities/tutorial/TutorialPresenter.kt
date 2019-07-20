package com.fourteenrows.p2pcs.activities.tutorial

import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase

class TutorialPresenter(toView: TutorialActivity, private val database: ModelDatabase = ModelFirebase()) {
    private var view = toView

}