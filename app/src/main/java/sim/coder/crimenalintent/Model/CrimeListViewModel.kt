package sim.coder.crimenalintent.Model

import androidx.lifecycle.ViewModel
import sim.coder.crimenalintent.CrimeRepository

class CrimeListViewModel : ViewModel() {

//    val crimes = mutableListOf<Crime>()
//
//    init {
//        for (i in 0 until 100) {
//            val crime = Crime()
//            crime.title = "Crime #$i"
//            crime.isSolved = i % 2 == 0
//            crimes += crime
//        }
//    }

    private val crimeRepository = CrimeRepository.get()
    val  crimeListLiveData = crimeRepository.getCrimes()

    fun addCrime(crime:Crime){
        CrimeRepository.get().addCrime(crime)
    }


}