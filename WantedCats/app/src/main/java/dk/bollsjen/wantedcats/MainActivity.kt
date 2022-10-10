package dk.bollsjen.wantedcats

import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import dk.bollsjen.wantedcats.databinding.ActivityMainBinding
import dk.bollsjen.wantedcats.databinding.FragmentFirstBinding
import dk.bollsjen.wantedcats.repositories.*
import dk.bollsjen.wantedcats.models.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var instance: MainActivity
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        instance = this
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences("dk.bollsjen.wantedcats", MODE_PRIVATE)
        Singleton.users.add(LoginInfo(1, "Fiskerbent", "1234"))
        Singleton.users.add(LoginInfo(2, "Torsken", "1234"))
        Singleton.users.add(LoginInfo(3, "Børge", "1234"))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onResume() {
        super.onResume()
        if(pref!!.getBoolean("firstrue", true)){
            pref!!.edit().putBoolean("firstrue", false).commit()

        }else{
            auth.signOut()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}