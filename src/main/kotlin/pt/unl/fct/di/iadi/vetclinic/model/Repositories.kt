package pt.unl.fct.di.iadi.vetclinic.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Month
import java.util.*

interface PetRepository : JpaRepository<PetDAO, Long> {

    // Query defined by adopting a conventional name construction
    fun findByName(name:String): MutableIterable<PetDAO>

    // A query that loads all Pets with prefetching of the appointments associated
    @Query("select p from PetDAO p left join fetch p.appointments where p.id = :id")
    fun findByIdWithAppointment(id:Long) : Optional<PetDAO>

    @Query("select p from PetDAO p  where  p.frozen = false")
    fun findAllByFrozenFalse():List<PetDAO>

}

interface AppointmentRepository: JpaRepository<AppointmentDAO, Long>

interface UserRepository : JpaRepository<UserDAO, Long> {
    fun findByUsername(username: String) : Optional<UserDAO>
}

interface ClientRepository : JpaRepository<ClientDAO, Long> {
    //fun findByName(name:String): MutableIterable<ClientDAO>

    @Query("select c from ClientDAO c inner join fetch c.appointments where c.id = :id")
    fun findByIdWithAppointment(id: Long): Optional<ClientDAO>

    @Query("select c from ClientDAO c inner join fetch c.pets where c.id = :id")
    fun findByIdWithPet(id: Long): Optional<ClientDAO>
}

interface VetRepository : JpaRepository<VetDAO, Long> {
   @Query("select c from VetDAO c inner join fetch c.appointments where c.id = :id")
    fun findByIdWithAppointment(id: Long): Optional<VetDAO>

    @Query("select c from VetDAO c  where  c.frozen = false")
    fun findAllByFrozenFalse():List<VetDAO>

    fun findByUsername(username: String) : Optional<VetDAO>
}

interface AdminRepository : JpaRepository<AdminDAO, Long> {
    fun findByUsername(username: String) : Optional<AdminDAO>
}

@Repository
interface ScheduleRepository : JpaRepository<ScheduleDAO, Long> {

    @Query("SELECT s FROM ScheduleDAO s  where s.vet = :vet and s.month = :month ")
    fun findByVetAndMonth( @Param("vet") vet: VetDAO,  @Param("month") month: Month) : Optional<ScheduleDAO>

    @Query("SELECT s FROM ScheduleDAO s  where s.vet = :vet")
    fun findByVet(@Param("vet") vet: VetDAO) : List<ScheduleDAO>

}

interface  ShiftRepository : JpaRepository<ShiftDAO, Long>