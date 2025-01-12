package com.mondol.mynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AddData extends AppCompatActivity {

    public static boolean ROAD= true;
    public static String id= "";
    public static String type= "";
    public static String title= "";
    public static String details= "";
    public static String date= "";
    public static String time= "";


    TextView tv_date, tv_time;
    ImageView img_back, img_tikMark, delete_icon, image_share;
    EditText ed_noteType, ed_title, ed_details;
    DatabaseHelper databaseHelper;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        delete_icon= findViewById(R.id.delete_icon);
        image_share= findViewById(R.id.image_share);
        tv_time= findViewById(R.id.tv_time);
        tv_date= findViewById(R.id.tv_date);
        img_back = findViewById(R.id.img_back);
        img_tikMark = findViewById(R.id.img_tikMark);
        ed_noteType = findViewById(R.id.ed_noteType);
        ed_title = findViewById(R.id.ed_title);
        ed_details = findViewById(R.id.ed_details);
        databaseHelper = new DatabaseHelper(this);
        getWindow().setStatusBarColor(ContextCompat.getColor(AddData.this, R.color.statusbar));
        getWindow().setNavigationBarColor(ContextCompat.getColor(AddData.this, R.color.statusbar));


//============================== DAdd note ===============================================
        img_tikMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ROAD==true){

                    if (ed_noteType.getText().toString().length()<=0){
                        ed_noteType.setText("Personal");
                    }
                    else if (ed_title.getText().toString().length()<=0){
                        ed_title.setError("Input Title");
                    }
                    else if (ed_details.getText().toString().length()<=0){
                        ed_details.setError("Type Your Note");
                    }
                    else{
                        databaseHelper.addData(ed_noteType.getText().toString(), ed_title.getText().toString(), ed_details.getText().toString());
                        Toast.makeText(AddData.this, "Save", Toast.LENGTH_SHORT).show();
                        ed_noteType.setText("");
                        ed_title.setText("");
                        ed_details.setText("");
                        if (mInterstitialAd!=null){
                            mInterstitialAd.show(AddData.this);
                        }
                    }
                }
                else {
                    databaseHelper.updateData(id, ed_noteType.getText().toString(), ed_title.getText().toString(), ed_details.getText().toString());
                    if (mInterstitialAd!=null){
                        mInterstitialAd.show(AddData.this);
                    }
                    Toast.makeText(AddData.this, "Update", Toast.LENGTH_SHORT).show();
                }
            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//================================ Update note =============================================



        if (ROAD==false){
            delete_icon.setVisibility(View.VISIBLE);
            image_share.setVisibility(View.VISIBLE);
            ed_noteType.setText(type);
            ed_title.setText(title);
            ed_details.setText(details);
            tv_time.setText("Create time: "+time);
            tv_date.setText("Create date: "+date);
        }
        else {
            image_share.setVisibility(View.GONE);
            delete_icon.setVisibility(View.GONE);
            ed_noteType.setText("");
            ed_title.setText("");
            ed_details.setText("");
        }

        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(AddData.this);
                final View customLayout = getLayoutInflater().inflate(R.layout.dialoginterface, null);
                builder.setView(customLayout);
                dialog = builder.create();
                dialog.show();

                Button btn_No, btn_yes;
                btn_yes= customLayout.findViewById(R.id.btn_yes);
                btn_No= customLayout.findViewById(R.id.btn_no);

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseHelper.delete(id);
                        ed_noteType.setText("");
                        ed_title.setText("");
                        ed_details.setText("");
                        tv_time.setText("Time");
                        tv_date.setText("Date");
                        dialog.dismiss();
                        if (mInterstitialAd!=null){
                            mInterstitialAd.show(AddData.this);
                        }
                        onBackPressed();

                    }
                });

                btn_No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });



            }
        });

        image_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor= databaseHelper.getShare(id);

                if (cursor!=null && cursor.moveToNext()){
                    String shareTitle= cursor.getString(2);
                    String shareDetails= cursor.getString(3);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,"Title: "+shareTitle+"\nDetails: "+shareDetails);
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }

            }
        });

        fullScreenAd();
    }
//===================================================

    public void fullScreenAd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-6621199192974495/6442088914", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                fullScreenAd();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                mInterstitialAd=null;
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                fullScreenAd();
                            }
                        });

                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}