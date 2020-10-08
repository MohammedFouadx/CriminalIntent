package sim.coder.fragments.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sim.coder.fragments.Model.CrimeListViewModel
import sim.coder.fragments.R
import java.text.DateFormat

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }
    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        updateUI()

        return view
    }

    private fun updateUI() {
        val crimes =
            crimeListViewModel.crimes
        adapter = CrimeAdapter (crimes)
        crimeRecyclerView .adapter = adapter
    }
    abstract open  class CrimeHolder(view: View) : RecyclerView.ViewHolder(view){
        abstract open fun bind(item:Crime)

    }
    private inner class NormalCrimeHolder(view: View) :  CrimeHolder(view),
        View.OnClickListener {
        private lateinit var crime: Crime
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)
        private val titleTextView: TextView = itemView.findViewById(R.id.requiredcrime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.requiredcrime_date)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }
        override fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = DateFormat.getDateInstance(DateFormat.FULL).format(this.crime.date).toString()
            solvedImageView.visibility=if(crime.isSolved){
                View.VISIBLE
            }
            else
                View.GONE
        }
    }
    private  inner class RequirredCrimeHolder(view: View) : CrimeHolder(view){
        private lateinit var crime: Crime
        val requiredCrimeTextView: TextView = itemView.findViewById(R.id.requiredcrime_title)
        val requireddateTextView: TextView = itemView.findViewById(R.id.requiredcrime_date)


        override fun bind(crime: Crime) {
            this.crime = crime

            requiredCrimeTextView.text = this.crime.title
            requireddateTextView.text= DateFormat.getDateInstance(DateFormat.FULL).format(this.crime.date).toString()
        }

    }
    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val EmergencyCrime = 1
        val crime = 2


        override fun getItemViewType(position: Int): Int {
            return if (crimes[position].Rpolice == true)
                return EmergencyCrime
            else
                return crime


        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view: View?
            var recyclerViewholder: RecyclerView.ViewHolder
            when (viewType) {
                EmergencyCrime -> {

                    val view = layoutInflater.inflate(
                        R.layout.list_item_police_required_crime,
                        parent,
                        false
                    )

                    recyclerViewholder = RequirredCrimeHolder(view)
                }
                else -> {
                    val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                    recyclerViewholder = NormalCrimeHolder(view)
                }

            }
            return recyclerViewholder

        }



        override fun getItemCount(): Int {
            return crimes.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val crime = crimes[position]
            if (holder is RequirredCrimeHolder)
                holder.bind(crime)
            else
                if(holder is NormalCrimeHolder)
                    holder.bind(crime)





        }
    }
    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}