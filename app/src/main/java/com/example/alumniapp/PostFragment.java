package com.example.alumniapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class PostFragment extends Fragment {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageButton btnSelect;
    private ImageView profileimage;
    String user_id;
    String Category,userEmail,imageurl,mobileno,userName;

    private String userChoosenTask;
    private FirebaseAuth mAuth;
    TextView profilename;
    private StorageReference storageReference;
    CircleImageView circleImageView;
    FirebaseDatabase database;
    EditText Posttext;
    Button post;
    private DatabaseReference mDatabaseRef,mDatabaseRefallpost,mDatabaseRefuserpost;

    private ImageView imageView;
    public  PostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_post, container, false);


        btnSelect =  view.findViewById(R.id.btnSelectPhoto);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        profileimage = (ImageView) view.findViewById(R.id.postprofileImage);
        profilename=view.findViewById(R.id.postprofilename);
        Posttext=view.findViewById(R.id.posttext);
        post=view.findViewById(R.id.post);
        circleImageView=view.findViewById(R.id.postprofileimage1);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              uploadFile();
            }
        });


        database = FirebaseDatabase.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        mDatabaseRefallpost = FirebaseDatabase.getInstance().getReference("allpost");
        mDatabaseRefuserpost = FirebaseDatabase.getInstance().getReference("userpost");



        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        Loaduserdata();

        // Inflate the layout for this fragment
        return view;
    }


    int k=0;

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

                k=1;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                profileimage.setImageBitmap(bitmap);

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

    String postid;
    void update()
    {

        String text=Posttext.getText().toString();

            postid = mDatabaseRef.push().getKey();
            //all post

            mDatabaseRefallpost.child(postid).child("PostId").setValue(postid);
            mDatabaseRefallpost.child(postid).child("PostOwnerName").setValue(userName);
            mDatabaseRefallpost.child(postid).child("PostImage").setValue(url);
            mDatabaseRefallpost.child(postid).child("PostText").setValue(text);
            mDatabaseRefallpost.child(postid).child("PostOwnerImage").setValue(imageurl);

        // user post

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();

        mDatabaseRefuserpost.child(user_id).child(postid).child("PostId").setValue(postid);
        mDatabaseRefuserpost.child(user_id).child(postid).child("PostOwnerName").setValue(userName);
        mDatabaseRefuserpost.child(user_id).child(postid).child("PostImage").setValue(url);
        mDatabaseRefuserpost.child(user_id).child(postid).child("PostText").setValue(text);
        mDatabaseRefuserpost.child(user_id).child(postid).child("PostOwnerImage").setValue(imageurl);



    }




    void Loaduserdata()
    {

        mAuth = FirebaseAuth.getInstance();
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
                profilename.setText(userName);
                Glide.with(getActivity()).load(imageurl).into(circleImageView);



            }


            @Override
            public void onCancelled(DatabaseError error) {



                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }

}
