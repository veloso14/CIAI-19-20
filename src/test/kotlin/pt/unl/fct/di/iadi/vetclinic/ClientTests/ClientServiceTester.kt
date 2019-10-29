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
        val consulta1 = AppointmentDAO(1, Date(), "consulta1", PetDAO(), antonio)
        val consulta2 = AppointmentDAO(2, Date(), "consulta1", PetDAO(), antonio)
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





}