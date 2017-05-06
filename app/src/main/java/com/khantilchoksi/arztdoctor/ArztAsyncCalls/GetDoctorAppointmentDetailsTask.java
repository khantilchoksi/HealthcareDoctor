package com.khantilchoksi.arztdoctor.ArztAsyncCalls;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.khantilchoksi.arztdoctor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Khantil on 22-03-2017.
 */

public class GetDoctorAppointmentDetailsTask extends AsyncTask<Void, Void, Boolean> {

    private static final String LOG_TAG = GetDoctorAppointmentDetailsTask.class.getSimpleName();
    Context context;
    Activity activity;

    String mAppointmentId;
    String mPatientName;
    String mPatientSex;
    String mPatientAge;
    String mPatientMobileNumber;
    String mPatientBloodGroup;
    String mClinicName;
    String mAppointmentDay;
    String mAppointmentDate;
    String mVisitingHours;
    String mConsultationFees;
    ArrayList<String> mAttachementImagesPath;


    ProgressDialog progressDialog;

    public interface AsyncResponse {
        void processDoctorClinicTaskInfoFinish(String patientName, String patientSex,
                                               String patinetAge, String patientMobileNumber,
                                               String patientBloodGroup, String clinicName,
                                               String appointmentDay, String appointmentDate,
                                               String visitingHours,
                                               String consultationFees,
                                               ArrayList<String> attachementImagesPath,
                                               ProgressDialog progressDialog);
    }

    public AsyncResponse delegate = null;

    public GetDoctorAppointmentDetailsTask(String appointmentId, Context context, Activity activity, AsyncResponse asyncResponse, ProgressDialog progressDialog){
        this.mAppointmentId = appointmentId;
        this.context = context;
        this.activity = activity;
        this.delegate = asyncResponse;
        this.progressDialog = progressDialog;

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String clientCredStr;

        try {

            final String CLIENT_BASE_URL = context.getResources().getString(R.string.base_url).concat("getDoctorAppointmentDetails");
            URL url = new URL(CLIENT_BASE_URL);


            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);


            Uri.Builder builder = new Uri.Builder();
            Map<String, String> parameters = new HashMap<>();
            parameters.put("appointmentId",mAppointmentId);


            // encode parameters
            Iterator entries = parameters.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                entries.remove();
            }
            String requestBody = builder.build().getEncodedQuery();
            Log.d(LOG_TAG, "Service Call URL : " + CLIENT_BASE_URL);
            Log.d(LOG_TAG, "Post parameters : " + requestBody);

            //OutputStream os = urlConnection.getOutputStream();
            OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(requestBody);    //bcz no parameters to be sent

            writer.flush();
            writer.close();
            os.close();

            urlConnection.connect();

            // Read the input stream into a String
            //InputStream inputStream = urlConnection.getInputStream();
            InputStream inputStream;
            int status = urlConnection.getResponseCode();
            Log.d(LOG_TAG, "URL Connection Response Code " + status);

            //if(status >= 400)
            //  inputStream = urlConnection.getErrorStream();
            //else
            inputStream = urlConnection.getInputStream();


            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return false;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return false;
            }

            clientCredStr = buffer.toString();

            Log.d(LOG_TAG, "Appointments Credential JSON String : " + clientCredStr);


            return fetchAppointmentsClinics(clientCredStr);

        } catch (IOException e) {
            Log.d(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return false;
        } catch (JSONException e) {
            Log.d(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.d(LOG_TAG, "Error closing stream", e);
                }
            }
            //return false;
        }
    }

    @Override
    protected void onCancelled() {
        progressDialog.dismiss();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        Log.d(LOG_TAG, "Success Boolean Tag: " + success.toString());
        if (success) {


            delegate.processDoctorClinicTaskInfoFinish(
                    mPatientName,
                    mPatientSex,
                    mPatientAge,
                    mPatientMobileNumber,
                    mPatientBloodGroup,
                    mClinicName,
                    mAppointmentDay,
                    mAppointmentDate,
                    mVisitingHours,
                    mConsultationFees,
                    mAttachementImagesPath,
                    progressDialog
            );

        } else {

            progressDialog.dismiss();


                /*Snackbar.make(, R.string.error_unknown_error,
                        Snackbar.LENGTH_LONG)
                        .show();*/
            Toast.makeText(context,context.getResources().getString(R.string.error_unknown_error),Toast.LENGTH_SHORT).show();

        }
    }

    private boolean fetchAppointmentsClinics(String clientCredStr) throws JSONException {



        final String mPatientNameString = "patientName";
        final String mPatientSexString = "patientSex";
        final String patientMobileNumberString = "patientMobileNumber";
        final String patientAgeString = "patientAge";
        final String patientBloodGroupString = "patientBloodGroup";

        final String mClinicNameString = "clinicName";
        final String mAppointmentDayString = "slotDay";
        final String mAppointmentDateString = "appointmentDate";
        final String mSlotStartTimeString = "slotStartTime";
        final String mSlotEndTimeString = "slotEndTime";
        final String mConsultationFeesString = "slotFees";

        final String mAttachmentListString = "attachmentList";
        final String prePathString = "prePath";


        JSONObject clientJson = new JSONObject(clientCredStr);



        if(clientJson != null) {

            mPatientName = clientJson.getString(mPatientNameString);
            mPatientSex = clientJson.getString(mPatientSexString);
            mPatientMobileNumber = clientJson.getString(patientMobileNumberString);
            mPatientAge = clientJson.getString(patientAgeString);
            mPatientBloodGroup = clientJson.getString(patientBloodGroupString);


            mClinicName = clientJson.getString(mClinicNameString);
            mAppointmentDay = clientJson.getString(mAppointmentDayString);

            mVisitingHours = clientJson.getString(mSlotStartTimeString) + " - " + clientJson.getString(mSlotEndTimeString);
            mConsultationFees = clientJson.getString(mConsultationFeesString);


            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            try {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.setTime(sdf.parse(clientJson.getString(mAppointmentDateString)));

                myFormat = "EEEE, MMMM dd, yyyy"; //In which you need put here
                sdf = new SimpleDateFormat(myFormat, Locale.US);

                mAppointmentDate = sdf.format(myCalendar.getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String prePath = clientJson.getString(prePathString);
            JSONArray pathsJsonArray = clientJson.getJSONArray(mAttachmentListString);
            mAttachementImagesPath = new ArrayList<String>();
            for(int j=0;j<pathsJsonArray.length();j++){
                Log.d(LOG_TAG,"Image Path: "+prePath.concat(pathsJsonArray.getString(j)));
                mAttachementImagesPath.add(prePath.concat(pathsJsonArray.getString(j)));
            }

            Log.d(LOG_TAG,"Image Path List from AsyncTask:"+mAttachementImagesPath.toString());

            return true;
        }



        return false;
    }


}
