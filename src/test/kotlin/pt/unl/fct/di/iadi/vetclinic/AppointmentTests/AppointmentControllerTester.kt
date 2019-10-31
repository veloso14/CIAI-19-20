package pt.unl.fct.di.iadi.vetclinic.AppointmentTests

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
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTester {

    @Autowired
    lateinit var mvc:MockMvc

    @MockBean
    lateinit var apts:AppointmentService

    @MockBean
    lateinit var clients: ClientService

    @MockBean
    lateinit var pets: PetService

    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val veloso = ClientDAO(1L,"Veloso","vel@gmail.com","vela","1234",987682,"Pio", emptyList<PetDAO>(), emptyList<AppointmentDAO>())
        val vet = VetDAO(1L,"Guilherme","vel@gmail.com","vela","1234",987682,"Pio","rosto.jpg",10, false, emptyList<AppointmentDAO>())


        val consulta = AppointmentDAO(1L,Date(), "consulta",PetDAO(), ClientDAO(), VetDAO())
        val exame = AppointmentDAO(2L,Date(), "exame", PetDAO(), ClientDAO(), VetDAO())
        val pantufas = PetDAO(1L, "pantufas", "Dog",false, emptyList(), veloso)
        val aptsDAO = ArrayList(listOf(consulta, exame))

        val aptsDTO = aptsDAO.map { AppointmentDTO(it.id, it.date,it.desc, it.pet.id, it.client.id, it.vet.id) }

        val aptsURL = "/appointments"
    }


    @Test
    fun `Test GET all appointments`() {
        Mockito.`when`(apts.getAllAppointments()).thenReturn(aptsDAO)

        val result = mvc.perform(get(aptsURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize<Any>(aptsDTO.size)))
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<AppointmentDTO>>(responseString)
        assertThat(responseDTO, equalTo(aptsDTO))
    }


    @Test
    fun `Test Get One appointment`() {
        Mockito.`when`(apts.getOneAppointment(1)).thenReturn(consulta)


        assertThat(apts.getOneAppointment(1L), equalTo(consulta))
    }

    @Test
    fun `Test GET One appointment (Not Found)`() {

        Mockito.`when`(apts.getOneAppointment(100)).thenThrow(NotFoundException("not found"))
        mvc.perform(get("$aptsURL/100"))
                .andExpect(status().is4xxClientError)

    }

    fun <T>nonNullAny(t:Class<T>):T = Mockito.any(t)

    @Test
    fun `Test POST One appointment`() {

        val caramelo =  PetDAO(0, "pantufas", "Dog",false, emptyList(), veloso)

        val revisao = AppointmentDTO(0, Date(), "revisao",0,1,1)
        val revisaoDAO = AppointmentDAO(revisao.id,revisao.date,revisao.desc, caramelo,veloso, vet)

        val revisaoJSON = mapper.writeValueAsString(revisao)

        Mockito.`when`(apts.addNewAppointment(nonNullAny(AppointmentDAO::class.java)))
                .then { assertThat(it.getArgument(0), equalTo(revisaoDAO)); it.getArgument(0) }

        mvc.perform(post(aptsURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(revisaoJSON))
                .andExpect(status().isOk)
    }


}