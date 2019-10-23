package pt.unl.fct.di.iadi.vetclinic.services

import org.springframework.stereotype.Service
import pt.unl.fct.di.iadi.vetclinic.model.BackListDAO
import pt.unl.fct.di.iadi.vetclinic.model.BlaclListRepository

@Service
class BlackListService(val lista: BlaclListRepository) {

    fun getAllKeys(): List<BackListDAO> = lista.findAll().toList()

    fun addKey(client: BackListDAO) {
        lista.save(client)
    }

    fun checkKey(id: String): Boolean {
        return lista.existsById(id)
    }


}