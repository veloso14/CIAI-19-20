package pt.unl.fct.di.iadi.vetclinic.UserTester

import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.di.iadi.vetclinic.PetTests.PetRepoTester
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserRepository

@RunWith(SpringRunner::class)
@SpringBootTest
class UserRepoTester {

    @Autowired
    lateinit var userRepo:UserRepository

    companion object Constants {

        val veloso = UserDAO(1,"Veloso","joao.veloso@neec-fct.com","sir_veloso","123456",962839449,"Pio 12")

    }

    // IMPORTANT: the database is not cleaned between tests, it means that it will keep the pets
    // saved in previous tests

    @Test
    fun `basic test on findAll`() {
        assertThat(userRepo.findAll().toList(), equalTo(emptyList()))
    }
    


    @Test
    fun `basic test on save and delete`() {
        val user = userRepo.save(veloso)
        assertThat(user.id, CoreMatchers.not(equalTo(UserRepoTester.veloso.id)))//the id is different because it is generated by Spring
        userRepo.delete(user)
        assertThat(userRepo.findAll().toList(), equalTo(emptyList()))
    }


}