package com.fourteenrows.p2pcs.UnitTests

import com.fourteenrows.p2pcs.objects.quests.ActiveQuest
import com.fourteenrows.p2pcs.objects.quests.QuestAuthorizing
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

class QuestAuthorizingTest {

    @Test
    fun getMisisonFor() {
        val questSelector = QuestAuthorizing(8)
        val actived = ArrayList<ActiveQuest>()
        actived.add(
            ActiveQuest(
                "qtripeasy",
                "test",
                10,
                123,
                1,
                123,
                "",
                Date(),
                Date(),
                0
            )
        )
        for (i in 0..10)
        //System.out.println(Random.nextInt())
            System.out.println(
                questSelector.getQuestFor(actived)
            )
    }
}