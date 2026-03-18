package org.example.app.auth

import org.example.app.model.User

/**
 * Role model + authorization helpers for the demo app.
 *
 * This app runs with mock auth only (no backend), so roles are derived from the demo sign-in selection
 * and used to gate UI navigation and section access.
 */
enum class AppRole {
    ADMIN,
    STAFF,
    VOLUNTEER,
}

/**
 * Section-level permissions used by the sidebar navigation and screens.
 *
 * Keep this conservative: if a role can't access a section, we hide it in navigation and block direct navigation.
 */
enum class AppSection {
    OVERVIEW,
    DONATIONS,
    VOLUNTEERS,
    PROJECTS,
    DONOR_CRM,
    REPORTS,
    SETTINGS,
}

object RoleAccess {

    // PUBLIC_INTERFACE
    fun primaryRole(user: User?): AppRole? {
        /** Returns the user's primary app role (Admin/Staff/Volunteer) if available. */
        if (user == null) return null
        return user.appRole
    }

    // PUBLIC_INTERFACE
    fun canAccessSection(role: AppRole?, section: AppSection): Boolean {
        /** Returns true if a role may access a section. Null role means unauthenticated -> no access. */
        if (role == null) return false

        return when (role) {
            AppRole.ADMIN -> true // Admin sees everything.

            AppRole.STAFF -> when (section) {
                AppSection.OVERVIEW,
                AppSection.DONATIONS,
                AppSection.VOLUNTEERS,
                AppSection.PROJECTS,
                AppSection.DONOR_CRM,
                AppSection.REPORTS,
                -> true

                // Restrict "Settings" to Admin to demonstrate role separation.
                AppSection.SETTINGS -> false
            }

            AppRole.VOLUNTEER -> when (section) {
                // Volunteer: mission execution oriented (projects + volunteers) + overview.
                AppSection.OVERVIEW,
                AppSection.VOLUNTEERS,
                AppSection.PROJECTS,
                -> true

                // No finance/CRM/reports/settings for volunteers in this demo.
                AppSection.DONATIONS,
                AppSection.DONOR_CRM,
                AppSection.REPORTS,
                AppSection.SETTINGS,
                -> false
            }
        }
    }

    // PUBLIC_INTERFACE
    fun visibleSections(role: AppRole?): List<AppSection> {
        /** Returns the list of sections that should be visible in the sidebar for a role. */
        return AppSection.entries.filter { canAccessSection(role, it) }
    }
}
