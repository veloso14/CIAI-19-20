package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.api.UserPasswordDTO
import pt.unl.fct.di.iadi.vetclinic.model.*

@Service
class AdminService(val admins: AdminRepository,
                   val appointments: AppointmentRepository,
                   val pets: PetRepository,
                   val clients: ClientRepository,
                   val vets: VetRepository,
                   val users: UserRepository
) {




    fun getOneAdmin(id: Long): AdminDAO =
            admins.findById(id)
                    .orElseThrow { NotFoundException("There is no admin with Id $id") }

    fun getAllAdmins(): List<AdminDAO> = admins.findAll().toList()


    fun hireAdmin(admin:AdminDAO) =
            when {
                admin.id != 0L -> throw PreconditionFailedException("Id must be 0 in insertion")
                users.findByUsername(admin.username).isPresent -> throw PreconditionFailedException("There is already an user with the specified username")
                else -> {admin.password = BCryptPasswordEncoder().encode(admin.password);users.save(admin)}
            }


    fun fireAdmin(id:Long){
        if(getOneAdmin(id).username != "default"){
            getOneAdmin(id).let { users.delete(it) }
        }
        else
            throw PreconditionFailedException ("You're not able to remove the default account")
    }

    fun updateUser(id: Long, user: AdminDAO) =
            getOneAdmin(id).let { it.update(user); admins.save(it) }

    fun updatePassword(id: Long, pass: UserPasswordDTO) = getOneAdmin(id).let { it.changePassword(BCryptPasswordEncoder().encode(pass.password)); admins.save(it) }


    fun getOneAdminByUsername(username: String): AdminDAO =
            admins.findByUsername(username)
                    .orElseThrow { NotFoundException("There is no Admin with username $username") }

}