package com.example.remindme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.remindme.HomeMenu.HomeScreen;
import com.example.remindme.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterTabFragment extends Fragment {

    EditText registerName, registerEmail, registerPassword, registerConfPass;
    FirebaseAuth fAuth;
    DatabaseReference fDatabase;
    User user;
    Context context;
    String TAG = "RegisterTabFragment";
    SharedPreferences shpref;

    // Error Messages for registration fields
    CharSequence nameToast = "Username is required";
    CharSequence nameCheckToast = "Username is not between 5 and 16 characters";
    CharSequence emailToast = "Email is required";
    CharSequence emailCheckToast = "This is not a valid email. Please provide a TU/e email";
    CharSequence passwordToast = "Password is required";
    CharSequence confPassToast = "Please confirm your password";
    CharSequence confPassCheckToast = "The passwords do not match";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        // Necessary initializations of views/contexts/firebase
        View v = inflater.inflate(R.layout.fragment_register_tab, container, false);
        context = getContext().getApplicationContext();

        // References to the xml interface
        Button button = v.findViewById(R.id.registerbutton2);
        registerName = v.findViewById(R.id.registerName);
        registerEmail = v.findViewById(R.id.registerEmail);
        registerPassword = v.findViewById(R.id.registerPassword);
        registerConfPass = v.findViewById(R.id.registerConfPass);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://use-iot-default-rtdb.europe-west1.firebasedatabase.app/")
                .child("users");

        setTestingInput();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gets input from the fields
                String name = registerName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String confPass = registerConfPass.getText().toString();

                shpref = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shpref.edit();

                editor.putString("username", name);
                editor.apply();

                if(!checkInputFields(name, email, password, confPass)) {
                    return;
                }

                registerUser(name, email, password);

            }
        });

        return  v;
    }

    private void registerUser(String name, String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String uid;

                Log.d(TAG, "user account successfully created");

                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(uid == null) {
                    Log.d(TAG, "UID is null");
                    return;
                }

                user = new User(uid, name, email);
                fDatabase.child(uid).setValue(user).addOnSuccessListener(unused -> {
                    Log.d(TAG, "user added to the database successfully");
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "Error while adding user to database " + e.getMessage());
                });

                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                openActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkInputFields(String name, String email, String password, String confPass) {
        // Checks requirements for the inputs
        if (name.isEmpty()) {
            registerName.setError(nameToast);
            return false;
        }

        if (email.isEmpty()) {
            registerEmail.setError(emailToast);
            return false;
        }

        if (password.isEmpty()) {
            registerPassword.setError(passwordToast);
            return false;
        }

        if (confPass.isEmpty()) {
            registerConfPass.setError(confPassToast);
            return false;
        }

        if (name.length() < 5 || name.length() > 16) {
            registerName.setError(nameCheckToast);
            return false;
        }

        if (!(email.contains("@") && email.contains("."))) {
            registerEmail.setError(emailCheckToast);
            return false;
        }


        if (!password.equals(confPass)) {
            Toast toast = Toast.makeText(getContext(), password + " is not the same as " + confPass, Toast.LENGTH_SHORT);
            toast.show();
            registerPassword.setError(confPassCheckToast);
            registerConfPass.setError(confPassCheckToast);
            return false;
        }

        return true;
    }

    public void openActivity(){
        Intent intent = new Intent(RegisterTabFragment.this.getContext(), HomeScreen.class);

        // clear back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("CURRENT_USER", user);
        startActivity(intent);
    }

    public void setTestingInput() {
        registerName.setText("test1234");
        registerEmail.setText("test1234@gmail.com");
        registerPassword.setText("1234567");
        registerConfPass.setText("1234567");
    }
}
