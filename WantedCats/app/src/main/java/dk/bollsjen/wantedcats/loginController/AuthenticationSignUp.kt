package dk.bollsjen.wantedcats.loginController

import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bollsjen.wantedcats.LoginDirections

sealed class AuthenticationSignUp {

    companion object {

        private val authentication = Firebase.auth

        fun SignUpUser(email: String, password: String, navController: NavController) {

            authentication.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        authentication.currentUser
                        var action = LoginDirections.actionLoginToFirstFragment()
                        navController.navigate(action)
                    }
                }
        }
    }

}