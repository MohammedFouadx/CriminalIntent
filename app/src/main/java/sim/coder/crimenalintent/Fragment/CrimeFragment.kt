package sim.coder.crimenalintent.Fragment

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import sim.coder.crimenalintent.Model.Crime
import sim.coder.crimenalintent.Model.CrimeDetailViewModel
import sim.coder.crimenalintent.R
import java.util.*
import androidx.lifecycle.Observer
import sim.coder.crimenalintent.PictureUtils
import java.io.File
import java.text.SimpleDateFormat


private const val DIALOG_DATE="DialogDate"
private const val REQUEST_DATE=0
private const val DATE_FORMAT = "EEE, MMM, dd"
private const val REQUEST_CONTACT=1
private const val REQUEST_PHONE=2
private const val REQUEST_PHOTO= 3

class CrimeFragment : Fragment() , DatePickerFragment.Callbacks , TimePickerFragment.Callbacks {


    private lateinit var crime: Crime
    private  var pictureUtils = PictureUtils()
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var timeButton: Button
    private lateinit var reportButton: Button
    private lateinit var suspectButton: Button
    private lateinit var phoneNumberButton: Button
    private lateinit var photoFile:File
    private lateinit var photoUri: Uri
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView
    private val simpleDateFormat = SimpleDateFormat("EEE,MMM,yyyy,dd")
    private val date= simpleDateFormat.format(Date())

    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
        val id: UUID = arguments?.getSerializable("ARG_CRIME_ID") as UUID
        crimeDetailViewModel.loadCrime(id)
        //Toast.makeText(context,id.toString(),Toast.LENGTH_LONG).show()
    }


    companion object {
        fun newInstance(crimeId: UUID): CrimeFragment {

            val args = Bundle().apply {
                putSerializable("ARG_CRIME_ID", crimeId)
            }

            return CrimeFragment().apply {
                arguments = args
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
        timeButton = view.findViewById(R.id.crime_time) as Button
        reportButton = view.findViewById(R.id.crime_report) as Button
        suspectButton = view.findViewById(R.id.crime_suspect) as Button
        phoneNumberButton = view.findViewById(R.id.crime_suspect_number) as Button
        photoButton = view.findViewById(R.id.crime_camera) as ImageButton
        photoView = view.findViewById(R.id.crime_photo) as ImageView





        timeButton.setOnClickListener {
            val calendar = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)

                timeButton.text = SimpleDateFormat("HH:mm",Locale.US).format(calendar.time)
            }

            TimePickerDialog(
                context, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true
            ).show()

        }

        dateButton.setOnClickListener {
            DatePickerFragment.newInctance(crime.date).apply {
                setTargetFragment(this@CrimeFragment, REQUEST_DATE)
                show(this@CrimeFragment.requireFragmentManager(), DIALOG_DATE)




            }
        }

        reportButton.setOnClickListener {
            var intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(Intent.EXTRA_TITLE, crime.title)
            }.also {
                val chooserIntent = Intent.createChooser(it, getString(R.string.send_report))
                startActivity(chooserIntent)
            }

        }

        suspectButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            val resolvedActivity: ResolveInfo? =packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null) {
                isEnabled = false
            }
            setOnClickListener {
                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
            }
            //pickContactIntent.addCategory(Intent.CATEGORY_HOME)


        }

        phoneNumberButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val num= "tel:${crime.suspectPhoneNumber}"
            val numberSuspectIntent = Intent(Intent.ACTION_DIAL,Uri.parse(num))
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(numberSuspectIntent, PackageManager.MATCH_DEFAULT_ONLY)
            if(resolvedActivity==null){
                isEnabled = false
            }
            setOnClickListener {
                val phoneActivities:List<ResolveInfo> =
                    packageManager.queryIntentActivities(numberSuspectIntent,PackageManager.MATCH_DEFAULT_ONLY)
                activity!!.startActivity(numberSuspectIntent)
                }
            }

        photoButton.apply{
            val packageManager :PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity :ResolveInfo? = packageManager.resolveActivity(captureImage,PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity==null){
                isEnabled = false
            }
            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                val cameraActivities : List<ResolveInfo> =
                    packageManager.queryIntentActivities(
                        captureImage,PackageManager.MATCH_DEFAULT_ONLY
                    )
                for (cameraActivity in cameraActivities){
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }

        return view
    }



    private fun updatePhotoView() {
        if (photoFile.exists()) {
            val bitmap = pictureUtils.getScaledBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
        } else {
            photoView.setImageDrawable(null)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_CONTACT && data != null -> {
                val contactUri: Uri? = data.data
                val queryFields = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val cursor = requireActivity().contentResolver
                    .query(contactUri!!, queryFields, null, null, null)
                cursor?.use {
                    if (it.count == 0) {
                        return
                    }

                    it.moveToFirst()
                    val suspect = it.getString(0)
                    //val suspectNumber=it.getString(1)
                    crime.suspects = suspect
                   // crime.suspectPhoneNumber =suspectNumber
                    crimeDetailViewModel.saveCrime(crime)
                    suspectButton.text = suspect
                }
            }

            requestCode == REQUEST_PHOTO && data !=null ->{
                val bitmap = BitmapFactory.decodeFile(photoFile.getPath())
                requireActivity().revokeUriPermission(photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updatePhotoView()
            }

        }



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner, Observer { crime ->
            crime?.let { crime ->
                this.crime = crime
                photoFile = crimeDetailViewModel.getPhotoFile(crime)
                photoUri = FileProvider.getUriForFile(requireActivity(),
                    "sim.coder.crimenalintent.fileprovider",photoFile)
                updateUI()
            }
        })


    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = date

        if (crime.suspects.isNotEmpty()) {
            suspectButton.text = crime.suspects

        }
        updatePhotoView()
        //Toast.makeText(context,crime.suspects,Toast.LENGTH_LONG).show()
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
    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    override fun onDateSelected(date: Date) {
        crime.date=date
        updateUI()

    }

    override fun onTimeSelected(time: java.sql.Time) {
        updateUI()
    }


    private fun getCrimeReport(): String {



        val solvedString = if (crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()
        var suspect = if (crime.suspects.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspects)
        }
        return getString(R.string.crime_report,
            crime.title, dateString, solvedString, suspect)
    }


}