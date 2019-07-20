package com.fourteenrows.p2pcs.objects.quests

import java.lang.Math.pow
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.random.Random

class QuestAuthorizing(private val level: Long) {
    private val types = QuestTypes.values().copyOfRange(0, QuestTypes.values().size - 1)

    private val difficulties = arrayOf("veryEasy", "easy", "medium", "hard", "veryHard", "masochist")
    private val quests = LinkedHashMap<Int, ArrayList<String>>()
    private val probabilities = LinkedList<Double>()

    init {
        initializeQuestList()
        initializeGaussianProbabilities()
    }

    // Gaussiana by wikipedia
    private fun gaussianFunction(a: Double, b: Double, c: Double, x: Double) =
        a * Math.exp(-pow(x - b, 2.0) / (2 * pow(c, 2.0)))

    // Inizializza la mappa delle quest [difficoltà, [quests di quella difficoltà]]
    private fun initializeQuestList() {
        for (i in 0 until difficulties.size) {
            val difficultyQuests = ArrayList<String>()
            types.forEach {
                difficultyQuests.add(generateId(it, difficulties[i]))
            }
            quests[i] = difficultyQuests
        }
    }

    // Genera le probabilità in base alla traslazione della gaussiana nell'asse
    // delle x (livello / difficoltà)
    private fun initializeGaussianProbabilities() {
        val maxLevel = 10.0    // Livello massimo
        val a = 1.0             // Altezza massima della gaussiana
        val b = (level * difficulties.size) / maxLevel  // Centro della gaussiana
        val c = 0.7             // Larghezza della gaussiana

        quests.forEach {
            probabilities.add(gaussianFunction(a, b, c, it.key.toDouble()))
        }
    }

    private fun generateId(type: QuestTypes, difficulty: String): String = "q$type$difficulty"

    private fun removeAlredyActived(unselectableQuests: ArrayList<ActiveQuest>) {
        quests.forEach { diffQuests ->
            unselectableQuests.forEach { quest ->
                diffQuests.value.remove(quest.qid)
            }
        }
    }

    private fun removeAlredyActivedWithStrings(unselectableQuests: ArrayList<String>) {
        quests.forEach { diffQuests ->
            unselectableQuests.forEach { quest ->
                diffQuests.value.remove(quest)
            }
        }
    }

    /* Vedo il vettore delle probabilità come un intervallo suddiviso in più parti
        di ampiezza pari alle probabilità calcolata. Quindi dato un numero random in 0..tot
        posso ottenere l'indice_i con probabilità pari alla probabilità di scegliere
        un numero nell'intervallo x_a..x_b rispetto al range 0..tot dove x_a e x_b sono
        gli estremi dell'intervallo di indice i
        Se in quell'indice non ci sono quest attivabili (sono già tutte attive), decremento
        l'indice, in estremis si becca qella più difficile.
        Se non ci sono proprio quest, prendi il calendario ed invoca qualche santo
     */
    private fun getQuestIndexDifficulty(): Int {
        val total = probabilities.stream().reduce(0.0, Double::plus)
        var random = 0.0
        if (total != 0.0) {
            random = Random.nextDouble(0.0, total)
        }
        var index = 0   // Index of choosen difficulty quest
        var tot = 0.0
        for (i in 0 until probabilities.size) {
            if (random < tot) {
                ++index
            }
            tot += probabilities[i]
        }
        index = probabilities.size - index - 1

        // Check if all quests of that difficulty are actived
        var zeroCheck = false
        while (quests[index]!!.size == 0) {
            if (zeroCheck) {
                index = quests.size - 1
            }
            if (index == 0) {
                index = 1
                zeroCheck = true
            } else {
                --index
            }
        }
        return index
    }

    fun getQuestFor(unselectableQuests: ArrayList<ActiveQuest>): String {
        removeAlredyActived(unselectableQuests)
        val index = getQuestIndexDifficulty()

        // Choose one quest with chosen difficulty
        return quests[index]!![Random.nextInt(0, quests[index]!!.size)]
    }

    fun getQuestForWithStrings(unselectableQuests: ArrayList<String>): String {
        removeAlredyActivedWithStrings(unselectableQuests)
        val index = getQuestIndexDifficulty()

        // Choose one quest with chosen difficulty
        return quests[index]!![Random.nextInt(0, quests[index]!!.size)]
    }
}