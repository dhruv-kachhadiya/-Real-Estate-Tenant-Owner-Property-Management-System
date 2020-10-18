package crazy.developer.landlord_sgp_project.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import crazy.developer.landlord_sgp_project.Model.ImageAdapter;
import crazy.developer.landlord_sgp_project.Model.Upload;
import crazy.developer.landlord_sgp_project.R;

public class ImagesActivity extends AppCompatActivity {
    //private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ListView mListView;
    private ListView mListView2;
    public static ProgressDialog progressDialog;


    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);


        TextView textView = findViewById(R.id.text_view_add_advert);
        progressDialog=new ProgressDialog(ImagesActivity.this);

        mListView = findViewById(R.id.listViewAdvert);




        mUploads = new ArrayList<>();

        showProgressBar();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("rental_list");



        // Adapter For ListView 1
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }

                mAdapter = new ImageAdapter(ImagesActivity.this, mUploads);

                mListView.setAdapter(mAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Upload upload = mUploads.get(i);

                showDialogBox(upload.getName(), upload.getImageUrl(), upload.getAddress(), upload.getPostcode(), upload.getLocation(),
                        upload.getBedroom(), upload.getPropertyType(), upload.getEmail(), upload.getPhone(), upload.getRent());


            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ImagesActivity.this, UploadActivity.class));
            }
        });
    }

    private void showDialogBox(final String name, final String url, final String address, final String postcode, final String location, final String bedroom,
                               final String type, final String email, final String phone, final String rent) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ImagesActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_advert, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Advert From " + name);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        final TextView textViewType = dialogView.findViewById(R.id.text_view_h0);
        final TextView textViewRent = dialogView.findViewById(R.id.text_view_h1);
        final TextView textViewFullAddress = dialogView.findViewById(R.id.text_view_h2);

        final TextView textViewName = dialogView.findViewById(R.id.text_view_h3);
        final TextView textViewPhone = dialogView.findViewById(R.id.text_view_h4);
        final TextView textViewEmail = dialogView.findViewById(R.id.text_view_h5);


        final ImageView imageView = dialogView.findViewById(R.id.dialog_ImageView);

        Picasso.with(this).load(url).placeholder(R.drawable.rent_dummy).fit().centerCrop().into(imageView);
        textViewName.setText("Name: " + name);
        textViewPhone.setText("Phone: " + phone);
        textViewEmail.setText("Email: " + email);

        textViewFullAddress.setText(address + ", " + postcode + ", " + location);
        textViewRent.setText("£" + rent + "pcm");
        textViewType.setText(bedroom + " " + "Bedroom" + " " + type);

    }

    public static void showProgressBar() {
        progressDialog.show();

        progressDialog.setContentView(R.layout.progress_dialog);

        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

}
