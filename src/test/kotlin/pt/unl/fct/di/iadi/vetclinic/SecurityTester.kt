package pt.unl.fct.di.iadi.vetclinic

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pt.unl.fct.di.iadi.vetclinic.services.PetService


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTester {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var pets: PetService

    companion object {
        val petsURL = "/pets"
    }

    @Test
    @WithMockUser(username = "aUser", password = "aPassword", roles = ["USER"])
    fun `Test GET all pets (a user)`() {
        Mockito.`when`(pets.getAllPets()).thenReturn(emptyList())

        mvc.perform(MockMvcRequestBuilders.get(petsURL))
                .andExpect(status().isOk())
    }

    @Test
    fun `Test GET all pets (no user)`() {
        Mockito.`when`(pets.getAllPets()).thenReturn(emptyList())

        mvc.perform(MockMvcRequestBuilders.get(petsURL))
                .andExpect(status().is4xxClientError()) // 403
    }
}