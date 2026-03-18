package org.example.app

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import org.example.app.auth.AppSection
import org.example.app.auth.AuthSession
import org.example.app.auth.RoleAccess
import org.example.app.ui.ListFragment
import org.example.app.ui.LoginFragment
import org.example.app.ui.OverviewFragment

/**
 * App entry point.
 *
 * Hosts a sidebar-driven navigation shell with auth scaffolding and mock-backed data screens.
 * Navigation visibility and access are role-based (Admin/Staff/Volunteer) in demo mode.
 */
class MainActivity : AppCompatActivity(), LoginFragment.Listener {

    private lateinit var toolbar: MaterialToolbar

    private lateinit var navOverview: MaterialButton
    private lateinit var navDonations: MaterialButton
    private lateinit var navVolunteers: MaterialButton
    private lateinit var navProjects: MaterialButton
    private lateinit var navDonorCrm: MaterialButton
    private lateinit var navReports: MaterialButton
    private lateinit var navSettings: MaterialButton

    private lateinit var btnAuth: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        bindNavViews()
        wireNavigation()
        updateAuthUi()

        if (savedInstanceState == null) {
            if (AuthSession.isSignedIn()) {
                navigateToOverview()
            } else {
                navigateToLogin()
            }
        }
    }

    private fun bindNavViews() {
        navOverview = findViewById(R.id.navOverview)
        navDonations = findViewById(R.id.navDonations)
        navVolunteers = findViewById(R.id.navVolunteers)
        navProjects = findViewById(R.id.navProjects)
        navDonorCrm = findViewById(R.id.navDonorCrm)
        navReports = findViewById(R.id.navReports)
        navSettings = findViewById(R.id.navSettings)
        btnAuth = findViewById(R.id.btnAuth)
    }

    private fun wireNavigation() {
        navOverview.setOnClickListener { ensureAuthorized(AppSection.OVERVIEW) { navigateToOverview() } }
        navDonations.setOnClickListener { ensureAuthorized(AppSection.DONATIONS) { navigateToList(ListFragment.Kind.DONATIONS) } }
        navVolunteers.setOnClickListener { ensureAuthorized(AppSection.VOLUNTEERS) { navigateToList(ListFragment.Kind.VOLUNTEERS) } }
        navProjects.setOnClickListener { ensureAuthorized(AppSection.PROJECTS) { navigateToList(ListFragment.Kind.PROJECTS) } }
        navDonorCrm.setOnClickListener { ensureAuthorized(AppSection.DONOR_CRM) { navigateToList(ListFragment.Kind.DONOR_CRM) } }
        navReports.setOnClickListener { ensureAuthorized(AppSection.REPORTS) { navigateToList(ListFragment.Kind.REPORTS) } }
        navSettings.setOnClickListener { ensureAuthorized(AppSection.SETTINGS) { navigateToList(ListFragment.Kind.SETTINGS) } }

        btnAuth.setOnClickListener {
            if (AuthSession.isSignedIn()) {
                AuthSession.signOut()
                updateAuthUi()
                navigateToLogin()
            } else {
                navigateToLogin()
            }
        }
    }

    private fun ensureAuthorized(section: AppSection, action: () -> Unit) {
        if (!AuthSession.isSignedIn()) {
            navigateToLogin()
            return
        }

        val role = AuthSession.getRole()
        if (!RoleAccess.canAccessSection(role, section)) {
            // Block access even if invoked (defense-in-depth).
            supportActionBar?.subtitle = getString(R.string.auth_access_denied)
            return
        }

        action()
    }

    private fun applyRoleBasedNavigationVisibility() {
        val role = AuthSession.getRole()

        navOverview.visibility = if (RoleAccess.canAccessSection(role, AppSection.OVERVIEW)) View.VISIBLE else View.GONE
        navDonations.visibility = if (RoleAccess.canAccessSection(role, AppSection.DONATIONS)) View.VISIBLE else View.GONE
        navVolunteers.visibility = if (RoleAccess.canAccessSection(role, AppSection.VOLUNTEERS)) View.VISIBLE else View.GONE
        navProjects.visibility = if (RoleAccess.canAccessSection(role, AppSection.PROJECTS)) View.VISIBLE else View.GONE
        navDonorCrm.visibility = if (RoleAccess.canAccessSection(role, AppSection.DONOR_CRM)) View.VISIBLE else View.GONE
        navReports.visibility = if (RoleAccess.canAccessSection(role, AppSection.REPORTS)) View.VISIBLE else View.GONE
        navSettings.visibility = if (RoleAccess.canAccessSection(role, AppSection.SETTINGS)) View.VISIBLE else View.GONE
    }

    private fun setSelectedNav(selected: MaterialButton?) {
        val all = listOf(navOverview, navDonations, navVolunteers, navProjects, navDonorCrm, navReports, navSettings)

        val selectedBg = ContextCompat.getColor(this, R.color.ocean_surface_alt)
        val normalBg = ContextCompat.getColor(this, android.R.color.transparent)

        val selectedText = ContextCompat.getColor(this, R.color.ocean_primary)
        val normalText = ContextCompat.getColor(this, R.color.ocean_text)

        val selectedStroke = ContextCompat.getColor(this, R.color.ocean_divider)

        for (btn in all) {
            val isSelected = (btn == selected)
            btn.setBackgroundColor(if (isSelected) selectedBg else normalBg)
            btn.setTextColor(if (isSelected) selectedText else normalText)

            // Subtle "pill" outline for the active item.
            btn.strokeWidth = if (isSelected) 1 else 0
            btn.strokeColor = if (isSelected) ColorStateList.valueOf(selectedStroke) else null

            // Slightly denser feel for nav.
            btn.cornerRadius = 14
        }
    }

    private fun navigateToLogin() {
        supportActionBar?.title = getString(R.string.auth_sign_in)
        setSelectedNav(null)
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, LoginFragment())
            .commit()
    }

    private fun navigateToOverview() {
        supportActionBar?.title = getString(R.string.nav_overview)
        setSelectedNav(navOverview)
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, OverviewFragment())
            .commit()
    }

    private fun navigateToList(kind: ListFragment.Kind) {
        val section = when (kind) {
            ListFragment.Kind.DONATIONS -> AppSection.DONATIONS
            ListFragment.Kind.VOLUNTEERS -> AppSection.VOLUNTEERS
            ListFragment.Kind.PROJECTS -> AppSection.PROJECTS
            ListFragment.Kind.DONOR_CRM -> AppSection.DONOR_CRM
            ListFragment.Kind.REPORTS -> AppSection.REPORTS
            ListFragment.Kind.SETTINGS -> AppSection.SETTINGS
        }

        // If role visibility changes while in-app, avoid navigating into a blocked section.
        if (!RoleAccess.canAccessSection(AuthSession.getRole(), section)) {
            supportActionBar?.subtitle = getString(R.string.auth_access_denied)
            return
        }

        supportActionBar?.title = when (kind) {
            ListFragment.Kind.DONATIONS -> getString(R.string.nav_donations)
            ListFragment.Kind.VOLUNTEERS -> getString(R.string.nav_volunteers)
            ListFragment.Kind.PROJECTS -> getString(R.string.nav_projects)
            ListFragment.Kind.DONOR_CRM -> getString(R.string.nav_donor_crm)
            ListFragment.Kind.REPORTS -> getString(R.string.nav_reports)
            ListFragment.Kind.SETTINGS -> getString(R.string.nav_settings)
        }

        setSelectedNav(
            when (kind) {
                ListFragment.Kind.DONATIONS -> navDonations
                ListFragment.Kind.VOLUNTEERS -> navVolunteers
                ListFragment.Kind.PROJECTS -> navProjects
                ListFragment.Kind.DONOR_CRM -> navDonorCrm
                ListFragment.Kind.REPORTS -> navReports
                ListFragment.Kind.SETTINGS -> navSettings
            }
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, ListFragment.newInstance(kind))
            .commit()
    }

    private fun updateAuthUi() {
        btnAuth.text = if (AuthSession.isSignedIn()) getString(R.string.auth_sign_out) else getString(R.string.auth_sign_in)

        val user = AuthSession.getUser()
        supportActionBar?.subtitle = if (user != null) {
            "${getString(R.string.auth_signed_in_as)} ${user.name} • ${user.appRole}"
        } else {
            null
        }

        applyRoleBasedNavigationVisibility()
    }

    override fun onSignedIn() {
        updateAuthUi()
        navigateToOverview()
    }
}
