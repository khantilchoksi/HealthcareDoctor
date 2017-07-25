package com.khantilchoksi.arztdoctor.data;

import android.provider.BaseColumns;

/**
 * Created by khantilchoksi on 25/07/17.
 */

public class AppointmentsContract {
    // To prevent someone from accidentally instantiating the contract class make the constructor private.
    private AppointmentsContract(){}

    /* Inner class that defines the table contents */
    public static final class AppointmentsEntry implements BaseColumns {
        public static final String TABLE_NAME = "appointments";

        public static final String COLUMN_APPOINTMENT_ID = "appointment_id";

        public static final String COLUMN_PATIENT_NAME = "patient_name";

        public static final String COLUMN_CLINIC_NAME = "clinic_name";

        public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";

        public static final String COLUMN_APPOINTMENT_DAY = "appointment_day";

        public static final String COLUMN_APPOINTMENT_START_TIME = "appointment_start_time";

        public static final String COLUMN_APPOINTMENT_END_TIME = "appointment_end_time";

    }

}
