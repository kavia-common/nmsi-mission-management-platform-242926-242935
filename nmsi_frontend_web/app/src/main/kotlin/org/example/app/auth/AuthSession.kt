package org.example.app.auth

import org.example.app.model.User

/**
 * Simple in-memory auth session for demo/mock mode.
 *
 * In a real app this would integrate with backend tokens + encrypted storage.
 */
object AuthSession {
    private var currentUser: User? = null

    // PUBLIC_INTERFACE
    fun isSignedIn(): Boolean {
        /** Returns whether a user is signed in for this app process. */
        return currentUser != null
    }

    // PUBLIC_INTERFACE
    fun getUser(): User? {
        /** Returns current signed-in user (if any). */
        return currentUser
    }

    // PUBLIC_INTERFACE
    fun signInAsDemo(email: String): User {
        /** Signs in as a demo user and returns the created user. */
        val user = User(
            id = "u_demo",
            name = "Demo Admin",
            email = email.ifBlank { "demo@nmsi.org" },
            roles = listOf("ADMIN", "FINANCE", "COORDINATOR"),
        )
        currentUser = user
        return user
    }

    // PUBLIC_INTERFACE
    fun signOut() {
        /** Signs out the current user. */
        currentUser = null
    }
}
