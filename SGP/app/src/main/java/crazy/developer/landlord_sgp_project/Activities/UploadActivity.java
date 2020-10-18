package crazy.developer.landlord_sgp_project.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

import crazy.developer.landlord_sgp_project.Model.Properties;
import crazy.developer.landlord_sgp_project.Model.Upload;
import crazy.developer.landlord_sgp_project.R;

public class UploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private String userID;
    private FirebaseAuth mAuth;


    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private EditText mEditTextFileName, mEditTextEmail, mEditTextPhone;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private String imageURL;

    private EditText mEditTextLocation, mEditTextAddress, mEditTextPostcode, mEditTextBedroom, mEditTextRent;
    private Spinner spinnerPropertyType;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef,myRef;

    private StorageTask mUploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);

        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mEditTextPhone = findViewById(R.id.edit_text_phone);
        mEditTextEmail = findViewById(R.id.edit_text_email);

        mEditTextLocation = findViewById(R.id.edit_text_location);
        mEditTextAddress = findViewById(R.id.edit_text_address);
        mEditTextPostcode = findViewById(R.id.edit_text_postcode);

        mEditTextBedroom = findViewById(R.id.edit_text_bedroom);
        mEditTextRent = findViewById(R.id.edit_text_rent);

        spinnerPropertyType = findViewById(R.id.spinner_propertyTypeAd);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("rental_list");
        myRef = FirebaseDatabase.getInstance().getReference().child("users");


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        userID = user.getUid();


        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(UploadActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageView);

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {

        if (mImageUri != null) {
            StorageReference Imagename = mStorageRef.child("image" + mImageUri.getLastPathSegment());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setProgress(0);
                }
            }, 500);


            Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    imageURL = String.valueOf(uri);
                }
            });

            Imagename.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(UploadActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                    String uploadId = mDatabaseRef.push().getKey();
                    Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                            mEditTextLocation.getText().toString().trim(), mEditTextAddress.getText().toString().trim(), mEditTextPostcode.getText().toString().trim(),
                            mEditTextBedroom.getText().toString().trim(), spinnerPropertyType.getSelectedItem().toString(), mEditTextRent.getText().toString().trim(),
                            mEditTextEmail.getText().toString().trim(), mEditTextPhone.getText().toString().trim(), taskSnapshot.getStorage().getDownloadUrl().toString(),uploadId);


                    mDatabaseRef.child(uploadId).setValue(upload);

                    myRef.child(userID).child("rental").child(uploadId).setValue(upload);

                }
            });
        }
        else
        {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }



    }


}
