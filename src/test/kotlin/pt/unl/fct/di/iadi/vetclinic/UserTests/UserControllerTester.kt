package pt.unl.fct.di.iadi.vetclinic.UserTests

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
import pt.unl.fct.di.iadi.vetclinic.api.UserDTO
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.ClientDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
import pt.unl.fct.di.iadi.vetclinic.services.*
import java.util.*
import kotlin.collections.ArrayList


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTester {

    @Autowired
    lateinit var mvc:MockMvc

    @MockBean
    lateinit var users: UserService


    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val antonio = UserDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao")
        val chenel = UserDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao")

        val usersDAO = mutableListOf(antonio, chenel);

        val usersDTO = usersDAO.map{ UserDTO(it.id,it.name,it.email,it.username,it.password,it.cellphone,it.address) }


        val usersURL = "/users"
    }



    @Test
    fun `Test Get One User`() {
        Mockito.`when`(users.getOneUser(1)).thenReturn(antonio)

        val result = mvc.perform(get("$usersURL/1"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<UserDTO>(responseString)
        assertThat(responseDTO, equalTo(usersDTO[0]))
    }

    @Test
    fun `Test GET One Pet (Not Found)`() {
        Mockito.`when`(users.getOneUser(2)).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$usersURL/2"))
                .andExpect(status().is4xxClientError)
    }

    fun <T>nonNullAny(t:Class<T>):T = Mockito.any(t)




}