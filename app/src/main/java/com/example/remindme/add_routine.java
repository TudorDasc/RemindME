package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.remindme.HomeMenu.HomeScreen;
import com.example.remindme.Models.Routine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class add_routine extends AppCompatActivity {
    EditText registerRoutineName, step1Text, step2Text, step3Text;
    ImageView step1Picture, step2Picture, step3Picture;
    Uri imagePath1, imagePath2, imagePath3;
    DatabaseReference fDatabase;
    Routine routine;
    Context context;
    String TAG = "add_routine";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);

        context = getApplicationContext();

        fDatabase = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://use-iot-default-rtdb.europe-west1.firebasedatabase.app/")
                .child("Routines");

        Button button_routine = findViewById(R.id.add_routine2);
        registerRoutineName = findViewById(R.id.register_routine_name);
        step1Text = findViewById(R.id.description_routine3);
        step2Text = findViewById(R.id.description_routine2);
        step3Text = findViewById(R.id.description_routine5);
        step1Picture = findViewById(R.id.profile_image);
        step2Picture = findViewById(R.id.profile_image2);
        step3Picture = findViewById(R.id.profile_image3);

        ArrayList<String> totalSteps = new ArrayList<>();
        totalSteps.add(String.valueOf(step1Text));
        totalSteps.add(String.valueOf(step2Text));
        totalSteps.add(String.valueOf(step3Text));

        button_routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gets input from the fields
                String name = registerRoutineName.getText().toString();
                if(!checkInputFields(name)) {
                    return;
                }
                createRoutine(name, totalSteps);
                openActivity();
            }
        });

        step1Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent1 = new Intent(Intent.ACTION_PICK);
                photoIntent1.setType("image/*");
                startActivityForResult(photoIntent1, 1);
            }
        });

        step2Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent2 = new Intent(Intent.ACTION_PICK);
                photoIntent2.setType("image/*");
                startActivityForResult(photoIntent2, 1);
            }
        });

        step3Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent3 = new Intent(Intent.ACTION_PICK);
                photoIntent3.setType("image/*");
                startActivityForResult(photoIntent3, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data !=null){
            imagePath1 = data.getData();
            getImageInImageVIew(imagePath1, step1Picture);
        }
    }

    private void getImageInImageVIew(Uri imagePath, ImageView stepPicture) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stepPicture.setImageBitmap(bitmap);

    }

//    private void uploadImage(Uri imagePath) {
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Uploading...");
//        progressDialog.show();
//
//        FirebaseStorage.getInstance().getReference("steps_images/" + UUID.randomUUID().toString()).putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if (task.isSuccessful()){
//                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            if (task.isSuccessful()){
//                                updateStepPicture(task.getResult().toString());
//                            }
//                        }
//                    });
//                    Toast.makeText(add_routine.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(add_routine.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                }
//                progressDialog.dismiss();
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
//                progressDialog.setMessage("Uploaded " + (int) progress + "%");
//            }
//        });
//
//    }
//
//    private void updateStepPicture(String url){
//        FirebaseDatabase.getInstance().getReference("Routines/"
//                + "name" + "/profilePicture").setValue(url);
//    }

    private void createRoutine(String name, ArrayList<String> totalSteps){

        String filename = name + " file";
        //String uid = UUID.randomUUID().toString();
        String step1 = step1Text.getText().toString();
        String step2 = step2Text.getText().toString();
        String step3 = step3Text.getText().toString();
        routine = new Routine(name, filename, totalSteps, step1, step2, step3);

        fDatabase.child(name).setValue(routine).addOnSuccessListener(unused -> {
            Log.d(TAG, "Elderly added to the database successfully");
        }).addOnFailureListener(e -> {
            Log.d(TAG, "Error while adding elderly to database " + e.getMessage());
        });

        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
    }

    private boolean checkInputFields(String name) {
        // Checks requirements for the inputs
        if (name.isEmpty()) {
            registerRoutineName.setError("Name required");
            return false;
        }

        return true;
    }

    public void openActivity(){
        Intent intent = new Intent(this, HomeScreen.class);

        // clear back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}