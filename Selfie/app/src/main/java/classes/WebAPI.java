package classes;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;import java.lang.Exception;import java.lang.String;import java.lang.StringBuilder;
        import com.google.gson.Gson;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by marvin on 5/18/14.
 */
public class WebAPI {


    public static Category[] getAllCategories() {


        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://lobster-nachos.appspot.com/webapi"); //TODO fill in url

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


    protected void onPostExecute(Category[] feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }


    public static Item[] getAllItem() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("url"); //TODO fill in url

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
}