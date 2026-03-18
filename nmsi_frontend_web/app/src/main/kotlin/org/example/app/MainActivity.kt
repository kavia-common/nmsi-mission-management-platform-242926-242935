package org.example.app

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import org.example.app.auth.AuthSession
import org.example.app.ui.ListFragment
import org.example.app.ui.LoginFragment
import org.example.app.ui.OverviewFragment

/**
 * App entry point.
 *
 * Hosts a sidebar-driven navigation shell with auth scaffolding and mock-backed data screens.
 */
class MainActivity : AppCompatActivity(), LoginFragment.Listener {

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

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        bindNavViews()
        wireNavigation()
        updateAuthButton()

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
        navOverview.setOnClickListener { ensureSignedIn { navigateToOverview() } }
        navDonations.setOnClickListener { ensureSignedIn { navigateToList(ListFragment.Kind.DONATIONS) } }
        navVolunteers.setOnClickListener { ensureSignedIn { navigateToList(ListFragment.Kind.VOLUNTEERS) } }
        navProjects.setOnClickListener { ensureSignedIn { navigateToList(ListFragment.Kind.PROJECTS) } }
        navDonorCrm.setOnClickListener { ensureSignedIn { navigateToList(ListFragment.Kind.DONOR_CRM) } }
        navReports.setOnClickListener { ensureSignedIn { navigateToList(ListFragment.Kind.REPORTS) } }
        navSettings.setOnClickListener { ensureSignedIn { navigateToList(ListFragment.Kind.SETTINGS) } }

        btnAuth.setOnClickListener {
            if (AuthSession.isSignedIn()) {
                AuthSession.signOut()
                updateAuthButton()
                navigateToLogin()
            } else {
                navigateToLogin()
            }
        }
    }

    private fun ensureSignedIn(action: () -> Unit) {
        if (!AuthSession.isSignedIn()) {
            navigateToLogin()
            return
        }
        action()
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

    private fun updateAuthButton() {
        btnAuth.text = if (AuthSession.isSignedIn()) getString(R.string.auth_sign_out) else getString(R.string.auth_sign_in)
    }

    override fun onSignedIn() {
        updateAuthButton()
        navigateToOverview()
    }
}
