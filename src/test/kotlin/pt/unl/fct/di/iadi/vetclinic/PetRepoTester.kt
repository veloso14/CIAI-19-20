package pt.unl.fct.di.iadi.vetclinic

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.di.iadi.vetclinic.model.ClientDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetRepository

@RunWith(SpringRunner::class)
@SpringBootTest
class PetRepoTester {

    @Autowired
    lateinit var pets:PetRepository

    companion object Constants {
        val pantufas = PetDAO(-1L, "pantufas", "Dog", emptyList(), ClientDAO())
        val bigodes = PetDAO(-1L, "bigodes", "Cat", emptyList(), ClientDAO())
    }

    // IMPORTANT: the database is not cleaned between tests, it means that it will keep the pets
    // saved in previous tests

    @Test
    fun `basic test on findAll`() {
        assertThat(pets.findAll().toList(), equalTo(emptyList()))
    }

    @Test
    fun `basic test on save and delete`() {
        val pet = pets.save(pantufas)
        assertThat(pet.id, not(equalTo(pantufas.id)))//the id is different because it is generated by Spring
        assertThat(pet.name, equalTo(pantufas.name))
        assertThat(pet.species, equalTo(pantufas.species))

        assertThat(pets.findAll().toList(), equalTo(listOf<PetDAO>(pet)))

        pets.delete(pet);

        assertThat(pets.findAll().toList(), equalTo(emptyList()))
    }

    @Test
    fun `another test on save and delete`() {
        val pet0 = pets.save(pantufas)
        assertThat(pet0.id, not(equalTo(pantufas.id)))//the id is different because it is generated by Spring
        assertThat(pet0.name, equalTo(pantufas.name))
        assertThat(pet0.species, equalTo(pantufas.species))

        assertThat(pets.findAll().toList(), equalTo(listOf(pet0)))

        val pet1 = pets.save(bigodes)
        assertThat(pet1.id, not(equalTo(bigodes.id)))//the id is different because it is generated by Spring
        assertThat(pet1.name, equalTo(bigodes.name))
        assertThat(pet1.species, equalTo(bigodes.species))

        assertThat(pets.findAll().toList(), equalTo(listOf(pet0, pet1)))

        pets.delete(pet0)

        assertThat(pets.findAll().toList(), equalTo(listOf(pet1)))

        pets.delete(pet1)

        assertThat(pets.findAll().toList(), equalTo(emptyList()))
    }

}