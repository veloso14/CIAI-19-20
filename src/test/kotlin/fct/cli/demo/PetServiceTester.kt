package fct.cli.demo

import fct.cli.demo.exceptions.PetNotFoundException
import fct.cli.demo.services.PetService
import model.PetDAO
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest

class PetServiceTester {
    @Autowired
    lateinit var pets:PetService

    companion object Contants{
        val pantufas = PetDAO(id: 1L, name:"Pantufas", species:"Dog")
    }

    @Test
    fun 'basic test on getAll'(){
        Assert.assertThat(pets.getAllPets(), equalsTo(emptyList()))
    }

    @Test
    fun 'basic test on getOne'(){
        assertThat(pets.getOnePet(id:1L), equalsTo(pantufas))
    }

    @Test(expected = PetNotFoundException::class)
    fun 'test on getOne exception'(){
        pets.getOnepet(id:0L)
    }

    @Test
    fun 'test on addNewPet'(){
        pets.addOne(pantufas)
        assertThat(pets.getOnePet(pantufas.id), equalsTo(pantufas))
    }

}