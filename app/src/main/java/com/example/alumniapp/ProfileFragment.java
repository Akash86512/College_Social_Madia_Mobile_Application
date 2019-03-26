package com.example.alumniapp;



import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class ProfileFragment extends Fragment {
   private Button ChoosePhoto,save;
   private TextView Name,Email,MobileNumber;
   private ImageView photo;

    private FirebaseAuth mAuth;

    private StorageReference storageReference;
    FirebaseDatabase database;
    private DatabaseReference mDatabaseRef,mDatabaseRef1;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment

        database = FirebaseDatabase.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");


        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        Name=view.findViewById(R.id.name);
        MobileNumber=view.findViewById(R.id.mobilenumber);
        Email=view.findViewById(R.id.email);
        save=view.findViewById(R.id.save);
        ChoosePhoto=view.findViewById(R.id.choose);
        photo=view.findViewById(R.id.profileimage);
        showUserdata();

        ChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();

            }
        });

        Loaduserdata();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile();
            }
        });



        return view;
    }


    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 234;

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                photo.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }













String url;
    Uri downloadUrl;
    private void uploadFile() {
        //checking if file is available



        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();


            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            sRef.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Task<Uri> uriTask = task.getResult().getStorage().getDownloadUrl();
                    while(!uriTask.isComplete());
                    downloadUrl = uriTask.getResult();
                    url=downloadUrl.toString();
                    update();
                    progressDialog.dismiss();


                    Toast.makeText(getActivity(),"data update",Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });


        } else {

            Toast.makeText(getActivity()," data not update",Toast.LENGTH_LONG).show();
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    String user_id;
    void update()
    {

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        Toast.makeText(getActivity(),"email"+user_id,Toast.LENGTH_LONG).show();

        mDatabaseRef.child(user_id).child("imageurl").setValue(url);


    }

    String Category,userEmail,imageurl,mobileno,userName;
    void Loaduserdata()
    {

        user_id = mAuth.getCurrentUser().getUid();

        mDatabaseRef.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //   This method is called once with the initial value and again
                //   whenever data at this location is updated.

                 Category = (String) dataSnapshot.child("Category").getValue();
                 userEmail = (String) dataSnapshot.child("Email").getValue();
                 imageurl = (String) dataSnapshot.child("imageurl").getValue();
                 mobileno = (String) dataSnapshot.child("mobileno").getValue();
                 userName= (String) dataSnapshot.child("Name").getValue();
                Name.setText(userName);
                Email.setText(userEmail);
                MobileNumber.setText(mobileno);
                Glide.with(getActivity()).load(imageurl).into(photo);

            }


            @Override
            public void onCancelled(DatabaseError error) {



                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }

    void showUserdata()
    {

        Toast.makeText(getActivity(),"name"+userName,Toast.LENGTH_LONG).show();



    }

}