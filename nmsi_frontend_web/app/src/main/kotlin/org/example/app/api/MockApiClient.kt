package org.example.app.api

import org.example.app.auth.AppRole
import org.example.app.model.ActivityItem
import org.example.app.model.Donation
import org.example.app.model.MissionProject
import org.example.app.model.OverviewStats
import org.example.app.model.Volunteer
import kotlin.random.Random

/**
 * Mock-backed API client.
 *
 * This replaces backend REST calls until a backend is available.
 * All methods are synchronous (called from a background thread) for simplicity.
 */
class MockApiClient {

    private fun simulateLatency() {
        try {
            Thread.sleep(250)
        } catch (_: InterruptedException) {
            // ignore
        }
    }

    // PUBLIC_INTERFACE
    fun getOverviewStats(): OverviewStats {
        /** Returns overview stats for the dashboard. */
        simulateLatency()
        return OverviewStats(
            donationsMtdInr = 125000L,
            activeVolunteers = 84,
            projects = 12,
            pendingTasks = 7,
        )
    }

    // PUBLIC_INTERFACE
    fun getRecentActivity(): List<ActivityItem> {
        /** Returns a recent activity feed for the overview page. */
        simulateLatency()
        return listOf(
            ActivityItem("a1", "Donation received", "₹5,000 from Joseph (Kerala)", "2026-03-18T06:00:00Z"),
            ActivityItem("a2", "Volunteer onboarded", "Anita assigned to Chennai region", "2026-03-17T12:10:00Z"),
            ActivityItem("a3", "Project update", "School supplies drive marked In Progress", "2026-03-17T08:00:00Z"),
        )
    }

    // PUBLIC_INTERFACE
    fun listDonations(role: AppRole? = AppRole.ADMIN): List<Donation> {
        /** Returns donation list items, filtered by role in demo mode. */
        simulateLatency()

        // Volunteers should not see finance data.
        if (role == AppRole.VOLUNTEER) return emptyList()

        return listOf(
            Donation("d1", "Joseph", 5000, "2026-03-18", "Completed"),
            Donation("d2", "Maria", 12000, "2026-03-16", "Completed"),
            Donation("d3", "Anonymous", 2500, "2026-03-14", "Pending"),
        )
    }

    // PUBLIC_INTERFACE
    fun listVolunteers(): List<Volunteer> {
        /** Returns volunteer list items. */
        simulateLatency()
        return listOf(
            Volunteer("v1", "Anita", "Chennai", "+91-90000-00001"),
            Volunteer("v2", "Thomas", "Kochi", "+91-90000-00002"),
            Volunteer("v3", "Rahul", "Bengaluru", "+91-90000-00003"),
        )
    }

    // PUBLIC_INTERFACE
    fun listProjects(): List<MissionProject> {
        /** Returns mission project list items. */
        simulateLatency()
        return listOf(
            MissionProject("p1", "School Supplies Drive", "Tamil Nadu", "In Progress"),
            MissionProject("p2", "Medical Camp", "Kerala", "Planned"),
            MissionProject("p3", "Community Kitchen", "Karnataka", "Active"),
        )
    }

    // PUBLIC_INTERFACE
    fun listDonorCrm(role: AppRole? = AppRole.ADMIN): List<ActivityItem> {
        /** Returns donor CRM items (mocked as activity-like entries), filtered by role in demo mode. */
        simulateLatency()

        if (role == AppRole.VOLUNTEER) return emptyList()

        return listOf(
            ActivityItem("c1", "Follow-up due", "Call Maria about recurring donation", "2026-03-18T09:30:00Z"),
            ActivityItem("c2", "Thank-you note", "Send receipt to Joseph", "2026-03-18T10:00:00Z"),
        )
    }

    // PUBLIC_INTERFACE
    fun listReports(role: AppRole? = AppRole.ADMIN): List<ActivityItem> {
        /** Returns report items, filtered by role in demo mode. */
        simulateLatency()

        if (role == AppRole.VOLUNTEER) return emptyList()

        val month = Random.nextInt(1, 12).toString().padStart(2, '0')
        return listOf(
            ActivityItem("r1", "Monthly Donations Report", "2026-$month", "2026-03-18T00:00:00Z"),
            ActivityItem("r2", "Volunteer Activity Report", "Last 30 days", "2026-03-18T00:00:00Z"),
        )
    }
}
