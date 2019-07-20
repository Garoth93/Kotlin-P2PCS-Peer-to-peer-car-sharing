package com.fourteenrows.p2pcs.activities.info

import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase

class InfoPresenter(toView: IInfoView, private val database: ModelDatabase = ModelFirebase()) :
    IInfoPresenter {

    private var view = toView
}