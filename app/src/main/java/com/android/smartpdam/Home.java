package com.android.smartpdam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.smartpdam.Adapter.waterflowAdapter;
import com.android.smartpdam.model.waterflowModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private TextView textViewUser;
    private RecyclerView recWaterflow;
    private waterflowAdapter WaterflowAdapter;
    private List<waterflowModel> waterflowList;
    private FirebaseAuth mAuth;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("waterflowData"); // Reference to waterflowData

        textViewUser = findViewById(R.id.textViewUser);
        recWaterflow = findViewById(R.id.recyclerView);
        Button buttonLogout = findViewById(R.id.buttonLogout);

        // Set up RecyclerView
        recWaterflow.setLayoutManager(new LinearLayoutManager(this));
        waterflowList = new ArrayList<>();
        WaterflowAdapter = new waterflowAdapter(this, waterflowList);
        recWaterflow.setAdapter(WaterflowAdapter);

        // Get the full name passed from LoginActivity
        String fullName = getIntent().getStringExtra("fullName");
        if (fullName != null) {
            textViewUser.setText(fullName);
        }

        // Load waterflow data from Realtime Database
        loadWaterflowData();

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(Home.this, Login.class));
                finish();
            }
        });
    }

    private void loadWaterflowData() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                waterflowList.clear();
                double totalRateAandB = 0;
                double rateC = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    waterflowModel model = snapshot.getValue(waterflowModel.class);
                    if (model != null) {
                        waterflowList.add(model);
                        if (model.getNama().equals("Keran C")) {
                            rateC = model.getRate1();
                        } else {
                            totalRateAandB += model.getRate1();
                        }
                    }
                }
                WaterflowAdapter.notifyDataSetChanged();

                // Check for leakage
                if (rateC < totalRateAandB) {
                    Toast.makeText(Home.this, "Kebocoran terdeteksi!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, "Failed to load data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
