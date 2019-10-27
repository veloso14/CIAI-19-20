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
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pt.unl.fct.di.iadi.vetclinic.api.AppointmentDTO
import pt.unl.fct.di.iadi.vetclinic.api.PetAptsDTO
import pt.unl.fct.di.iadi.vetclinic.api.PetDTO
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.ClientDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.VetDAO
import pt.unl.fct.di.iadi.vetclinic.services.AppointmentService
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PetService
import pt.unl.fct.di.iadi.vetclinic.services.PreconditionFailedException
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

    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val consulta = AppointmentDAO(1L,LocalDateTime.MIN, LocalDateTime.MAX, "consulta",false, PetDAO(), ClientDAO(),VetDAO())
        val exame = AppointmentDAO(2L,LocalDateTime.MIN, LocalDateTime.MAX, "exame",false, PetDAO(), ClientDAO(), VetDAO())

        val aptsDAO = ArrayList(listOf(consulta, exame))

        val aptsDTO = aptsDAO.map { AppointmentDTO(it.id, it.start,it.end,it.desc, it.complete) }

        val aptsURL = "/appointments"
    }



    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["ADMIN"])
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
        val revisao = AppointmentDTO(0,LocalDateTime.MIN, LocalDateTime.MAX, "revisao",false)
        val revisaoDAO = AppointmentDAO(revisao.id,revisao.start,revisao.end,revisao.desc,revisao.complete, PetDAO(), ClientDAO(),VetDAO())

        val revisaoJSON = mapper.writeValueAsString(revisao)

        Mockito.`when`(apts.addNewAppointment(nonNullAny(AppointmentDAO::class.java)))
                .then { assertThat(it.getArgument(0), equalTo(revisaoDAO)); it.getArgument(0) }

        mvc.perform(post(aptsURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(revisaoJSON))
                .andExpect(status().isOk)
    }


}