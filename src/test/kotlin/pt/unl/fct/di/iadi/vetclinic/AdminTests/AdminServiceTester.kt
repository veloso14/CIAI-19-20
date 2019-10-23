package pt.unl.fct.di.iadi.vetclinic.AdminTests

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.di.iadi.vetclinic.model.*
import pt.unl.fct.di.iadi.vetclinic.services.AdminService
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PetService
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class AdminServiceTester {

    @Autowired
    lateinit var admins: AdminService

    @MockBean
    lateinit var repo: UserRepository

    companion object Constants {
        val veloso = UserDAO(1L, "Veloso", "joao.veloso@neec-fct.com", "jmveloso", "123456", 962839449, "Pio 12")
        val userDAO = mutableListOf(veloso);
    }

    @Test
    fun `basic test on findEmployee`() {
        Mockito.`when`(repo.findById("jmveloso")).thenReturn(Optional.of(veloso));
        assertThat(admins.findEmployee("jmveloso"), equalTo(veloso))
    }

}