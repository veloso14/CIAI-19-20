package pt.unl.fct.di.iadi.vetclinic

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentDAO
import pt.unl.fct.di.iadi.vetclinic.model.AppointmentRepository
import pt.unl.fct.di.iadi.vetclinic.model.PetDAO
import pt.unl.fct.di.iadi.vetclinic.model.PetRepository
import java.time.LocalDateTime
import java.util.*

@SpringBootApplication
class VetClinicApplication {

    @Bean
    fun init(
            pets:PetRepository,
            apts:AppointmentRepository
    ) = CommandLineRunner {
        val pantufas = PetDAO(1L, "pantufas", "Dog", emptyList())
        val bigodes = PetDAO(2L, "bigodes", "Cat", emptyList())
        val petsDAO = mutableListOf(pantufas, bigodes);
        pets.saveAll(petsDAO)
        val apt = AppointmentDAO(1L, LocalDateTime.now(), LocalDateTime.now(), "consulta", pantufas)
        apts.save(apt)
    }
}

fun main(args: Array<String>) {
    runApplication<VetClinicApplication>(*args)
}
