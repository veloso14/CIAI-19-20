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
import org.springframework.security.test.context.support.WithMockUser
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

        val userURL = "/users"
    }

    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["ADMIN"])
    fun `Test GET all users`() {
        Mockito.`when`(users.getAllUser()).thenReturn(userDAO)

        val result = mvc.perform(get(userURL+ "/all"))
                .andExpect(status().isOk())
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<ArrayList<UserDTO>>(responseString)
        assertThat(responseDTO.get(0).email , equalTo(veloso.email))
        assertThat(responseDTO.get(0).address , equalTo(veloso.address))
        assertThat(responseDTO.get(0).cellphone , equalTo(veloso.cellphone))
        assertThat(responseDTO.get(0).username , equalTo(veloso.username))
    }


    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["ADMIN"])
    fun `Test Get One User`() {
        Mockito.`when`(users.getOneUser("jmveloso")).thenReturn(veloso)

        val result = mvc.perform(get("$userURL/jmveloso"))
                .andExpect(status().isOk)
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<PetDTO>(responseString)
        //   assertThat(responseDTO, equalTo(UserDAO[0]))
    }

    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["USER"])
    fun `Test cliente não tem permissoes`() {
        Mockito.`when`(users.getOneUser("jmveloso")).thenReturn(veloso)

        mvc.perform(get("$userURL/jmveloso"))
                .andExpect(status().isForbidden)
                .andReturn()

    }

    @Test
    fun `Test GET One User (Not Found)`() {
        Mockito.`when`(users.getOneUser("jm.veloso")).thenThrow(NotFoundException("not found"))

        mvc.perform(get("$userURL/2"))
                .andExpect(status().is4xxClientError)
    }


}