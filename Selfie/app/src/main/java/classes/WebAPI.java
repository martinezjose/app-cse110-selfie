package classes;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
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

        import org.json.JSONObject;

        import android.util.Log;

/**
 * Created by marvin on 5/18/14.
 */
public class WebAPI {


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


    public static Item[] getAllItems() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://lobster-nachos.appspot.com/webapi/items"); //TODO fill in url

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
/*
    public static void sendPing(int tableID)
    {
        InputStream inputStream = null;
        String result;
        StringBuilder builder = new StringBuilder();
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost("http://lobster-nachos.appspot.com/pings");

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("TableID", tableID);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");


            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
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

*/

}