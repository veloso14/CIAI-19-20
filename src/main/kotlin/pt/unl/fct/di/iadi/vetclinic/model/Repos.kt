package pt.unl.fct.di.iadi.vetclinic.model

import org.springframework.data.repository.CrudRepository

interface PetRepository : CrudRepository<PetDAO, Long>

interface AppointRepository  : CrudRepository<AppointmentDAO, Long>

interface UserRepository  : CrudRepository<UserDAO, Long>