package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.example.remindme.Models.Elderly;
import com.example.remindme.Models.Routine;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class search_elderly extends AppCompatActivity {

    DatabaseReference fDatabase, fDatabase2;
    Elderly elderly;
    Context context;
    String TAG = "edd_elderly";
    ListView listViewData;

    // hashmap that holds all routines stored in Firebase
    Map<String, String> elderlyMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_elderly);

        context = getApplicationContext();
        listViewData = findViewById(R.id.list_elderlys);

        fDatabase2 = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://use-iot-default-rtdb.europe-west1.firebasedatabase.app/");
        fetchRoutinesFromFirebase();
    }

    public void fetchRoutinesFromFirebase() {

        fDatabase2.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.exists()) {
                    Log.d("Add_Elderly", "no routine fetched");
                    return;
                }

                Log.d("Add_Elderly", "Fetching ALL routines from database... ");

                for(DataSnapshot ds: snapshot.getChildren()) {
                    Elderly elderly = ds.getValue(Elderly.class);
                    elderlyMap.put(elderly.getName(), " ");
                }

                //BaseAdapter adapter = new RoutineListAdapter(arrayRoutines);
                BaseAdapter adapter = new search_elderly.ElderlyListAdapter(elderlyMap);
                listViewData.setAdapter(adapter);

                Log.d("Add_Elderly", "Routines fetched from Firebase: " + elderlyMap.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private class ElderlyListAdapter extends BaseAdapter {

        private final ArrayList mData;


        public ElderlyListAdapter(Map<String, String> map) {
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

