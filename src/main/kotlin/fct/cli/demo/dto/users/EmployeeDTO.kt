package fct.cli.demo.dto.users

import java.net.URI

 open class EmployeeDTO( name:String ,  username:String , password:String , email:String , cellphone:Long , adress:String, val picture: URI): UserDTO( name , username, password , email , cellphone , adress)