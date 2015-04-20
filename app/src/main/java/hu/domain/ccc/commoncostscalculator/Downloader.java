package hu.domain.ccc.commoncostscalculator;

import android.os.Handler;
import android.os.Looper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by David on 2015.04.20..
 */
public class Downloader extends Thread {
    private String url;
    private ArrayList<NameValuePair> data;
    private OnConnectionListener onConnectionListener;
    private Handler handler;
    byte[] resultByte;
    String result;


    public Downloader(ArrayList<NameValuePair> data) {
        this.url = "http://ccc.elitemagyaritasok.info";
        this.data = data;
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode()==200) {
                JSONObject json = new JSONObject();
                resultByte = EntityUtils.toByteArray(response.getEntity());
                result = new String(resultByte, "UTF-8");
                postDownloadSuccess(result);
            }
            else {
                postDownloadFailed(response.getStatusLine().getReasonPhrase());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            postDownloadFailed(e.getLocalizedMessage());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postDownloadSuccess(final String result){
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                if (onConnectionListener != null){
                    onConnectionListener.onDownloadSuccess(result);
                }
            }
        });
    }

    private void postDownloadFailed(final String message){
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                if (onConnectionListener != null){
                    onConnectionListener.onDownloadFailed(message);
                }
            }
        });
    }

    public void setOnConnectionListener(OnConnectionListener onConnectionListener) {
        this.onConnectionListener = onConnectionListener;
    }

    public interface OnConnectionListener{
        void onDownloadSuccess(String result);
        void onDownloadFailed(String message);
    }
}
