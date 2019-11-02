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
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pt.unl.fct.di.iadi.vetclinic.api.*
import pt.unl.fct.di.iadi.vetclinic.model.*
import pt.unl.fct.di.iadi.vetclinic.services.ClientService
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PetService
import pt.unl.fct.di.iadi.vetclinic.services.PreconditionFailedException
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTester {

    @Autowired
    lateinit var mvc:MockMvc

    @MockBean
    lateinit var clients:ClientService

    @MockBean
    lateinit var pets: PetService

    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val antonio = ClientDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao", emptyList(), emptyList())
        val chenel = ClientDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao", emptyList(), emptyList())
        val clientsDAO = ArrayList(listOf(antonio, chenel))

       // val clientsPetsDTO =
              //  clientsDAO.map { ClientPetsDTO(ClientDTO(it.id,it.name,it.email,it.username,it.password,it.cellphone,it.address),it.pets.map { PetDTO(it) }) }

        val clientsDTO = clientsDAO.map{ClientDTO(it.id,it.name,it.email,it.username,it.password,it.cellphone,it.address)}


        val clientsURL = "/clients"
    }


    @Test
   // @WithMockUser(username = "aUser", password = "aPassword", roles = ["ADMIN"])
    fun `Test Get One Client`() {
        Mockito.`when`(clients.getOneClient(2)).thenReturn(chenel)

        val result = mvc.perform(get("$clientsURL/2"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<ClientDTO>(responseString)
        assertThat(responseDTO, equalTo(clientsDTO[1]))


    }

    @Test
    fun `Test GET One Client (Not Found)`() {
        Mockito.`when`(clients.getOneClient(2)).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$clientsURL/2"))
                .andExpect(status().is4xxClientError)
    }

    fun <T>nonNullAny(t:Class<T>):T = Mockito.any(t)

    @Test
    fun `Test checking appointments`() {
        val veloso = ClientDAO(1L,"Veloso","vel@gmail.com","vela","1234",987682,"Pio", emptyList<PetDAO>(), emptyList())
        val vet = VetDAO(1L,"Guilherme","vel@gmail.com","vela","1234",987682,"Pio","rosto.jpg",10, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>())

        val apt = AppointmentDAO(2, Date(),"consulta", PetDAO(), veloso, vet)

        veloso.appointments = listOf(apt)

        val aptDAO = ArrayList(listOf(apt))
        val aptDTO = aptDAO.map{AppointmentDTO(it.id,it.date,it.desc, it.pet.id, it.client.id, it.vet.id)}


        Mockito.`when`(clients.appointmentsOfClient(1)).thenReturn(listOf(apt))


        val result = mvc.perform(get("$clientsURL/1/appointments"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<AppointmentDTO>>(responseString)
        assertThat(responseDTO, equalTo(aptDTO))
    }

    @Test
    fun `Test checking appointments of non client`() {
        Mockito.`when`(clients.appointmentsOfClient(1))
                .thenThrow(NotFoundException("not found"))

        mvc.perform(get("$clientsURL/1/appointments"))
                .andExpect(status().is4xxClientError)
    }

    @Test
    fun `Test booking an appointment`() {
        val veloso = ClientDAO(1L,"Veloso","vel@gmail.com","vela","1234",987682,"Pio", emptyList<PetDAO>(), emptyList())

        val apt = AppointmentDTO(0, Date(), "consulta",0,1, 1)
        val aptDAO = AppointmentDAO(apt, PetDAO(), ClientDAO(), VetDAO())
        veloso.appointments = listOf(aptDAO)

        val vet = VetDAO(1L,"Guilherme","vel@gmail.com","vela","1234",987682,"Pio","rosto.jpg",10, false, listOf<AppointmentDAO>(aptDAO), emptyList<ScheduleDAO>())


        val aptJSON = mapper.writeValueAsString(apt)

        Mockito.`when`(clients.newAppointment(nonNullAny(AppointmentDAO::class.java)))
                .then { assertThat( it.getArgument(0), equalTo(aptDAO)); it.getArgument(0) }

        Mockito.`when`(clients.getOneClient(1)).thenReturn(veloso)

        mvc.perform(post("$clientsURL/1/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aptJSON))
                .andExpect(status().isOk)
    }

    @Test
    fun `Bad request add appointment on id not 0`() {
        val veloso = ClientDAO(1L,"Veloso","vel@gmail.com","vela","1234",987682,"Pio", emptyList<PetDAO>(), emptyList())
        val vet = VetDAO(1L,"Guilherme","vel@gmail.com","vela","1234",987682,"Pio","rosto.jpg",10, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>())

        val apt = AppointmentDTO(2, Date(), "consulta",0,1,1)
        val aptDAO = AppointmentDAO(apt,PetDAO(), veloso, vet)
        veloso.appointments = listOf(aptDAO)

        val aptJSON = mapper.writeValueAsString(apt)

        Mockito.`when`(clients.newAppointment(nonNullAny(AppointmentDAO::class.java)))
                .thenThrow( PreconditionFailedException("id 0"))

        Mockito.`when`(clients.getOneClient(1)).thenReturn(veloso)

        mvc.perform(post("$clientsURL/1/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aptJSON))
                .andExpect(status().is4xxClientError)

    }

    @Test
    fun `Test checking pets`() {
        val veloso = ClientDAO(1L,"Veloso","vel@gmail.com","vela","1234",987682,"Pio", emptyList<PetDAO>(), emptyList())
        val louro = PetDAO(1, "louro", "Papagaio",false, emptyList(), veloso)

        veloso.pets = listOf(louro)

        val louroDAO = ArrayList(listOf(louro))
        val louroDTO = louroDAO.map{PetDTO(it.id, it.name, it.species,it.frozen, it.owner.id)}


        Mockito.`when`(clients.petsOfClient(1)).thenReturn(listOf(louro))


        val result = mvc.perform(get("$clientsURL/1/pets"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<PetDTO>>(responseString)
        assertThat(responseDTO, equalTo(louroDTO))
    }

    @Test
    fun `Test checking pets of non client`() {
        Mockito.`when`(clients.petsOfClient(1))
                .thenThrow(NotFoundException("not found"))

        mvc.perform(get("$clientsURL/1/pets"))
                .andExpect(status().is4xxClientError)
    }

    @Test
    fun `Test new pet`() {

        val louro = PetDTO(0, "louro", "Papagaio",false, 0)


        val louroDAO = PetDAO(louro, emptyList(), ClientDAO())

        val veloso = ClientDAO(0,"Veloso","vel@gmail.com","vela","1234",987682,"Pio", listOf<PetDAO>(louroDAO), emptyList<AppointmentDAO>())


        veloso.pets = listOf(louroDAO)

        val aptJSON = mapper.writeValueAsString(louro)

        Mockito.`when`(clients.newPet(nonNullAny(PetDAO::class.java)))
                .then { assertThat( it.getArgument(0), equalTo(louroDAO)); it.getArgument(0) }

        Mockito.`when`(clients.getOneClient(1)).thenReturn(veloso)

        mvc.perform(post("$clientsURL/1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aptJSON))
                .andExpect(status().isOk)
    }

    @Test
    fun `Bad request add pet on id not 0`() {
        val veloso = ClientDAO(1L,"Veloso","vel@gmail.com","vela","1234",987682,"Pio", emptyList<PetDAO>(), emptyList())
        val louro = PetDTO(2, "louro", "Papagaio",false, 1)
        val louroDAO = PetDAO(louro, emptyList(), veloso)

        veloso.pets = listOf(louroDAO)

        val aptJSON = mapper.writeValueAsString(louro)

        Mockito.`when`(clients.newPet(nonNullAny(PetDAO::class.java)))
                .thenThrow( PreconditionFailedException("id 0"))

        Mockito.`when`(clients.getOneClient(1)).thenReturn(veloso)

        mvc.perform(post("$clientsURL/1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aptJSON))
                .andExpect(status().is4xxClientError)

    }




}