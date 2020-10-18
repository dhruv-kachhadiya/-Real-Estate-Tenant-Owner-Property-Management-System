package crazy.developer.landlord_sgp_project.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import crazy.developer.landlord_sgp_project.Model.Properties;
import crazy.developer.landlord_sgp_project.Model.PropertyList;
import crazy.developer.landlord_sgp_project.R;

public class ViewPropertyActivity extends AppCompatActivity {

    ListView listViewProperties;
    public static ProgressDialog progressDialog;

    List<Properties> propertiesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_property);

        listViewProperties = (ListView) findViewById(R.id.listViewProperties2);
        progressDialog=new ProgressDialog(ViewPropertyActivity.this);

        propertiesList = new ArrayList<>();
        showProgressBar();

        FirebaseDatabase.getInstance().getReference("property_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                propertiesList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Properties property = data.getValue(Properties.class);

                    propertiesList.add(property);
                }

                PropertyList adapter = new PropertyList(ViewPropertyActivity.this, propertiesList);
                listViewProperties.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listViewProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Properties properties =  propertiesList.get(i);

                showDialogBox(properties.getPropertyID(), properties.getPropertyLocation(), properties.getPropertyType(), properties.getPropertyRental(), properties.getPropertyManagementName(), properties.getPropertyTenancyLength(), properties.getPropertyTenantName(), properties.getPropertyAddress(), properties.getPropertyPostcode(), properties.getPropertyBedrooms(), properties.getPropertyRefurb());
            }
        });

    }

    private void showDialogBox(final String propertyID, final String propertyLocation, final String propertyType, final String propertyRental, final String managementName, final String tenancyLength, final String tenantName, final String propertyAddress, final String propertyPostcode, final String propertyBedrooms, final String propertyRefurb) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ViewPropertyActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.general_property_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button buttonCancel = (Button) dialogView.findViewById(R.id.btnClose);
        final Button buttonReport = (Button) dialogView.findViewById(R.id.btnReport);

        dialogBuilder.setTitle("Your Property " + propertyLocation);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();



        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String PropertiesLocation = propertyLocation;
                String PropertyType = propertyType;
                String PropertyRental = propertyRental;
                String ManagementName = managementName;
                String Address = propertyAddress;
                String Postcode = propertyPostcode;
                String TenancyL = tenancyLength;
                String TenantName = tenantName;
                String Bedrooms = propertyBedrooms;
                String Refurb = propertyRefurb;
                Intent reportIntent = new Intent(ViewPropertyActivity.this, PropertyInformationActivity.class);
                reportIntent.putExtra("propertyCurrent", PropertiesLocation);
                reportIntent.putExtra("propertyType", PropertyType);
                reportIntent.putExtra("propertyRental", PropertyRental);
                reportIntent.putExtra("managementName", ManagementName);
                reportIntent.putExtra("Address", Address);
                reportIntent.putExtra("Postcode", Postcode);
                reportIntent.putExtra("Length", TenancyL);
                reportIntent.putExtra("Tenant", TenantName);
                reportIntent.putExtra("Bedrooms", Bedrooms);
                reportIntent.putExtra("Refurb", Refurb);


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
