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
import pt.unl.fct.di.iadi.vetclinic.api.AppointmentDTO
import pt.unl.fct.di.iadi.vetclinic.api.PetAptsDTO
import pt.unl.fct.di.iadi.vetclinic.api.PetDTO
import pt.unl.fct.di.iadi.vetclinic.model.*
import pt.unl.fct.di.iadi.vetclinic.services.*
import java.util.*
import kotlin.collections.ArrayList


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTester {

    @Autowired
    lateinit var mvc:MockMvc

    @MockBean
    lateinit var pets:PetService

    @MockBean
    lateinit var vets: VetService

    @MockBean
    lateinit var clients:ClientService


    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val pantufas = PetDAO(1L, "pantufas", "Dog",false, emptyList(), ClientDAO())
        val bigodes = PetDAO(2L, "bigodes", "Cat",false, emptyList(), ClientDAO())
        val veloso = ClientDAO(1L,"Veloso","vel@gmail.com","vela","1234",987682,"Pio", emptyList<PetDAO>(), emptyList<AppointmentDAO>())

        val petsDAO = ArrayList(listOf(pantufas, bigodes))

        val petsAptsDTO =
            petsDAO.map { PetAptsDTO(PetDTO(it.id, it.name, it.species,it.frozen,0),
                                            it.appointments.map { AppointmentDTO(it) }) }



        val petsURL = "/pets"
    }

    @Test
    fun `Test GET all pets`() {
        Mockito.`when`(pets.getAllPets()).thenReturn(petsDAO)

        val result = mvc.perform(get(petsURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(petsAptsDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<PetAptsDTO>>(responseString)
        assertThat(responseDTO, equalTo(petsAptsDTO))
    }

    @Test
    fun `Test Get One Pet`() {
        Mockito.`when`(pets.getOnePet(1)).thenReturn(pantufas)

        val result = mvc.perform(get("$petsURL/1"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<PetAptsDTO>(responseString)
        assertThat(responseDTO, equalTo(petsAptsDTO[0]))
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
        val louro = PetDTO(0, "louro", "Papagaio",false,0)
        val louroDAO = PetDAO(louro.id, louro.name, louro.species, louro.frozen, emptyList(), ClientDAO())

        val louroJSON = mapper.writeValueAsString(louro)

        Mockito.`when`(pets.addNewPet(nonNullAny(PetDAO::class.java)))
                .then { assertThat(it.getArgument(0), equalTo(louroDAO)); it.getArgument(0) }

        mvc.perform(post(petsURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(louroJSON))
                .andExpect(status().isOk)
    }

    @Test
    fun `Test checking appointments`() {
        val louro = PetDAO(1, "louro", "Papagaio",false, emptyList(), ClientDAO())
        val apt = AppointmentDAO(2, Date(),"consulta", louro,louro.owner, VetDAO())
        louro.appointments = listOf(apt)

        Mockito.`when`(pets.appointmentsOfPet(1)).thenReturn(listOf(apt))

        //val result =
        mvc.perform(get("$petsURL/1/appointments"))
                .andExpect(status().isOk)
                .andReturn()

        /* TODO: need to compare with result
        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<PetAptsDTO>>(responseString)
        assertThat(responseDTO, equalTo(petsAptsDTO))
        */
    }

    @Test
    fun `Test checking appointments of non pet`() {
        Mockito.`when`(pets.appointmentsOfPet(1))
                .thenThrow(NotFoundException("not found"))

        mvc.perform(get("$petsURL/1/appointments"))
                .andExpect(status().is4xxClientError)
    }

    @Test
    fun `Test adding an appointment to a pet`() {

        val juli = ClientDAO(1,"Julian","julian@gmail.com","juli","1234",987682,"Pio", emptyList<PetDAO>(), emptyList<AppointmentDAO>())
        val lopez = VetDAO(1,"Lopez","lopez@gmail.com","chavez","1234",1234, "Rua Romao","rosto.jpg", 11, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>().toMutableList())
        val louro = PetDAO(1, "louro", "Papagaio",false, emptyList(), ClientDAO())
        val apt = AppointmentDTO(0, Date(), "consulta",1,1, 1)
        val aptDAO = AppointmentDAO(apt,louro, juli, lopez)
        louro.appointments = listOf(aptDAO)

        val aptJSON = mapper.writeValueAsString(apt)

        Mockito.`when`(pets.newAppointment(nonNullAny(AppointmentDAO::class.java)))
                .then { assertThat( it.getArgument(0), equalTo(aptDAO)); it.getArgument(0) }

        Mockito.`when`(pets.getOnePet(1)).thenReturn(
                louro)

        mvc.perform(post("$petsURL/1/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aptJSON))
                .andExpect(status().isOk)
    }

    @Test
    fun `Bad request on id not 0`() {
        val louro = PetDAO(1, "louro", "Papagaio",false, emptyList(), ClientDAO())
        val apt = AppointmentDTO(2, Date(), "consulta",1,0, 0)
        val aptDAO = AppointmentDAO(apt,louro, ClientDAO(), VetDAO())
        louro.appointments = listOf(aptDAO)

        val aptJSON = mapper.writeValueAsString(apt)

        Mockito.`when`(pets.newAppointment(nonNullAny(AppointmentDAO::class.java)))
                .thenThrow( PreconditionFailedException("id 0"))

        Mockito.`when`(pets.getOnePet(1)).thenReturn(louro)

        mvc.perform(post("$petsURL/1/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aptJSON))
                .andExpect(status().is4xxClientError)

    }
}