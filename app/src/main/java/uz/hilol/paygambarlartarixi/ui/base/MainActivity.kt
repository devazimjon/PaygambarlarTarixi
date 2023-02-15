package uz.hilol.paygambarlartarixi.ui.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.hilol.paygambarlartarixi.R
import uz.hilol.paygambarlartarixi.di.AppComponent
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<MainActivityViewModel> { factory }


    lateinit var bottomNav: BottomNavigationView

    private val currentNavController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppComponent.get().inject(this)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_nav)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    if (!currentNavController.popBackStack(R.id.homeFragment, false)) {
                        currentNavController.navigate(R.id.homeFragment)
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.apps -> {
                    currentNavController.navigate(R.id.appsFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.settings -> {
                    currentNavController.navigate(R.id.profileFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.contact_us -> {
                    startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(HILOL_SUPPORT_URL))
                    )
                    return@setOnNavigationItemSelectedListener false
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        currentNavController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.homeFragment ||
                destination.id == R.id.appsFragment ||
                destination.id == R.id.profileFragment
            ) {
                bottomNav.visibility = View.VISIBLE
            } else {
                bottomNav.visibility = View.GONE
            }
        }
    }

    companion object {
        const val HILOL_SUPPORT_URL = "https://t.me/Hilol_admin"
    }
}