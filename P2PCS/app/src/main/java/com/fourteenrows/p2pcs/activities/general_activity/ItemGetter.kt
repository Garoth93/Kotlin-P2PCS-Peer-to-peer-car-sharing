package com.fourteenrows.p2pcs.activities.general_activity

import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.objects.items.Item

@Suppress("UNCHECKED_CAST")
class ItemGetter(private val listener: IItemListener) {
    val database = ModelFirebase()

    private fun getRandomItem(box: ArrayList<String>?) = box!!.random()

    fun getBooster(box: Map<String, ArrayList<String>>, except: String) {
        val item = getRandomItem(box["booster"])
        database.getBooster(item)
            .addOnSuccessListener {
                val map = it.data!!
                listener.showItem(
                    Item(
                        map["name"] as String,
                        map["description"] as String,
                        map["cost"] as Long,
                        false,
                        it.id
                    )
                )
                //TODO("GET IMAGE")
            }
    }

    fun getAvatar(box: Map<String, ArrayList<String>>, except: String) {
        val item = getRandomItem(box["avatar"])
        database.getAvatarItem(item)
            .addOnSuccessListener {
                val map = it.data!!
                listener.showItem(
                    Item(
                        map["name"] as String,
                        map["description"] as String,
                        map["cost"] as Long,
                        false,
                        it.id
                    )
                )
                //TODO("GET IMAGE")
            }
    }
}