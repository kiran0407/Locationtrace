package kiran541.ench.com.locationtrace;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final int PERMISSION_ALL = 1;
    private GoogleMap mMap;
    TrackGPS gps;
    EditText locationSearch;
    ListView teamlist;
    Button searchs;
    String addres,myurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationSearch = (EditText) findViewById(R.id.search);
        teamlist = (ListView) findViewById(R.id.listt);

        searchs = (Button) findViewById(R.id.search_button);
        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
      // new kilomilo().execute(inputurl.VALIDATE);
        searchs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addres = locationSearch.getText().toString();
                if (addres.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter place u want search", Toast.LENGTH_SHORT).show();
                } else {

                    onMapSearch();
                }
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.clear();
        gps = new TrackGPS(MapsActivity.this);
        Double lat =gps .getLatitude();
        Double lng =  gps.getLongitude();
        LatLng sydney = new LatLng(lat,lng);
        float zoomLevel =10;
        myurl=inputurl.VALIDATE+"?slat="+lat+"&slng="+lng;
       // new kilomilo().execute(inputurl.VALIDATE);
        new kilomilo().execute(myurl);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.bhdpi)));

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in velachery"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomLevel));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(sydney)      // Sets the center of the map to Mountain View
                .zoom(10)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addCircle(new CircleOptions().center(sydney)
                        .radius(5000)
                        .strokeWidth(1.0f)
                        .strokeColor(Color.BLUE)
                /*.fillColor(0x550000FF)*/);

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onMapSearch() {
        mMap.clear();
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.bhdpi)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,6.5f));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12.5f), 2000, null);
            Circle circle = mMap.addCircle(new CircleOptions().center(latLng).radius(5000).strokeColor(Color.BLUE).strokeWidth(2.0f));
            mMap.setMaxZoomPreference(14.5f);
            mMap.setMinZoomPreference(6.5f);

            myurl=inputurl.VALIDATE+"?slat="+latLng.latitude+"&slng="+latLng.longitude;
            // new kilomilo().execute(inputurl.VALIDATE);
            new kilomilo().execute(myurl);
            //  Toast.makeText(getApplicationContext(), , Toast.LENGTH_SHORT).show();
        }
    }


    public class MovieAdap extends ArrayAdapter {
        private List<Locations> movieModelList;
        private int resource;
        private int selectedPosition = -1;
        Context context;
        private LayoutInflater inflater;
        MovieAdap(Context context, int resource, List<Locations> objects) {
            super(context, resource, objects);
            movieModelList = objects;
            this.context =context;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getViewTypeCount() {
            return 1;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder  ;
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
                holder = new ViewHolder();
                holder.logo=(TextView) convertView.findViewById(R.id.text45);
                holder.tname=(TextView) convertView.findViewById(R.id.text46);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            //  holder.checkBox.setTag(position);
            Locations cc=movieModelList.get(position);
            holder.logo.setText(cc.getLoclat());
            holder.tname.setText(cc.getLoclong());

            Double   latitud=Double.parseDouble(cc.getLoclat());
            Double  longitud=Double.parseDouble(cc.getLoclong());
            //holder.tname.setText(getItem(position).getName());
            Toast.makeText(getApplicationContext(),cc.getLoclat(),Toast.LENGTH_LONG).show();
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.lochdpi))
                    .title("kiran"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,6.5f));
//            Circle circle = mMap.addCircle(new CircleOptions().center(latLng).radius(5000).strokeColor(Color.BLUE).strokeWidth(2.0f));
//            mMap.setMaxZoomPreference(14.5f);
//            mMap.setMinZoomPreference(6.5f);
            //Toast.makeText(getApplicationContext(),cc.getTeamlogo(),Toast.LENGTH_LONG).show();
            // Picasso.with(context).load(cc.getTeamlogo()).fit().error(R.drawable.footballlogo).fit().into(holder.logo);
            //Glide.with(context).load(cc.getTeamlogo()).into(holder.logo);
            return convertView;
        }

        class ViewHolder{


            public TextView tname,logo;
            public CheckBox checkBox;

        }
    }
    public class kilomilo extends AsyncTask<String,String, List<Locations>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected List<Locations> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("result");
                List<Locations> milokilo = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    Locations catego = gson.fromJson(finalObject.toString(), Locations.class);
                    milokilo.add(catego);
                }
                return milokilo;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(final List<Locations> movieMode) {
            super.onPostExecute(movieMode);
            if (movieMode!=null)
            {
                MovieAdap adapter = new MovieAdap(getApplicationContext(), R.layout.restaurantlist, movieMode);
                teamlist.setAdapter(adapter);
                teamlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Locations item = movieMode.get(position);
//                        Intent intent=new Intent(TeamSelection.this,NewsActivity.class);
//                        intent.putExtra("pname",user1);
//
//                        startActivity(intent);
                    }
                });
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Check your internet connection",Toast.LENGTH_SHORT).show();
            }
        }
    }






    }

