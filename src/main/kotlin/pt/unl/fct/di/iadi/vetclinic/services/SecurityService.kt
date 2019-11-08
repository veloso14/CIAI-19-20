package pt.unl.fct.di.iadi.vetclinic.services


import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*
import java.util.*


@Service
class SecurityService(val admins: AdminRepository,
                   val appointments: AppointmentRepository,
                   val pets: PetRepository,
                   val clients: ClientRepository,
                   val vets: VetRepository,
                   val users: UserRepository ) {

    //TODO ve isto sff
    fun addUser(newUser : UserDAO): Optional<UserDAO> {
            val aUser = users.findByUsername(newUser.username)

            return if ( aUser.isPresent )
                Optional.empty()
            else {
                newUser.password = BCryptPasswordEncoder().encode(newUser.password)
                Optional.of(users.save(newUser))
            }
        }


   //serve para editar pet e delete pet
   //TODO isto não é preciso o spring faz nos roles
    fun canEditPet(principal: UserDetails, petID:Long):Boolean {
        var pet = pets.findById(petID)
        return (pet.isPresent  && pet.get().owner.username == principal.username )
    }

    //TODO isto não é preciso o spring faz nos roles
    fun canCompleteAppointment(principal: UserDetails, aptID:Long):Boolean{
        var apt = appointments.findById(aptID)
        return (apt.isPresent  && apt.get().vet.username == principal.username)

    }

    //serve para as passwords tb
    //TODO isto não é preciso o spring faz nos roles
    fun canGetAllPetsOfClient(principal: UserDetails, clientID:Long):Boolean{
        var client = clients.findById(clientID)
        return client.isPresent  && client.get().username == principal.username
    }

    //TODO isto não é preciso o spring faz nos roles
    fun canGetAppointmentOfClient(principal: UserDetails, clientID:Long):Boolean{
        var client = clients.findById(clientID)
        return client.isPresent  && client.get().username == principal.username
    }

    //serve para as passwords tb
    //TODO isto não é preciso o spring faz nos roles
    fun canEditVet(principal: UserDetails, vetID:Long):Boolean{
        var vet = vets.findById(vetID)
        return vet.isPresent  && vet.get().username == principal.username
    }

    //serve para as passwords tb
    //TODO isto não é preciso o spring faz nos roles
    fun canEditAdmin(principal: UserDetails, adminID:Long):Boolean{
        var admin = admins.findById(adminID)
        return admin.isPresent  && admin.get().username == principal.username
    }

    //serve para as passwords tb
    //TODO isto não é preciso o spring faz nos roles
    fun canEditClient(principal: UserDetails, clientID:Long):Boolean{
        var client = clients.findById(clientID)
        return client.isPresent  && client.get().username == principal.username
    }

    //Isto tem que ser testado pk nao sei se funciona
    //mas poupava as 3 funçoes anteriores, ficava so esta
    //TODO isto não é preciso o spring faz nos roles
    fun canEdit(principal: UserDetails, id:Long):Boolean{
        var user = users.findById(id)
        return user.isPresent  && user.get().username == principal.username
    }






}