package com.example.arbitragetracker.scanner;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

public class Scanner {
    private Context context;

    public Scanner(Context context) {
        this.context = context;
    }

    public void startScan(@NonNull BarcodeScanListener listener) {
        //sets settings for the scanner
        //formats are what is being scanned(QR or barcodes)
        GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
                .build();

        //runs the scanner
        GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(context, options);

        scanner.startScan()
                .addOnSuccessListener(barcode -> {
                    // Pass scanned barcode value to listener
                    listener.onBarcodeScanned(barcode.getRawValue());
                })
                .addOnFailureListener(e -> {
                    // Handle scan failure
                    Log.e("BarcodeScanner", "Barcode scan failed: " + e.getMessage());
                    listener.onScanFailed(e);
                });
    }

    //This interface is needed to notify the caller when the scanning operation is complete
    public interface BarcodeScanListener {
        void onBarcodeScanned(@NonNull String barcodeValue);
        void onScanFailed(@NonNull Exception e);
    }
}
