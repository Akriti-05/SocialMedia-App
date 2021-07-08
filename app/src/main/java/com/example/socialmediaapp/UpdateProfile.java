package com.example.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

public class UpdateProfile extends AppCompatActivity {
    EditText etname, etBio, etProfession, etEmail, etWeb;
    Button button;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    String currentuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        etBio=findViewById(R.id.et_bio_up);
        etEmail=findViewById(R.id.et_email_up);
        etname=findViewById(R.id.et_name_up);
        etProfession=findViewById(R.id.et_profession_up);
        etWeb=findViewById(R.id.et_website_up);
        button=findViewById(R.id.btn_up);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        documentReference=db.collection("user").document(currentuid);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.getResult().exists()) {
                            String nameResult = task.getResult().getString("name");
                            String bioResult = task.getResult().getString("bio");
                            String emailResult = task.getResult().getString("email");
                            String webResult = task.getResult().getString("web");
                            String profResult = task.getResult().getString("prof");
                            String url = task.getResult().getString("url");


                            etname.setText(nameResult);
                            etBio.setText(bioResult);
                            etEmail.setText(emailResult);
                            etWeb.setText(webResult);
                            etProfession.setText(profResult);
                        }else{
                            Toast.makeText(UpdateProfile.this, "No profile exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateProfile() {

        String name= etname.getText().toString();
        String bio= etBio.getText().toString();
        String web= etWeb.getText().toString();
        String prof= etProfession.getText().toString();
        String email= etEmail.getText().toString();

        final DocumentReference sDoc= db.collection("user").document(currentuid);
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                //DocumentSnapshot snapshot = transaction.get(sfDocRef);


                transaction.update(sDoc, "name",name );
                transaction.update(sDoc, "prof",prof );
                transaction.update(sDoc, "email",email );
                transaction.update(sDoc, "web",web );
                transaction.update(sDoc, "bio",bio );


                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateProfile.this, "updated", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfile.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}