package sim.coder.crimenalintent.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.UUID
import java.util.Date

@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false,
    var suspects:String="",
    var suspectPhoneNumber:String="")



{

    val photoFileName
        get() = "IMG_$id.jpg"

    override fun toString(): String {
        return "Crime(id=$id,title=$title,date=$date,isSolved=$isSolved)"
    }

}

data class Time(
    var time:Time
)