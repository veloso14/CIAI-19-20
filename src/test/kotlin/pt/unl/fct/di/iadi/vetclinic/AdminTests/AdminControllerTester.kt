package pt.unl.fct.di.iadi.vetclinic.AdminTests

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
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pt.unl.fct.di.iadi.vetclinic.api.PetDTO
import pt.unl.fct.di.iadi.vetclinic.api.UserDTO
import pt.unl.fct.di.iadi.vetclinic.model.AdminDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
import pt.unl.fct.di.iadi.vetclinic.services.AdminService
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.UserService


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTester {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var admins: AdminService

    companion object {
        // To avoid all annotations JsonProperties in data classes
        // see: https://github.com/FasterXML/jackson-module-kotlin
        // see: https://discuss.kotlinlang.org/t/data-class-and-jackson-annotation-conflict/397/6
        val mapper = ObjectMapper().registerModule(KotlinModule())

        val admin = AdminDAO(1L, "admin", "admin@admin.pt", "admin", "123456", 965216264, "morada", 123)
        val userDAO = ArrayList(listOf(admin))

        val adminURL = "/user/admin"
    }

    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["ADMIN"])
    fun `Test GET all employees`() {
        Mockito.`when`(admins.getAllEmployees()).thenReturn(userDAO)

        val result = mvc.perform(get(adminURL + "/employees"))
                .andExpect(status().isOk())
                .andReturn()

        val responseString = result.response.contentAsString
        val responseDTO = mapper.readValue<ArrayList<UserDTO>>(responseString)
        assertThat(responseDTO.get(0).email , equalTo(admin.email))
        assertThat(responseDTO.get(0).address , equalTo(admin.address))
        assertThat(responseDTO.get(0).cellphone , equalTo(admin.cellphone))
        assertThat(responseDTO.get(0).username , equalTo(admin.username))
    }


}