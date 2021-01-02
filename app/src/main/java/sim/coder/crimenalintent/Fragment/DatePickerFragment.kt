package sim.coder.crimenalintent.Fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import java.util.*


private  const val  ARG_DATE ="date"
class DatePickerFragment:DialogFragment() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState)

        val dateListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            val resultDate: Date = GregorianCalendar(year, month, day).time
            targetFragment?.let { fragment ->
                (fragment as Callbacks).onDateSelected(resultDate)
            }
        }

        val date = arguments?.getSerializable(ARG_DATE) as Date

        var calendar=Calendar.getInstance()
        calendar.time=date
        val year = calendar.get(Calendar.YEAR)
        val month= calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireContext(),dateListener,year,month,day)

    }


    companion object {
        fun newInctance(date:Date):DatePickerFragment{
            var args=Bundle().apply {
                putSerializable(ARG_DATE,date)
            }

            return DatePickerFragment().apply {
                arguments=args
            }
        }
    }


    interface Callbacks{
        fun onDateSelected(date: Date)
    }




}