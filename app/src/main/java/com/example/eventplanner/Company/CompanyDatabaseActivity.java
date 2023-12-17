package com.example.eventplanner.Company;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.eventplanner.Objects.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CompanyDatabaseActivity {
    public interface UploadCallback {
        void onProgress(int progress);
        void onSuccess(String downloadUrl);
        void onFailure(String message);

    }


    public static void uploadImage(Uri selectedImageUri, UploadCallback callback) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/" + UUID.randomUUID().toString());
        UploadTask uploadTask = storageRef.putFile(selectedImageUri);
        uploadTask.addOnProgressListener(snapshot -> {
            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            if (callback != null) {
                callback.onProgress((int) progress);
            }
        }).addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                if (callback != null) {
                    callback.onSuccess(downloadUri.toString());
                }
            });
        }).addOnFailureListener(e -> {
            if (callback != null) {
                callback.onFailure(e.getMessage());
            }
        });
    }
    public static void postEvent( HashMap<String,Object> event) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Event").add(event).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                try {
                    if (task.isSuccessful()) {
                        DocumentReference snapshot = task.getResult();
                        snapshot.update("e_id", snapshot.getId());
                    } else {
                        // Handle unsuccessful task completion
                        // ...
                    }
                } catch (Exception e) {
                    // Handle exception
                    Log.d("TAG", "onComplete: Exception - " + e.getMessage());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure
                Log.d("TAG", "onFailure: " + e.getMessage());
            }
        });


    }


}


