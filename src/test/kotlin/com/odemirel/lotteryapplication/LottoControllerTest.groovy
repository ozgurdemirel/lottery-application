package com.odemirel.lotteryapplication

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class LottoControllerTest extends Specification {

    MockMvc mockMvc
    LottoService mockLottoService

    def setup() {
        mockLottoService = Mock(LottoService)
        mockMvc = standaloneSetup(new LottoController(lottoService: mockLottoService)).build()
    }

    def "calling web service works correctly"() {

        given: "mock service method(s) "
        1 * mockLottoService.getDrawings() >> {
            [
                    new PowerDrawing(LocalDate.of(2020, 12, 12), [3, 4, 5, 6, 7].toSet(), 34),
                    new PowerDrawing(LocalDate.of(2020, 12, 12), [3, 4, 5, 6, 7].toSet(), 34),
                    new PowerDrawing(LocalDate.of(2020, 12, 12), [3, 4, 5, 6, 7].toSet(), 34)
            ].toSet()
        }

        when: "calling web service and get a response"

        def response = mockMvc
                .perform(MockMvcRequestBuilders.get("/").contentType(MediaType.TEXT_PLAIN))
                .andReturn().response

        def result = response.getContentAsString()

        then: "expected a valid response ..."
        response.status == 200
    }

}
