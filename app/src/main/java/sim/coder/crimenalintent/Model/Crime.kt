package sim.coder.crimenalintent.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import java.util.Date

@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false)

{


    override fun toString(): String {
        return "Crime(id=$id,title=$title,date=$date,isSolved=$isSolved)"
    }

}