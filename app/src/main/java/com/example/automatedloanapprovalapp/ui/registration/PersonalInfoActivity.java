package com.example.automatedloanapprovalapp.ui.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.automatedloanapprovalapp.R;

import java.io.Serializable;
import java.util.Calendar;

public class PersonalInfoActivity extends AppCompatActivity implements Serializable {
    private EditText fullNameEditText,emailEditText,dateOfBirthEditText,nationalityEditText,phoneNumberEditText,addressEditText;


    private Spinner genderSpinner;


    private String fullName, email, dateOfBirth, nationality, address, phoneNumber, selectedGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // Initialize UI elements
        fullNameEditText = findViewById(R.id.etFullName);
        emailEditText = findViewById(R.id.etEmail);
        dateOfBirthEditText = findViewById(R.id.etDateOfBirth);
        genderSpinner = findViewById(R.id.spinnerGender);
        nationalityEditText = findViewById(R.id.etNationality);
        addressEditText = findViewById(R.id.etAddress);
        phoneNumberEditText = findViewById(R.id.etPhoneNumber);

        dateOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });


        // Populate the gender spinner with options
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // Handle gender selection
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                 selectedGender = parentView.getItemAtPosition(position).toString();
                // You can store the selected gender or perform any other actions here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        Button nextButton = findViewById(R.id.btnNextPersonal);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate user input
                if (validateInput()) {
                    // Data is valid, proceed to the next step
                    moveToNextStep();
                }
            }
        });
    }

    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the current date as the default selected date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Handle the selected date (selectedYear, selectedMonth, selectedDay)
                        // Update the TextInputEditText with the selected date
                        String formattedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        dateOfBirthEditText.setText(formattedDate);
                    }
                },
                year, month, day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }


    private boolean validateInput() {
         fullName = fullNameEditText.getText().toString().trim();
         email = emailEditText.getText().toString().trim();
         dateOfBirth = dateOfBirthEditText.getText().toString().trim();
         nationality = nationalityEditText.getText().toString().trim();
         address = addressEditText.getText().toString().trim();
         phoneNumber = phoneNumberEditText.getText().toString().trim();

        // Validate each field
        if (fullName.isEmpty() || email.isEmpty() || dateOfBirth.isEmpty() ||
                nationality.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
            showToast("Please fill in all fields.");
            return false;
        }

        if (!isValidEmail(email)) {
            showToast("Invalid email address.");
            return false;
        }

        // Add more validation logic as needed for date of birth, phone number, etc.

        // All data is valid
        return true;
    }

    private boolean isValidEmail(String email) {
        // You can use a regular expression or other methods to validate the email format
        // For simplicity, this example uses a basic check
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void moveToNextStep() {
        // Create an Intent to navigate to the next activity
        Intent intent = new Intent(this, EmploymentDetailsActivity.class);

        // Create a RegistrationData object and set user details
        // Pass the RegistrationData object to the next activity with a different key
        // Pass each item individually
        intent.putExtra("fullName", fullName);
        intent.putExtra("email", email);
        intent.putExtra("dateOfBirth",dateOfBirth);
        intent.putExtra("nationalID", nationality);
        intent.putExtra("address", address);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("selectedGender",selectedGender);

        // Start the next activity
        startActivity(intent);
    }


    }
