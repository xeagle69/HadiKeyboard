package com.example.hadikeyboard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class QrReaderActivity extends AppCompatActivity {


    private static final String TAG = "xeagle6913";

    private String lastScannedText;


    private DecoratedBarcodeView barcodeView;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null) {
                // Prevent duplicate scans
                return;
            }


            lastScannedText = result.getText();
            Log.d(TAG, "lastScannedText >>> " + lastScannedText);


            Toast toast =  Toast.makeText(QrReaderActivity.this,lastScannedText,Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();










            Intent intent = new Intent();
            intent.setAction("QrHadiIntent");
            intent.putExtra("action", "QrReader");
            intent.putExtra("scanned_string", lastScannedText);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            sendBroadcast(intent);
            finish();



        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 10112) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                barcodeView.resume();
                barcodeView.decodeContinuous(callback);
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 10112);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_reader);
        barcodeView = findViewById(R.id.barcode_scanner);


        if (ContextCompat.checkSelfPermission(QrReaderActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 10112);
        }else {
            barcodeView.resume();
            barcodeView.decodeContinuous(callback);
        }


    }




    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}