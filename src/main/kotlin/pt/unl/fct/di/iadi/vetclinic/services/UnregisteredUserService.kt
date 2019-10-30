package pt.unl.fct.di.iadi.vetclinic.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.*

@Service
class UnregisteredUserService(
        val users: UserRepository,
        val vets: VetRepository,

        val admins: AdminRepository

) {

    val logger = LoggerFactory.getLogger(UnregisteredUserService::class.java)

    fun login(username:String, password:String){}

    fun getAllAdmins(): List<AdminDAO> = admins.findAll().toList()

    fun getAllVets(): List<VetDAO> = vets.findAll().toList()
}