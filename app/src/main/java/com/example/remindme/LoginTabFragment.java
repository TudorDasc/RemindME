package com.example.remindme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.remindme.HomeMenu.HomeScreen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginTabFragment extends Fragment {

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        // Necessary initializations
        View v = inflater.inflate(R.layout.fragment_login_tab, container, false);

        return inflater.inflate(R.layout.fragment_login_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        // Error Messages for registration fields
        CharSequence emailToast = "Email is required";
        CharSequence emailCheckToast = "This is not a valid email";
        CharSequence passwordToast = "Password is required";

        // References to the xml interface
        EditText loginEmail, loginPassword;
        TextView forgotPass;
        Button button;
        button = view.findViewById(R.id.loginButton);

        forgotPass = view.findViewById(R.id.forgotpw_text);
        loginEmail = view.findViewById(R.id.loginEmail);
        loginPassword = view.findViewById(R.id.loginPassword);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                if (email.isEmpty()) {
                    loginEmail.setError(emailToast);
                    return;
                }

                if (!(email.contains("@") && email.contains("."))) {
                    loginEmail.setError(emailCheckToast);
                    return;
                }

                if (password.isEmpty()) {
                    loginPassword.setError(passwordToast);
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show();
                        openActivity(context);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText changeMail = new EditText(view.getContext());
                final AlertDialog.Builder passwordChangeDialog = new AlertDialog.Builder(view.getContext());
                passwordChangeDialog.setTitle("Forgot Password?");
                passwordChangeDialog.setMessage("Enter your Email to receive Password Reset Link.");
                passwordChangeDialog.setView(changeMail);

                passwordChangeDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = changeMail.getText().toString();

                        if (mail.length() == 0) {
                            Toast.makeText(context, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
                        } else if (!(mail.contains("@") && mail.contains("."))) {
                            Toast.makeText(context, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
                        } else {
                            fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Reset link sent to your email!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Error! Reset link is not sent." +
                                            e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                passwordChangeDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close dialog
                    }
                });
                passwordChangeDialog.create().show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Context context = getContext().getApplicationContext();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Toast.makeText(context, "Already Logged In", Toast.LENGTH_SHORT).show();

            FirebaseAuth.getInstance().signOut(); // -> for testing purposes until we have logout
            openActivity(context);
        }
    }

    public void openActivity(Context context){
        Intent intent = new Intent(context, HomeScreen.class);

        // clear back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
