package org.example.app.model

/**
 * Data models used by the Mission Management UI.
 */

data class User(
    val id: String,
    val name: String,
    val email: String,
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
