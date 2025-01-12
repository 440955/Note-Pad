package com.mondol.mynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar1;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FloatingActionButton floatingActionButton;
    ListView listView;
    ArrayList<HashMap<String, String>>arrayList= new ArrayList<>();
    HashMap<String, String>hashMap= new HashMap<>();
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.statusbar));
        getWindow().setNavigationBarColor(ContextCompat.getColor(MainActivity.this, R.color.statusbar));

        toolbar1=findViewById(R.id.toolBar);
        drawerLayout=findViewById(R.id.drawerlayout);
        navigationView=findViewById(R.id.navigation_view);
        floatingActionButton= findViewById(R.id.fab_button);
        listView = findViewById(R.id.listView);
        databaseHelper = new DatabaseHelper(this);


        setSupportActionBar(toolbar1);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar1,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu, getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener( this);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddData.ROAD= true;
                startActivity(new Intent(MainActivity.this, AddData.class));
            }
        });



    }
//=====================================================

    public void loadData(Cursor cursor){
        if (cursor!=null && cursor.getCount()>0){
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()){
                int ID = cursor.getInt(0);
                String Type = cursor.getString(1);
                String Title = cursor.getString(2);
                String Details = cursor.getString(3);
                String Date = cursor.getString(4);
                String Time = cursor.getString(5);

                hashMap = new HashMap<>();
                hashMap.put("ID", ""+ID);
                hashMap.put("Type", Type);
                hashMap.put("Title", Title);
                hashMap.put("Details", Details);
                hashMap.put("Date", Date);
                hashMap.put("Time", Time);
                arrayList.add(hashMap);
            }
            listView.setAdapter(new Adapter());
        }
        else{
            arrayList= new ArrayList<>();
            hashMap= new HashMap<>();
            listView.setAdapter(new Adapter());

            AlertDialog alertDialog;
            AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
            final  View layoutView= getLayoutInflater().inflate(R.layout.savedialog, null);
            builder.setView(layoutView);
            alertDialog= builder.create();
            alertDialog.show();
        }
    }



//==========================Adapter=====================================
    public class Adapter extends BaseAdapter{
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv_details, tv_date;
            LayoutInflater layoutInflater = getLayoutInflater();
            View myView = layoutInflater.inflate(R.layout.item, parent, false);

            CardView relative_item= myView.findViewById(R.id.relative_item);
            tv_details = myView.findViewById(R.id.tv_details);
            tv_date = myView.findViewById(R.id.tv_date);

            hashMap = arrayList.get(position);

            tv_details.setText(hashMap.get("Details"));
            tv_date.setText(hashMap.get("Date"));

            relative_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hashMap= arrayList.get(position);

                    AddData.id=hashMap.get("ID");
                    AddData.type= hashMap.get("Type");
                    AddData.title= hashMap.get("Title");
                    AddData.details= hashMap.get("Details");
                    AddData.date= hashMap.get("Date");
                    AddData.time= hashMap.get("Time");
                    AddData.ROAD=false;
                    startActivity(new Intent(MainActivity.this, AddData.class));
                }
            });

            return myView;
        }
    }
//================================================================
    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadData(databaseHelper.getAllData());
    }
//=============================Navigation======================================
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
        if (menuitem.getItemId() == R.id.navi_home) {

        }else if (menuitem.getItemId()==R.id.navi_writeNote){
            startActivity(new Intent(MainActivity.this, AddData.class));
        }
        else if (menuitem.getItemId()== R.id.nevi_policy){
            if (isNetworkAvailable(MainActivity.this)){
                Policy.OPERATE="PrivacyPolicy";
                startActivity(new Intent(MainActivity.this, Policy.class));
            }
            else{
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        }
        else if (menuitem.getItemId()==R.id.nevi_about){
            if (isNetworkAvailable(MainActivity.this)){
                Policy.OPERATE="Developer";
                startActivity(new Intent(this, Policy.class));
            }
            else{
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        } else if (menuitem.getItemId()==R.id.shareApp) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/html");
            share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.arafat.mynote");
            startActivity(share);
        } else if (menuitem.getItemId()==R.id.rateUs) {
            Intent rate = new Intent(android.content.Intent.ACTION_VIEW);
            rate.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.arafat.mynote"));
            startActivity(rate);
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
//============================== network check ==================
    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }
//========================
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView =(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                loadData(databaseHelper.searchData(newText));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}