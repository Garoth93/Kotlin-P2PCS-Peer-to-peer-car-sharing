package com.fourteenrows.p2pcs.activities.avatar

import android.os.Bundle
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity

class AvatarActivity : GeneralActivity(), IAvatarView {
    private lateinit var presenter: IAvatarPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)
        initializeDrawer()

        presenter = AvatarPresenter(this)
/*
        val database = ModelFirebase()

        //ITEM
        /*val items = arrayOf(
            ItemBase("Capelli", "descrizione", 123),
            ItemBase("Occhi", "descrizione", 123),
            ItemBase("Pene", "descrizione", 123),
            ItemBase("Bocca", "descrizione", 123),
            ItemBase("Busto", "descrizione", 123)
        )
        items.forEach {
            //database.insert("Items", "", it)
        }*/

        //BOOSTERS
        /* Categorie:
        *   0 Moltiplicatore exp
        *   1 Moltiplicatore Gaiacoins
        *   2 Moltiplicatore exp e GaiaCoins
        *   3 Bonus passeggeri
        * */
        val boosters = arrayOf(
            BoosterToDatabase("Plenty of wisdom S", 0, 20, 1),
            BoosterToDatabase("Plenty of wisdom M", 0, 30, 1),
            BoosterToDatabase("Plenty of wisdom L", 0, 40, 1),
            BoosterToDatabase("Opulence S", 1, 20, 1),
            BoosterToDatabase("Opulence M", 1, 30, 1),
            BoosterToDatabase("Opulence L", 1, 40, 1),
            BoosterToDatabase("Lucky traveler S", 2, 15, 1),
            BoosterToDatabase("Lucky traveler M ", 2, 20, 1),
            BoosterToDatabase("Lucky traveler L ", 2, 30, 1),
            BoosterToDatabase("The more, the better", 3, 25, 1)
        )

        val bids = ArrayList<String>()
        var typeIndex = 0
        var sizeIndex = 0
        boosters.forEach {
            if (typeIndex != 3) {
                val bid = "b" + typeIndex.toShort() + sizeIndex.toShort()
                bids.add(bid)
                database.insert("Boosters", bid, it)
                if (++sizeIndex == 3) {
                    typeIndex++
                    sizeIndex = 0
                }
            } else {
                val bid = "b" + typeIndex.toShort() + "0"
                bids.add(bid)
                database.insert("Boosters", bid, it)
            }
        }

        //PACKAGE
        val box = Box(ArrayList<String>(), bids)
        database.insert("Box", "action", box)

