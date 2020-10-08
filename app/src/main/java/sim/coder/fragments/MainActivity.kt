package sim.coder.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sim.coder.fragments.Fragment.CrimeFragment
import sim.coder.fragments.Fragment.CrimeListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment= supportFragmentManager.findFragmentById(R.id.fragment_Container)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_Container, CrimeListFragment())
            .commit()

    }
}
