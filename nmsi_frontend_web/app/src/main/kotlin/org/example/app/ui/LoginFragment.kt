package org.example.app.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.example.app.R
import org.example.app.auth.AuthSession

/**
 * Login screen for demo/mock auth.
 */
class LoginFragment : Fragment() {

    interface Listener {
        fun onSignedIn()
    }

    private var listener: Listener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? Listener
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val email: TextInputEditText = view.findViewById(R.id.inputEmail)
        val status: TextView = view.findViewById(R.id.loginStatus)
        val btn: MaterialButton = view.findViewById(R.id.btnDemoSignIn)

        btn.setOnClickListener {
            val e = email.text?.toString() ?: ""
            val user = AuthSession.signInAsDemo(e)
            status.text = "Signed in as ${user.name} (${user.email})"
            listener?.onSignedIn()
        }
    }
}
