package dk.bollsjen.wantedcats.loginController

import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bollsjen.wantedcats.LoginDirections
import dk.bollsjen.wantedcats.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController

sealed class AuthenticationLogin {

    companion object {

        val authentication = Firebase.auth

        fun signIn(email: String, password: String, navController: NavController) {

            val user = authentication.currentUser
            if(user != null) {
                var action = LoginDirections.actionLoginToCreateCat(1)
                navController.navigate(action)
            }

            authentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        authentication.currentUser
                        var action = LoginDirections.actionLoginToCreateCat(1)
                        navController.navigate(action)
                    }
                }
        }

        fun loginSignOut() {
            authentication.signOut()
        }
    }

}