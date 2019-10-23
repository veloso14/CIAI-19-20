package pt.unl.fct.di.iadi.vetclinic.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class HTTPNotFoundException(s: String) : Exception(s)

class NotFoundException(s:String): RuntimeException(s)

class PreconditionFailedException(s:String) : RuntimeException(s)