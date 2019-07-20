package com.fourteenrows.p2pcs.activities.avatar

import com.fourteenrows.p2pcs.model.database.ModelDatabase
import com.fourteenrows.p2pcs.model.database.ModelFirebase

class AvatarPresenter(toView: IAvatarView, database: ModelDatabase = ModelFirebase()) : IAvatarPresenter {

    private var view = toView
}