package fct.cli.demo.repository
import fct.cli.demo.model.PetDAO
import org.springframework.data.repository.CrudRepository


interface PetRepository : CrudRepository<PetDAO, Long>