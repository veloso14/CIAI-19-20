package pt.unl.fct.di.iadi.vetclinic.model

import org.springframework.data.repository.CrudRepository

interface PetRepository : CrudRepository<PetDAO, Long>