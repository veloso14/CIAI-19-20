package pt.unl.fct.di.iadi.vetclinic.AppointmentTests
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
import pt.unl.fct.di.iadi.vetclinic.services.AppointmentService
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PreconditionFailedException
import pt.unl.fct.di.iadi.vetclinic.services.VetService
import java.time.LocalDateTime
import java.time.Month
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class AppointmentServiceTester {

    @Autowired
    lateinit var apts: AppointmentService

    @Autowired
    lateinit var vets: VetService


    @MockBean
    lateinit var repo:AppointmentRepository


    companion object Constants {
        val consulta = AppointmentDAO(1L, Date(), "consulta",PetDAO(), ClientDAO(), VetDAO())
        val exame = AppointmentDAO(2L,Date(), "exame", PetDAO(), ClientDAO(), VetDAO())

        val data = Date(2019,1,1,9,0)
        val consulta_marcada = AppointmentDAO(1L, data, "consulta",PetDAO(), ClientDAO(), VetDAO())

        val aptsDAO = ArrayList(listOf(consulta, exame))

    }

    @Test
    fun `basic test on getAll`() {
        Mockito.`when`(repo.findAll()).thenReturn(aptsDAO);

        assertThat(apts.getAllAppointments(), equalTo(aptsDAO as List<AppointmentDAO>))
    }

    @Test
    fun `basic test on getOne`() {
        Mockito.`when`(repo.findById(1L)).thenReturn(Optional.of(consulta));

        assertThat(apts.getOneAppointment(1L), equalTo(consulta))
    }

    @Test(expected = NotFoundException::class)
    fun `test on getOne() exception`() {
        //did not find the desired pet on the DB hence an empty Optional
        Mockito.`when`(repo.findById(anyLong())).thenReturn(Optional.empty())

        apts.getOneAppointment(0L)
    }

    @Test
    fun `test on adding a new appointment`() {
        Mockito.`when`(repo.save(Mockito.any(AppointmentDAO::class.java)))
                .then {
                    val apt:AppointmentDAO = it.getArgument(0)
                    assertThat(apt.id, equalTo(0L))
                    assertThat(apt.date, equalTo(consulta.date))
                    assertThat(apt.desc, equalTo(consulta.desc))
                    assertThat(apt.pet, equalTo(consulta.pet))
                    apt
                }

        apts.addNewAppointment(AppointmentDAO(0L, consulta.date, consulta.desc, consulta.pet, consulta.client, consulta.vet))
    }

    @Test
    fun `test 1`() {
        val data = Date(2019,1,1,9,0)
        val consulta_marcada = AppointmentDAO(1L, data, "consulta",PetDAO(), ClientDAO(), VetDAO())
       println("consulta marcada: " +  apts.checkAvailable(consulta_marcada))
        val antonio = VetDAO(1L, "Antonio", "antonio@gmail.com", "tony",
                "1234", 1234, "Rua Romao", "rosto.jpg", 11,
                true, emptyList(), emptyList())
        val schedule = vets.createSchedule(ScheduleDAO(antonio, Month.APRIL))
        schedule.getShiftsList()[1].getSlotsList()[0].setAvailableFalse()
        println("consulta marcada: " +  apts.checkAvailable(consulta_marcada))

    }

    @Test(expected = PreconditionFailedException::class)
    fun `test on adding a new pet (Error)`() {
        apts.addNewAppointment(consulta) // pantufas has a non-0 id
    }


}