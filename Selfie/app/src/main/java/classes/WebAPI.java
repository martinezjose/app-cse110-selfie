package classes;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.util.ArrayList;
        import java.lang.Exception;import java.lang.String;import java.lang.StringBuilder;
        import com.google.gson.Gson;

        import org.apache.http.HttpEntity;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.HttpResponse;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.entity.StringEntity;

        import org.json.JSONArray;
        import org.json.JSONObject;
        import uix.Order;
        import uix.OrderDetail;

        import android.content.Context;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.util.Log;

/**
 * Created by marvin on 5/18/14.
 */
public class WebAPI {

    public boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /* getAllCategories()
     * Description: retrieves JSON responses for categories and creates Category objects
     * PRECONDITION: JSON response in http://lobster-nachos.appspot.com/webapi/categories
     * POSTCONDITION: Category objects are created for all the categories from JSON response
     * Returns: an array of Category objects
     */

    public static Category[] getAllCategories() {


        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://lobster-nachos.appspot.com/webapi/categories");

        Gson gson = new Gson();

        try {
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String category;

            //retrieves string from json response
            while ((category = reader.readLine()) != null) {
                builder.append(category);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //creates a java object from json response
        return gson.fromJson(builder.toString(), Category[].class);


    }

    /* getAllItems()
     * Description: retrieves JSON responses for menu items and creates Item objects
     * PRECONDITION: JSON response in http://lobster-nachos.appspot.com/webapi/items
     * POSTCONDITION: Item objects are created for all the items from JSON response
     * Returns: an array of Item objects
     */

    public static Item[] getAllItems() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://lobster-nachos.appspot.com/webapi/items");

        Gson gson = new Gson();

        try {
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String item;

            //retrieves string from json response
            while ((item = reader.readLine()) != null) {
                builder.append(item);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //creates a java object from json response
        return gson.fromJson(builder.toString(), Item[].class);
    }

    /* sendPairingCode()
     * Description: sends pairing code to server and links tablet to server with a TableID
     * PRECONDITION: the User has entered an appropriate pairing code
     * POSTCONDITION: server responds with a long that represents a tableID
     * Returns: TableID assigned by the server; -1 if getting tableID failed.
     */
    public static long sendPairingCode(int code)
    {
        InputStream inputStream;
        long tableID = -1;
        String result;

        StringBuilder builder = new StringBuilder();
        try {


            HttpClient httpclient = new DefaultHttpClient();

            // make POST request to lobster-nachos
            //TODO add the correct url
            //HttpPost httpPost = new HttpPost("http://lobster-nachos.appspot.com/webapi/tables?pairingCode="+code);
            HttpGet httpGet = new HttpGet("http://lobster-nachos.appspot.com/webapi/tables?pairingCode=" + code);


            // Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //retrieves string from json response
            while ((result = reader.readLine()) != null) {
                builder.append(result);
            }

            if(builder.toString().contains("TableID")){

                JSONObject jObj = new JSONObject(builder.toString());

                tableID = jObj.getLong("TableID"); //TODO check that this is correct key name
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tableID;
    }

    public static Recommendation[] getAllRecommendedItems() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://lobster-nachos.appspot.com/webapi/recommendations");

        Gson gson = new Gson();


            try {
                HttpResponse response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String category;

                //retrieves string from json response
                while ((category = reader.readLine()) != null) {
                    builder.append(category);
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //creates a java object from json response
       return gson.fromJson(builder.toString(), Recommendation[].class);

    }

    public static void pingWaiter()
    {
        InputStream inputStream;
        String result;
        StringBuilder builder = new StringBuilder();
        try {


            HttpClient httpclient = new DefaultHttpClient();

            // make POST request to lobster-nachos
            HttpPost httpPost = new HttpPost("http://lobster-nachos.appspot.com/pings");

            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("TableID", Order.getTableId());


            json = jsonObject.toString();

            // set json to StringEntity
            StringEntity se = new StringEntity(json);


            httpPost.setEntity(se);

            // Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");


            // Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //retrieves string from json response
            while ((result = reader.readLine()) != null) {
                builder.append(result);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void postOrders()
    {
        InputStream inputStream;
        String result;
        StringBuilder builder = new StringBuilder();
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make POST request to lobster-nachos
            HttpPost httpPost = new HttpPost("http://lobster-nachos.appspot.com/pings");

            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("TableID", Order.getTableId());

            ArrayList<OrderDetail> theOrder = Order.getTheOrder();
            JSONObject jObj = new JSONObject();
            for(OrderDetail od: theOrder)
            {
                jObj.accumulate("Item", od.getTheItem());
                jObj.accumulate("Quantity", od.getQuantity());
            }

            jsonObject.put("Order", jObj);

            // convert JSONObject to JSON to String
            json = jsonObject.toString();

            // set json to StringEntity
            StringEntity se = new StringEntity(json);

            // set httpPost Entity
            httpPost.setEntity(se);

            // Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");


            // Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //retrieves string from json response
            while ((result = reader.readLine()) != null) {
                builder.append(result);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}