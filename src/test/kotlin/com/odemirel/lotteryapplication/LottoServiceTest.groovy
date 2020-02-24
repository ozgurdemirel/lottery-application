package com.odemirel.lotteryapplication

import spock.lang.Specification
import spock.lang.Unroll

class LottoServiceTest extends Specification {

    LottoService lottoService

    def setup() {
        lottoService = new LottoService()
    }

    def "get drawing with valid lotto data"() {
        given: "lotto data"
            def lottoData = new LottoData()
            lottoService.lottoData = lottoData
        when: "getting expected drawing"
            Set<Drawing> drawings = lottoService.getDrawings()
        then: "expecting that two drawing are returned"
            drawings
            drawings.size() == 2
    }


    def "get drawing with bad lotto data"() {
        given: "mock lotto data referenced by lotto service"
            def lottoData = Mock(LottoData)
            lottoService.lottoData = lottoData

        when: "having expected bad lotto data and get drawings"
            lottoData.getPowerDrawing() >> {
                throw  new IllegalArgumentException("Core picks not in acceptable range")
            }
            lottoService.getDrawings()

        then: "expected exception occurs"
            IllegalArgumentException iae = thrown()
            iae.message == "Core picks not in acceptable range"
    }


}
