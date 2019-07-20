package com.fourteenrows.p2pcs.UnitTests

import com.fourteenrows.p2pcs.objects.cars.FetchedVehicle
import com.fourteenrows.p2pcs.objects.cars.Vehicle
import com.fourteenrows.p2pcs.objects.reservations.Reservation
import com.fourteenrows.p2pcs.objects.reservations.ReservationCardObject
import com.fourteenrows.p2pcs.objects.reservations.ReservationCardType
import com.fourteenrows.p2pcs.objects.user.User
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class Objects {
    @Test
    fun testCardObject() {
        val type = ReservationCardType.TITLE_PAST_PRENOTATION
        val cardObject = ReservationCardObject(type)
        assertEquals(cardObject.cardType, type)
    }

    @Test
    fun testFetchedVehicle() {
        val date = Date(1234567)
        val model = "modello"
        val owner = "owner"
        val plate = "AA123AA"
        val seats = 2L
        val cid = "asd123"
        val ia = false
        val fv = FetchedVehicle(date, model, owner, plate, seats, ia, cid)
        assertEquals(date, fv.final_availability)
        assertEquals(model, fv.model)
        assertEquals(owner, fv.owner)
        assertEquals(plate, fv.plate)
        assertEquals(seats, fv.seats)
        assertEquals(cid, fv.cid)
        assertEquals(ia, fv.instant_availability)
    }

    @Test
    fun testkReservation() {
        val car = "action"
        val model = "model"
        val owner = "owner"
        val date = Date(123456)
        val res = Reservation(car, model, owner, date, false)
        assertEquals(date, res.start_slot)
        assertEquals(model, res.model)
        assertEquals(owner, res.owner)
        assertEquals(car, res.carId)
    }

    @Test
    fun testUser() {
        val name = "name"
        val surname = "surname"
        val mail = "mail"
        val exp = 123L
        val gaiaCoin = 1234L
        val weekpoints = 12L
        val user = User(name, surname, mail, exp, gaiaCoin, weekpoints, Date(0), Date(0))
        assertEquals(name, user.name)
        assertEquals(surname, user.surname)
        assertEquals(mail, user.mail)
        assertEquals(exp, user.exp)
        assertEquals(gaiaCoin, user.gaia_coins)
        assertEquals(weekpoints, user.week_points)
    }

    @Test
    fun testVehicle() {
        val date = Date(1234567)
        val model = "modello"
        val owner = "owner"
        val plate = "AA123AA"
        val seats = 2L
        val cid = "asd123"
        val ia = false
        val vehicle = Vehicle(date, model, owner, plate, seats, ia)
        assertEquals(date, vehicle.final_availability)
        assertEquals(model, vehicle.model)
        assertEquals(owner, vehicle.owner)
        assertEquals(plate, vehicle.plate)
        assertEquals(seats, vehicle.seats)
        assertEquals(ia, vehicle.instant_availability)
    }

}