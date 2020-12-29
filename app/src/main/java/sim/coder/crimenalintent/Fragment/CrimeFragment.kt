package sim.coder.crimenalintent.Fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import sim.coder.crimenalintent.Model.Crime
import sim.coder.crimenalintent.Model.CrimeDetailViewModel
import sim.coder.crimenalintent.R
import java.util.*
import androidx.lifecycle.Observer
import java.text.SimpleDateFormat


private const val DIALOG_DATE="DialogDate"
private const val REQUEST_DATE=0


class CrimeFragment : Fragment() , DatePickerFragment.Callbacks , TimePickerFragment.Callbacks {



    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var timeButton:Button

    private val crimeDetailViewModel:CrimeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
        val id:UUID= arguments?.getSerializable("ARG_CRIME_ID") as UUID
        crimeDetailViewModel.loadCrime(id)
        //Toast.makeText(context,id.toString(),Toast.LENGTH_LONG).show()
    }


    companion object{
        fun newInstance(crimeId:UUID):CrimeFragment{

            val args = Bundle().apply {
                putSerializable("ARG_CRIME_ID",crimeId)
            }

            return CrimeFragment().apply {
                arguments=args
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        timeButton= view.findViewById(R.id.crime_time) as Button


        timeButton.setOnClickListener {
            val calendar = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)

                timeButton.text = SimpleDateFormat("HH:mm").format(calendar.time)
            }

            TimePickerDialog(context, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true
            ).show()

        }

        dateButton.setOnClickListener {
            DatePickerFragment.newInctance(crime.date).apply {
                setTargetFragment(this@CrimeFragment, REQUEST_DATE)
                show(this@CrimeFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }


//        dateButton.apply {
//            text = crime.date.toString()
//            isEnabled = false
//        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner, Observer {
            crime ->
            crime?.let {crime ->
                this.crime=crime
                updateUI()
            }
        })


    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()
        //solvedCheckBox.isChecked = crime.isSolved
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        titleField.addTextChangedListener(titleWatcher)

        solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                crime.isSolved = isChecked
            }
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)


    }
    override fun onDateSelected(date: Date) {
        crime.date=date
        updateUI()

    }

    override fun onTimeSelected(time: java.sql.Time) {
        updateUI()
    }


}