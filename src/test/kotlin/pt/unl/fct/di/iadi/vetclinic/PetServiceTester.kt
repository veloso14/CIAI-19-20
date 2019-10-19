package pt.unl.fct.di.iadi.vetclinic

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
import pt.unl.fct.di.iadi.vetclinic.model.ClientDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetRepository
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PetService
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class PetServiceTester {

    @Autowired
    lateinit var pets:PetService

    @MockBean
    lateinit var repo:PetRepository

    companion object Constants {
       // val client = ClientDAO(1L,"ss","uu","pp","pp",1L,"ss", emptyList())
        val pantufas = PetDAO(1L, "pantufas", "Dog", emptyList(), emptyList())
        val bigodes = PetDAO(2L, "bigodes", "Cat", emptyList(), emptyList())
        val petsDAO = mutableListOf(pantufas, bigodes);
    }

    @Test
    fun `basic test on getAll`() {
        Mockito.`when`(repo.findAll()).thenReturn(petsDAO);

        assertThat(pets.getAllPets(), equalTo(petsDAO as List<PetDAO>))
    }

    @Test
    fun `basic test on getOne`() {
        Mockito.`when`(repo.findById(1L)).thenReturn(Optional.of(pantufas));

        assertThat(pets.getOnePet(1L), equalTo(pantufas))
    }

    @Test(expected = NotFoundException::class)
    fun `test on getOne() exception`() {
        //did not find the desired pet on the DB hence an empty Optional
        Mockito.`when`(repo.findById(anyLong())).thenReturn(Optional.empty())

        pets.getOnePet(0L)
    }

    @Test
    fun `test on addNewPet()`() {
        Mockito.`when`(repo.save(Mockito.any(PetDAO::class.java)))
                .then {
                    val pet:PetDAO = it.getArgument(0)
                    assertThat(pet.id, equalTo(pantufas.id))
                    assertThat(pet.species, equalTo(pantufas.species))
                    assertThat(pet.name, equalTo(pantufas.name))
                    pet
                }

        pets.addNewPet(pantufas)
    }
}