/*
        //BADGE
        /* Categorie:
        *   0 viaggi completati
        *   1 livello raggiunto
        *   2 item acquistate
        *   3 x persone con cui si ha viaggiato
        * */
        val badges = arrayOf(
            Badge(
                "Completati 5 viaggi",
                "Vacationer",
                5L,
                0
            ),
            Badge(
                "Completati 10 viaggi",
                "Tourist",
                10L,
                0
            ),
            Badge(
                "Completati 15 viaggi",
                "Backpacker",
                15L,
                0
            ),
            Badge(
                "Completati 20 viaggi",
                "Pilgrim",
                20L,
                0
            ),
            Badge(
                "Completati 25 viaggi",
                "Adventurer",
                25L,
                0
            ),
            Badge(
                "Completati 50 viaggi",
                "Expert traveller",
                50L,
                0
            ),
            Badge(
                "Completati 100 viaggi",
                "Globetrotter",
                100L,
                0
            ),
            Badge(
                "Raggiunto il lv 5",
                "Amaryllis",
                5L,
                1
            ),
            Badge(
                "Raggiunto il lv 10",
                "Bird of paradise",
                10L,
                1
            ),
            Badge(
                "Raggiunto il lv 15",
                "Chrysanthemum",
                15L,
                1
            ),
            Badge(
                "Raggiunto il lv 20",
                "Dahlia",
                20L,
                1
            ),
            Badge(
                "Raggiunto il lv 25",
                "Elderflower",
                25L,
                1
            ),
            Badge(
                "Raggiunto il lv 30",
                "Freesia",
                30L,
                1
            ),
            Badge(
                "Raggiunto il lv 35",
                "Gladiolus",
                35L,
                1
            ),
            Badge(
                "Raggiunto il lv 40",
                "Hydrangea",
                40L,
                1
            ),
            Badge(
                "Raggiunto il lv 45",
                "Iris",
                45L,
                1
            ),
            Badge(
                "Raggiunto il lv 50",
                "Jasmine",
                50L,
                1
            ),
            Badge(
                "Raggiunto il lv 55",
                "Kalanchoe",
                55L,
                1
            ),
            Badge(
                "Raggiunto il lv 60",
                "Lily",
                60L,
                1
            ),
            Badge(
                "Raggiunto il lv 65",
                "Marigold",
                65L,
                1
            ),
            Badge(
                "Raggiunto il lv 70",
                "Narcissus",
                70L,
                1
            ),
            Badge(
                "Raggiunto il lv 75",
                "Orchid",
                75L,
                1
            ),
            Badge(
                "Raggiunto il lv 80",
                "Poinsettia",
                80L,
                1
            ),
            Badge(
                "Raggiunto il lv 85",
                "Rose",
                85L,
                1
            ),
            Badge(
                "Raggiunto il lv 90",
                "Snapdragon",
                90L,
                1
            ),
            Badge(
                "Raggiunto il lv 95",
                "Tulip",
                95L,
                1
            ),
            Badge(
                "Raggiunto il lv 100",
                "Ursinia",
                100L,
                1
            ),
            Badge(
                "Percorsi 5 km",
                "Greenhorn",
                5L,
                2
            ),
            Badge(
                " Percorsi 10 km ",
                "Novice",
                10L,
                2
            ),
            Badge(
                " Percorsi 15 km ",
                "Amateur",
                15L,
                2
            ),
            Badge(
                " Percorsi 20 km ",
                "Expert",
                20L,
                2
            ),
            Badge(
                " Percorsi 25 km ",
                "Champion",
                25L,
                2
            ),
            Badge(
                " Percorsi 50 km ",
                "Title-holder",
                50L,
                2
            ),
            Badge(
                " Percorsi 100 km ",
                "No one can stop me",
                100L,
                2
            ),
            Badge(
                "Acquistati 2 items dallo shop",
                "Daily Shopper",
                2L,
                3
            ),
            Badge (
                "Acquistati 5 items dallo shop",
                "Fashionista",
                5L,
                3
            ),
            Badge(
                "Acquistati 10 items dallo shop",
                "Trendsetter",
                10L,
                3
            ),
            Badge(
                "Acquistati 15 items dallo shop",
                "Shopaholic",
                15L,
                3
            ),
            Badge(
                "Acquistati 20 items dallo shop",
                "Big Spender",
                20L,
                3
            ),
            Badge(
                "Acquistati 25 items dallo shop",
                "Wealthy",
                25L,
                3
            ),
            Badge(
                "Acquistati 30 items dallo shop",
                "Mida’s favourite",
                30L,
                3
            ),
            Badge(
                "Acquistati 50 items dallo shop",
                "Filthy Rich",
                50L,
                3
            ),
            Badge(
                "Viaggiato con 5 persone",
                "Looking for friends",
                5L,
                4
            ),
            Badge(
                "Viaggiato con 10 persone",
                "Nature’s friend",
                10L,
                4
            ),
            Badge(
                "Viaggiato con 15 persone",
                "Enviroment compatible",
                15L,
                4
            ),
            Badge(
                "Viaggiato con 20 persone",
                "Charmer",
                20L,
                4
            ),
            Badge(
                "Viaggiato con 25 persone",
                "Plenty of friends",
                25L,
                4
            ),
            Badge(
                "Viaggiato con 50 persone",
                "Adored",
                50L,
                4
            ),
            Badge(
                "Viaggiato con 100 persone",
                "Everybody loves me",
                100L,
                4
            )

        )
        var count = 0
        badges.forEach {
            database.insert("Badges", "badge$count", it)
            count++
        }*/

        //ToDatabaseQuestS
        /* Categorie:
        *   0 effettua x viaggi in un unica prenotazione
        *   1 effettua un totale di x viaggi
        *   2 viaggia per un totale di x km
        *   3 viaggia per x km in un unico viaggio
        *   4 viaggia con x persone in un unico viaggio
        *   5 viaggia con x persone
        *   6 porta a termine x prenotazioni
        * */
        val ToDatabaseQuests = arrayOf(
            ToDatabaseQuest(
                "Effettua un viaggio in una prenotazione",
                "action",
                50,
                50,
                0,
                1
            ),
            ToDatabaseQuest(
                "Effettua due viaggi in una prenotazione",
                "action",
                80,
                80,
                0,
                2
            ),
            ToDatabaseQuest(
                "Effettua tre viaggi in una prenotazione",
                "action",
                100,
                100,
                0,
                3
            ),
            ToDatabaseQuest(
                "Effettua quattro viaggi in una prenotazione",
                "action",
                200,
                200,
                0,
                4
            ),
            ToDatabaseQuest(
                "Effettua cinque viaggi in una prenotazione",
                "action",
                400,
                400,
                0,
                5
            ),
            ToDatabaseQuest(
                "Effettua sei viaggi in una prenotazione",
                "action",
                900,
                900,
                0,
                6
            ),

            ToDatabaseQuest(
                "Effettua un totale di cinque viaggi",
                "action",
                100,
                100,
                1,
                5
            ),
            ToDatabaseQuest(
                "Effettua un totale di sette viaggi",
                "action",
                150,
                150,
                1,
                7
            ),
            ToDatabaseQuest(
                "Effettua un totale di dieci viaggi",
                "action",
                200,
                200,
                1,
                10
            ),
            ToDatabaseQuest(
                "Effettua un totale di dodici viaggi",
                "action",
                250,
                250,
                1,
                12
            ),
            ToDatabaseQuest(
                "Effettua un totale di quattordici viaggi",
                "action",
                300,
                300,
                1,
                14
            ),
            ToDatabaseQuest(
                "Effettua un totale di quindici viaggi",
                "action",
                350,
                350,
                1,
                15
            ),


            ToDatabaseQuest(
                "Viaggia per un totale di 5km",
                "action",
                50,
                50,
                2,
                5
            ),
            ToDatabaseQuest(
                "Viaggia per un totale di 10km ",
                "action",
                70,
                70,
                2,
                10
            ),
            ToDatabaseQuest(
                "Viaggia per un totale di 20km ",
                "action",
                90,
                90,
                2,
                20
            ),
            ToDatabaseQuest(
                "Viaggia per un totale di 50km ",
                "action",
                400,
                400,
                2,
                50
            ),
            ToDatabaseQuest(
                "Viaggia per un totale di 70km ",
                "action",
                500,
                500,
                2,
                70
            ),
            ToDatabaseQuest(
                "Viaggia per un totale di 100km ",
                "action",
                800,
                800,
                2,
                100
            ),


            ToDatabaseQuest(
                "Viaggia per 1km (in un unico viaggio)",
                "action",
                10,
                10,
                3,
                1
            ),
            ToDatabaseQuest(
                " Viaggia per 3km (in un unico viaggio)",
                "action",
                35,
                35,
                3,
                3
            ),
            ToDatabaseQuest(
                "Viaggia per 5km (in un unico viaggio)",
                "action",
                60,
                60,
                3,
                5
            ),
            ToDatabaseQuest(
                "Viaggia per 10km (in un unico viaggio)",
                "action",
                80,
                80,
                3,
                10
            ),
            ToDatabaseQuest(
                "Viaggia per 15km (in un unico viaggio)",
                "action",
                90,
                90,
                3,
                15
            ),
            ToDatabaseQuest(
                "Viaggia per 20km (in un unico viaggio)",
                "action",
                100,
                100,
                3,
                20
            ),


            ToDatabaseQuest(
                "Viaggia con una persona(in un unico viaggio)",
                "action",
                50,
                50,
                4,
                1
            ),
            ToDatabaseQuest(
                "Viaggia con due persone(in un unico viaggio)",
                "action",
                80,
                80,
                4,
                2
            ),
            ToDatabaseQuest(
                "Viaggia con tre persone(in un unico viaggio)",
                "action",
                120,
                120,
                4,
                3
            ),
            ToDatabaseQuest(
                "Viaggia con quattro persone(in un unico viaggio)",
                "action",
                200,
                200,
                4,
                4
            ),
            ToDatabaseQuest(
                "Viaggia con cinque persone(in un unico viaggio)",
                "action",
                250,
                250,
                4,
                5
            ),
            ToDatabaseQuest(
                "Viaggia con sei persone(in un unico viaggio)",
                "action",
                300,
                300,
                4,
                6
            ),


            ToDatabaseQuest(
                "Viaggia con un totale di tre persone",
                "action",
                50,
                50,
                5,
                3
            ),
            ToDatabaseQuest(
                "Viaggia con un totale di cinque persone",
                "action",
                90,
                90,
                5,
                5
            ),
            ToDatabaseQuest(
                "Viaggia con un totale di sette persone",
                "action",
                300,
                30,
                5,
                7
            ),
            ToDatabaseQuest(
                "Viaggia con un totale di dieci persone",
                "action",
                400,
                400,
                5,
                10
            ),
            ToDatabaseQuest(
                "Viaggia con un totale di dodici persone",
                "action",
                600,
                600,
                5,
                12
            ),
            ToDatabaseQuest(
                "Viaggia con un totale di quattordici persone",
                "action",
                800,
                800,
                5,
                14
            ),


            ToDatabaseQuest(
                "Effettua e porta a termine una prenotazione",
                "action",
                50,
                50,
                6,
                1
            ),
            ToDatabaseQuest(
                "Effettua e porta a termine due prenotazioni",
                "action",
                100,
                100,
                6,
                2
            ),
            ToDatabaseQuest(
                "Effettua e porta a termine tre prenotazioni",
                "action",
                200,
                200,
                6,
                3
            ),
            ToDatabaseQuest(
                "Effettua e porta a termine cinque prenotazioni",
                "action",
                300,
                300,
                6,
                5
            ),
            ToDatabaseQuest(
                "Effettua e porta a termine sette prenotazioni",
                "action",
                500,
                500,
                6,
                7
            ),
            ToDatabaseQuest(
                "Effettua e porta a termine dieci prenotazioni",
                "action",
                700,
                700,
                6,
                10
            )
        )

        //qeasykm
        val difficulty = arrayOf("veryEasy", "easy", "medium", "hard", "veryHard", "masochist")
        val type = arrayOf("trip", "totalTrip", "totalKm", "km", "person", "totalPerson", "totalReservation")

        var i = 0
        var j = 0
        ToDatabaseQuests.forEach {

        }


        ToDatabaseQuests.forEach {
            if (i == 6) {
                i = 0
                ++j
            }
            if (j == 7) {
                j = 0
            }
            val s = "q" + type[j] + difficulty[i]
            database.insert("Quests", s, it)
            ++i
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
        */
    }
}