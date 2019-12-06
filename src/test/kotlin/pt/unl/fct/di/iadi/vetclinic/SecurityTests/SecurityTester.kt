package pt.unl.fct.di.iadi.vetclinic

import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pt.unl.fct.di.iadi.vetclinic.AdminTests.AdminControllerTester
import pt.unl.fct.di.iadi.vetclinic.api.AdminDTO
import pt.unl.fct.di.iadi.vetclinic.model.AdminRepository
import pt.unl.fct.di.iadi.vetclinic.services.AdminService
import pt.unl.fct.di.iadi.vetclinic.services.PetService
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTester {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var pets: PetService

    @MockBean
    lateinit var admins: AdminService

    companion object {
        val petsURL = "/pets"
        val adminsURL = "/admins"
    }

    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["VET"])
    fun `Test GET all pets (a vet)`() {
        Mockito.`when`(pets.getAllPets()).thenReturn(emptyList())

        mvc.perform(MockMvcRequestBuilders.get(petsURL))
                .andExpect(status().isOk())
    }

    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["ADMIN"])
    fun `Test Acesso Admins (Com Role certo)`() {

        //Mockito.`when`(admins.getOneAdmin(1)).thenReturn(AdminControllerTester.cid)
        Mockito.`when`(admins.getAllAdmins()).thenReturn(emptyList())

         mvc.perform(MockMvcRequestBuilders.get("adminsURL"))
                .andExpect(status().isOk)
                //.andReturn()
    }

    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["USER"])
    fun `Test Acesso Admins (Sem Role certo)`() {

        mvc.perform(MockMvcRequestBuilders.get("/admins/2"))
                .andExpect(status().is4xxClientError())
    }

    @Test
    fun `Test GET all pets (no user)`() {
        Mockito.`when`(pets.getAllPets()).thenReturn(emptyList())

        mvc.perform(MockMvcRequestBuilders.get(petsURL))
                .andExpect(status().is4xxClientError()) // 403
    }
}