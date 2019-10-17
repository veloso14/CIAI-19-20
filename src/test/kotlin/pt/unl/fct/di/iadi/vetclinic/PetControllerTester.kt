package pt.unl.fct.di.iadi.vetclinic

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pt.unl.fct.di.iadi.vetclinic.api.PetDTO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PetService


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTester {

    @Autowired
    lateinit var mvc:MockMvc

    @MockBean
    lateinit var pets:PetService

    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val pantufas = PetDAO(1L, "pantufas", "Dog", emptyList())
        val bigodes = PetDAO(2L, "bigodes", "Cat", emptyList())
        val petsDAO = ArrayList(listOf(pantufas, bigodes))

        val petsDTO = petsDAO.map { PetDTO(it.id, it.name, it.species) }

        val petsURL = "/pets"
    }

    @Test
    fun `Test GET all pets`() {
        Mockito.`when`(pets.getAllPets()).thenReturn(petsDAO)

        val result = mvc.perform(get(petsURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(petsDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<PetDTO>>(responseString)
        assertThat(responseDTO, equalTo(petsDTO))
    }

    @Test
    fun `Test Get One Pet`() {
        Mockito.`when`(pets.getOnePet(1)).thenReturn(pantufas)

        val result = mvc.perform(get("$petsURL/1"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<PetDTO>(responseString)
        assertThat(responseDTO, equalTo(petsDTO[0]))
    }

    @Test
    fun `Test GET One Pet (Not Found)`() {
        Mockito.`when`(pets.getOnePet(2)).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$petsURL/2"))
                .andExpect(status().is4xxClientError)
    }

    fun <T>nonNullAny(t:Class<T>):T = Mockito.any(t)

    @Test
    fun `Test POST One Pet`() {
        val louro = PetDTO(0, "louro", "Papagaio")
        val louroDAO = PetDAO(louro.id, louro.name, louro.species, emptyList())

        val louroJSON = mapper.writeValueAsString(louro)

        Mockito.`when`(pets.addNewPet(nonNullAny(PetDAO::class.java)))
                .then { assertThat(it.getArgument(0), equalTo(louroDAO)) }

        mvc.perform(post(petsURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(louroJSON))
                .andExpect(status().isOk)
    }

}