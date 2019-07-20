package com.fourteenrows.p2pcs.objects.quests

import java.util.*

class ActiveQuestDB(
    val activation_time: Date,
    val completed: Date,
    val progress: Long
)