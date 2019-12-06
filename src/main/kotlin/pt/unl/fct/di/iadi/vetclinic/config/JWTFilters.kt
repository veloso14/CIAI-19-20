package pt.unl.fct.di.iadi.vetclinic.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import pt.unl.fct.di.iadi.vetclinic.model.ClientDAO
import pt.unl.fct.di.iadi.vetclinic.model.UserSecurityDAO
import pt.unl.fct.di.iadi.vetclinic.services.UserService
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap


object JWTSecret {
    private const val passphrase = "este é um grande segredo que tem que ser mantido escondido"
    val KEY: String = Base64.getEncoder().encodeToString(passphrase.toByteArray())
    const val SUBJECT = "JSON Web Token for CIAI 2019/20"
    const val VALIDITY = 1000 * 60 * 60 * 10 * 10 // 100 minutes in microseconds
}

private fun addResponseToken(authentication: Authentication, response: HttpServletResponse) {

    val claims = HashMap<String, Any?>()
    claims["username"] = authentication.name
    claims["roles"]= authentication.authorities

    val token = Jwts
            .builder()
            .setClaims(claims)
            .setSubject(JWTSecret.SUBJECT)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWTSecret.VALIDITY))
            .signWith(SignatureAlgorithm.HS256, JWTSecret.KEY)
            .compact()

    response.addHeader("Authorization", "Bearer $token")
}

class UserPasswordAuthenticationFilterToJWT (
        defaultFilterProcessesUrl: String?,
        private val anAuthenticationManager: AuthenticationManager,
        val users: UserService
) : AbstractAuthenticationProcessingFilter(defaultFilterProcessesUrl) {

    override fun attemptAuthentication(request: HttpServletRequest?,
                                       response: HttpServletResponse?): Authentication? {
        //getting user from request body
        val user = ObjectMapper().readValue(request!!.inputStream, UserSecurityDAO::class.java)

        // perform the "normal" authentication
        val auth = anAuthenticationManager.authenticate(UsernamePasswordAuthenticationToken(user.username, user.password ,users.getAuthorities(user.username) ))

        return if (auth.isAuthenticated) {
            // Proceed with an authenticated user
            SecurityContextHolder.getContext().authentication = auth
            auth
        } else
            null
    }

    override fun successfulAuthentication(request: HttpServletRequest,
                                          response: HttpServletResponse,
                                          filterChain: FilterChain?,
                                          auth: Authentication) {

        // When returning from the Filter loop, add the token to the response
        addResponseToken(auth, response)
    }
}

class UserAuthToken(private var login:String, private var authorities:MutableCollection<out GrantedAuthority> ) : Authentication {

    override fun getAuthorities() = authorities
    //override fun getAuthorities() = null

    override fun setAuthenticated(isAuthenticated: Boolean) {}

    override fun getName() = login

    override fun getCredentials() = null

    override fun getPrincipal() = this

    override fun isAuthenticated() = true

    override fun getDetails() = login
}

class JWTAuthenticationFilter(): GenericFilterBean() {


    override fun doFilter(request: ServletRequest?,
                          response: ServletResponse?,
                          chain: FilterChain?) {

        val authHeader = (request as HttpServletRequest).getHeader("Authorization")

        if( authHeader != null && authHeader.startsWith("Bearer ") ) {
            val token = authHeader.substring(7) // Skip 7 characters for "Bearer "
            val claims = Jwts.parser().setSigningKey(JWTSecret.KEY).parseClaimsJws(token).body

            // should check for token validity here (e.g. expiration date, session in db, etc.)
            val exp = (claims["exp"] as Int).toLong()
            if ( exp < System.currentTimeMillis()/1000) // in seconds

                (response as HttpServletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED) // RFC 6750 3.1

            else {

                //val userDetails :UserDetails = customUserDetailsService.loadUserById(userId)
                //var authentication: UsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                val roles = claims.get("roles")

                val authentication = UserAuthToken(claims["username"] as String, getAuthorities(claims))

                // Can go to the database to get the actual user information (e.g. authorities)

                SecurityContextHolder.getContext().authentication = authentication

                // Renew token with extended time here. (before doFilter)
                addResponseToken(authentication, response as HttpServletResponse)

                chain!!.doFilter(request, response)
            }
        } else {
            chain!!.doFilter(request, response)
        }



    }

    private fun getAuthorities(map: Map<String, *>): MutableCollection<GrantedAuthority> {

        val authorities = map["roles"]
        return if (authorities is String) {
            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
        } else if (authorities is MutableCollection<*>) {
            AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.collectionToCommaDelimitedString(authorities))
        } else {
            throw IllegalArgumentException("Authorities must be either a String or a Collection")
        }
    }
}


class UserPasswordSignUpFilterToJWT (
        defaultFilterProcessesUrl: String?,
        private val users: UserService
) : AbstractAuthenticationProcessingFilter(defaultFilterProcessesUrl) {

    override fun attemptAuthentication(request: HttpServletRequest?,
                                       response: HttpServletResponse?): Authentication? {
        //getting user from request body
        val user = ObjectMapper().readValue(request!!.inputStream, ClientDAO::class.java)
        var authorities : MutableList<GrantedAuthority>  = mutableListOf()
        authorities.add(SimpleGrantedAuthority("CLIENT"))

        return users
                .addUser(user)
                .orElse( null )
                .let {
                    val auth = UserAuthToken(user.username, authorities )
                    SecurityContextHolder.getContext().authentication = auth
                    auth
                }
    }

    override fun successfulAuthentication(request: HttpServletRequest,
                                          response: HttpServletResponse,
                                          filterChain: FilterChain?,
                                          auth: Authentication) {

        addResponseToken(auth, response)
    }
}
