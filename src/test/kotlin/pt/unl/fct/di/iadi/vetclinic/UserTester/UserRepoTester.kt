package pt.unl.fct.di.iadi.vetclinic.UserTester

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.di.iadi.vetclinic.model.UserRepository

@RunWith(SpringRunner::class)
@SpringBootTest
class UserRepoTester {

    @Autowired
    lateinit var user:UserRepository

    companion object Constants {

    }

    // IMPORTANT: the database is not cleaned between tests, it means that it will keep the pets
    // saved in previous tests

    @Test
    fun `basic test on findAll`() {
        assertThat(user.findAll().toList(), equalTo(emptyList()))
    }

    @Test
    fun `basic test on save and delete`() {
        assertThat(user.findAll().toList(), equalTo(emptyList()))
    }


}