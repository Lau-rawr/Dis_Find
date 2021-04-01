package l.w.disenyfind;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText openTime;
    private EditText closingTime;
//    private ArrayList<String> parks;
private LinkedHashMap<String, String> parks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        //navbar
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        spinner = (Spinner) findViewById(R.id.ParkList);
//        parks = new ArrayList<>();

        parks = new LinkedHashMap<>();
        parks.put("Walt Disney World - Magic Kingdom","WaltDisneyWorldMagicKingdom" );
        parks.put("Walt Disney World - Epcot","WaltDisneyWorldEpcot");
        parks.put("Walt Disney World - Hollywood Studios","WaltDisneyWorldHollywoodStudios");
        parks.put("Walt Disney World - Animal Kingdom","WaltDisneyWorldAnimalKingdom");

        List<String> parksKeyValues = new ArrayList<>(parks.keySet());

        //dropdown list/spinner of parks
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, parksKeyValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }

    public void requestInfo (View view){
        InfoRetrieval info = new InfoRetrieval();
        try{
            String selection = spinner.getSelectedItem().toString();
            // getting the value of the key that was selected
            String selectedPark = parks.get(selection);

            String serviceURL =
                    " https://api.themeparks.wiki/preview/parks/" + selectedPark +
                            "/calendar";
            info.execute(serviceURL);
        } catch (Exception e){
            Toast.makeText(getApplication(),e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    private class InfoRetrieval extends AsyncTask<String, Integer, String[]> {
        /*The onPreExecute() method is called before the background tasks are initiated and can be
         * used to perform initialization steps.
         *
         * This method runs on the main thread so may be used to update the user interface. * */
        @Override
        protected void onPreExecute() {
        }

        /* The code to be performed in the background on a different thread from the main thread
         * resides in the doInBackground() method.
         *
         * This method does not have access to the main thread so cannot make user interface changes.*/
        @Override
        protected String[] doInBackground(String... rideUrl) {

            String[] rideInfo = new String[3];

            try {
                publishProgress(0);
                URL url = new URL(rideUrl[0]);

                //open connection and pass input
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                //tell the connection to connect
                conn.connect();

                // Read response.
                InputStream inputStream = conn.getInputStream();
                final String resp = convertStreamToString(inputStream);

                // string to json object from the response
                JSONObject rideObj = new JSONObject(resp);

                //getting data fields we need the name is the key part of the key value pair
                rideInfo[0] = Double.toString(rideObj.getDouble("duration"));
                rideInfo[1] = rideObj.getString("opened_on");

                // the way the API is written is either an int or null
                try {
                    String rideHgt = Integer.toString(rideObj.getInt("height_restriction"));
                    rideInfo[2]=rideHgt;
                }catch(JSONException e){
                    rideInfo[2]="0";
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return rideInfo;
        }


        /* The onProgressUpdate() method, however, is called each time a call is made to the
         * publishProgress() method from within the doInBackground() method and can be used
         * to update the user interface with progress information.*/
        @Override
        protected void onProgressUpdate(Integer... values) {
            updates.setText(R.string.getting_ride_info);
        }


        /* The onPostExecute() method is called when the tasks performed within the
         * doInBackground() method complete.
         *
         * This method is passed the value returned by the doInBackground() method and runs
         * within the main thread allowing user interface updates to be made.*/

        protected void onPostExecute(String[] rideInfo) {
            super.onPostExecute(rideInfo);
            //displays the text returned by the doInBackground() method
            rideDuration.setText(rideInfo[0]);
            rideOpened.setText(rideInfo[1]);
            rideHeight.setText(rideInfo[2]);
            updates.setText(R.string.done);

        }


        /**
         * Helper function to convert stream to a string
         */
        private String convertStreamToString(InputStream is) {
            Scanner s = new Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next().replace(",", ",\n") : "";
        }
    }



}


//  private class InfoRetrieval extends AsyncTask<String, Integer, ArrayList<String>> {
//
//        /*The onPreExecute() method is called before the background tasks are initiated and can be
//         * used to perform initialization steps.
//         *
//         * This method runs on the main thread so may be used to update the user interface. * */
//        @Override
//        protected void onPreExecute() {
//
//        }
//
//        @Override
//        protected ArrayList<String> doInBackground(String... strings) {
//            try {
//
//                URL url = new URL("https://api.themeparks.wiki/preview/parks");
//
//                //open connection and pass input
//                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//
//                // Read response into a stream then convert it to array
//                InputStream inputStream = conn.getInputStream();
//                convertStreamToArray(inputStream);
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return parks;
//        }
//
//        /* The onProgressUpdate() method, however, is called each time a call is made to the
//         * publishProgress() method from within the doInBackground() method and can be used
//         * to update the user interface with progress information.*/
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//
//        }
//
//
//        /* The onPostExecute() method is called when the tasks performed within the
//         * doInBackground() method complete.
//         *
//         * This method is passed the value returned by the doInBackground() method and runs
//         * within the main thread allowing user interface updates to be made.*/
//
//        protected void onPostExecute(ArrayList<String> parks) {
//       //     super.onPostExecute(parks);
//
//
//        }
//    }
//
//
//        /**
//         * Helper function to convert stream to a string
//         */
//        private void convertStreamToArray(InputStream is) {
//            Scanner s = new Scanner(is).useDelimiter(",");
//            while (s.hasNext()) {
//                // removes any special characters and adds a space before every capitol letter
//                parks.add(s.next().replaceAll("[^a-zA-Z0-9]", "").replaceAll("(.)([A-Z])", "$1 $2"));
//            }
//        }


