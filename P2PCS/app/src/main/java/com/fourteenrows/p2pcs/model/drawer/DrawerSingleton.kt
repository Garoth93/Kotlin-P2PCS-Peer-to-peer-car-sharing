package com.fourteenrows.p2pcs.model.drawer

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.authentication.login.LoginActivity
import com.fourteenrows.p2pcs.activities.booster.BoosterActivity
import com.fourteenrows.p2pcs.activities.car.CarActivity
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import com.fourteenrows.p2pcs.activities.info.InfoActivity
import com.fourteenrows.p2pcs.activities.profile.ProfileActivity
import com.fourteenrows.p2pcs.activities.quest.QuestActivity
import com.fourteenrows.p2pcs.activities.reservation.ReservationActivity
import com.fourteenrows.p2pcs.activities.shop.ShopActivity
import com.fourteenrows.p2pcs.activities.trip.TripActivity
import com.fourteenrows.p2pcs.activities.tutorial.TutorialActivity
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import com.fourteenrows.p2pcs.model.utility.ImageHelper
import com.fourteenrows.p2pcs.objects.user.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar.view.*
import java.util.*


object DrawerSingleton {
    private val database = ModelFirebase()
    private val items = mutableListOf<PrimaryDrawerItem>()

    fun getDrawer(activity: GeneralActivity) {
        loadActivityLinks(activity)
        loadUserStates(activity)
    }

    private fun loadActivityLinks(
        activity: Activity
    ) {
        if (items.size != 0)
            return
        items.add(
            generateItem(
                "Prenotazioni",
                R.drawable.icon_reservations,
                activity,
                ReservationActivity()
            )!!
        )
        items.add(
            generateItem(
                "Veicoli",
                R.drawable.icon_cars,
                activity,
                CarActivity()
            )!!
        )
        items.add(
            generateItem(
                "Viaggi", R.drawable.icon_trips, activity,
                TripActivity()
            )!!
        )
        items.add(
            generateItem(
                "Shop",
                R.drawable.icon_shop,
                activity,
                ShopActivity()
            )!!
        )
        items.add(
            generateItem(
                "Profilo",
                R.drawable.icon_profile,
                activity,
                ProfileActivity()
            )!!
        )
        items.add(
            generateItem(
                "Missioni",
                R.drawable.icon_quests,
                activity,
                QuestActivity()
            )!!
        )/*
        items.add(
            generateItem(
                "Avatar",
                R.drawable.icon_avatar,
                activity,
                AvatarActivity()
            )!!
        )*/
        items.add(
            generateItem(
                "Boosters",
                R.drawable.icon_boosters,
                activity,
                BoosterActivity()
            )!!
        )
        items.add(
            generateItem(
                "Aiuto",
                R.drawable.icon_tutorial,
                activity,
                TutorialActivity()
            )!!
        )
        items.add(
            generateItem(
                "Info",
                R.drawable.icon_info,
                activity,
                InfoActivity()
            )!!
        )
        val logout = {
            activity.getSharedPreferences("userData", 0).edit().clear().apply()
            FirebaseAuth.getInstance().signOut()
        }
        items.add(
            generateItemWithFun(
                "Logout",
                R.drawable.icon_logout,
                activity,
                LoginActivity(),
                logout
            )!!
        )
    }

    fun loadUserStates(activity: GeneralActivity) {
        val cache: SharedPreferences = activity.getSharedPreferences("userData", 0)
        lateinit var user: User
        if (!cache.contains("mail")) {
            database.getUserDocument()
                .addOnSuccessListener {
                    user = database.buildUser(it)

                    val editor: SharedPreferences.Editor = cache.edit()
                    editor.putString("name", user.name)
                    editor.putString("surname", user.surname)
                    editor.putString("mail", user.mail)
                    editor.putLong("exp", user.exp)
                    editor.putLong("gaia_coins", user.gaia_coins)
                    editor.putLong("week_points", user.week_points)
                    editor.putLong("last_free_change_quest", user.last_free_change_quest.time)
                    editor.apply()
                    updateDrawer(user, activity)
                }
        } else {
            user = User(
                cache.getString("name", "")!!,
                cache.getString("surname", "")!!,
                cache.getString("mail", "")!!,
                cache.getLong("exp", 0),
                cache.getLong("gaia_coins", 0),
                cache.getLong("week_points", 0),
                Date(cache.getLong("last_free_change_quest", 0)),
                Date(cache.getLong("last_daily_new_quest", 0))
            )
            updateDrawer(user, activity)
        }
    }

