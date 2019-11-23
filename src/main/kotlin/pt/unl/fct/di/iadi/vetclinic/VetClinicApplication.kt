package pt.unl.fct.di.iadi.vetclinic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pt.unl.fct.di.iadi.vetclinic.model.*
import java.util.*

@SpringBootApplication
class VetClinicApplication {

    @Bean()
    @Profile("runtime") // to avoid conflicts with mocked repository components
    fun init(
            pets:PetRepository,
            apts: AppointmentRepository,
            clients: ClientRepository,
            vets: VetRepository,
            admins: AdminRepository
    ) =
        CommandLineRunner {
           val vet = VetDAO(1L,"Guilherme","vel@gmail.com","vela",BCryptPasswordEncoder().encode("1234"),987682,"Pio","rosto.jpg",10, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>())
            vets.save(vet)
            val pantufas = PetDAO(1L, "pantufas", "dog",false, emptyList<AppointmentDAO>(), ClientDAO())
            val bigodes = PetDAO(2L, "bigode", "cat",false, emptyList<AppointmentDAO>(), ClientDAO())
            pets.saveAll(listOf(pantufas,bigodes))
            val consulta = AppointmentDAO(3L, Date(),"consulta", pantufas,pantufas.owner, vet)
            apts.save(consulta)

            val veloso = ClientDAO(1L,"Veloso","vel@gmail.com","vela",BCryptPasswordEncoder().encode("1234"),987682,"Pio", emptyList<PetDAO>(), emptyList())
            val antonio = ClientDAO(1L,"Antonio","antonio@gmail.com","tony",BCryptPasswordEncoder().encode("1234"),1234, "Rua Romao", emptyList(), emptyList())
            val chenel = ClientDAO(2L,"Chenel","chenel@gmail.com","chenel",BCryptPasswordEncoder().encode("1234"),1234, "Rua Romao", emptyList(), emptyList())
            clients.saveAll(listOf(veloso,antonio,chenel))

            val defaultAdmin = AdminDAO(1L,"default","","default", BCryptPasswordEncoder().encode("1234"),987682,"Pio","rosto.jpg",1)
            admins.save(defaultAdmin)

        }
}

fun main(args: Array<String>) {
    runApplication<VetClinicApplication>(*args)
}
