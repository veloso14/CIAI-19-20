package fct.cli.demo.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class PetDAO(
        @Id val id:Long,
        val name: String,
        val species : String)
