package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*

@Service
class AdminService(val admins: AdminRepository,
                 val appointments: AppointmentRepository,
                 val pets: PetRepository,
                 val clients: ClientRepository,
                 val vets: VetRepository,
                 val users: UserRepository,
                 val vetService: VetService
                   ) {




    fun getOneAdmin(id: Long): AdminDAO =
            admins.findById(id)
                    .orElseThrow { NotFoundException("There is no admin with Id $id") }

    fun getAllAdmins(): List<AdminDAO> = admins.findAll().toList()

    /*
    fun hireVet(vet:VetDAO) =
        if (vet.id != 0L)
            throw PreconditionFailedException("Id must be 0 in insertion")
        else vets.save(vet)
     */

  /*  fun hireAdmin(admin:AdminDAO) {
        val aUser = users.findByUsername(admin.username)
         if ( aUser.isPresent ) throw PreconditionFailedException("There is already an user with the specified username")
        else {
            //vet.password = BCryptPasswordEncoder().encode(user.password)
            admin.password = admin.password
            admins.save(admin)
        }
    }
   */

    fun hireAdmin(admin:AdminDAO) =
            when {
                admin.id != 0L -> throw PreconditionFailedException("Id must be 0 in insertion")
                users.findByUsername(admin.username).isPresent -> throw PreconditionFailedException("There is already an user with the specified username")
                else -> {admin.password = BCryptPasswordEncoder().encode(admin.password);admins.save(admin)}
            }



    //fun findEmployee(id: Long): UserDAO = users.findById(id).orElseThrow { NotFoundException("There is no user with Id $id") }
    fun findAdmin(id:Long):AdminDAO = admins.findById(id).orElseThrow { NotFoundException("There is no user with Id $id") }
 
    /* apagar
    fun fireVet(id:Long){
        val user = findEmployee(id)
        if (user is VetDAO){
            user.updateFrozen(true)
            vets.save(user)
        }
    }

     */

    fun fireAdmin(id:Long){
        if(id != 1L){
            findAdmin(id).let { admins.delete(it) }
        }
        else
            throw PreconditionFailedException ("You're not able to remove the default account")
    }

    fun updateUser(id: Long, user: AdminDAO) =
            getOneAdmin(id).let { it.update(user); admins.save(it) }

    fun updatePassword(id: Long, password: String) = getOneAdmin(id).let { it.changePassword(BCryptPasswordEncoder().encode(password)); admins.save(it) }


}