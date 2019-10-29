package pt.unl.fct.di.iadi.vetclinic.services

class NotFoundException(s:String): RuntimeException(s)

class PreconditionFailedException(s:String) : RuntimeException(s)