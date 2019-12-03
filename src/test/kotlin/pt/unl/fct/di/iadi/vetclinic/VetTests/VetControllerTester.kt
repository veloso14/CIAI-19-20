package pt.unl.fct.di.iadi.vetclinic.VetTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pt.unl.fct.di.iadi.vetclinic.api.*
import pt.unl.fct.di.iadi.vetclinic.model.*
import pt.unl.fct.di.iadi.vetclinic.services.*
import java.time.Month
import java.util.*
import kotlin.collections.ArrayList
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import pt.unl.fct.di.iadi.vetclinic.AppointmentTests.AppointmentControllerTester.Companion.vet


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class VetControllerTester {

    @Autowired
    lateinit var mvc:MockMvc

    @MockBean
    lateinit var vets: VetService

    @MockBean
    lateinit var vetRepo: VetRepository


    companion object {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val antonio = VetDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao","rosto.jpg", 11, false, emptyList(), emptyList())
        val chenel = VetDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao","rosto.jpg", 12, false, emptyList(), emptyList())
        val vetsDAO = mutableListOf(antonio, chenel);

        val vetsDTO =
                vetsDAO.map { VetDTO(it.id,it.name,it.email,it.username,it.password,it.cellphone, it.address,it.photo,it.employeeID) }

        val pantufas = PetDAO(1L, "pantufas", "Dog",false, emptyList(), ClientDAO())
        val bigodes = PetDAO(2L, "bigodes", "Cat",false, emptyList(), ClientDAO())
        val petsDAO = mutableListOf(pantufas, bigodes);

        val petsDTO =
                petsDAO.map {PetDTO(it.id, it.name, it.species, 0) }


        val manzanares = ClientDAO(1L,"JoseMari","man@gmail.com","manza","1234",1234, "Rua Romao", emptyList<PetDAO>(), emptyList<AppointmentDAO>())
        val campuzano = ClientDAO(2L,"Tomas","camp@gmail.com","camp","1234",1234, "Rua Romao", emptyList<PetDAO>(), emptyList<AppointmentDAO>())
        val clientsDAO = mutableListOf(manzanares, campuzano)

        val clientsDTO =
                clientsDAO.map { ClientDTO(it.id,it.name,it.email,it.username,it.password,it.cellphone, it.address, it.photo) }



        val consulta1 = AppointmentDAO(1L, Date(), "consulta1", PetDAO(), ClientDAO(), VetDAO())
        val consulta2 = AppointmentDAO(2L, Date(), "consulta1", PetDAO(), ClientDAO(), VetDAO())
        val consultasDAO = mutableListOf(consulta1, consulta2);

        val consultasDTO = consultasDAO.map { AppointmentDTO(it.id, it.date,it.desc, it.pet.id, it.client.id, it.vet.id) }


        val vetsURL = "/vets"
    }

    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["VET"])
    fun `Test Get One vet (Vet Role)`() {
        Mockito.`when`(vets.getOneVet(1)).thenReturn(antonio)
        val vet = VetDAO(1L,"Guilherme","vel@gmail.com","aUser","aPassword",987682,"Pio","rosto.jpg",10, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>())

        Mockito.`when`( vetRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(vet ))

        val result = mvc.perform(get("$vetsURL/1"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<VetDTO>(responseString)
        assertThat(responseDTO, equalTo(vetsDTO[0]))
    }


    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["ADMIN"])
    fun `Test Get One vet (Admin Role)`() {
        Mockito.`when`(vets.getOneVet(1)).thenReturn(antonio)

        val result = mvc.perform(get("$vetsURL/1"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<VetDTO>(responseString)
        assertThat(responseDTO, equalTo(vetsDTO[0]))
    }

    @Test
    fun `Test GET One Vet (Not Found and No role)`() {
        Mockito.`when`(vets.getOneVet(2)).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$vetsURL/2"))
                .andExpect(status().isForbidden)
    }

    fun <T>nonNullAny(t:Class<T>):T = Mockito.any(t)

/*
    @Test
    fun `Test GET all pets`() {
        Mockito.`when`(vets.getAllPets()).thenReturn(petsDAO)

        val result = mvc.perform(get("$vetsURL/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(petsDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<PetDTO>>(responseString)
        assertThat(responseDTO, equalTo(petsDTO))
    }

    @Test
    fun `Test GET all clients`() {
        Mockito.`when`(vets.getAllClients()).thenReturn(clientsDAO)

        val result = mvc.perform(get("$vetsURL/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(clientsDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<ClientDTO>>(responseString)
        assertThat(responseDTO, equalTo(clientsDTO))
    }

    @Test
    fun `Test GET all appointments`() {
        Mockito.`when`(vets.getAllAppointments()).thenReturn(consultasDAO)

        val result = mvc.perform(get("$vetsURL/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(consultasDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<AppointmentDTO>>(responseString)
        assertThat(responseDTO, equalTo(consultasDTO))
    }

 */
@Test
fun `Test checking appointments (No Login)`() {
    val veloso = ClientDAO(1L,"Veloso","vel@gmail.com","vela","1234",987682,"Pio", emptyList<PetDAO>(), emptyList())
    val vet = VetDAO(1L,"Guilherme","vel@gmail.com","vela","1234",987682,"Pio","rosto.jpg",10, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>())

    val apt = AppointmentDAO(2, Date(),"consulta", PetDAO(), veloso, vet)

    veloso.appointments = listOf(apt)

    val aptDAO = ArrayList(listOf(apt))
    val aptDTO = aptDAO.map{AppointmentDTO(it.id,it.date,it.desc, it.pet.id, it.client.id, it.vet.id)}


    Mockito.`when`(vets.appointmentsOfVet(1)).thenReturn(listOf(apt))

    mvc.perform(get("$vetsURL/1/appointments"))
            .andExpect(status().isForbidden)
            .andReturn()


}


    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["VET"])
    fun `Test checking appointments`() {
        val veloso = ClientDAO(1L,"Veloso","vel@gmail.com","vela","1234",987682,"Pio", emptyList<PetDAO>(), emptyList())
        val vet = VetDAO(1L,"Guilherme","vel@gmail.com","vela","1234",987682,"Pio","rosto.jpg",10, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>().toMutableList())

        val apt = AppointmentDAO(2, Date(),"consulta", PetDAO(), veloso, vet)

        veloso.appointments = listOf(apt)

        val aptDAO = ArrayList(listOf(apt))
        val aptDTO = aptDAO.map{AppointmentDTO(it.id,it.date,it.desc, it.pet.id, it.client.id, it.vet.id)}


        Mockito.`when`(vets.appointmentsOfVet(1)).thenReturn(listOf(apt))
        val vetUser = VetDAO(1L,"Guilherme","vel@gmail.com","aUser","aPassword",987682,"Pio","rosto.jpg",10, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>())

        Mockito.`when`( vetRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(vetUser ))


        val result = mvc.perform(get("$vetsURL/1/appointments"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<AppointmentDTO>>(responseString)
        assertThat(responseDTO, equalTo(aptDTO))
    }


    @Test
    fun `Test checking appointments of non vet  (No role)`() {
        Mockito.`when`(vets.appointmentsOfVet(1))
                .thenThrow(NotFoundException("not found"))

        mvc.perform(get("$vetsURL/1/appointments"))
                .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["ADMIN"])
    fun `Test POST One vet`() {
        val curroDTO = VetDTO(0, "Romero","vel@gmail.com","vela","1234",987682,"Pio","",1)
        val curroDAO = VetDAO(curroDTO, emptyList(), emptyList())

        val curroJSON = mapper.writeValueAsString(curroDTO)

        Mockito.`when`(vets.hireVet(nonNullAny(VetDAO::class.java)))
                .then { assertThat(it.getArgument(0), equalTo(curroDAO)); it.getArgument(0) }

        mvc.perform(post(vetsURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(curroJSON))
                .andExpect(status().isOk)
    }


    @Test
    fun `Test POST One schedule`() {
        val scheduleDTO = ScheduleDTO(0, Month.JANUARY, antonio.id)
        var scheduleDAO = ScheduleDAO(antonio, scheduleDTO.month)
        scheduleDAO = vets.createSchedule(vet, Month.JANUARY)
        val scheduleJSON = mapper.writeValueAsString(scheduleDTO)

        Mockito.`when`(vets.setSchedule(vet.id, "JAN"))
                .then { assertThat(it.getArgument(0), equalTo(scheduleDAO)); it.getArgument(0) }

        mvc.perform(post("$vetsURL/1/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(scheduleJSON))
                .andExpect(status().isOk)
    }


}