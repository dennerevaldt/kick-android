package br.unisinos.kickoffapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dennerevaldtmachado on 28/06/16.
 */
public class ConnectionUtil {
    private static final String URL_BASE = "https://kickapi.herokuapp.com";
    //private static final String URL_BASE = "http://10.0.2.2:3000";
    //private static final String URL_BASE = "http://192.168.1.6:3000";

    /**
     * Check has connection in context
     * @param context
     * @return boolean
     */
    public static Boolean hasConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Connection from url
     * @param urlRequisition
     * @param method
     * @param setDoInput
     * @param setDoOutInput
     * @return HttpUrlConnection connection
     * @throws IOException
     */
    public static HttpURLConnection connect (String urlRequisition, String method, Boolean setDoInput, Boolean setDoOutInput, String token) throws IOException {
        URL url = new URL(URL_BASE + urlRequisition);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoInput(setDoInput);
        connection.setDoOutput(setDoOutInput);
        if (token != null) {
            connection.setRequestProperty("x-access-token", token);
        }
        connection.connect();
        return connection;
    }

    /**
     * Tranform bytes for String
     * @param is
     * @return String
     * @throws IOException
     */
    public static String bytesForString(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        // O 'byteArrayOutputStream' vai armazenar todos os bytes lidos
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // Precisamos saber quantos bytes foram lidos
        int bytesRead;
        // Vamos lendo 1KB por vez..
        while ((bytesRead = is.read(buffer)) != -1) {
            // Copiando a quantidade de bytes lidos do buffer para o byteArrayOutputStream
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
    }
}
