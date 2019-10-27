package pt.unl.fct.di.iadi.vetclinic.AdminTests
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.di.iadi.vetclinic.model.*
import pt.unl.fct.di.iadi.vetclinic.services.AdminService
import pt.unl.fct.di.iadi.vetclinic.services.UserService
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class AdminServiceTester {

    @Autowired
    lateinit var admins: AdminService

    @MockBean
    lateinit var repo: UserRepository

    companion object Constants {

        val admin = AdminDAO(1L, "admin", "admin@admin.pt", "admin", "123456", 965216264, "morada", 123)
        val vet = VetDAO(1L, "vet", "vet@vet.pt", "vet", "123456", 928764321, "morada", 123, emptyArray<AppointmentDAO>().toMutableList(), false)
        // val veloso = UserDAO(1L, "Veloso", "joao.veloso@neec-fct.com", "jmveloso", "123456", 962839449, "Pio 12")
      //  val userDAO = mutableListOf(veloso);
        val adminDAO = listOf(admin, vet)
    }

    @Test
    fun `basic test on getAllEmployees`() {
        Mockito.`when`(repo.findAll()).thenReturn(adminDAO);
        assertThat(admins.getAllEmployees(), equalTo(adminDAO as List<UserDAO>))
    }

}