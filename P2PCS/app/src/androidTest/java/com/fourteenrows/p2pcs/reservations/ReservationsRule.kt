package com.fourteenrows.p2pcs.reservations

import androidx.test.rule.ActivityTestRule
import com.fourteenrows.p2pcs.activities.reservation.ReservationActivity
import com.fourteenrows.p2pcs.activities.reservation.add_reservation.AddReservationActivity
import com.fourteenrows.p2pcs.activities.reservation.add_reservation.add_reservation_vehicle.AddReservationVehicleActivity
import com.fourteenrows.p2pcs.model.database.ModelFirebase
import java.lang.Thread.sleep

class ReservationsRule(activityClass: Class<ReservationActivity>) : ActivityTestRule<ReservationActivity>(activityClass) {
    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        ModelFirebase().authenticateUser("fourteenrows@gmail.com", "testacc")
        sleep(3000)
    }
}