package com.khantilchoksi.arztdoctor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.khantilchoksi.arztdoctor.ArztAsyncCalls.GetAppointmentsOfDoctorClinicTask;

import java.util.ArrayList;



public class UpcomingClinicAppointmentsFragment extends Fragment implements  GetAppointmentsOfDoctorClinicTask.AsyncResponse{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mRootview;
    private RecyclerView mRecyclerView;
    private AppointmentsAdapter mAppointmentAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Appointment> mAppointmentsList;
    private LinearLayout mNoAppointmentsLinearLayout;

    private ProgressDialog progressDialog;
    private String mClinicId;
    String mClinicName;


    public UpcomingClinicAppointmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingClinicAppointmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingClinicAppointmentsFragment newInstance(String param1, String param2) {
        UpcomingClinicAppointmentsFragment fragment = new UpcomingClinicAppointmentsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootview = inflater.inflate(R.layout.fragment_upcoming_clinic_appointments, container, false);

        mClinicId = getActivity().getIntent().getStringExtra("clinicId");
        mClinicName = getActivity().getIntent().getStringExtra("clinicName");

        mNoAppointmentsLinearLayout = (LinearLayout) mRootview.findViewById(R.id.no_appointments_available_layout);

        mRecyclerView = (RecyclerView) mRootview.findViewById(R.id.appointments_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return mRootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        initDataset();
    }

    private void initDataset() {
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Upcoming Appointments...");
        progressDialog.show();

        GetAppointmentsOfDoctorClinicTask getAppointmentsOfDoctorClinicTask =
                new GetAppointmentsOfDoctorClinicTask(mClinicId, mClinicName,
                getContext(),
                getActivity(),this,progressDialog);
        getAppointmentsOfDoctorClinicTask.execute((Void) null);
    }


    @Override
    public void processFinish(ArrayList<Appointment> appointmentsList, ProgressDialog progressDialog) {
        this.mAppointmentsList = appointmentsList;


        if(mAppointmentsList.isEmpty()){
            mNoAppointmentsLinearLayout.setVisibility(View.VISIBLE);
        }else{
            mAppointmentAdapter = new AppointmentsAdapter(this.mAppointmentsList, getActivity());
            mRecyclerView.setAdapter(mAppointmentAdapter);
        }
        progressDialog.dismiss();
    }
}
