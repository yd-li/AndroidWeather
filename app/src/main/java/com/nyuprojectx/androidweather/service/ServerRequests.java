package com.nyuprojectx.androidweather.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nyuprojectx.androidweather.user.GetUserCallback;
import com.nyuprojectx.androidweather.user.Post;
import com.nyuprojectx.androidweather.user.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://neoskywalker7.com/projectx/";
    // public static final String SERVER_ADDRESS = "/Users/YuandaLi/AndroidStudioProjects/AndroidWeather/PHPFiles/";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallBack) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }

    public void fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, userCallBack).execute();
    }

    public void postAsyncTask(Post post, GetUserCallback userCallBack) {
        progressDialog.show();
        new PostAsyncTask(post, userCallBack).execute();
    }

    /**
     * parameter sent to task upon execution progress published during
     * background computation result of the background computation
     */

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallback userCallBack;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("uname", user.uname));
            dataToSend.add(new BasicNameValuePair("email", user.email));
            dataToSend.add(new BasicNameValuePair("passwd", user.passwd));
            dataToSend.add(new BasicNameValuePair("bio", user.bio));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Android_Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            return httpRequestParams;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            userCallBack.done(null);
        }
    }

    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback userCallBack;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("uname", user.uname));
            dataToSend.add(new BasicNameValuePair("passwd", user.passwd));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Android_Login.php");

            User returnedUser = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                JSONObject jObject = new JSONObject(result);

                System.out.println("JSON to string:\n" + jObject.toString());

                if (jObject.length() != 0){
                    Log.v("happened", "2");
                    int uid;
                    String email;
                    String bio;
                    // int uid = jObject.getInt("uid");
                    // String email = jObject.getString("email");
                    // String date = jObject.getInt("date");
                    uid = -1;
                    email = "e@mail";
                    bio = "bio";
                    returnedUser = new User(uid, user.uname, user.email, user.passwd, bio);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            super.onPostExecute(returnedUser);
            progressDialog.dismiss();
            userCallBack.done(returnedUser);
        }
    }

    public class PostAsyncTask extends AsyncTask<Void, Void, Void> {
        Post post;
        GetUserCallback userCallBack;

        public PostAsyncTask(Post post, GetUserCallback userCallBack) {
            this.post = post;
            this.userCallBack = userCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("uid", String.valueOf(post.uid)));
            dataToSend.add(new BasicNameValuePair("lat", String.valueOf(post.latLng.latitude)));
            dataToSend.add(new BasicNameValuePair("lng", String.valueOf(post.latLng.longitude)));
            dataToSend.add(new BasicNameValuePair("status", post.status));
            dataToSend.add(new BasicNameValuePair("mood", String.valueOf(post.mood)));
            System.out.println(String.valueOf(post.uid) + ", " + post.status + ", " + String.valueOf(post.mood));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Android_Post.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private HttpParams getHttpRequestParams() {
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            return httpRequestParams;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            userCallBack.done(null);
        }
    }}
