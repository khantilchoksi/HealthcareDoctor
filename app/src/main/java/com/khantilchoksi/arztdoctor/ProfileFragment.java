package com.khantilchoksi.arztdoctor;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.khantilchoksi.arztdoctor.ArztAsyncCalls.GetDoctorMainSpecialitiesTask;
import com.khantilchoksi.arztdoctor.ArztAsyncCalls.GetDoctorProfileTask;
import com.khantilchoksi.arztdoctor.ArztAsyncCalls.GetDoctorQualitficationsTask;
import com.khantilchoksi.arztdoctor.ArztAsyncCalls.SaveDoctorQualificationTask;
import com.khantilchoksi.arztdoctor.ArztAsyncCalls.SaveDoctorSpecialityTask;
import com.khantilchoksi.arztdoctor.ArztAsyncCalls.SaveDoctorProfileTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements GetDoctorProfileTask.AsyncResponse, GetDoctorQualitficationsTask.AsyncResponse, GetDoctorMainSpecialitiesTask.AsyncResponse{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText birthdayEditTextView;
    final Calendar myCalendar = Calendar.getInstance();

    //private OnFragmentInteractionListener mListener;

    private static final int REQUEST_LOCATION = 1;  // The request code for location
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String LOG_TAG = getClass().getSimpleName();

    private View mRootView;

    private EditText mFullNameEditText;
    private EditText mMobileEditText;
    private RadioGroup mGenderRadioGroup;
    private EditText mEmailEditText;
    private Button mSaveButton;
    private MultiSelectionSpinner multiSelectionQualificaitonSpinner;
    private MultiSelectionSpinner multiSelectionSpecialitySpinner;


    private ProgressDialog progressDialog;

    //user entered values
    String fullName = null;
    int gender = 0;

    String birthdate = null;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_my_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            case R.id.save_profile:
                //Toast.makeText(getContext(),"Saved!",Toast.LENGTH_SHORT).show();
                saveButtonClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_profile, container, false);
        //Toast.makeText(getActivity(),"Profile Fragment Created!",Toast.LENGTH_LONG).show();

        //making rounded profile picture
        ImageButton userProfilePhoto = (ImageButton) mRootView.findViewById(R.id.user_profile_photo);
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.default_profile);
        RoundedBitmapDrawable drawable =
                RoundedBitmapDrawableFactory.create(res, bitmap);
        drawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
        drawable.setCircular(true);
        userProfilePhoto.setImageDrawable(drawable);


        mFullNameEditText = (EditText) mRootView.findViewById(R.id.full_name);
        //mFullNameEditText.setText(Utility.getPatientFullName(getContext()));

        mMobileEditText = (EditText) mRootView.findViewById(R.id.mobile_number);
        mMobileEditText.setText(Utility.getPatientMobileNumber(getContext()));

        mGenderRadioGroup = (RadioGroup) mRootView.findViewById(R.id.radio_group_gender);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBirthdayTextView();
            }

        };

        birthdayEditTextView = (EditText) mRootView.findViewById(R.id.birthday);
        birthdayEditTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),date,1995,0, 1).show();

            }
        });


        mEmailEditText = (EditText) mRootView.findViewById(R.id.email_id);

        setUpDegreeSpinner();

        //setUpSpecialitySpinner();

        //setUpCitySpinner();
        /*progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Profile Info...");
        progressDialog.show();
        GetDoctorProfileTask getPatientProfileTask = new GetDoctorProfileTask(getContext(),getActivity(),this,progressDialog);
        getPatientProfileTask.execute((Void) null);*/

        //degree spinner
        //setUpDegreeSpinner();

        //setUpSpecialitySpinner();

        /*buildGoogleApiClient();

        Button detectCurrentLocationButton = (Button) mRootView.findViewById(R.id.detect_current_location_button);
        detectCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        });*/



        mSaveButton = (Button) mRootView.findViewById(R.id.btn_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonClick();
            }
        });

        return mRootView;
    }

    @Override
    public void processFinish(String fullName, int gender, String birthdate, String emailId,
                              ArrayList<String>  qualificationList, ArrayList<String> specialityList, ProgressDialog progressDialog) {
        //GetDoctorProfileTask finished
        mFullNameEditText.setText(fullName);
        setGenderRadioButton(gender);

        setBirthdate(birthdate);

        mEmailEditText.setText(emailId);


        Log.d(LOG_TAG, "Received selected qualification list: "+qualificationList);
        multiSelectionQualificaitonSpinner.setSelection(qualificationList);

        Log.d(LOG_TAG, "Received selected speciality list: "+specialityList);

        multiSelectionSpecialitySpinner.setSelection(specialityList);


        progressDialog.dismiss();
    }

    private void setGenderRadioButton(int gender){
        switch (gender){
            case 1:
                mGenderRadioGroup.check(R.id.radio_male);
                break;
            case 2:
                mGenderRadioGroup.check(R.id.radio_female);
                break;
            case 3:
                mGenderRadioGroup.check(R.id.radio_others);
        }
    }

    private void setBirthdate(String birthdate){
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        try {
            myCalendar.setTime(sdf.parse(birthdate));
            updateBirthdayTextView();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateBirthdayTextView(){
        String myFormat = "MMMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthdayEditTextView.setText(sdf.format(myCalendar.getTime()));
    }

    private void setUpDegreeSpinner(){
        multiSelectionQualificaitonSpinner = (MultiSelectionSpinner) mRootView.findViewById(R.id.degreeSpinner);
        multiSelectionQualificaitonSpinner.setSpinnerTitle("Select Degrees");

        multiSelectionQualificaitonSpinner.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {

            }

            @Override
            public void selectedStrings(List<String> strings) {
                //Toast.makeText(getContext(), strings.toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getContext(), "Heya"+strings.toString(), Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG,"Qualification List: "+strings.toString());
                SaveDoctorQualificationTask saveDoctorQualificationTask = new SaveDoctorQualificationTask(
                        getContext(),getActivity(), strings);
                saveDoctorQualificationTask.execute((Void) null);

            }
        });

        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Profile Info...");
        progressDialog.show();

        GetDoctorQualitficationsTask getDoctorQualitficationsTask = new GetDoctorQualitficationsTask(getContext(),
                getActivity(),this,progressDialog);
        getDoctorQualitficationsTask.execute((Void) null);
    }

    private void setUpSpecialitySpinner(){

        multiSelectionSpecialitySpinner = (MultiSelectionSpinner) mRootView.findViewById(R.id.speciality_spinner);
        multiSelectionSpecialitySpinner.setSpinnerTitle("Select Your Specialities");

        multiSelectionSpecialitySpinner.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {

            }

            @Override
            public void selectedStrings(List<String> strings) {
                //Toast.makeText(getContext(), strings.toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getContext(), "Heya"+strings.toString(), Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG,"Speciality List: "+strings.toString());
                SaveDoctorSpecialityTask saveDoctorSpecialityTask = new SaveDoctorSpecialityTask(
                        getContext(),getActivity(), strings);
                saveDoctorSpecialityTask.execute((Void) null);

            }
        });

        GetDoctorMainSpecialitiesTask getDoctorMainSpecialitiesTask = new GetDoctorMainSpecialitiesTask(getContext(),
                getActivity(),this,progressDialog);
        getDoctorMainSpecialitiesTask.execute((Void) null);
    }

    /*private void setUpCitySpinner(){
        //BloodGroup Spinner Spinner
        String[] bloodGroupData = {
                "Ahmedabad",
                "Surat",
                "Jamnagar",
                "Rajkot",
                "Mehsana",
                "Surat",
                "Vapi",
                "Vadodara",
        };

        final SpinnerAdapter cityArrayAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_item, bloodGroupData, getContext().getString(R.string.prompt_city));

        final Spinner bloodGroupSpinner = (Spinner) mRootView.findViewById(R.id.citySpinner);
        bloodGroupSpinner.setAdapter(cityArrayAdapter);

        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    cityArrayAdapter.setSelectedItem(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    public void saveButtonClick(){
        if (!validate()) {
            return;
        }

        int selectedGenderId = mGenderRadioGroup.getCheckedRadioButtonId();

        switch (selectedGenderId){
            case R.id.radio_male:
                gender = 1;
                break;
            case R.id.radio_female:
                gender = 2;
                break;
            case R.id.radio_others:
                gender = 3;
                break;
        }


        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthdate = sdf.format(myCalendar.getTime());



        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving Profile Info...");
        progressDialog.show();

        SaveDoctorProfileTask saveDoctorProfileTask = new SaveDoctorProfileTask(getContext(), getActivity(),
                fullName,gender,birthdate,progressDialog);
        saveDoctorProfileTask.execute((Void) null);
    }

    public boolean validate() {
        boolean valid = true;

        mFullNameEditText.setError(null);

        fullName = mFullNameEditText.getText().toString();


        View focusView = null;
        if(fullName.isEmpty()){
            mFullNameEditText.setError(getString(R.string.error_field_required));
            focusView = mFullNameEditText;
            valid = false;
        }

        if (!valid) {
            //not valid data
            focusView.requestFocus();
        }

        return valid;
    }



    @Override
    public void processDoctorQualificationFinish(ArrayList<Integer> qualificationIdList, ArrayList<String> qualificationNameList) {
        //doctor qualifications
        //String[] array = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
        multiSelectionQualificaitonSpinner.setItems(qualificationNameList);

        setUpSpecialitySpinner();
    }

    @Override
    public void processSpecialityFinish(ArrayList<String> specialityNameList, ArrayList<String> specialityDescriptionList, ArrayList<String> specialityIconUrlList) {
        //String[] array = {"Ontology", "Physician", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};

        multiSelectionSpecialitySpinner.setItems(specialityNameList);


        GetDoctorProfileTask getPatientProfileTask = new GetDoctorProfileTask(getContext(),getActivity(),this,progressDialog);
        getPatientProfileTask.execute((Void) null);


    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
