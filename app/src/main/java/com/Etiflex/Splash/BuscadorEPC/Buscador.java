package com.Etiflex.Splash.BuscadorEPC;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.ValueIterator;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Etiflex.Splash.ConnectorManager;
import com.Etiflex.Splash.Methods;
import com.com.tools.Beeper;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.bean.RXInventoryTag;
import com.uhf.uhf.R;
import com.zebra.BarCodeReader;

public class Buscador extends AppCompatActivity {//implements BarCodeReader.DecodeCallback, BarCodeReader.ErrorCallback {

    private ProgressBar pb_potencia;
    private TextView txt_progress;
    private RelativeLayout PurplePanel, ROOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        setContentView(R.layout.activity_buscador);

        pb_potencia = findViewById(R.id.Buscador_progressBar);
        txt_progress = findViewById(R.id.txt_porcentaje_buscador);
        PurplePanel = findViewById(R.id.colored_panel);
        ROOT = findViewById(R.id.buscador_root_layout);

        Beeper.release();
        Beeper.init(this);

        if (savedInstanceState == null) {
            PurplePanel.setVisibility(View.INVISIBLE);

            ViewTreeObserver viewTreeObserver = PurplePanel.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            PurplePanel.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            PurplePanel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
            }
        }

        /*int num = BarCodeReader.getNumberOfReaders();
        bcr = BarCodeReader.open(num, getApplicationContext()); // Android 4.3 and above

        bcr.setDecodeCallback(this);

        bcr.setErrorCallback(this);

        bcr.setParameter(765, 0); // For QC/MTK platforms
        bcr.setParameter(764, 3);

        bcr.setParameter(687, 4);
        tg = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);*/

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 134){
            new ConnectorManager().Connect(rx);
            //doDecode();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == 134)
        ConnectorManager.connector.disConnect();
        return super.onKeyUp(keyCode, event);
    }

    private RXObserver rx = new RXObserver(){
        @Override
        protected void onInventoryTag(RXInventoryTag tag) {

            if(tag.strEPC.equals("50 38 98 00 00 00 00 00 00 05 29 59")){
                pb_potencia.setProgress(Math.round(Float.parseFloat(tag.strRSSI)));
                txt_progress.setText(String.valueOf(tag.strRSSI)+"%");
                new Methods().PlayBeep_Short();
            }

        }

        @Override
        protected void onInventoryTagEnd(RXInventoryTag.RXInventoryTagEnd endTag) {
            ConnectorManager.mReader.realTimeInventory((byte) 0xff, (byte) 0x01);
        }
    };

    private void circularRevealActivity() {

        int cx = PurplePanel.getWidth() / 2;
        int cy = PurplePanel.getHeight() / 2;

        float finalRadius = Math.max(PurplePanel.getWidth(), PurplePanel.getHeight());
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(PurplePanel, cx, cy, 0, finalRadius);
        circularReveal.setDuration(1000);
        circularReveal.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) { }
            @Override
            public void onAnimationEnd(Animator animation) {
                ROOT.setVisibility(View.VISIBLE);
                PurplePanel.setVisibility(View.GONE);
            }
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
        PurplePanel.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    /*static final int STATE_IDLE = 0;
    static final int STATE_DECODE = 1;
    static final int STATE_HANDSFREE = 2;
    static final int STATE_PREVIEW = 3;
    static final int STATE_SNAPSHOT = 4;
    static final int STATE_VIDEO = 5;

    private int state = STATE_IDLE;
    private String decodeDataString;
    private static int decCount = 0;

    private long mStartTime;
    private long mBarcodeCount = 0;
    private long mConsumTime;
    private ToneGenerator tg = null;

    static private boolean sigcapImage = true;

    private int trigMode = BarCodeReader.ParamVal.LEVEL;

    private BarCodeReader bcr = null;


    private void doDecode() {
        if (setIdle() != STATE_IDLE)
            return;

        state = STATE_DECODE;
        decCount = 0;
        decodeDataString = new String("");
        try {
            mStartTime = System.currentTimeMillis();
            bcr.startDecode();
        } catch (Exception e) { }

    }

    private int setIdle() {
        int prevState = state;
        int ret = prevState;

        state = STATE_IDLE;
        switch (prevState) {
            case STATE_HANDSFREE:
            case STATE_DECODE:
                bcr.stopDecode();
                break;

            case STATE_VIDEO:
                bcr.stopPreview();
                break;

            case STATE_SNAPSHOT:
                ret = STATE_IDLE;
                break;

            default:
                ret = STATE_IDLE;
        }
        return ret;
    }

    private boolean isHandsFree()
    {
        return (trigMode == BarCodeReader.ParamVal.HANDSFREE);
    }

    private String byte2hex(byte [] buffer){
        String h = "";

        for(int i = 0; i < buffer.length; i++){
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if(temp.length() == 1){
                temp = "0" + temp;
            }
            h = h + " "+ temp;
        }

        return h;

    }

    private boolean isAutoAim()
    {
        return (trigMode == BarCodeReader.ParamVal.AUTO_AIM);
    }

    private boolean beepMode = true;
    private void beep() {
        if (tg != null)
            tg.startTone(ToneGenerator.TONE_CDMA_NETWORK_CALLWAITING);
    }

    public void onDecodeComplete(int symbology, int length, byte[] data, BarCodeReader reader){
        if (state == STATE_DECODE)
            state = STATE_IDLE;

        if(length == BarCodeReader.DECODE_STATUS_MULTI_DEC_COUNT)
            decCount = symbology;

        if (length > 0)
        {
            if (isHandsFree()==false && isAutoAim()==false)
                bcr.stopDecode();

            if (symbology == 0x69)
            {
                if (sigcapImage)
                {
                    Bitmap bmSig = null;
                    int scHdr = 6;
                    if (length > scHdr)
                        bmSig = BitmapFactory.decodeByteArray(data, scHdr, length-scHdr);
                }


                decodeDataString += new String(data);

                mBarcodeCount++;
                long consum = System.currentTimeMillis() - mStartTime;
                mConsumTime += consum;
                decodeDataString += "\n\r" + "本次消耗时间:" + consum + "毫秒" + "\n\r" + "平均速度:" + (mConsumTime / mBarcodeCount) + "毫秒/个";




            } else {

                if (symbology == 0x99)	{
                    symbology = data[0];
                    int n = data[1];
                    int s = 2;
                    int d = 0;
                    int len = 0;
                    byte d99[] = new byte[data.length];
                    for (int i=0; i<n; ++i)
                    {
                        s += 2;
                        len = data[s++];
                        System.arraycopy(data, s, d99, d, len);
                        s += len;
                        d += len;
                    }
                    d99[d] = 0;
                    data = d99;
                }

                Log.d("012", "ret="+byte2hex(data));

                decodeDataString += new String(data);

                mBarcodeCount++;
                long consum = System.currentTimeMillis() - mStartTime;
                mConsumTime += consum;
                decodeDataString += "\n\r" + "本次消耗时间:" + consum + "毫秒" + "\n\r" +  "平均速度:" + (mConsumTime / mBarcodeCount) + "毫秒/个";
                Log.e("peashepe--->",decodeDataString);
                txt_progress.setText(new String(data));


                if(decCount > 1)
                {

                    decodeDataString += new String(" ; ");
                }
                else
                {
                    decodeDataString = new String("");

                }
            }

            if (beepMode)
                beep();
        }
        else
        {
            switch (length)
            {
                case BarCodeReader.DECODE_STATUS_TIMEOUT:

                    break;

                case BarCodeReader.DECODE_STATUS_CANCELED:

                    break;

                case BarCodeReader.DECODE_STATUS_ERROR:
                default:


                    break;
            }
        }
}


    @Override
    public void onEvent(int event, int info, byte[] data, BarCodeReader reader) {

    }

    @Override
    public void onError(int error, BarCodeReader reader) {

    }*/
}
