package org.example.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.api.MockApiClient
import org.example.app.auth.AuthSession

/**
 * Generic list fragment that displays one of the app sections using mock API data.
 */
class ListFragment : Fragment() {

    enum class Kind {
        DONATIONS,
        VOLUNTEERS,
        PROJECTS,
        DONOR_CRM,
        REPORTS,
        SETTINGS,
    }

    private val api = MockApiClient()
    private val adapter = SimpleRowAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val title: TextView = view.findViewById(R.id.listTitle)
        val recycler: RecyclerView = view.findViewById(R.id.recycler)
        val empty: TextView = view.findViewById(R.id.emptyText)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        val kindName = requireArguments().getString(ARG_KIND) ?: Kind.DONATIONS.name
        val kind = Kind.valueOf(kindName)

        title.text = when (kind) {
            Kind.DONATIONS -> getString(R.string.nav_donations)
            Kind.VOLUNTEERS -> getString(R.string.nav_volunteers)
            Kind.PROJECTS -> getString(R.string.nav_projects)
            Kind.DONOR_CRM -> getString(R.string.nav_donor_crm)
            Kind.REPORTS -> getString(R.string.nav_reports)
            Kind.SETTINGS -> getString(R.string.nav_settings)
        }

        Thread {
            val role = AuthSession.getRole()

            val rows = when (kind) {
                Kind.DONATIONS -> api.listDonations(role).map {
                    SimpleRowAdapter.RowItem("${it.donorName} — ₹${it.amountInr}", "${it.dateIso} • ${it.status}")
                }

                Kind.VOLUNTEERS -> api.listVolunteers().map {
                    SimpleRowAdapter.RowItem(it.name, "${it.region} • ${it.phone}")
                }

                Kind.PROJECTS -> api.listProjects().map {
                    SimpleRowAdapter.RowItem(it.name, "${it.region} • ${it.status}")
                }

                Kind.DONOR_CRM -> api.listDonorCrm(role).map {
                    SimpleRowAdapter.RowItem(it.title, it.subtitle)
                }

                Kind.REPORTS -> api.listReports(role).map {
                    SimpleRowAdapter.RowItem(it.title, it.subtitle)
                }

                Kind.SETTINGS -> listOf(
                    SimpleRowAdapter.RowItem("Organization", "National Missionary Society of India (NMSI)"),
                    SimpleRowAdapter.RowItem("Theme", "Ocean Professional"),
                    SimpleRowAdapter.RowItem("Data Source", "Mock API (no backend connected)"),
                    SimpleRowAdapter.RowItem("Signed-in Role", role?.name ?: "None"),
                )
            }

            requireActivity().runOnUiThread {
                adapter.submit(rows)

                // If empty, provide a slightly more helpful message for role-filtered sections.
                empty.text = if (rows.isEmpty() && role != null) {
                    "No items (may be restricted for role: ${role.name})"
                } else if (rows.isEmpty()) {
                    "No items"
                } else {
                    empty.text
                }

                empty.visibility = if (rows.isEmpty()) View.VISIBLE else View.GONE
            }
        }.start()
    }

    companion object {
        private const val ARG_KIND = "kind"

        // PUBLIC_INTERFACE
        fun newInstance(kind: Kind): ListFragment {
            /** Create a list fragment instance for a given section kind. */
            val f = ListFragment()
            f.arguments = Bundle().apply { putString(ARG_KIND, kind.name) }
            return f
        }
    }
}
