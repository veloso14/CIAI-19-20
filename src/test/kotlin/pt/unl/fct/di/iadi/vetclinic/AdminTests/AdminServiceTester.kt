package pt.unl.fct.di.iadi.vetclinic.AdminTests

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
import pt.unl.fct.di.iadi.vetclinic.services.*
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class AdminServiceTester {

    @Autowired
    lateinit var admins: AdminService

    @MockBean
    lateinit var repo:AdminRepository

    @MockBean
    lateinit var vetsRepo:VetRepository

    @MockBean
    lateinit var clientsRepo:ClientRepository

    @MockBean
    lateinit var petsRepo:PetRepository

    @MockBean
    lateinit var aptRepo:AppointmentRepository

    companion object Constants {
        val cid = AdminDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao","rosto.jpg", 11)
        val curro = AdminDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao","rosto.jpg", 12)
        val adminsDAO = mutableListOf(cid, curro);


        val antonio = VetDAO(1L,"Antonio","antonio@gmail.com","tony","1234",1234, "Rua Romao","rosto.jpg", 11, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>())
        val chenel = VetDAO(2L,"Chenel","chenel@gmail.com","chenel","1234",1234, "Rua Romao","rosto.jpg", 12, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>())
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
        Mockito.`when`(repo.findById(1L)).thenReturn(Optional.of(cid));

        assertThat(admins.getOneAdmin(1L), equalTo(cid))
    }

    @Test(expected = NotFoundException::class)
    fun `test on getOne() exception`() {
        Mockito.`when`(repo.findById(anyLong())).thenReturn(Optional.empty())

        admins.getOneAdmin(0L)
    }

    @Test
    fun `basic test on getAllAdmins`() {
        Mockito.`when`(repo.findAll()).thenReturn(adminsDAO);

        assertThat(admins.getAllAdmins(), equalTo(adminsDAO as List<AdminDAO>))
    }

    /*
    @Test
    fun `basic test on getAllPets`() {
        Mockito.`when`(petsRepo.findAll()).thenReturn(petsDAO);

        assertThat(admins.getAllPets(), equalTo(petsDAO as List<PetDAO>))
    }

    @Test
    fun `basic test on getAllClients`() {
        Mockito.`when`(clientsRepo.findAll()).thenReturn(clientsDAO);

        assertThat(admins.getAllClients(), equalTo(clientsDAO as List<ClientDAO>))
    }

    @Test
    fun `basic test on getAllApts`() {
        Mockito.`when`(aptRepo.findAll()).thenReturn(consultasDAO);

        assertThat(admins.getAllAppointments(), equalTo(consultasDAO as List<AppointmentDAO>))
    }

     */

/*
    @Test
    fun `test on retrieving appointments 1`() {
        val consulta1 = AppointmentDAO(1, Date(), "consulta1", PetDAO(), ClientDAO(), antonio)
        val consulta2 = AppointmentDAO(2, Date(), "consulta1", PetDAO(), ClientDAO(), antonio)
        antonio.appointments = listOf(consulta1, consulta2)

        Mockito.`when`(vetsRepo.findByIdWithAppointment(antonio.id)).thenReturn(Optional.of(antonio))

        assertThat(admins.getVetsAppointments(antonio.id), equalTo(antonio.appointments))
    }

    @Test
    fun `test on retrieving appointments 2`() {
        antonio.appointments = emptyList()

        Mockito.`when`(vetsRepo.findByIdWithAppointment(antonio.id)).thenReturn(Optional.of(antonio))

        assertThat(admins.getVetsAppointments(antonio.id), equalTo(antonio.appointments))
    }

 */
/*
    @Test
    fun `test on hiring a new vet`() {
        Mockito.`when`(vetsRepo.save(Mockito.any(VetDAO::class.java)))
                .then {
                    val vet:VetDAO = it.getArgument(0)
                    assertThat(vet.id, equalTo(0L))
                    assertThat(vet.name, equalTo(antonio.name))
                    assertThat(vet.email, equalTo(antonio.email))
                    assertThat(vet.username, equalTo(antonio.username))
                    assertThat(vet.password, equalTo(antonio.password))
                    assertThat(vet.cellphone, equalTo(antonio.cellphone))
                    assertThat(vet.address, equalTo(antonio.address))
                    assertThat(vet.photo, equalTo(antonio.photo))
                    assertThat(vet.employeeID, equalTo(antonio.employeeID))
                    assertThat(vet.frozen, equalTo(antonio.frozen))
                    assertThat(vet.appointments, equalTo(antonio.appointments))
                    assertThat(vet.schedules, equalTo(antonio.schedules))
                    vet
                }

        admins.hireVet(VetDAO(0L, antonio.name, antonio.email, antonio.username, antonio.password, antonio.cellphone, antonio.address,antonio.photo, antonio.employeeID, antonio.frozen, antonio.appointments, antonio.schedules))
    }

    @Test(expected = PreconditionFailedException::class)
    fun `test on hiring a new vet (Error)`() {
        admins.hireVet(antonio) // antonio has a non-0 id
    }

    */

    @Test
    fun `test on hiring a new admin`() {
        Mockito.`when`(repo.save(Mockito.any(AdminDAO::class.java)))
                .then {
                    val admin:AdminDAO = it.getArgument(0)
                    assertThat(admin.id, equalTo(0L))
                    assertThat(admin.name, equalTo(cid.name))
                    assertThat(admin.email, equalTo(cid.email))
                    assertThat(admin.username, equalTo(cid.username))
                    assertThat(admin.password, equalTo(cid.password))
                    assertThat(admin.cellphone, equalTo(cid.cellphone))
                    assertThat(admin.address, equalTo(cid.address))
                    assertThat(admin.photo, equalTo(cid.photo))
                    assertThat(admin.employeeID, equalTo(cid.employeeID))
                    admin
                }

        admins.hireAdmin(AdminDAO(0L, cid.name, cid.email, cid.username, cid.password, cid.cellphone, cid.address,cid.photo, cid.employeeID))
    }

    @Test(expected = PreconditionFailedException::class)
    fun `test on hiring a new admin (Error)`() {
        admins.hireAdmin(cid) // antonio has a non-0 id
    }

    /* @Test
     fun `test on firing a vet`() {
         Mockito.`when`(vetsRepo.delete(Mockito.any(VetDAO::class.java)))
                 .then {
                     val vet:VetDAO = it.getArgument(0)
                     assertThat(vet.id, equalTo(0L))
                     assertThat(vet.name, equalTo(antonio.name))
                     assertThat(vet.email, equalTo(antonio.email))
                     assertThat(vet.username, equalTo(antonio.username))
                     assertThat(vet.password, equalTo(antonio.password))
                     assertThat(vet.cellphone, equalTo(antonio.cellphone))
                     assertThat(vet.address, equalTo(antonio.address))
                     assertThat(vet.employeeID, equalTo(antonio.employeeID))
                     assertThat(vet.frozen, equalTo(antonio.frozen))
                     assertThat(vet.appointments, equalTo(antonio.appointments))
                     vet
                 }

         admins.fireVet(0)
     }

    */

    @Test(expected = NotFoundException::class)
    fun `test on firing a vet (Error)`() {
        admins.fireVet(antonio.id) // antonio has a non-0 id
    }





}