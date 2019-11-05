package pt.unl.fct.di.iadi.vetclinic.services


import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*


@Service
class SecurityService(val admins: AdminRepository,
                   val appointments: AppointmentRepository,
                   val pets: PetRepository,
                   val clients: ClientRepository,
                   val vets: VetRepository,
                   val users: UserRepository ) {


   //serve para editar pet e delete pet
    fun canEditPet(user: UserDAO, petID:Long):Boolean {
        var pet = pets.findById(petID)
        return (pet.isPresent && user is ClientDAO && pet.get().owner.id == user.id )
    }

    fun canCompleteAppointment(user: UserDAO, aptID:Long):Boolean{
        var apt = appointments.findById(aptID)
        return (apt.isPresent && user is VetDAO && apt.get().vet.id == user.id)

    }

    //serve para as passwords tb
    fun canGetAllPetsOfClient(user:UserDAO, clientID:Long):Boolean{
        return (user is ClientDAO && user.id == clientID)
    }

    fun canGetAppointmentOfClient(user:UserDAO, clientID:Long):Boolean{
        return (user is ClientDAO && user.id == clientID)
    }

    //serve para as passwords tb
    fun canEditVet(user:UserDAO, clientID:Long):Boolean{
        return (user is VetDAO && user.id == clientID)
    }

    //serve para as passwords tb
    fun canEditAdmin(user:UserDAO, clientID:Long):Boolean{
        return (user is AdminDAO && user.id == clientID)
    }

    //serve para as passwords tb
    fun canEditClient(user:UserDAO, clientID:Long):Boolean{
        return (user is ClientDAO && user.id == clientID)
    }






}