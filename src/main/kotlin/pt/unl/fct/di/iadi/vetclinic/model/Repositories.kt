package pt.unl.fct.di.iadi.vetclinic.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PetRepository : JpaRepository<PetDAO, Long> {

    // Query defined by adopting a conventional name construction
    fun findByName(name:String): MutableIterable<PetDAO>

    // A query that loads all Pets with prefetching of the appointments associated
    @Query("select p from PetDAO p left join fetch p.appointments where p.id = :id")
    fun findByIdWithAppointment(id:Long) : Optional<PetDAO>
}

interface AppointmentRepository: JpaRepository<AppointmentDAO, Long>
interface VetScheduleRepository : JpaRepository<VetScheduleDAO, Long> {
    @Query("select s from VetScheduleDAO s where s.vet.name = :id")
    fun findByVetId(id:String) : Optional <VetScheduleDAO>
}
interface UserRepository : JpaRepository<UserDAO, String> {
    fun findByUsername(username: String) : UserDAO
}

interface BlaclListRepository : JpaRepository<BackListDAO, String> {}
interface VetRepository : JpaRepository<VetDAO, String> {
    @Query("select c from VetDAO c inner join fetch c.appointments where c.name = :name")
    fun findByIdWithAppointment(name: String): Optional<VetDAO>
}
interface AdminRepository : JpaRepository<AdminDAO, String> {}
interface ClientRepository : JpaRepository<ClientDAO, String> {
    @Query("select c from ClientDAO c inner join fetch c.appointments where c.name = :name")
    fun findByIdWithAppointment(name: String): Optional<ClientDAO>
}

