package be.kdg.kandoe.kandoe.exception;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.net.SocketTimeoutException;

import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

/**
 * Created by Edward on 19/05/2015.
 */
public class ExceptionHelper {
    public static void logRetrofitError (RetrofitError error, String TAG){
        if(error == null)return;

        try {
            String json =  new String(((TypedByteArray)error.getResponse().getBody()).getBytes());
            Log.v(TAG, "jsonString     : " + json.toString());
        }catch(NullPointerException e) {
            //System.out.println("1");
        }try{
            Log.v(TAG, "response status: " + error.getResponse().getStatus());
        }catch(NullPointerException e) {
            //System.out.println("2");
        }try{
            Log.v(TAG, "kind           : " + error.getKind());
        }catch(NullPointerException e) {
            //System.out.println("3");
        }try{
            Log.v(TAG, "url            : " + error.getUrl());
        }catch(NullPointerException e) {
            //System.out.println("4");
        }try{
            Log.v(TAG, "response url   : " + error.getResponse().getUrl());
        }catch(NullPointerException e) {
            //System.out.println("5");
        }try{
            Log.v(TAG, "response reason: " + error.getResponse().getReason());
        }catch(NullPointerException e) {
            //System.out.println("6");
        }try{
            Log.v(TAG, "response body  : " + error.getResponse().getBody().toString());
        }catch(NullPointerException e) {
            //System.out.println("7");
        }

    }
    class RestError {
        @SerializedName("code")
        public int code;
        @SerializedName("error")
        public String errorDetails;
    }
    public static void showRetrofitError(RetrofitError error,Context context, String TAG){
        String networkMessage ="";
        String networkCode = null;
        if (context == null){
            Log.e(TAG,"context bestaat niet");
            return;
        }
        if (error.getResponse() != null && error.getResponse().getStatus() != 0 && error.getKind().toString() != null) {
            Log.v(TAG,"================= Retrofit Error =================");
            Log.v(TAG,"Errorcode : " + error.getResponse().getStatus() + " " + error.getKind().toString());
            try{
                String json =  new String(((TypedByteArray)error.getResponse().getBody()).getBytes());
                String x = json.replace("{","").replace("}","").replace("\"","").replace("Message","").replace(":","");
                Log.v(TAG,"Message   : " + x);
                if(!networkMessage.contains("<!DOCTYPE"))
                Toast.makeText(context, x, Toast.LENGTH_SHORT).show();
            } catch(NullPointerException e){
                Log.e(TAG,"kon message niet omzetten/tonen");
            }
            Log.v(TAG,"==================================================");

        }else if (error.getKind() == RetrofitError.Kind.NETWORK) {
            if (error.getMessage() != null && error.getMessage().contains("authentication")) {
                //401 errors
                networkMessage = "Aanvraag was niet geautoriseerd";
                networkCode = "401";
            } else if (error.getCause() != null && error.getCause() instanceof SocketTimeoutException) {
                //Socket Timeout
                networkMessage = "Aanvraagtijd verstreken - check uw internetconnectie";
                networkCode = "408";
            } else {
                //No Connection
                networkMessage = "Geen internet connectie";
                networkCode = "~503";
            }
            Log.v(TAG,"============ Retrofit Error (Network) ============");
            Log.v(TAG,"Errorcode : " + networkCode);
            Log.v(TAG,"Message   : " + networkMessage);
            if(networkMessage!=null || networkMessage!=""){
                if(!networkMessage.contains("<!DOCTYPE")){
                    Toast.makeText(context, networkMessage, Toast.LENGTH_SHORT).show();
                }
            }
            Log.v(TAG,"==================================================");
        }
    }
}
