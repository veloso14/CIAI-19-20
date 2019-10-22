package pt.unl.fct.di.iadi.vetclinic.UserTester


import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetRepository
import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserRepository
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PetService
import pt.unl.fct.di.iadi.vetclinic.services.UserService
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest
class UserServiceTester {

    @Autowired
    lateinit var users:UserService

    @MockBean
    lateinit var repo:UserRepository

    companion object Constants {

        val veloso = UserDAO(1L,"Veloso","joao.veloso@neec-fct.com","jmveloso","123456",962839449,"Pio 12")
        val userDAO = mutableListOf(veloso);
    }

    @Test
    fun `basic test on getAll`() {
        Mockito.`when`(repo.findAll()).thenReturn(userDAO);
        assertThat(users.getAllUser(), equalTo(userDAO as List<UserDAO>))
    }

    @Test
    fun `basic test on getOne`() {
        Mockito.`when`(repo.findById("jmveloso")).thenReturn(Optional.of(veloso));
        assertThat(users.getOneUser("jmveloso"), equalTo(veloso))
    }

    @Test(expected = NotFoundException::class)
    fun `test on getOne() exception`() {
        //did not find the desired pet on the DB hence an empty Optional
       Mockito.`when`(repo.findById(anyString())).thenReturn(Optional.empty())
        //Basta  lancar excepção
        //Porque é que não lança????
        //TODO
        repo.getOne("Veloso")

    }

    @Test
    fun `test on addNewPet()`() {
        Mockito.`when`(repo.save(Mockito.any(UserDAO::class.java)))
                .then {
                    val user:UserDAO = it.getArgument(0)
                    assertThat(user.id, equalTo(veloso.id))
                    assertThat(user.email, equalTo(veloso.email))
                    assertThat(user.name, equalTo(veloso.name))
                    user
                }

        repo.save(veloso)
    }
}