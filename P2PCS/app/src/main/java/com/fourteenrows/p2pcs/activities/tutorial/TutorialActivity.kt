package com.fourteenrows.p2pcs.activities.tutorial

import android.os.Bundle
import android.view.Menu
import com.fourteenrows.p2pcs.R
import com.fourteenrows.p2pcs.activities.general_activity.GeneralActivity
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity : GeneralActivity() {
    private lateinit var presenter: TutorialPresenter
    private lateinit var listAdapter: TutorialListAdapter
    private var list = listOf(
        "Boosters",
        "GaiaCoins",
        "Missioni",
        "Prenotazioni",
        "Profilo",
        "Punti esperienza",
        "Shop",
        "Veicoli",
        "Viaggi"
    )
    private var map = HashMap<String, List<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        initializeDrawer()

        presenter = TutorialPresenter(this)
        initData()
        listAdapter = TutorialListAdapter(this, list, map)
        listTutorial.setAdapter(listAdapter)
    }

    private fun initData() {
        val profilo = mutableListOf(
            "La pagina \"Profilo\" è raggiungibile attraverso il menù. Al suo interno è possibile visualizzare e cambiare i dati relativi all' account. È possibile modificare tutti" +
                    " i dati tranne la mail. Per modificare la mail è necessario contattare GaiaGo. Per modificare la foto profilo, cliccare sull'immagine profilo e selezionare la cartella che contiene " +
                    "la nuova immagine. Per modificare gli altri dati, cliccare sul campo del dato da modificare, inserire i nuovi dati e confermare."
        )

        val veicoli = mutableListOf(
            "La pagina \"Veicoli\" è raggiungibile attraverso il menù." + " In questa pagina è possibile inserire una nuova auto posseduta da condividere, o modificare " +
                    "le auto già registrate. Per aggiungere un veicolo, premere il pulsante \"+\", compilare i campi con i dati corretti e premere il pulsante \"Inserisci auto\". L'auto" +
                    " sarà quindi visibile nella pagina \"Veicoli\". Per visualizzare un'altro dei veicoli inseriti, utilizzare le frecce poste accanto all'immagine dell' auto. Per modificare " +
                    "la foto profilo, cliccare sull'immagine dell'auto e selezionare la cartella che contiene" +
                    "la nuova immagine che si vuole caricare. Per modificare i dati di un auto, cliccare sul campo del dato da modificare, inserire i nuovi dati e confermare. " +
                    "È possibile eliminare un auto cliccando sull' icona del bidone rosso posta a fianco del modello dell'auto."
        )

        val prenotazioni = mutableListOf(
            "Per prenotare un’auto bisogna accedere alla pagina " + "\"Prenotazioni\"" + " dal menù e premere il pulsante + per aggiungere una nuova prenotazione. " +
                    "Negli appositi campi, bisogna inserire la data e la fascia oraria in cui si vuole avere l’auto, dopodichè sarà possibile selezionare una delle auto disponibili nel periodo richiesto. " +
                    "Dopo aver selezionato l’auto desiderata, cliccare sul pulsante \"Inserisci prenotazione\" e confermare. " +
                    "Sarà ora possibile visualizzare la prenotazione nella pagina \"Prenotazioni\", sotto \"Prenotazioni attive\"."
        )

        val viaggi = mutableListOf(
            "Per poter viaggiare con l’applicazione bisogna necessariamente avere una prenotazione attiva. Premere il pulsante \"+\" per aggiungere il viaggio da svolgere. " +
                    "Bisogna inserire il luogo di partenza e di arrivo negli appositi campi, in questa schermata possono essere aggiunti i partecipanti al viaggio in possesso di un account, " +
                    "per permettergli di guadagnare punti. Il numero di partecipanti massimi è indicato al di sotto del luogo di arrivo. Per concludere premere il pulsante \"Viaggia\" e confermare."
        )

        val gaiacoin = mutableListOf(
            "I \"GaiaCoins\" sono la moneta usata per gli scambi all’interno dell’applicazione. " +
                    "È possibile ottenerne portando a termine i viaggi. " +
                    "È, inoltre, possibile scambiare \"GaiaCoins\" con premi ed oggetti nella pagina \"Shop\", raggiungibile attraverso il menù. " +
                    "Possono anche essere utilizzarli per cambiare ricompense e missioni."
        )

        val exp = mutableListOf(
            "I \"Punti esperienza\" possono essere ottenuti portando a termine i viaggi. " +
                    "I punti vengono utilizzati per aumentare il livello. " +
                    "Ogni settimana i punti guadagnati vengono conteggiati nella classifica della \"Leaderboard\" e vengono resettati ogni domenica."
        )

        val shop = mutableListOf(
            "La pagina \"Shop\", raggiungibile attraverso il menù, contiene tutti gli oggetti ed i premi ottenibili utilizzando i \"GaiaCoin\". " +
                    "Sono ottenibili sia cupon per l'acquisto di beni, come buoni amazon, sia bonus per l'applicazione, come i \"Boosters\"." +
                    "Per acquistare un oggetto dallo \"Shop\", cliccare sul pulsante con il carrello a destra e confermare la scelta. " +
                    "I \"Boosters\" acquistati saranno visibili alla pagina \"Boosters\""
        )

        val boosters = mutableListOf(
            "I \"Boosters\" sono dei bonus ottenibili all'interno dell'applicazione. Essi possono portare ad un aumento di \"GaiaCoins\", di \"Punti esperienza\" o di entrambi. " +
                    "I bonus garantiti dai boosters sono usufruibili solo per la prima prenotazione in seguito alla loro attivazione. " +
                    "Per attivare un \"Booster\", accedere alla pagina \"Boosters\" (raggiungibile dal menù) e cliccare sul pulsante con la fiammella a destra. " +
                    "Un booster attivo mostrerà un messaggio in basso a sinistra. " +
                    "Il numero di booster di quel tipo di cui si è in possesso si può vedere nella fiammella verde in alto a destra. " +
                    "I booster sono ottenibili nello \"Shop\" o al completamento di missioni."
        )

        val missioni = mutableListOf(
            "La pagina \"Missioni\", raggiungibile attraverso il menù, contiene l'elenco di tutte le missioni attive in quel momento. " +
                    "In questa pagina è possibile controllare lo stato di avanzamento, cambiarle e vedere le ricompense ottenibili al loro completamento. " +
                    "È possibile avere un massimo di quattro missioni attive in totale. Nel caso ce ne siano meno, verrà proposta una nuova missione ogni giorno fino al raggiungimento " +
                    "del numero massimo di missioni. È possibile cambiare gratuitamente una missione solamente una volta al giorno, altri cambi saranno effettuati a pagamento tramite i \"GaiaCoins\". "
        )

        map[list[0]] = boosters
        map[list[1]] = gaiacoin
        map[list[2]] = missioni
        map[list[3]] = prenotazioni
        map[list[4]] = profilo
        map[list[5]] = exp
        map[list[6]] = shop
        map[list[7]] = veicoli
        map[list[8]] = viaggi
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}