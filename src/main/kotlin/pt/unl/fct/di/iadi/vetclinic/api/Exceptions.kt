package pt.unl.fct.di.iadi.vetclinic.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(s:String) : RuntimeException(s)