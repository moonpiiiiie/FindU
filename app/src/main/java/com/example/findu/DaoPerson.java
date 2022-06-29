package com.example.findu;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DaoPerson {
    private DatabaseReference databaseReference;

    public DaoPerson(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Person.class.getSimpleName());
    }

    public Task<Void> add(Person ppl){
         //throw exception


        return databaseReference.push().setValue(ppl);

    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap){
        return databaseReference.child(key).updateChildren(hashMap);

    }

}
