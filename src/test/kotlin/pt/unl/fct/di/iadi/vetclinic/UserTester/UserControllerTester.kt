package pt.unl.fct.di.iadi.vetclinic.UserTester

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
import pt.unl.fct.di.iadi.vetclinic.api.PetDTO
import pt.unl.fct.di.iadi.vetclinic.api.UserDTO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.UserService


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTester {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var users: UserService

    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val veloso = UserDAO(1, "Veloso", "joao.veloso@neec-fct.com", "sir_veloso", "123456", 962839449, "Pio 12")

        val userDAO = ArrayList(listOf(veloso))

        //val userDTO = userDAO.map { PetDTO(it.id, it.name, it.email , it.address , it.password) }

        val userURL = "/users"
    }

    @Test
    fun `Test GET all pets`() {
        Mockito.`when`(users.getAllUser()).thenReturn(userDAO)

        val result = mvc.perform(get(userURL))
                .andExpect(status().isOk())
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<List<UserDTO>>(responseString)
        //assertThat(responseDTO, equalTo(UserDTO))
    }

    @Test
    fun `Test Get One Pet`() {
        Mockito.`when`(users.getOneUser("jm.veloso")).thenReturn(veloso)

        val result = mvc.perform(get("$userURL/1"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<PetDTO>(responseString)
        //   assertThat(responseDTO, equalTo(UserDAO[0]))
    }

    @Test
    fun `Test GET One Pet (Not Found)`() {
        Mockito.`when`(users.getOneUser("jm.veloso")).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$userURL/2"))
                .andExpect(status().is4xxClientError)
    }

    fun <T> nonNullAny(t: Class<T>): T = Mockito.any(t)

    @Test
    fun `Test POST One Pet`() {
        val louro = PetDTO(0, "louro", "Papagaio")
        val louroDAO = PetDAO(louro.id, louro.name, louro.species, emptyList(), emptyList())

        val louroJSON = mapper.writeValueAsString(louro)

        Mockito.`when`(users.addNewUser(nonNullAny(UserDAO::class.java)))
                .then { assertThat(it.getArgument(0), equalTo(louroDAO)) }

        mvc.perform(post(userURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(louroJSON))
                .andExpect(status().isOk)
    }
}