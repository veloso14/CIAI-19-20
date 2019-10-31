package pt.unl.fct.di.iadi.vetclinic.AdminTests

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
import pt.unl.fct.di.iadi.vetclinic.api.*
import pt.unl.fct.di.iadi.vetclinic.model.*
import pt.unl.fct.di.iadi.vetclinic.services.*
import java.util.*
import kotlin.collections.ArrayList


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTester {

    @Autowired
    lateinit var mvc:MockMvc

    @MockBean
    lateinit var admins: AdminService


    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val cid = AdminDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao","rosto.jpg", 11)
        val curro = AdminDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao","rosto.jpg", 12)
        val adminsDAO = mutableListOf(cid, curro);

        val adminsDTO =
                adminsDAO.map { AdminDTO(it.id,it.name,it.email,it.username,it.password,it.cellphone, it.address,it.photo,it.employeeID) }


        val antonio = VetDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao","rosto.jpg", 11, false, emptyList<AppointmentDAO>())
        val chenel = VetDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao","rosto.jpg", 12, false, emptyList<AppointmentDAO>())
        val vetsDAO = mutableListOf(antonio, chenel);

        val vetsDTO =
                vetsDAO.map { VetDTO(it.id,it.name,it.email,it.username,it.password,it.cellphone, it.address, it.photo,it.employeeID, it.frozen) }

        val pantufas = PetDAO(1L, "pantufas", "Dog",false, emptyList(), ClientDAO())
        val bigodes = PetDAO(2L, "bigodes", "Cat",false, emptyList(), ClientDAO())
        val petsDAO = mutableListOf(pantufas, bigodes);

        val petsDTO =
                petsDAO.map {PetDTO(it.id, it.name, it.species,it.frozen, it.owner.id) }


        val manzanares = ClientDAO(1L,"JoseMari","man@gmail.com","manza","1234",1234, "Rua Romao", emptyList<PetDAO>(), emptyList<AppointmentDAO>())
        val campuzano = ClientDAO(2L,"Tomas","camp@gmail.com","camp","1234",1234, "Rua Romao", emptyList<PetDAO>(), emptyList<AppointmentDAO>())
        val clientsDAO = mutableListOf(manzanares, campuzano)

        val clientsDTO =
                clientsDAO.map { ClientDTO(it.id,it.name,it.email,it.username,it.password,it.cellphone, it.address) }



        val consulta1 = AppointmentDAO(1L, Date(), "consulta1", PetDAO(), ClientDAO(), VetDAO())
        val consulta2 = AppointmentDAO(2L, Date(), "consulta1", PetDAO(), ClientDAO(), VetDAO())
        val consultasDAO = mutableListOf(consulta1, consulta2);

        val consultasDTO = consultasDAO.map { AppointmentDTO(it.id, it.date,it.desc, it.pet.id, it.client.id, it.vet.id) }



        val adminsURL = "/admins"
    }

    @Test
    fun `Test Get One Admin`() {
        Mockito.`when`(admins.getOneAdmin(1)).thenReturn(cid)

        val result = mvc.perform(get("$adminsURL/1"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<AdminDTO>(responseString)
        assertThat(responseDTO, equalTo(adminsDTO[0]))
    }

    @Test
    fun `Test GET One Admin (Not Found)`() {
        Mockito.`when`(admins.getOneAdmin(2)).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$adminsURL/2"))
                .andExpect(status().is4xxClientError)
    }

    fun <T>nonNullAny(t:Class<T>):T = Mockito.any(t)

    @Test
    fun `Test GET all pets`() {
        Mockito.`when`(admins.getAllPets()).thenReturn(petsDAO)

        val result = mvc.perform(get("$adminsURL/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(petsDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<PetDTO>>(responseString)
        assertThat(responseDTO, equalTo(petsDTO))
    }

    @Test
    fun `Test GET all clients`() {
        Mockito.`when`(admins.getAllClients()).thenReturn(clientsDAO)

        val result = mvc.perform(get("$adminsURL/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(clientsDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<ClientDTO>>(responseString)
        assertThat(responseDTO, equalTo(clientsDTO))
    }

    @Test
    fun `Test GET all appointments`() {
        Mockito.`when`(admins.getAllAppointments()).thenReturn(consultasDAO)

        val result = mvc.perform(get("$adminsURL/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(consultasDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<AppointmentDTO>>(responseString)
        assertThat(responseDTO, equalTo(consultasDTO))
    }

    @Test
    fun `Test GET all admins`() {
        Mockito.`when`(admins.getAllAdmins()).thenReturn(adminsDAO)

        val result = mvc.perform(get(adminsURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(adminsDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<AdminDTO>>(responseString)
         assertThat(responseDTO, equalTo(adminsDTO))
    }

    @Test
    fun `Test GET all vets`() {
        Mockito.`when`(admins.getAllVets()).thenReturn(vetsDAO)

        val result = mvc.perform(get("$adminsURL/vets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(vetsDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<VetDTO>>(responseString)
        assertThat(responseDTO, equalTo(vetsDTO))
    }


    @Test
    fun `Test hire One Vet`() {
        val vinhas = VetDTO(0,"Antonio","antonio@gmail.com","vinha","1234",1234, "Rua Romao","rosto.jpg", 11, false)

        val vinhasDAO = VetDAO(vinhas.id,vinhas.name,vinhas.email,vinhas.username, vinhas.password, vinhas.cellphone, vinhas.address,vinhas.photo, vinhas.employeeID,vinhas.frozen, emptyList<AppointmentDAO>() )

        val vinhasJSON = mapper.writeValueAsString(vinhas)

        Mockito.`when`(admins.hireVet(nonNullAny(VetDAO::class.java)))
                .then { assertThat(it.getArgument(0), equalTo(vinhasDAO)); it.getArgument(0) }

        mvc.perform(post("$adminsURL/vets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(vinhasJSON))
                .andExpect(status().isOk)
    }

    @Test
    fun `Test hire One Admin`() {
        val vinhas = AdminDTO(0,"Antonio","antonio@gmail.com","vinha","1234",1234, "Rua Romao","rosto.jpg", 11)

        val vinhasDAO = AdminDAO(vinhas.id,vinhas.name,vinhas.email,vinhas.username, vinhas.password, vinhas.cellphone, vinhas.address, vinhas.photo, vinhas.employeeID)

        val vinhasJSON = mapper.writeValueAsString(vinhas)

        Mockito.`when`(admins.hireAdmin(nonNullAny(AdminDAO::class.java)))
                .then { assertThat(it.getArgument(0), equalTo(vinhasDAO)); it.getArgument(0) }

        mvc.perform(post(adminsURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(vinhasJSON))
                .andExpect(status().isOk)
    }

    //fazer dos fires

    @Test
    fun `Test checking appointments of a vet`() {
        //val veloso = ClientDAO(1L,"Veloso","vel@gmail.com","vela","1234",987682,"Pio", emptyList<PetDAO>(), emptyList())
        val vet = VetDAO(1L,"Guilherme","vel@gmail.com","vela","1234",987682,"Pio","rosto.jpg",10, false, emptyList<AppointmentDAO>())

        val apt = AppointmentDAO(2, Date(),"consulta", PetDAO(), ClientDAO(), vet)

        vet.appointments = listOf(apt)

        val aptDAO = ArrayList(listOf(apt))
        val aptDTO = aptDAO.map{AppointmentDTO(it.id,it.date,it.desc, it.pet.id, it.client.id, it.vet.id)}


        Mockito.`when`(admins.getVetsAppointments(1)).thenReturn(listOf(apt))


        val result = mvc.perform(get("$adminsURL/vets/1/appointments"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<AppointmentDTO>>(responseString)
        assertThat(responseDTO, equalTo(aptDTO))
    }

    @Test
    fun `Test checking appointments of non vet`() {
        Mockito.`when`(admins.getVetsAppointments(1))
                .thenThrow(NotFoundException("not found"))

        mvc.perform(get("$adminsURL/vets/1/appointments"))
                .andExpect(status().is4xxClientError)
    }






}