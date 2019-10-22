package pt.unl.fct.di.iadi.vetclinic.model

import com.sun.istack.NotNull
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import pt.unl.fct.di.iadi.vetclinic.api.AppointmentDTO
import pt.unl.fct.di.iadi.vetclinic.api.PetDTO
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
data class PetDAO(
        @Id @GeneratedValue val id:Long,
                            var name: String,
                            var species: String,
        @OneToMany(mappedBy = "pet")
                            var appointments:List<AppointmentDAO>,
       /* @ManyToOne(fetch = FetchType.LAZY , optional = false)
        @JoinColumn(name = "owner_id" , nullable = false)
        @OnDelete(action = OnDeleteAction.CASCADE)
        var owner:ClientDAO */

        @ElementCollection
                             var notes:List<String>
        //@ManyToOne(fetch = FetchType.LAZY , optional = false)
     //   @JoinColumn(name = "owner_id" , nullable = false)
       // @OnDelete(action = OnDeleteAction.CASCADE)
      //  var owner:ClientDAO

) {
    constructor(pet: PetDTO, apts:List<AppointmentDAO>,notes:List<String>/*, owner:ClientDAO*/) : this(pet.id,pet.name,pet.species, apts, notes/*, owner*/)

    fun update(other:PetDAO) {
        this.name = other.name
        this.species = other.species
        this.appointments = other.appointments
        this.notes = other.notes
        //this.owner = other.owner
    }
}

@Entity
data class AppointmentDAO(
        @Id @GeneratedValue val id:Long,
                            var start: LocalDateTime,
                            var end:LocalDateTime,
                            var desc:String,
        @ManyToOne(fetch = FetchType.LAZY , optional = false)
        @JoinColumn(name = "pet_id" , nullable = false)
        @OnDelete(action = OnDeleteAction.CASCADE)
                            var pet:PetDAO

) {

    constructor(apt: AppointmentDTO, pet:PetDAO) : this(apt.id, apt.start, apt.end, apt.desc, pet)
}

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
open class UserDAO( @Id @GeneratedValue val id:Long,
                    var name:String,
                    @NotNull
                    @Column(name = "u_email" , unique = true)
                   var  email :String,
                    @NotNull
                    @Column(name = "u_username" , unique = true)
                    var  username:String ,
                    @NotNull
                    @Column(name = "u_pass")
                    var password:String,
                    @NotNull
                    @Column(name = "u_cellphone")
                    var cellphone:Long,  @NotNull
                    @Column(name = "u_address")
                    var address:String){
    constructor(user: UserDAO) : this(user.id, user.name,  user.email,  user.username , user.password, user.cellphone, user.address)


}

@Entity
class ClientDAO(id: Long,
                name: String,
                email :String,
                username:String ,
                password:String,
                cellphone:Long,
                address:String/*,
                @OneToMany()
                var pets:List<PetDAO>*/):UserDAO(id,name, email ,  username , password, cellphone, address){
    constructor(client: ClientDAO/*, pets:List<PetDAO>*/) : this(client.id, client.name,  client.email,  client.username , client.password, client.cellphone, client.address/*, pets*/)
    //val pets:MutableList<PetDAO> = mutableListOf()
}

// val picture: URI
@Entity
class VetDAO(id: Long,name: String, email :String,  username:String , password:String, cellphone:Long, address:String, var employeeID:Long):UserDAO(id,name, email ,  username , password, cellphone, address){
    constructor(vet: VetDAO) : this(vet.id, vet.name,  vet.email,  vet.username , vet.password, vet.cellphone, vet.address, vet.employeeID)
   // val appointments:MutableList<AppointmentDAO> = mutableListOf()
}


@Entity
class AdminDAO(id: Long,name: String, email :String,  username:String , password:String, cellphone:Long, address:String,var employeeID:Long):UserDAO(id,name, email ,  username , password, cellphone, address){
    constructor(admin: AdminDAO) : this(admin.id, admin.name,  admin.email,  admin.username , admin.password, admin.cellphone, admin.address, admin.employeeID)
}


