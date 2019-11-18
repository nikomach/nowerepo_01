package com.zacharadamian.drunkornot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    TextView txtData;
    Button btnGo;
    Spinner spSex;
    EditText txtMass;
    EditText txtEthanolIntake;
    Button btnCalculate;
    TextView txtBACResult;

    ArrayAdapter<CharSequence> adapter;
    public void initView() {
        btnGo = findViewById(R.id.btnGo);
        txtData = this.findViewById(R.id.txtData);
        spSex = findViewById(R.id.spSex);
        txtMass = findViewById(R.id.editTextMass);
        txtEthanolIntake = findViewById(R.id.editTextEthanolIntake);
        btnCalculate = findViewById(R.id.btnCalculate);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_dropdown_item_1line);
        spSex.setAdapter(adapter);
        spSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (spSex.getSelectedItem().equals("male")) {
//
//                }else{
//
//                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spSex.setSelection(0);
            }
        });
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("sensor");
                Query last = mDatabase.orderByKey().limitToLast(1);
                last.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String result = ds.getValue().toString();
                            txtData.setText(result);
                        }
                    }
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        txtData.setText("error");
                    }
                });
            }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sex sex = spSex.getSelectedItem().equals("Male") ? Sex.Male : Sex.Female;
                Body body = new Body(sex, Integer.valueOf(txtMass.getText().toString()));
                txtBACResult.setText(String.valueOf(body.CalculateBAC(Integer.valueOf(txtEthanolIntake.getText().toString()), 1))); //hardcoded drinkingSpan WIP
            }
        });
    }
}
