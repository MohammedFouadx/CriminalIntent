package sim.coder.crimenalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sim.coder.crimenalintent.Fragment.CrimeFragment
import sim.coder.crimenalintent.Fragment.CrimeListFragment
import java.util.*

class MainActivity : AppCompatActivity() , CrimeListFragment.CallBacks {

    override fun onItemSelected(crimeId: UUID) {
        val fragment= CrimeFragment.newInstance(crimeId)
            val fm = supportFragmentManager
            fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = CrimeListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
    }

