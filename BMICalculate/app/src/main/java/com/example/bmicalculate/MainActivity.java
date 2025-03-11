package com.example.bmicalculate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
     Button btnCalculate;
     RadioButton rdoMale, rdoFemale;
     EditText editAge, editFeet, editInches, editHeight;
     TextView textGiatri, textBMI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        String alertText = "This is my variable text";
        Toast.makeText(this,alertText, Toast.LENGTH_SHORT).show();
        findView();
        setupButtonClickListenr();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupButtonClickListenr() {
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int age = Integer.parseInt(editAge.getText().toString());
                double bmi = calculateBMI();
//                DisplayResult(bmi);
                if(age >= 18){
                    DisplayResult(bmi);
                }else{
                    DisplayGuidance(bmi);
                }

            }
        });
    }

    private double calculateBMI() {
        int feet = Integer.parseInt(editFeet.getText().toString());
        int inches = Integer.parseInt(editInches.getText().toString());
        int height = Integer.parseInt(editHeight.getText().toString());

        int totalInches = (feet * 12) + inches;

        double heightInMeters = totalInches * 0.0254;

        double bmi = height / (heightInMeters * heightInMeters);

        return bmi;

    }

    private void DisplayResult(double bmi){
        String bmiText = String.format("%.2f", bmi);
        String comment = "";

        if(bmi < 18.5){
            comment = "-You are underweight.";
        }else if(bmi < 25){
            comment = "-You are overweight.";
        }else{
            comment = "-You are a healthy weight.";
        }


        textBMI.setText(bmiText);
        textGiatri.setText(comment);
    }

    private  void DisplayGuidance(double bmi){
        String bmiText = String.format("%.2f", bmi);
        String comment = "";
        if(rdoMale.isChecked()){
            comment = "-As you are under 18, please consult with you doctor for the healthy range boys.";
        }else if(rdoFemale.isChecked()){
            comment = "-As you are under 18, please consult with you doctor for the healthy range girls.";
        }else{
            comment = "-As you are under 18, please consult with you doctor for the healthy range.";
        }
        textBMI.setText(bmiText);
        textGiatri.setText(comment);
    }

    private void findView(){
        btnCalculate = findViewById(R.id.btnCalculate);
        rdoMale = findViewById(R.id.rdoMale);
        rdoFemale = findViewById(R.id.rdoFemale);
        editAge = findViewById(R.id.editAge);
        editFeet = findViewById(R.id.editFeet);
        editInches = findViewById(R.id.editInches);
        editHeight = findViewById(R.id.editHeight);
        textGiatri = findViewById(R.id.textGiatri);
        textBMI = findViewById(R.id.textBMI);
    }
}