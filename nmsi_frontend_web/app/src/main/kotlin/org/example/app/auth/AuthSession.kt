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
    fun getRole(): AppRole? {
        /** Returns the primary application role for the current user (or null if signed out). */
        return currentUser?.appRole
    }

    // PUBLIC_INTERFACE
    fun signInAsDemo(email: String, role: AppRole): User {
        /** Signs in as a demo user with the requested role and returns the created user. */
        val normalizedEmail = email.ifBlank {
            when (role) {
                AppRole.ADMIN -> "admin.demo@nmsi.org"
                AppRole.STAFF -> "staff.demo@nmsi.org"
                AppRole.VOLUNTEER -> "volunteer.demo@nmsi.org"
            }
        }

        val (id, name, roles) = when (role) {
            AppRole.ADMIN -> Triple(
                "u_demo_admin",
                "Demo Admin",
                listOf("ADMIN", "FINANCE", "COORDINATOR"),
            )

            AppRole.STAFF -> Triple(
                "u_demo_staff",
                "Demo Staff",
                listOf("STAFF", "COORDINATOR"),
            )

            AppRole.VOLUNTEER -> Triple(
                "u_demo_volunteer",
                "Demo Volunteer",
                listOf("VOLUNTEER"),
            )
        }

        val user = User(
            id = id,
            name = name,
            email = normalizedEmail,
            appRole = role,
            roles = roles,
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
