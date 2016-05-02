package com.google.android.gms.samples.vision.face.facetracker;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Joshua on 3/4/2016.
 *
 * Only prints values to show that it accepts the ArrayList
 *
 * @TODO: 3/4/2016 Need to implement Fatigue Checking system.
 */
public class Fatigue {

    protected ArrayList<FaceGraphic.FaceData> data;
    protected List<Float> y = new ArrayList<Float>();
    protected List<Integer> x = new ArrayList<Integer>();
    /**
     * Constructor for class
     * @param data, ArrayList passed to the class
     */
    public Fatigue(ArrayList data) {
        this.data = data;


        for(int i = -12; i <= 12; i++){
            x.add(i);
        }

        for(FaceGraphic.FaceData temp : this.data) {
            y.add(temp.getBottom());
            Log.i("TEST", "Y ==> : " + temp.getBottom());
        }
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
        if(this.data.size() > 40){
            checkNodding();
        }
        else {
            checkEye();
            checkHeadUpDown();
        }

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
        MatrixVarible tempMaxtrixVarible = new MatrixVarible();
        ParabulaConstants tempabc = new ParabulaConstants();

        List<ParabulaConstants> abcArrayList = new ArrayList<ParabulaConstants>();

        for(int i = 0; i < 25; i = i + 5) {
            tempMaxtrixVarible = fillMatrixVaribles(x, y, i);
            tempabc = computeParabulaConstants(tempMaxtrixVarible);

            Log.i("TEST", "A value ==> : " + tempabc.A);
            Log.i("TEST", "B value ==> : " + tempabc.B);
            Log.i("TEST", "C value ==> : " + tempabc.C);

            if (tempabc.C >= 17.8) {
                FaceTrackerActivity.playSound();
                if(computerRsquared(tempabc,y,i)) {
                    FaceTrackerActivity.playReadySound();
                }
            }
            abcArrayList.add(tempabc);
        }
        Log.i("TEST","THIS IS ABCLISTSIZE ==> " + abcArrayList.size());
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

    public MatrixVarible fillMatrixVaribles(List<Integer> x, List<Float> y,int yOffset){
        MatrixVarible matrixVaribles = new MatrixVarible();

        for(int i = 0; i < x.size(); i++){
            matrixVaribles.sumx2 += (x.get(i) * x.get(i) * 3);
            matrixVaribles.sumx4 += x.get(i) * x.get(i) * x.get(i) * x.get(i);
        }

        for(int i = 0; i < x.size() * 3; i++) {
            matrixVaribles.sumx2y += x.get(i % x.size()) * x.get(i % x.size()) * y.get(i + yOffset);
            matrixVaribles.sumxy += x.get(i % x.size()) * y.get(i + yOffset);
            matrixVaribles.sumy += y.get(i + yOffset);
        }

        matrixVaribles.sizeOfList = x.size();

        return matrixVaribles;
    }

    public ParabulaConstants computeParabulaConstants(MatrixVarible matrixMaribles){
        ParabulaConstants abc = new ParabulaConstants();

        double numeratorA = 0.0;
        double numeratorB = 0.0;
        double numeratorC = 0.0;
        double denomiator = 0.0;

        denomiator = (matrixMaribles.sizeOfList * matrixMaribles.sumx2 * matrixMaribles.sumx4) - (matrixMaribles.sumx2 * matrixMaribles.sumx2 * matrixMaribles.sumx2);
        numeratorA = (matrixMaribles.sumy * matrixMaribles.sumx2 * matrixMaribles.sumx4) - (matrixMaribles.sumx2 * matrixMaribles.sumx2 * matrixMaribles.sumx2y);
        numeratorB = (matrixMaribles.sizeOfList * matrixMaribles.sumxy * matrixMaribles.sumx4) - (matrixMaribles.sumx2 * matrixMaribles.sumx2 * matrixMaribles.sumxy);
        numeratorC = (matrixMaribles.sizeOfList * matrixMaribles.sumx2 * matrixMaribles.sumx2y) - (matrixMaribles.sumy * matrixMaribles.sumx2 * matrixMaribles.sumx2);

        abc.A = (numeratorA/denomiator);
        abc.B = (numeratorB/denomiator);
        abc.C = (numeratorC/denomiator);

        return abc;
    }

    public boolean computerRsquared(ParabulaConstants abc, List<Float> y,int yOffset){
        boolean isResonable = false;
        float total = 0;
        float avg = 0;
        float numerator = 0;
        float denomiator = 0;

        for(int i = 0; i < x.size(); i++){
            total += y.get(i + yOffset);
        }

        avg = total/y.size();

        for(int i = 0; i < x.size(); i++)
        {
            numerator += Math.pow(((abc.A + abc.B * i + abc.C * (i * i)) - y.get(i+yOffset)), 2);
            denomiator += Math.pow(((y.get(i + yOffset)) - avg),2);
        }

        if(denomiator != 0) {
            Log.i("TEST", "THIS IS R SQUARED VALUE ==> " + (1 - (numerator / denomiator)));


            if (1 - (numerator / denomiator) < -26.5) {
                isResonable = true;
            }
        }
        return isResonable;
    }
}

class ParabulaConstants {
    public double A = 0.0;
    public double B = 0.0;
    public double C = 0.0;
}

class MatrixVarible{
    double sumx2 = 0.0;
    double sumx4 = 0.0;
    double sumx2y = 0.0;
    double sumxy = 0.0;
    double sumy = 0.0;
    int sizeOfList = 0;
}