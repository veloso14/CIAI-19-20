/**
Copyright 2019 João Costa Seco, Eduardo Geraldo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package pt.unl.fct.di.iadi.vetclinic.api
import pt.unl.fct.di.iadi.vetclinic.services.NotFoundException
import pt.unl.fct.di.iadi.vetclinic.services.PreconditionFailedException

/**
 * This function is used to wrap the implementation of a controller
 * and convert the inner exceptions used in services to exceptions
 * used in the api layer. This can also be implemented using Spring
 * exception handlers.
 */
fun <T> handle4xx(inner: () -> T): T =
        try {
            inner()

        }
        catch (e: IllegalArgumentException) {
            throw HTTPNotFoundException(e.message ?: "Bad Request")
        }
        catch (e: NotFoundException) {
            throw HTTPNotFoundException(e.message ?: "Not Found")
        } catch (e : PreconditionFailedException ) {
            throw HTTPBadRequestException(e.message ?: "Bad Request")
        }
