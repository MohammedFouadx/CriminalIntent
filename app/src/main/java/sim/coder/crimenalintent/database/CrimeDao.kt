package sim.coder.crimenalintent.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import sim.coder.crimenalintent.Model.Crime
import java.util.*

@Dao
interface CrimeDao {

    @Query("SELECT * FROM crime")

    //fun getCrimes(): List<Crime>
    fun getCrimes(): LiveData<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")

    //fun getCrime(id: UUID): Crime?
    fun getCrime(id: UUID): LiveData<Crime?>


    @Update
    fun updateCrime(crime: Crime)
    @Insert
    fun addCrime(crime: Crime)
}