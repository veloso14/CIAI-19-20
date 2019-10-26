package pt.unl.fct.di.iadi.vetclinic.VetTests

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
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pt.unl.fct.di.iadi.vetclinic.api.*
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.ClientDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.VetDAO
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.VetService
import java.time.LocalDateTime
import kotlin.collections.ArrayList


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class VetControllerTester {

    @Autowired
    lateinit var mvc:MockMvc

    @MockBean
    lateinit var vets: VetService

    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val antonio = VetDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao", 500, mutableListOf<AppointmentDAO>(),false)
        val chenel = VetDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao", 600, mutableListOf<AppointmentDAO>(),false)

        val vetsDAO = ArrayList(listOf(antonio, chenel))

        val vetsAptsDTO =
                vetsDAO.map { VetAptsDTO(VetDTO(it.id,it.name,it.email,it.username,it.password,it.cellphone,it.address,it.employeeID),
                        it.appointments.map { AppointmentDTO(it) }) }

        val vetsURL = "/vets"
    }



    @Test
    fun `Test Get One Vet`() {
        Mockito.`when`(vets.getOneVet("Antonio")).thenReturn(antonio)

        val result = mvc.perform(get("$vetsURL/Antonio"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<VetAptsDTO>(responseString)
        assertThat(responseDTO, equalTo(vetsAptsDTO[0]))
    }

    @Test
    fun `Test GET One Vet (Not Found)`() {
        Mockito.`when`(vets.getOneVet("jose")).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$vetsURL/jose"))
                .andExpect(status().is4xxClientError)
    }

    fun <T>nonNullAny(t:Class<T>):T = Mockito.any(t)



    @Test
    fun `Test checking appointments`() {
        val vicente = VetDAO(0,"Vicente","vario@gmail.com","vide","1234",1234, "Rua Romao",500,mutableListOf<AppointmentDAO>(), false)
        val apt = AppointmentDAO(2, LocalDateTime.MIN, LocalDateTime.MAX,"consulta",false, PetDAO(), ClientDAO(), vicente)
        vicente.appointments = mutableListOf(apt)

        Mockito.`when`(vets.appointmentsOfVet("Vicente")).thenReturn(mutableListOf(apt))

        //val result =
        mvc.perform(get("$vetsURL/Vicente/appointments"))
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
        Mockito.`when`(vets.appointmentsOfVet("jose"))
                .thenThrow(NotFoundException("not found"))

        mvc.perform(get("$vetsURL/jose/appointments"))
                .andExpect(status().is4xxClientError)
    }




}