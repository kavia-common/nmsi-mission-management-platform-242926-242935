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

/**
 * Overview dashboard fragment.
 */
class OverviewFragment : Fragment() {

    private val api = MockApiClient()
    private val adapter = SimpleRowAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val statDonations: TextView = view.findViewById(R.id.statDonations)
        val statVolunteers: TextView = view.findViewById(R.id.statVolunteers)
        val statProjects: TextView = view.findViewById(R.id.statProjects)
        val statTasks: TextView = view.findViewById(R.id.statTasks)

        val recent: RecyclerView = view.findViewById(R.id.recentRecycler)
        recent.layoutManager = LinearLayoutManager(requireContext())
        recent.adapter = adapter

        // Load in background to keep UI responsive
        Thread {
            val stats = api.getOverviewStats()
            val activity = api.getRecentActivity()

            requireActivity().runOnUiThread {
                statDonations.text = "₹ ${stats.donationsMtdInr}"
                statVolunteers.text = stats.activeVolunteers.toString()
                statProjects.text = stats.projects.toString()
                statTasks.text = stats.pendingTasks.toString()

                adapter.submit(activity.map { SimpleRowAdapter.RowItem(it.title, it.subtitle) })
            }
        }.start()
    }
}
