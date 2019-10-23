package pt.unl.fct.di.iadi.vetclinic.VetTests

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import pt.unl.fct.di.iadi.vetclinic.model.*
import pt.unl.fct.di.iadi.vetclinic.services.*
import java.time.LocalDateTime
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class VetServiceTester {

    @Autowired
    lateinit var vets: VetService

    @MockBean
    lateinit var repo: VetRepository

    @MockBean
    lateinit var aptRepo:AppointmentRepository

    companion object Constants {

        val antonio = VetDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao", 500, emptyList(),false)
        val chenel = VetDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao", 600, emptyList(),false)

        val vetsDAO = mutableListOf(antonio, chenel);

    }


    @Test
    fun `basic test on getOne`() {
        Mockito.`when`(repo.findById("Antonio")).thenReturn(Optional.of(antonio));

        assertThat(vets.getOneVet("Antonio"), equalTo(antonio))
    }

    @Test(expected = NotFoundException::class)
    fun `test on getOne() exception`() {
        //did not find the desired pet on the DB hence an empty Optional
        Mockito.`when`(repo.findById("")).thenReturn(Optional.empty())

        vets.getOneVet("")
    }


    @Test
    fun `test on retrieving appointments 1`() {
        val consulta1 = AppointmentDAO(1, LocalDateTime.MIN, LocalDateTime.MAX, "consulta1",false, PetDAO(),ClientDAO(), antonio)
        val consulta2 = AppointmentDAO(2, LocalDateTime.MIN, LocalDateTime.MAX, "consulta1",false, PetDAO(),ClientDAO(), antonio)
        antonio.appointments = listOf(consulta1, consulta2)

        Mockito.`when`(repo.findByIdWithAppointment(antonio.name)).thenReturn(Optional.of(antonio))

        assertThat(vets.appointmentsOfVet(antonio.name), equalTo(antonio.appointments))
    }

    @Test
    fun `test on retrieving appointments 2`() {
        antonio.appointments = emptyList()

        Mockito.`when`(repo.findByIdWithAppointment(antonio.name)).thenReturn(Optional.of(antonio))

        assertThat(vets.appointmentsOfVet(antonio.name), equalTo(antonio.appointments))
    }


}