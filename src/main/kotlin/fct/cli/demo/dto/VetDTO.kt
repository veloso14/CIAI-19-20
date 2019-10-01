package fct.cli.demo.dto


import fct.cli.demo.dto.users.EmployeeDTO
import java.net.URI

class VetDTO(name:String, username:String, password:String, email:String, cellphone:Long, adress:String, picture: URI): EmployeeDTO( name , username, password , email , cellphone , adress , picture)