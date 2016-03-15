package com.google.android.gms.samples.vision.face.facetracker;

import android.util.Log;
import java.util.ArrayList;


/**
 * Created by Joshua on 3/4/2016.
 *
 * Only prints values to show that it accepts the ArrayList
 *
 * @TODO: 3/4/2016 Need to implement Fatigue Checking system.
 */
public class Fatigue {

    protected ArrayList<FaceGraphic.FaceData> data;

    /**
     * Constructor for class
     * @param data, ArrayList passed to the class
     */
    public Fatigue(ArrayList data) {
        this.data = data;
    }

    /**
     * Setter for data
     * @param data, ArrayList of data being passed to class
     */
    public void setData(ArrayList data) {
        this.data = data;
    }

    /**
     * Getter for data
     * @return the ArrayList
     */
    public ArrayList getData() {
        return this.data;
    }

    /**
     * Prints the data using toString
     */
    public void printData() {
        for (Object info : data) {
            Log.i("TESTIINNGG", info.toString());
        }
    }

    public void checkIfFatigued(){
       checkEye();

    }

    public void checkEye(){
        int eyeCounter = 0;
        for(FaceGraphic.FaceData temp : data) {
            float leftEyeTemp = temp.getLeftEye();
            float rightEyeTemp = temp.getRightEye();

            if (leftEyeTemp < .30 && rightEyeTemp < .30){
                eyeCounter++;
                if (eyeCounter >= 10){
                    FaceGraphic.fatigueScore += 5;
                }
            }
            else{
                eyeCounter = 0;
            }

            Log.i("EyeCounterTest", "checkEye: leftEye = " + leftEyeTemp + ", checkEye: rightEye = " + rightEyeTemp);
        }
    }

    public void checkNodding(){
        //aaaaaahhhhhh god help us.....
    }

    public void checkHeadUpDown(){

    }
}
