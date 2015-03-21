package hu.domain.ccc.commoncostscalculator;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by David on 2015.03.21..
 */
public class ServerConnect {
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost;
    HttpResponse response;
    JSONObject returnJSON;
    public JSONObject Action(String url, List<NameValuePair> parameters){
        httppost = new HttpPost(url);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(parameters));
            response = httpclient.execute(httppost);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String responseText = null;
        try {
            responseText = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            returnJSON = new JSONObject(responseText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnJSON;
    }
}
