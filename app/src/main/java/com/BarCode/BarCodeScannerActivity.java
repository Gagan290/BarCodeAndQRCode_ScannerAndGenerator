package com.BarCode;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarCodeScannerActivity extends AppCompatActivity//extends BaseScannerActivity
        implements ZXingScannerView.ResultHandler {

    FrameLayout scannerFrameView;
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        setContentView(R.layout.activity_simple_scanner);
        //setupToolbar();

        //ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        scannerFrameView = (FrameLayout) findViewById(R.id.content_frame);

        /*mScannerView = new ZXingScannerView(this);
        scannerFrameView.addView(mScannerView);*/

        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                CustomZXingScannerView view = new CustomZXingScannerView(context);
                view.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimary));
                view.setLaserColor(ContextCompat.getColor(context, R.color.colorPrimary));
                view.setMaskColor(ContextCompat.getColor(context, R.color.colorTransparent));

                return view;
            }
        };
        scannerFrameView.addView(mScannerView);

    }

    @Override
    public void onResume() {
        super.onResume();

        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();

        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage("Contents = " + rawResult.getText() +
                "\n Format = " + rawResult.getBarcodeFormat().toString());
        AlertDialog alert1 = builder.create();
        alert1.show();


        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(BarCodeScannerActivity.this);
            }
        }, 2000);
    }
}
