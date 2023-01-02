package com.example.hostelofdiu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class add_building extends AppCompatActivity implements View.OnClickListener {

    Button back_btn, add_building;
    TextInputLayout building_name_edt, floor_edt, room_in_floor_edt, details;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HostelOfDIU);
        setContentView(R.layout.activity_add_building);
        getSupportActionBar().hide();


        building_name_edt = findViewById(R.id.add_building_page_building_name_edttxt_id);
        floor_edt = findViewById(R.id.add_building_page_building_flor_edttxt_id);
        room_in_floor_edt = findViewById(R.id.add_building_page_building_room_edttxt_id);
        details = findViewById(R.id.add_building_page_building_description_edttxt_id);

        back_btn = findViewById(R.id.add_building_page_back_btn_id);
        add_building = findViewById(R.id.add_building_page_add_building_btn_id);

        back_btn.setOnClickListener(this);
        add_building.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_building_page_back_btn_id) {
            finish();
        } else if (view.getId() == R.id.add_building_page_add_building_btn_id) {
            if (!validate_building_name_floor_room()) {
                return;
            } else {
                String floors = floor_edt.getEditText().getText().toString().trim();
                String rooms = room_in_floor_edt.getEditText().getText().toString().trim();
                String building_name = building_name_edt.getEditText().getText().toString().trim();
                String building_details = details.getEditText().getText().toString().trim();
                db.collection("buildings").whereEqualTo("building_name", building_name).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    int rooms_int = Integer.parseInt(rooms);
                                    int floors_int = Integer.parseInt(floors);
                                    int total_rooms = rooms_int * floors_int;
                                    Map<String, String> building = new HashMap<>();
                                    building.put("building_name", building_name);
                                    building.put("total_flor", floors);
                                    building.put("total_room_inflor", rooms);
                                    building.put("available_rooms", total_rooms + "");
                                    building.put("building_details", building_details);
                                    db.collection("buildings").add(building);
                                    Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "There is a Building already exist with that Name !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        }
    }

    private Boolean validate_building_name_floor_room() {
        String val_0 = floor_edt.getEditText().getText().toString().trim();
        String val_1 = room_in_floor_edt.getEditText().getText().toString().trim();
        String val_2 = building_name_edt.getEditText().getText().toString().trim();
        String val_3 = details.getEditText().getText().toString().trim();

        if (val_2.isEmpty()) {
            building_name_edt.setError("Field can not be empty");
            return false;
        } else if (val_0.isEmpty()) {
            floor_edt.setError("Field can not be empty");
            return false;
        } else if (val_1.isEmpty()) {
            room_in_floor_edt.setError("Field can not be empty");
            return false;
        } else if (val_3.isEmpty()) {
            details.setError("Field can not be empty");
            return false;
        } else {
            floor_edt.setError(null);
            room_in_floor_edt.setError(null);
            building_name_edt.setError(null);
            details.setError(null);
            return true;
        }
    }
}