package fct.cli.demo

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import fct.cli.demo.dto.PetDTO
import fct.cli.demo.model.PetDAO
import fct.cli.demo.services.PetService
import org.hamcrest.Matchers.hasSize
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTester {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var pets: PetService

    companion object {
        val pantufas = PetDAO(1L, "Pantufas", "Dog")
        val bigodes = PetDAO(2L, "Bigodes", "Cat")
        val petsDAO = listOf(pantufas, bigodes)

        val petsDTO = petsDAO.map { PetDTO(it.id, it.name, it.species) }

        val  petsURL = "/pets"
    }

    @Test
    fun test1() {
        assertThat(true, equalTo(true))
    }

   @Test
    fun `Test Get pets`() {

        Mockito.`when`(pets.getAllPets()).thenReturn(petsDAO)

        mvc.perform(get(petsURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(petsDTO.size)))
    }

}

