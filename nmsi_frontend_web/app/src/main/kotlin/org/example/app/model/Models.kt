package org.example.app.model

/**
 * Data models used by the Mission Management UI.
 */

import org.example.app.auth.AppRole

data class User(
    val id: String,
    val name: String,
    val email: String,
    /**
     * Primary application role used for navigation & section access in this demo app.
     * (ADMIN / STAFF / VOLUNTEER)
     */
    val appRole: AppRole,
    /**
     * Additional role labels for future expansion (e.g. FINANCE, COORDINATOR).
     * Not yet used for granular permissions in this demo, but kept to preserve existing structure.
     */
    val roles: List<String>,
)

data class Donation(
    val id: String,
    val donorName: String,
    val amountInr: Long,
    val dateIso: String,
    val status: String,
)

data class Volunteer(
    val id: String,
    val name: String,
    val region: String,
    val phone: String,
)

data class MissionProject(
    val id: String,
    val name: String,
    val region: String,
    val status: String,
)

data class ActivityItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val timestampIso: String,
)

data class OverviewStats(
    val donationsMtdInr: Long,
    val activeVolunteers: Int,
    val projects: Int,
    val pendingTasks: Int,
)
