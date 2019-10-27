package pt.unl.fct.di.iadi.vetclinic.ClientTests


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pt.unl.fct.di.iadi.vetclinic.api.*
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.ClientDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.VetDAO
import pt.unl.fct.di.iadi.vetclinic.services.ClientService
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PreconditionFailedException
import java.time.LocalDateTime
import kotlin.collections.ArrayList


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTester {

    @Autowired
    lateinit var mvc:MockMvc

    @MockBean
    lateinit var clients:ClientService

    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val antonio = ClientDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao", emptyList(), emptyList())
        val chenel = ClientDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao", emptyList(), emptyList())
        val clientsDAO = ArrayList(listOf(antonio, chenel))

        val clientsPetsDTO =
                clientsDAO.map { ClientPetsDTO(ClientDTO(it.id,it.name,it.email,it.username,it.password,it.cellphone,it.address),it.pets.map { PetDTO(it) }) }



        val clientsURL = "/clients"
    }

  /*  @Test
    fun `Test GET all clients`() {
        Mockito.`when`(clients.getAllClients()).thenReturn(clientsDAO)

        val result = mvc.perform(get(clientsURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(clientsPetsDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<ClientPetsDTO>>(responseString)
        assertThat(responseDTO, equalTo(clientsPetsDTO))
    } */

    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["ADMIN"])
    fun `Test Get One Client`() {
        Mockito.`when`(clients.getOneClient("Antonio")).thenReturn(antonio)

        val result = mvc.perform(get("$clientsURL/Antonio"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<ClientPetsDTO>(responseString)
        assertThat(responseDTO, equalTo(clientsPetsDTO[0]))
    }

    @Test
    fun `Test GET One Client (Not Found)`() {
        Mockito.`when`(clients.getOneClient("jose")).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$clientsURL/jose"))
                .andExpect(status().is4xxClientError)
    }

    fun <T>nonNullAny(t:Class<T>):T = Mockito.any(t)

 /*   @Test
    fun `Test POST One Client`() {
        val vicente = ClientDTO(0,"Vicente","vario@gmail.com","vide","1234",1234, "Rua Romao")
        val vicenteDAO = ClientDAO(vicente.id,vicente.name,vicente.email,vicente.username,vicente.password,vicente.cellphone,vicente.address, emptyList(), emptyList())

        val vicenteJSON = mapper.writeValueAsString(vicente)

        Mockito.`when`(clients.addNewClient(nonNullAny(ClientDAO::class.java)))
                .then { assertThat(it.getArgument(0), equalTo(vicenteDAO)); it.getArgument(0) }

        mvc.perform(post(clientsURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(vicenteJSON))
                .andExpect(status().isOk)
    }
    */

    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["ADMIN"])
    fun `Test checking appointments`() {
        val vicente = ClientDAO(0,"Vicente","vario@gmail.com","vide","1234",1234, "Rua Romao", emptyList(), emptyList())
        val apt = AppointmentDAO(2, LocalDateTime.MIN, LocalDateTime.MAX,"consulta",false, PetDAO(), vicente, VetDAO())
        vicente.appointments = listOf(apt)

        Mockito.`when`(clients.appointmentsOfClient("Vicente")).thenReturn(listOf(apt))

        //val result =
        mvc.perform(get("$clientsURL/Vicente/appointments"))
                .andExpect(status().isOk)
                .andReturn()

        /* TODO: need to compare with result
        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<PetAptsDTO>>(responseString)
        assertThat(responseDTO, equalTo(petsAptsDTO))
        */
    }

    @Test
    fun `Test checking appointments of non client`() {
        Mockito.`when`(clients.appointmentsOfClient("jose"))
                .thenThrow(NotFoundException("not found"))

        mvc.perform(get("$clientsURL/jose/appointments"))
                .andExpect(status().is4xxClientError)
    }

    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["ADMIN"])
    fun `Test adding an appointment to a client`() {
        val vicente = ClientDAO(1,"Vicente","vario@gmail.com","vide","1234",1234, "Rua Romao", emptyList(), emptyList())

        val apt = AppointmentDTO(0, LocalDateTime.MIN, LocalDateTime.MAX, "consulta",false)
        val aptDAO = AppointmentDAO(apt, PetDAO(), vicente, VetDAO())
        vicente.appointments = listOf(aptDAO)

        val aptJSON = mapper.writeValueAsString(apt)

        Mockito.`when`(clients.newAppointment(nonNullAny(AppointmentDAO::class.java)))
                .then { assertThat( it.getArgument(0), equalTo(aptDAO)); it.getArgument(0) }

        Mockito.`when`(clients.getOneClient("Vicente")).thenReturn(vicente)

        mvc.perform(post("$clientsURL/Vicente/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aptJSON))
                .andExpect(status().isOk)
    }

    @Test
    fun `Bad request on id not 0`() {
        val vicente = ClientDAO(1,"Vicente","vario@gmail.com","vide","1234",1234, "Rua Romao", emptyList(), emptyList())

        val apt = AppointmentDTO(2,LocalDateTime.MIN, LocalDateTime.MAX, "consulta",false)
        val aptDAO = AppointmentDAO(apt,PetDAO(),vicente, VetDAO())
        vicente.appointments = listOf(aptDAO)

        val aptJSON = mapper.writeValueAsString(apt)

        Mockito.`when`(clients.newAppointment(nonNullAny(AppointmentDAO::class.java)))
                .thenThrow( PreconditionFailedException("id 0"))

        Mockito.`when`(clients.getOneClient("Vicente")).thenReturn(vicente)

        mvc.perform(post("$clientsURL/Vicente/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aptJSON))
                .andExpect(status().is4xxClientError)

    }
}