package sim.coder.fragments.Model

import java.util.*

data class Crime(

    val id : UUID=UUID.randomUUID(),
    var title : String ="",
    //var detail:String="",
    var date: Date=Date(),
    var isSolved:Boolean=false,
    var requirePolice:Boolean=false


) {
}