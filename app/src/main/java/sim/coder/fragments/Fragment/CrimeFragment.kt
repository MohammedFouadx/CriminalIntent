package sim.coder.fragments.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import sim.coder.fragments.Model.Crime
import sim.coder.fragments.R

class CrimeFragment : Fragment() {

    lateinit var titleText:TextView
    lateinit var crime: Crime
    lateinit var dateButton:Button
    lateinit var solvedCheckedBox: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crime=Crime()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)

        val view= inflater.inflate(R.layout.fragment_crime,container,false)

        titleText=view.findViewById(R.id.title_TextView)
        dateButton=view.findViewById(R.id.btn_date)
        solvedCheckedBox=view.findViewById(R.id.crime_solved)

        solvedCheckedBox.apply {
            setOnClickListener {
                crime.isSolved=isChecked
            }
        }


        dateButton.apply {
           text= crime.date.toString()
            crime.isSolved=false
        }
        return view
    }

    override fun onStart() {
        super.onStart()

        val titleWacher= object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

               crime.title=s.toString()
                Toast.makeText(context,""+crime.title,Toast.LENGTH_SHORT).show()
            }


        }

        titleText.addTextChangedListener(titleWacher)

    }



}