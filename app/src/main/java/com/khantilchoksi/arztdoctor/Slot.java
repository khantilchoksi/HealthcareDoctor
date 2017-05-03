package com.khantilchoksi.arztdoctor;

/**
 * Created by khantilchoksi on 28/03/17.
 */

public class Slot {
    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Slot() {

    }

    private String slotId;
    private String day = null;
    private int dayIndex = 0;
    private String startTime = null;
    private String endTime = null;
    private int firstTimeSlotFees;
    private int normalSlotFees;

    public String getSlotId() {
        return slotId;
    }


    public void setFirstTimeSlotFees(int firstTimeSlotFees) {
        this.firstTimeSlotFees = firstTimeSlotFees;
    }

    public void setNormalSlotFees(int normalSlotFees) {
        this.normalSlotFees = normalSlotFees;
    }

    public int getFirstTimeSlotFees() {

        return firstTimeSlotFees;
    }

    public int getNormalSlotFees() {
        return normalSlotFees;
    }

    public Slot(String slotId, String day, String startTime, String endTime, int firstTimeSlotFees, int normalSlotFees) {

        this.slotId = slotId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.firstTimeSlotFees = firstTimeSlotFees;
        this.normalSlotFees = normalSlotFees;

    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }
}
