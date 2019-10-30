package pt.unl.fct.di.iadi.vetclinic.UserTests

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
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PetService
import pt.unl.fct.di.iadi.vetclinic.services.PreconditionFailedException
import pt.unl.fct.di.iadi.vetclinic.services.UserService
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class UserServiceTester {

    @Autowired
    lateinit var users: UserService

    @MockBean
    lateinit var repo:UserRepository


    companion object Constants {
        val antonio = UserDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao")
        val chenel = UserDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao")

        val usersDAO = mutableListOf(antonio, chenel);

    }



    @Test
    fun `basic test on getOne`() {
        Mockito.`when`(repo.findById(1L)).thenReturn(Optional.of(antonio));

        assertThat(users.getOneUser(1L), equalTo(antonio))
    }

    @Test(expected = NotFoundException::class)
    fun `test on getOne() exception`() {
        //did not find the desired pet on the DB hence an empty Optional
        Mockito.`when`(repo.findById(anyLong())).thenReturn(Optional.empty())

        users.getOneUser(0L)
    }

    fun `test on update pass`(){


        antonio.changePassword("outra")
        assertThat(users.getOneUser(antonio.id).password, equalTo("outra"))
    }


}