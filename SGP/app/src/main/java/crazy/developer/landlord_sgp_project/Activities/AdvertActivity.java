package crazy.developer.landlord_sgp_project.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import crazy.developer.landlord_sgp_project.Model.ImageAdapter;
import crazy.developer.landlord_sgp_project.Model.Properties;
import crazy.developer.landlord_sgp_project.Model.PropertyList;
import crazy.developer.landlord_sgp_project.Model.Upload;
import crazy.developer.landlord_sgp_project.R;


public class AdvertActivity extends AppCompatActivity {

    Button Add;

    ListView listViewProperties;
    List<Upload> propertiesList;



    private DatabaseReference myRef,property_list;
    private String userID;
    private FirebaseAuth mAuth;
    public static ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog=new ProgressDialog(AdvertActivity.this);
        setContentView(R.layout.activity_advert);

        Add = findViewById(R.id.btnAddAdvert);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdvertActivity.this, UploadActivity.class));
            }
        });


        listViewProperties = findViewById(R.id.listViewProperties2);
        propertiesList = new ArrayList<>();

//
        myRef = FirebaseDatabase.getInstance().getReference().child("users");
        property_list = FirebaseDatabase.getInstance().getReference().child("rental_list");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        userID = user.getUid();


        listViewProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Upload upload =  propertiesList.get(i);

                showDialogBox(upload.getmRentalID(),upload.getRent(),upload.getPropertyType(),upload.getPostcode(),upload.getPhone(),upload.getName(),upload.getLocation(),upload.getEmail(),upload.getBedroom(),upload.getAddress());

            }
        });

    }

    private void showDialogBox(final String rentalID,final String rent, final String propertytype, final String postcode, final String phone, final String name, final String location, final String email, final String bedrooms, final String address) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdvertActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_box, null);
        dialogBuilder.setView(dialogView);


        final Button buttonCancel = (Button) dialogView.findViewById(R.id.btnClose);
        final Button buttonReport = (Button) dialogView.findViewById(R.id.btnReport);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);

        dialogBuilder.setTitle("Your Property " + location);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialogBox(rentalID, location);
                alertDialog.dismiss();
            }
        });

        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Rent=rent;
                String Property_type=propertytype;
                String Phone=phone;
                String Name=name;
                String Location=location;
                String Email=email;



                Intent reportIntent = new Intent(AdvertActivity.this, RentalInformationActivity.class);
                reportIntent.putExtra("R", Rent);
                reportIntent.putExtra("T", Property_type);
                reportIntent.putExtra("L", Location);
                reportIntent.putExtra("N", Name);
                reportIntent.putExtra("P", Phone);
                reportIntent.putExtra("E", Email);



                startActivity(reportIntent);
                alertDialog.dismiss();
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }


    private void updateDialogBox(final String rentalID, final String location){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdvertActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_rental_detail, null);
        dialogBuilder.setView(dialogView);


        final Spinner spinner_propertyTypeAd = (Spinner) dialogView.findViewById(R.id.spinner_propertyTypeAd);

        final EditText edit_text_location =(EditText)dialogView.findViewById(R.id.edit_text_location);
        final EditText edit_text_address=(EditText)dialogView.findViewById(R.id.edit_text_address);
        final EditText edit_text_postcode=(EditText)dialogView.findViewById(R.id.edit_text_postcode);
        final EditText edit_text_bedroom=(EditText)dialogView.findViewById(R.id.edit_text_bedroom);
        final EditText edit_text_rent=(EditText)dialogView.findViewById(R.id.edit_text_rent);
        final EditText edit_text_file_name=(EditText)dialogView.findViewById(R.id.edit_text_file_name);
        final EditText edit_text_phone=(EditText)dialogView.findViewById(R.id.edit_text_phone);
        final EditText edit_text_email=(EditText)dialogView.findViewById(R.id.edit_text_email);


        final Button button_update = (Button) dialogView.findViewById(R.id.button_update);
        final Button button_delete = (Button) dialogView.findViewById(R.id.button_delete);
        final Button button_cancel = (Button) dialogView.findViewById(R.id.button_cancel);

        dialogBuilder.setTitle("Updating Property " + location);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProperties(rentalID);
                alertDialog.dismiss();
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loc = edit_text_location.getText().toString().trim();
                String add=edit_text_address.getText().toString().trim();
                String post=edit_text_postcode.getText().toString().trim();
                String bed=edit_text_bedroom.getText().toString().trim();
                String rents=edit_text_rent.getText().toString().trim();
                String file=edit_text_file_name.getText().toString().trim();
                String ph=edit_text_phone.getText().toString().trim();
                String mail=edit_text_email.getText().toString().trim();
                String pro_type=spinner_propertyTypeAd.getSelectedItem().toString();




                if (TextUtils.isEmpty(loc)){
                    edit_text_location.setError("Field Required");
                    return;
                }
                updateProperties(rentalID,loc,add,post,bed,rents,file,ph,mail,pro_type);

                alertDialog.dismiss();

            }
        });

       DatabaseReference mFirebaseRef = FirebaseDatabase.getInstance().getReference().child("users");

       Query queryLocation = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_address.setText(dataSnapshot.child("address").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query queryRental = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryRental.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_bedroom.setText(dataSnapshot.child("bedroom").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query queryAddress = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_email.setText(dataSnapshot.child("email").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query queryPostcode = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryPostcode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_location.setText(dataSnapshot.child("location").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query queryTenancyLength = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryTenancyLength.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_file_name.setText(dataSnapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query queryTenantName = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryTenantName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_phone.setText(dataSnapshot.child("phone").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Query queryManagementName = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryManagementName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_postcode.setText(dataSnapshot.child("postcode").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query queryRefurb = mFirebaseRef.child(userID).child("rental").child(rentalID);

        queryRefurb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edit_text_rent.setText(dataSnapshot.child("rent").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void deleteProperties(String propertyID){
        DatabaseReference drProperties = FirebaseDatabase.getInstance().getReference("users").child(userID).child("rental").child(propertyID);
        DatabaseReference drProperties1 = FirebaseDatabase.getInstance().getReference("rental_list").child(propertyID);
        drProperties.removeValue();
        drProperties1.removeValue();
        Toast.makeText(AdvertActivity.this, "Deleted Property", Toast.LENGTH_SHORT).show();
    }

    private boolean updateProperties (String id1,String loc,String add,String post,String bed,String rents,String file,String ph,String mail,String pro_type){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userID).child("rental").child(id1);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("rental_list").child(id1);

        Upload upload = new Upload(file,loc,add,post,bed,pro_type,rents,mail,ph,"heelo",id1);

        databaseReference.setValue(upload);
        databaseReference1.setValue(upload);

        Toast.makeText(AdvertActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

        return true;
    }


    @Override
    public void onStart() {
        super.onStart();

        showProgressBar();
        FirebaseDatabase.getInstance().getReference("users").getRef().child(userID).child("rental").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                propertiesList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Upload upload = data.getValue(Upload.class);

                    propertiesList.add(upload);
                }

                progressDialog.dismiss();
                ImageAdapter adapter = new ImageAdapter(AdvertActivity.this, propertiesList);
                listViewProperties.setAdapter(adapter);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
