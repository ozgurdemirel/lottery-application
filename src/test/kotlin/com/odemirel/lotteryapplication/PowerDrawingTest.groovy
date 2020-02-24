package com.odemirel.lotteryapplication

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

class PowerDrawingTest extends Specification {


    def setup() {}

    def "power drawing - construct and validation success"() {
        given: "local date, power pick, and core picks defined"
        def localDate = LocalDate.of(2020, 1, 25)
        Integer powerPick = 1
        def corePicks = [1, 2, 3, 4, 5].toSet()
        when:
        def powerDrawing = new PowerDrawing(localDate, corePicks, powerPick)

        then: "power drawing is constructed with state"
        powerDrawing
        powerDrawing.getCorePicks()
        powerDrawing.getDate()
        powerDrawing.getPowerPick()

        and:

        when: "validation occurs"
        powerDrawing.validate()
        then: "no validation issue is reported"
        noExceptionThrown()
    }


    @Unroll
    def "power drawing - validation picks failure: #msg"() {

        given: "valid date"
        def date = LocalDate.of(2020, 1, 25)

        when: "validation occurs for each set of picks"
        Integer powerPick = pp1
        Set<Integer> corePicks = new HashSet()
        corePicks.add(cp1)
        corePicks.add(cp2)
        corePicks.add(cp3)
        corePicks.add(cp4)
        corePicks.add(cp5)
        def powerDrawing = new PowerDrawing(date, corePicks, powerPick)
        powerDrawing.validate()
        then: "validation exception occurs"
        thrown(IllegalArgumentException)
        //noExceptionThrown()
        where:
        cp1 | cp2 | cp3 | cp4 | cp5 | pp1 | msg
        2   | 2   | 4   | 5   | 1   | 22  | "duplicate"
        1   | 6   | 2   | 4   | 66  | 36  | "core beyond max"
        1   | 6   | 2   | 4   | 65  | 37  | "power beyond max"
        0   | 6   | 2   | 4   | 65  | 36  | "core below min"
        1   | 6   | 2   | 4   | 65  | 0   | "power below min"

    }

    @Unroll
    def "power drawing - validation success with core picks"() {
        given: "valid date"
            LocalDate date = LocalDate.of(2020, 10, 10)
        when: "valid picks are supplied"
            def powerDrawing = new PowerDrawing(date, [cp1, cp2, cp3, cp4, cp4, cp5].toSet(), pp1)
        then: "no validation issues"
            noExceptionThrown()
        where:
        cp1 | cp2 | cp3 | cp4 | cp5 | pp1
        1   | 2   | 3   | 4   | 5   | 1
        1   | 6   | 2   | 4   | 65  | 36
    }


    def "power drawing - bad arguments"() {
        given:
            LocalDate localDate = LocalDate.of(2010,12,12)
            Integer powerPick = 1
            def corePicks = [1, 2, 3, 4, 5].toSet()
        when:
            new PowerDrawing(null,corePicks,powerPick)
        then: "exception thrown as date cant be null"
            thrown(IllegalArgumentException)
        and:

        when:
            new PowerDrawing(localDate,null,powerPick)
        then: "exception thrown as core picks cant be null"
            thrown(IllegalArgumentException)

    }

}
