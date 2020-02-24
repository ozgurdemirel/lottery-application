package com.odemirel.lotteryapplication

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.util.*
import java.util.stream.IntStream
import kotlin.collections.HashSet

@SpringBootApplication
class LotteryApplication

fun main(args: Array<String>) {
    runApplication<LotteryApplication>(*args)
}

@RestController
class LottoController {

    @Autowired
    lateinit var lottoService: LottoService

    @RequestMapping(method = [RequestMethod.GET])
    fun load(): String {
        val drawing: Set<Drawing>

        try {
            drawing = lottoService.getDrawings()
        } catch (e: Exception) {
            return "Failed to load due to exception: $e.message"
        }

        val numOfDrawing = drawing.size
        return "Successfully loaded. Number of drawings loaded: $drawing"
    }
}

@Service
 class LottoService {

    @Autowired
    lateinit var lottoData: LottoData

    fun getDrawings(): Set<Drawing> {
        val drawings = HashSet<Drawing>()

        val powerDrawing1: Drawing = lottoData.getPowerDrawing()
        drawings.add(powerDrawing1)

        val powerDrawing2: Drawing = lottoData.getPowerDrawing()
        drawings.add(powerDrawing2)

        return drawings
    }

}

@Component
class LottoData {

    fun getPowerDrawing(): Drawing {

        val date: LocalDate = LocalDate.of(2020, 1, 1);
        val powerPick: Int = (1..PowerDrawing.MAX_POWER_PICK).random()
        val corePicks: HashSet<Int> = HashSet()
        for (n in 1..5) {
            while (true) {
                val pick: Int = (1..PowerDrawing.MAX_CORE_PICK).random()
                if (!corePicks.contains(pick)) {
                    corePicks.add(pick)
                    break
                }
            }
        }

        val powerDrawing = PowerDrawing(date, corePicks, powerPick)
        powerDrawing.validate()
        return powerDrawing
    }


    private fun ClosedRange<Int>.random() = Random()
            .nextInt(endInclusive - start) + start

}

abstract class Drawing(
        val date: LocalDate,
        val corePicks: Set<Int>
) {

    abstract fun validate()

    fun validateCorePicksSize(expectedSize: Int) {
        if (corePicks.isEmpty() || corePicks.size != expectedSize) {
            throw IllegalArgumentException("picks are not the expected size: $expectedSize")
        }
    }

}


data class PowerDrawing(
        private val powerDate: LocalDate,
        private val powerCorePicks: Set<Int>,
        val powerPick: Int
) : Drawing(powerDate, powerCorePicks) {

    companion object {
        const val MAX_CORE_PICK: Int = 65
        const val MAX_POWER_PICK: Int = 36
        const val CORE_PICKS_SIZE: Int = 5
    }

    override fun validate() {
        super.validateCorePicksSize(CORE_PICKS_SIZE)

        val corePicksInRange = corePicks.stream().allMatch { x ->
            IntStream.rangeClosed(1, MAX_CORE_PICK).anyMatch { r -> r == x }
        }

        if (!corePicksInRange) {
            throw IllegalArgumentException("core picks not in acceptable range 1 .. $MAX_CORE_PICK")
        }

        val powerPickInRange = IntStream.rangeClosed(1, MAX_POWER_PICK).anyMatch { r -> r == powerPick }

        if (!powerPickInRange) {
            throw IllegalArgumentException("Power pick not in acceptable range 1 ... $MAX_POWER_PICK")
        }

    }
}






