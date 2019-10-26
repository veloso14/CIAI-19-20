package pt.unl.fct.di.iadi.vetclinic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import pt.unl.fct.di.iadi.vetclinic.model.*
import java.time.LocalDateTime
import java.util.*

@SpringBootApplication
class VetClinicApplication {

    @Bean()
    @Profile("runtime") // to avoid conflicts with mocked repository components
    fun init(
            pets:PetRepository,
            apts: AppointmentRepository
    ) =
        CommandLineRunner {

            val pantufas = PetDAO(1L, "pantufas", "dog", emptyList<AppointmentDAO>(), emptyList<String>(), ClientDAO())
            val bigodes = PetDAO(2L, "bigode", "cat", emptyList<AppointmentDAO>(), emptyList<String>(), ClientDAO())
            pets.saveAll(listOf(pantufas,bigodes))
            val consulta = AppointmentDAO(3L, LocalDateTime.MIN, LocalDateTime.MAX,"consulta",false, pantufas,ClientDAO(), VetDAO())
            apts.save(consulta)
        }
}

fun main(args: Array<String>) {
    runApplication<VetClinicApplication>(*args)
}
