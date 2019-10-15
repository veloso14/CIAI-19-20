package pt.unl.fct.di.iadi.vetclinic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VetClinicApplication

fun main(args: Array<String>) {
    runApplication<VetClinicApplication>(*args)
}
