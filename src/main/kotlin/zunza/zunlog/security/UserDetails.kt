package zunza.zunlog.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import zunza.zunlog.model.User

class UserDetailss(
    private val user: User,
): UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(GrantedAuthority { user.role })
    }

    override fun getPassword(): String {
        return user.getPassword()
    }

    override fun getUsername(): String {
        return user.email
    }

    fun getUserId(): Long {
        return user.id
    }

    fun getUser(): User {
        return user
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
