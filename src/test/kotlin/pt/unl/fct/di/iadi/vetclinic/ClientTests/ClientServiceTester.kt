package pt.unl.fct.di.iadi.vetclinic.ClientTests

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.di.iadi.vetclinic.model.*
import pt.unl.fct.di.iadi.vetclinic.services.ClientService
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PreconditionFailedException
import java.time.LocalDateTime
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class ClientServiceTester {

    @Autowired
    lateinit var clients: ClientService

    @MockBean
    lateinit var repo: ClientRepository

    @MockBean
    lateinit var aptRepo:AppointmentRepository

    @MockBean
    lateinit var petRepo:PetRepository

    companion object Constants {

        val antonio = ClientDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao", emptyList(), emptyList())
        val chenel = ClientDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao", emptyList(), emptyList())

        val clientsDAO = mutableListOf(antonio, chenel);

    }


    @Test
    fun `basic test on getOne`() {
        Mockito.`when`(repo.findById(1L)).thenReturn(Optional.of(antonio));

        assertThat(clients.getOneClient(1L), equalTo(antonio))
    }

    @Test(expected = NotFoundException::class)
    fun `test on getOne() exception`() {
        //did not find the desired pet on the DB hence an empty Optional
        Mockito.`when`(repo.findById(anyLong())).thenReturn(Optional.empty())

        clients.getOneClient(0L)
    }

    @Test
    fun `test on retrieving appointments 1`() {
        val consulta1 = AppointmentDAO(1, Date(), "consulta1", PetDAO(), antonio, VetDAO())
        val consulta2 = AppointmentDAO(2, Date(), "consulta1", PetDAO(), antonio, VetDAO())
        antonio.appointments = listOf(consulta1, consulta2)

        Mockito.`when`(repo.findByIdWithAppointment(antonio.id)).thenReturn(Optional.of(antonio))

        assertThat(clients.appointmentsOfClient(antonio.id), equalTo(antonio.appointments))
    }

    @Test
    fun `test on retrieving appointments 2`() {
        antonio.appointments = emptyList()

        Mockito.`when`(repo.findByIdWithAppointment(antonio.id)).thenReturn(Optional.of(antonio))

        assertThat(clients.appointmentsOfClient(antonio.id), equalTo(antonio.appointments))
    }

    @Test
    fun `test on adding a new Appointment`() {
        val consulta = AppointmentDAO(0, Date(), "consulta", PetDAO(), antonio, VetDAO())

        antonio.appointments = emptyList()

        Mockito.`when`(aptRepo.save(Mockito.any(AppointmentDAO::class.java)))
                .then {
                    val apt:AppointmentDAO = it.getArgument(0)
                    assertThat(apt.id, equalTo(0L))
                    assertThat(apt.desc, equalTo(consulta.desc))
                    assertThat(apt.date, equalTo(consulta.date))
                    assertThat(apt.client, equalTo(antonio))
                    apt
                }
        clients.newAppointment(consulta)
    }

    @Test(expected = PreconditionFailedException::class)
    fun `test on adding a new Appointment (Precondition Failed)`() {
        val consulta = AppointmentDAO(1, Date(), "consulta", PetDAO(), antonio, VetDAO())
        clients.newAppointment(consulta)
    }

    @Test
    fun `test on retrieving pets 1`() {
        val pantufas = PetDAO(1L, "pantufas", "Dog", emptyList(), ClientDAO())
        val bigodes = PetDAO(2L, "bigodes", "Cat", emptyList(), ClientDAO())
        antonio.pets = listOf(pantufas,bigodes)

        Mockito.`when`(repo.findByIdWithPet(antonio.id)).thenReturn(Optional.of(antonio))

        assertThat(clients.petsOfClient(antonio.id), equalTo(antonio.pets))
    }

    @Test
    fun `test on retrieving pets 2`() {
        antonio.pets = emptyList()

        Mockito.`when`(repo.findByIdWithPet(antonio.id)).thenReturn(Optional.of(antonio))

        assertThat(clients.petsOfClient(antonio.id), equalTo(antonio.pets))
    }

    @Test
    fun `test on adding a new Pet`() {
        val pantufas = PetDAO(0, "pantufas", "Dog", emptyList(), antonio)

        antonio.pets = emptyList()

        Mockito.`when`(petRepo.save(Mockito.any(PetDAO::class.java)))
                .then {
                    val pet:PetDAO = it.getArgument(0)
                    assertThat(pet.id, equalTo(0L))
                    assertThat(pet.name, equalTo(pantufas.name))
                    assertThat(pet.species, equalTo(pantufas.species))
                    assertThat(pet.appointments, equalTo(pantufas.appointments))
                    assertThat(pet.owner, equalTo(antonio))
                    pet
                }
        clients.newPet(pantufas)
    }

    @Test(expected = PreconditionFailedException::class)
    fun `test on adding a new Pet (Precondition Failed)`() {
        val pantufas = PetDAO(1, "pantufas", "Dog", emptyList(), ClientDAO())
        clients.newPet(pantufas)
    }





}