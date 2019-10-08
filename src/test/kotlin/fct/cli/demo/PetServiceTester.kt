package fct.cli.demo


import fct.cli.demo.exceptions.PetNotFoundException
import fct.cli.demo.model.PetDAO
import fct.cli.demo.services.PetService
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest

class PetServiceTester {
    @Autowired
    lateinit var pets: PetService

    companion object Contants{
        val pantufas = PetDAO( 1L, "Pantufas", "Dog")
    }

    @Test
    fun `basic test on getAll`(){
        assertThat(pets.getAllPets(), equalTo(emptyList() ))
    }

    @Test
    fun `basic test on getOne`(){
        assertThat(pets.getOnepet(1L), equalTo(pantufas))
    }

    @Test(expected = PetNotFoundException::class)
    fun `test on getOne exception`(){
        pets.getOnepet(0L)
    }

    @Test
    fun `test on addNewPet`(){
        pets.addOnePet(pantufas)
        assertThat(pets.getOnepet(pantufas.id), equalTo(pantufas))
    }

}