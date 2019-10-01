package fct.cli.demo.dto.users


import java.net.URI

 class AdminDTO(name:String, username:String, password:String, email:String, cellphone:Long, adress:String, picture: URI): EmployeeDTO( name , username, password , email , cellphone , adress , picture)