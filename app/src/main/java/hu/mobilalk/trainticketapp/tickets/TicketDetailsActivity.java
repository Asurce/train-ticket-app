package hu.mobilalk.trainticketapp.tickets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import hu.mobilalk.trainticketapp.R;


public class TicketDetailsActivity extends AppCompatActivity {

    TicketItem ticket;

    // IMAGE
    Bitmap bitmap;
    ImageView imageView;

    // DETAILS
    TextView originCityTextView;
    TextView destCityTextView;
    TextView departTimeTextView;
    TextView arriveTimeTextView;
    TextView travelTimeTextView;
    TextView distanceTextView;
    TextView comfortTextView;
    TextView discountTextView;
    TextView priceButton;

    // BUTTONS
    Button downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);

        // ANDROID, MISC
        ticket = (TicketItem) getIntent().getSerializableExtra("ticketData");
        if (ticket == null) finish();

        // IMAGE
        imageView = findViewById(R.id.qrCode);
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            assert ticket != null;
            BitMatrix mMatrix = mWriter.encode(ticket.toString(), BarcodeFormat.PDF_417, 800, 200);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            bitmap = mEncoder.createBitmap(mMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // DETAILS
        originCityTextView = findViewById(R.id.originCityTextView);
        destCityTextView = findViewById(R.id.destCityTextView);
        departTimeTextView = findViewById(R.id.departTimeTextView);
        arriveTimeTextView = findViewById(R.id.arriveTimeTextView);
        travelTimeTextView = findViewById(R.id.travelTimeTextView);
        distanceTextView = findViewById(R.id.distanceTextView);
        comfortTextView = findViewById(R.id.comfortTextView);
        discountTextView = findViewById(R.id.discountTextView);
        priceButton = findViewById(R.id.priceTextView);

        originCityTextView.setText((ticket).getOriginCity());
        destCityTextView.setText((ticket).getDestCity());
        departTimeTextView.setText(DateFormat.format("HH:mm", (ticket).getDepartTime()));
        arriveTimeTextView.setText(DateFormat.format("HH:mm", (ticket).getArriveTime()));
        travelTimeTextView.setText((ticket).getTravelTime() + " perc");
        distanceTextView.setText((ticket).getDistance() + " km");
        comfortTextView.setText((ticket).getComfort());
        discountTextView.setText((ticket).getDiscount());
        priceButton.setText((ticket).getPrice() + "Ft");

        // BUTTONS
        downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(view -> {
            if(checkPermissionGranted()){
                Toast.makeText(this, "Barcode saved!", Toast.LENGTH_SHORT).show();
                saveToInternalStorage(bitmap);
            }else{
                requestPermission();
            }
        });

        // ACTION BAR
        getSupportActionBar().setTitle(R.string.ticket_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private boolean checkPermissionGranted(){
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            // Permission has already been granted
            return  true;
        } else {
            return false;
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }


    private void saveToInternalStorage(Bitmap bitmapImage){
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");
        File path = new File(directory,"ticket_"+ ticket.hashCode() +".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}