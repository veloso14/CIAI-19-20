package pt.unl.fct.di.iadi.vetclinic.VetTests

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
import pt.unl.fct.di.iadi.vetclinic.services.VetService
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class VetServiceTester {

    @Autowired
    lateinit var vets: VetService

    @MockBean
    lateinit var repo:VetRepository

    @MockBean
    lateinit var clientsRepo:ClientRepository

    @MockBean
    lateinit var petsRepo:PetRepository

    @MockBean
    lateinit var aptRepo:AppointmentRepository

    companion object Constants {
        val antonio = VetDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao","rosto.jpg", 11, false, emptyList<AppointmentDAO>())
        val chenel = VetDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao","rosto.jpg", 12, false, emptyList<AppointmentDAO>())
        val vetsDAO = mutableListOf(antonio, chenel);

        val pantufas = PetDAO(1L, "pantufas", "Dog",false, emptyList(), ClientDAO())
        val bigodes = PetDAO(2L, "bigodes", "Cat",false, emptyList(), ClientDAO())
        val petsDAO = mutableListOf(pantufas, bigodes);

        val manzanares = ClientDAO(1L,"JoseMari","man@gmail.com","manza","1234",1234, "Rua Romao", emptyList<PetDAO>(), emptyList<AppointmentDAO>())
        val campuzano = ClientDAO(2L,"Tomas","camp@gmail.com","camp","1234",1234, "Rua Romao", emptyList<PetDAO>(), emptyList<AppointmentDAO>())
        val clientsDAO = mutableListOf(manzanares, campuzano);

        val consulta1 = AppointmentDAO(1L, Date(), "consulta1", PetDAO(), ClientDAO(), VetDAO())
        val consulta2 = AppointmentDAO(2L, Date(), "consulta1", PetDAO(), ClientDAO(), VetDAO())
        val consultasDAO = mutableListOf(consulta1, consulta2);

    }


    @Test
    fun `basic test on getOne`() {
        Mockito.`when`(repo.findById(1L)).thenReturn(Optional.of(antonio));

        assertThat(vets.getOneVet(1L), equalTo(antonio))
    }

    @Test(expected = NotFoundException::class)
    fun `test on getOne() exception`() {
        //did not find the desired pet on the DB hence an empty Optional
        Mockito.`when`(repo.findById(anyLong())).thenReturn(Optional.empty())

        vets.getOneVet(0L)
    }

    @Test
    fun `basic test on getAllPets`() {
        Mockito.`when`(petsRepo.findAll()).thenReturn(petsDAO);

        assertThat(vets.getAllPets(), equalTo(petsDAO as List<PetDAO>))
    }

    @Test
    fun `basic test on getAllClients`() {
        Mockito.`when`(clientsRepo.findAll()).thenReturn(clientsDAO);

        assertThat(vets.getAllClients(), equalTo(clientsDAO as List<ClientDAO>))
    }

    @Test
    fun `basic test on getAllApts`() {
        Mockito.`when`(aptRepo.findAll()).thenReturn(consultasDAO);

        assertThat(vets.getAllAppointments(), equalTo(consultasDAO as List<AppointmentDAO>))
    }

    @Test
    fun `test on retrieving appointments 1`() {
        val consulta1 = AppointmentDAO(1, Date(), "consulta1", PetDAO(), ClientDAO(), antonio)
        val consulta2 = AppointmentDAO(2, Date(), "consulta1", PetDAO(), ClientDAO(), antonio)
        antonio.appointments = listOf(consulta1, consulta2)

        Mockito.`when`(repo.findByIdWithAppointment(antonio.id)).thenReturn(Optional.of(antonio))

        assertThat(vets.appointmentsOfVet(antonio.id), equalTo(antonio.appointments))
    }

    @Test
    fun `test on retrieving appointments 2`() {
        antonio.appointments = emptyList()

        Mockito.`when`(repo.findByIdWithAppointment(antonio.id)).thenReturn(Optional.of(antonio))

        assertThat(vets.appointmentsOfVet(antonio.id), equalTo(antonio.appointments))
    }



}