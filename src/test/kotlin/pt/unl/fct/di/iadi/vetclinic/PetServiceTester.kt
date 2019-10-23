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
import pt.unl.fct.di.iadi.vetclinic.model.*
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PetService
import pt.unl.fct.di.iadi.vetclinic.services.PreconditionFailedException
import java.time.LocalDateTime
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class PetServiceTester {

    @Autowired
    lateinit var pets:PetService

    @MockBean
    lateinit var repo:PetRepository

    @MockBean
    lateinit var aptRepo:AppointmentRepository

    companion object Constants {
        val pantufas = PetDAO(1L, "pantufas", "Dog", emptyList(), emptyList(), ClientDAO())
        val bigodes = PetDAO(2L, "bigodes", "Cat", emptyList(), emptyList(), ClientDAO())
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
    fun `test on adding a new pet`() {
        Mockito.`when`(repo.save(Mockito.any(PetDAO::class.java)))
                .then {
                    val pet:PetDAO = it.getArgument(0)
                    assertThat(pet.id, equalTo(0L))
                    assertThat(pet.name, equalTo(pantufas.name))
                    assertThat(pet.species, equalTo(pantufas.species))
                    assertThat(pet.appointments, equalTo(pantufas.appointments))
                    pet
                }

        pets.addNewPet(PetDAO(0L, pantufas.name, pantufas.species, pantufas.appointments, pantufas.notes, pantufas.owner))
    }

    @Test(expected = PreconditionFailedException::class)
    fun `test on adding a new pet (Error)`() {
        pets.addNewPet(pantufas) // pantufas has a non-0 id
    }

    @Test
    fun `test on retrieving appointments 1`() {
        val consulta1 = AppointmentDAO(1, LocalDateTime.MIN, LocalDateTime.MAX, "consulta1", pantufas,ClientDAO())
        val consulta2 = AppointmentDAO(2, LocalDateTime.MIN, LocalDateTime.MAX, "consulta1", pantufas,ClientDAO())
        pantufas.appointments = listOf(consulta1, consulta2)

        Mockito.`when`(repo.findByIdWithAppointment(pantufas.id)).thenReturn(Optional.of(pantufas))

        assertThat(pets.appointmentsOfPet(pantufas.id), equalTo(pantufas.appointments))
    }

    @Test
    fun `test on retrieving appointments 2`() {
        pantufas.appointments = emptyList()

        Mockito.`when`(repo.findByIdWithAppointment(pantufas.id)).thenReturn(Optional.of(pantufas))

        assertThat(pets.appointmentsOfPet(pantufas.id), equalTo(pantufas.appointments))
    }

    @Test
    fun `test on adding a new Appointment`() {
        val consulta = AppointmentDAO(0, LocalDateTime.MIN, LocalDateTime.MAX, "consulta", pantufas, ClientDAO())

        pantufas.appointments = emptyList()

        Mockito.`when`(aptRepo.save(Mockito.any(AppointmentDAO::class.java)))
                .then {
                    val apt:AppointmentDAO = it.getArgument(0)
                    assertThat(apt.id, equalTo(0L))
                    assertThat(apt.desc, equalTo(consulta.desc))
                    assertThat(apt.start, equalTo(consulta.start))
                    assertThat(apt.end, equalTo(consulta.end))
                    assertThat(apt.pet, equalTo(pantufas))
                    apt
                }
        pets.newAppointment(consulta)
    }

    @Test(expected = PreconditionFailedException::class)
    fun `test on adding a new Appointment (Precondition Failed)`() {
        val consulta = AppointmentDAO(1, LocalDateTime.MIN, LocalDateTime.MAX, "consulta", pantufas,ClientDAO())
        pets.newAppointment(consulta)
    }
}