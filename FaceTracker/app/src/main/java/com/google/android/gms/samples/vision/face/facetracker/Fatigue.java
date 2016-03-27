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
        checkHeadUpDown();
    }

    public void checkEye(){
        int eyeCounter = 3;
        for(FaceGraphic.FaceData temp : data) {

            float leftEyeTemp = temp.getLeftEye();
            float rightEyeTemp = temp.getRightEye();

            if (leftEyeTemp < .30 && rightEyeTemp < .30){
                eyeCounter++;
                if (eyeCounter >= 20){
                    FaceGraphic.fatigueScore += 5;
                }
            }
            else{
                eyeCounter = 0;
            }


           //Log.i("EyeCounterTest", "checkEye: leftEye = " + leftEyeTemp + ", checkEye: rightEye = " + rightEyeTemp);
        }

        //Log.i("TESTIINNGG", "POOoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooOP");
    }

    public void checkNodding(){

    }

    public void checkHeadUpDown(){
        int headDownUpCounter = 3;
        for(FaceGraphic.FaceData temp : data) {
            float sizeOfSquare = temp.getBottom() - temp.getTop();
            //Log.i("SizeOfSquare", "POOoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooOP " + sizeOfSquare);
            sizeOfSquare = sizeOfSquare * (float).3;
            if(temp.getBottom() > FaceGraphic.baseline + sizeOfSquare * .5){
                headDownUpCounter++;
                if(headDownUpCounter > 10){
                    FaceGraphic.fatigueScore += 50;
                }
            }
            else if(temp.getBottom() < FaceGraphic.baseline - sizeOfSquare * .9){
                headDownUpCounter++;
                if(headDownUpCounter > 10){
                    FaceGraphic.fatigueScore += 50;
                }
            }
            else{
                headDownUpCounter = 0;
            }
        }

    }
}
