package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.remindme.HomeMenu.HomeScreen;
import com.example.remindme.HomeMenu.persones;
import com.example.remindme.Models.Elderly;
import com.example.remindme.Models.Routine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class add_elderly extends AppCompatActivity {

    EditText registerElderlyName;
    DatabaseReference fDatabase, fDatabase2;
    Elderly elderly;
    Context context;
    String TAG = "edd_elderly";
    ListView listViewData;

    // hashmap that holds all routines stored in Firebase
    Map<String, String> routineMap = new HashMap<>();
    // hashmap that holds selected routines
    Map<String, String> selectedRoutines = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        context = getApplicationContext();
        listViewData = findViewById(R.id.list_routines);

        Button button_routine = findViewById(R.id.add_routine);
        Button button_person = findViewById(R.id.add_person);
        registerElderlyName = findViewById(R.id.fullname);

        fDatabase = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://use-iot-default-rtdb.europe-west1.firebasedatabase.app/")
                .child("elderly");

        fDatabase2 = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://use-iot-default-rtdb.europe-west1.firebasedatabase.app/");

        //for testing purposes:
//        arrayRoutines.put("routine 1: ","4 steps in total");
//        arrayRoutines.put("routine 2: ","7 steps in total");
//        arrayRoutines.put("routine 3: ","5 steps in total");
//        arrayRoutines.put("routine 4: ","9 steps in total");
//        arrayRoutines.put("routine 5: ","6 steps in total");
//        arrayRoutines.put("routine 6: ","3 steps in total");

        //get routines from the firebase
        fetchRoutinesFromFirebase();

        button_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gets input from the fields
                String name = registerElderlyName.getText().toString();
                if(!checkInputFields(name)) {
                    return;
                }
                registerElder(name);
            }
        });

        button_routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openActivity2();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Map.Entry<String, String> entry;
        int id;

        id = item.getItemId();

        if(id == R.id.item_done){
            String itemSelected = "Selected items: \n";

            // reset selected routines array
            selectedRoutines.clear();

            for(int i=0; i<listViewData.getCount(); i++){
                if (listViewData.isItemChecked(i)){
                    entry = (Map.Entry<String, String>)listViewData.getItemAtPosition(i);
                    selectedRoutines.put(entry.getKey(), entry.getValue());
                    itemSelected += entry.getKey() + "\n";
                }
            }

            Toast.makeText(this, itemSelected,Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void registerElder(String name) {

        String uid = UUID.randomUUID().toString();

        elderly = new Elderly(name);
        fDatabase.child(uid).setValue(elderly).addOnSuccessListener(unused -> {
            Log.d(TAG, "Elderly added to the database successfully");
        }).addOnFailureListener(e -> {
            Log.d(TAG, "Error while adding elderly to database " + e.getMessage());
        });

        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

        addRoutinesToUserDatabase(uid);
        addElderlyToCaregiver(name, uid);
        openActivity();
    }

    private boolean checkInputFields(String name) {
        // Checks requirements for the inputs
        if (name.isEmpty()) {
            registerElderlyName.setError("Name required");
            return false;
        }

        return true;
    }

    private void addElderlyToCaregiver(String name, String uid){
        FirebaseDatabase.getInstance().getReference("users/"
                + FirebaseAuth.getInstance().getCurrentUser().getUid()
                + "/elderly").child(uid).setValue(name);
    }

    /*
    Fetches all the existing routines from the database
     */
    public void fetchRoutinesFromFirebase() {

        fDatabase2.child("Routines").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists()) {
                    Log.d("Add_Elderly", "no routine fetched");
                    return;
                }

                Log.d("Add_Elderly", "Fetching ALL routines from database... ");

                for(DataSnapshot ds: snapshot.getChildren()) {
                    Routine routine = ds.getValue(Routine.class);
                    routineMap.put(routine.getFileName(), " ");
                }

                //BaseAdapter adapter = new RoutineListAdapter(arrayRoutines);
                BaseAdapter adapter = new RoutineListAdapter(routineMap);
                listViewData.setAdapter(adapter);

                Log.d("Add_Elderly", "Routines fetched from Firebase: " + routineMap.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addRoutinesToUserDatabase(String uid) {
        DatabaseReference ref2 = fDatabase.child(uid).child("routines");
        Map<String, Object> childUpdates = new HashMap<>();

        // add selected courses to DB
        ref2.setValue(selectedRoutines)
                .addOnSuccessListener(unused -> {
                    Log.d("RoutineSelect", "Selected routines added to DB: " + selectedRoutines);
                }).addOnFailureListener(e -> {
                    Log.d("RoutineSelect", "Selected routines FAILED to be added to database");
                });

        fDatabase.updateChildren(childUpdates);
    }

    public void openActivity(){
        Intent intent = new Intent(this, HomeScreen.class);

        // clear back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void openActivity2(){
        Intent intent = new Intent(this, add_routine.class);

        // clear back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private class RoutineListAdapter extends BaseAdapter {

        private final ArrayList mData;


        public RoutineListAdapter(Map<String, String> map) {
            mData = new ArrayList();
            mData.addAll(map.entrySet());
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Map.Entry<String, String> getItem(int position) {
            return (Map.Entry)mData.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final View result;

            if (view == null) {
                result = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.routine_list_item, viewGroup, false);
            } else {
                result = view;
            }

            CheckedTextView routineSelection = result.findViewById(R.id.routineSelectionTV);
            Map.Entry<String, String> item = getItem(i);

            routineSelection.setText(joinRoutine(item));

            return result;
        }

        private String joinRoutine(Map.Entry<String, String> item) {
            return item.getKey() + " " + item.getValue();
        }
    }
}