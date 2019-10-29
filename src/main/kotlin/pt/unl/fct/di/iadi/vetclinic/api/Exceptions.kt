package pt.unl.fct.di.iadi.vetclinic.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * This file contains the exceptions that are to be converted in
 * HTTP response status. Only to be used in controllers.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class HTTPNotFoundException(s:String) : Exception(s)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class HTTPBadRequestException(s:String) : Exception(s)