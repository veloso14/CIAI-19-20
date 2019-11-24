package pt.unl.fct.di.iadi.vetclinic

import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pt.unl.fct.di.iadi.vetclinic.model.*
import pt.unl.fct.di.iadi.vetclinic.services.PetService
import java.util.*
import jdk.nashorn.internal.objects.NativeArray.forEach



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
           val vet = VetDAO(1L,"Guilherme","vel@gmail.com","vela",BCryptPasswordEncoder().encode("1234"),987682,"Pio","rosto.jpg","VET",10, false, emptyList<AppointmentDAO>(), emptyList<ScheduleDAO>())
            vets.save(vet)
            val pantufas = PetDAO(2L, "pantufas", "dog",false, emptyList<AppointmentDAO>(), ClientDAO())
            val bigodes = PetDAO(3L, "bigode", "cat",false, emptyList<AppointmentDAO>(), ClientDAO())
            pets.saveAll(listOf(pantufas,bigodes))
            val consulta = AppointmentDAO(4L, Date(),"consulta", pantufas,pantufas.owner, vet)
            apts.save(consulta)

            val logger = LoggerFactory.getLogger(PetService::class.java)
            pets.findAll().forEach { pet -> logger.info("{}", pet) }

            val veloso = ClientDAO(5L,"Veloso","vel@gmail.com","vela",BCryptPasswordEncoder().encode("1234"),987682,"Pio", emptyList<PetDAO>(), emptyList())
            val antonio = ClientDAO(6L,"Antonio","antonio@gmail.com","tony",BCryptPasswordEncoder().encode("1234"),1234, "Rua Romao", emptyList(), emptyList())
            val chenel = ClientDAO(7L,"Chenel","chenel@gmail.com","chenel",BCryptPasswordEncoder().encode("1234"),1234, "Rua Romao", emptyList(), emptyList())
            clients.saveAll(listOf(veloso,antonio,chenel))

            val defaultAdmin = AdminDAO(8L,"default","","default", BCryptPasswordEncoder().encode("1234"),987682,"Pio","rosto.jpg","ADMIN",1)
            admins.save(defaultAdmin)

        }
}

fun main(args: Array<String>) {
    runApplication<VetClinicApplication>(*args)
}
