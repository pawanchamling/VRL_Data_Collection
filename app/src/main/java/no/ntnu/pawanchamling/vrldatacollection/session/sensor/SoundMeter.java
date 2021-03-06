package no.ntnu.pawanchamling.vrldatacollection.session.sensor;

/**
 * Created by Pawan Chamling on 10/04/15.
 */

import java.io.IOException;
import android.media.MediaRecorder;
import android.util.Log;

public class SoundMeter {
    // This file is used to record voice
    static final private double EMA_FILTER = 0.6;

    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;

    public void start() {

        if (mRecorder == null) {
            Log.i("@@@SoundMeter", "Starting SoundMeter");

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");

            try {
                mRecorder.prepare();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            mRecorder.start();
            mEMA = 0.0;
        }
    }

    public void stop() {
        if (mRecorder != null) {
           // System.out.println("@@@ Stopping SoundMeter");

            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
        }
        Log.i("@@@SoundMeter", "Stopping SoundMeter: Done");
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude()/2700.0);
        else
            return 0;

    }

    public double getAmplitude2() {

        start();

        if (mRecorder != null) {
            double amp = mRecorder.getMaxAmplitude() / 2700.0;
            Log.i("@@@SoundMeter", "amp = " + amp);
            stop();
            return (amp);
        }
        else {
            return 0;
        }


    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }
}

