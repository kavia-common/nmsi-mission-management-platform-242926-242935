package org.example.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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

    private fun wireNavigation() {
        findViewById<MaterialButton>(R.id.navOverview).setOnClickListener { ensureSignedIn { navigateToOverview() } }
        findViewById<MaterialButton>(R.id.navDonations).setOnClickListener {
            ensureSignedIn { navigateToList(ListFragment.Kind.DONATIONS) }
        }
        findViewById<MaterialButton>(R.id.navVolunteers).setOnClickListener {
            ensureSignedIn { navigateToList(ListFragment.Kind.VOLUNTEERS) }
        }
        findViewById<MaterialButton>(R.id.navProjects).setOnClickListener {
            ensureSignedIn { navigateToList(ListFragment.Kind.PROJECTS) }
        }
        findViewById<MaterialButton>(R.id.navDonorCrm).setOnClickListener {
            ensureSignedIn { navigateToList(ListFragment.Kind.DONOR_CRM) }
        }
        findViewById<MaterialButton>(R.id.navReports).setOnClickListener {
            ensureSignedIn { navigateToList(ListFragment.Kind.REPORTS) }
        }
        findViewById<MaterialButton>(R.id.navSettings).setOnClickListener {
            ensureSignedIn { navigateToList(ListFragment.Kind.SETTINGS) }
        }

        findViewById<MaterialButton>(R.id.btnAuth).setOnClickListener {
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

    private fun navigateToLogin() {
        supportActionBar?.title = getString(R.string.auth_sign_in)
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, LoginFragment())
            .commit()
    }

    private fun navigateToOverview() {
        supportActionBar?.title = getString(R.string.nav_overview)
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

        supportFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, ListFragment.newInstance(kind))
            .commit()
    }

    private fun updateAuthButton() {
        val btn: MaterialButton = findViewById(R.id.btnAuth)
        btn.text = if (AuthSession.isSignedIn()) getString(R.string.auth_sign_out) else getString(R.string.auth_sign_in)
    }

    override fun onSignedIn() {
        updateAuthButton()
        navigateToOverview()
    }
}