    private fun generateItemWithFun(
        name: String,
        icon: Int,
        activity: Activity,
        toActivity: GeneralActivity,
        logout: () -> Unit
    ): PrimaryDrawerItem? {
        return PrimaryDrawerItem()
            .withIdentifier(1)
            .withName(name)
            .withIcon(icon)
            .withOnDrawerItemClickListener { view, _, _ ->
                logout()
                view.context.startActivity(Intent(activity, toActivity.javaClass))
                false
            }
    }

    private fun generateItem(name: String, icon: Int, activity: Activity, toActivity: GeneralActivity) =
        PrimaryDrawerItem()
            .withIdentifier(1)
            .withName(name)
            .withIcon(icon)
            .withOnDrawerItemClickListener { view, _, _ ->
                view.context.startActivity(Intent(activity, toActivity.javaClass))
                false
            }
            .withSelectable(false)

    private fun generateDrawer(toolbar: Toolbar, activity: Activity, items: MutableList<PrimaryDrawerItem>, name: String, surname: String, email: String): Drawer? {
        val drawer = DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .withAccountHeader(
                generateAccountHeader(
                    activity,
                    "$name $surname",
                    email
                )!!
            )
            .withActionBarDrawerToggle(true)
            .withActionBarDrawerToggleAnimated(true)
            .withCloseOnClick(true)
            .withMultiSelect(false)
            .withSelectedItem(-1)
        for (item in items) {
            drawer.addDrawerItems(item)
        }
        return drawer.build()
    }

    private fun generateAccountHeader(activity: Activity, name: String, email: String) =
        AccountHeaderBuilder()
            .withActivity(activity)
            .addProfiles(generateProfile(name, email))
            .withSelectionListEnabled(false)
            .build()


    private fun generateProfile(name: String, email: String): ProfileDrawerItem? {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(iv: ImageView, u: Uri, d: Drawable, tag: String) {
                ImageHelper().setAvatar(iv, ModelFirebase().getUid()!!, d)
            }

            override fun cancel(imageView: ImageView) {
                Picasso.get().cancelRequest(imageView)
            }
        })
        return ProfileDrawerItem()
            .withName(name)
            .withEmail(email)
            .withIcon(ImageHelper().getLinkOf(ModelFirebase().getUid()!!))
    }

    private fun updateDrawer(user: User, activity: GeneralActivity) {
        activity.toolBar.gaiaCoinValue.text = user.gaia_coins.toString()
        activity.toolBar.weekPointValue.text = user.week_points.toString()
        generateDrawer(
            activity.toolBar,
            activity,
            items,
            user.name,
            user.surname,
            user.mail
        )
    }

    fun updateUser(activity: Activity): Task<DocumentSnapshot> {
        return database.getUserDocument()
            .addOnSuccessListener {
                val user: User = database.buildUser(it)
                activity.getSharedPreferences("userData", 0).edit().clear().apply()
                val cache: SharedPreferences = activity.getSharedPreferences("userData", 0)
                val editor: SharedPreferences.Editor = cache.edit()
                editor.putString("name", user.name)
                editor.putString("surname", user.surname)
                editor.putString("mail", user.mail)
                editor.putLong("exp", user.exp)
                editor.putLong("gaia_coins", user.gaia_coins)
                editor.putLong("week_points", user.week_points)
                editor.apply()
            }
    }
}