package com.leontheprofessional.test.whorepresentsyou.jsonparsing;

import android.util.Log;

import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


public class WhoRepresentsYouApi {

    private static final String LOG_TAG = WhoRepresentsYouApi.class.getSimpleName();

    private final String BASE_URL = "http://whoismyrepresentative.com/getall_mems.php?";
    private final String ZIP_OPTION = "zip=";
    private final String JSON_OPTION = "&output=json";

    public ArrayList<MemberModel> getAllMemberByZipCode(String zipCode) throws JSONException, MalformedURLException {
        if (zipCode != null && zipCode.length() > 0) {

            String urlString = BASE_URL + ZIP_OPTION + zipCode + JSON_OPTION;
            Log.v(LOG_TAG, "urlString: " + urlString);
            URL url = new URL(urlString);

            String jsonString = getJsonDataAsStringFromUrl(url);
            Log.v(LOG_TAG, "jsonString: " + jsonString);
            return processJsonString(jsonString);
        } else {
            return null;
        }
    }

    private String getJsonDataAsStringFromUrl(URL url) {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String jsonDataAsString = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            Log.v(LOG_TAG, "inputStream: " + inputStream.toString());

            StringBuffer stringBuffer = new StringBuffer();
            if (inputStream == null) return null;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }
            if (stringBuffer.length() == 0) return null;

            jsonDataAsString = stringBuffer.toString();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        Log.v(LOG_TAG, "jsonDataAsString: " + jsonDataAsString);
        return jsonDataAsString;
    }

    private ArrayList<MemberModel> processJsonString(String jsonString) throws JSONException {

        final String WRY_RESULTS = "results";
        final String WRY_NAME = "name";
        final String WRY_PARTY = "party";
        final String WRY_STATE = "state";
        final String WRY_DISTRICT = "district";
        final String WRY_PHONE = "phone";
        final String WRY_OFFICE = "office";
        final String WRY_LINK = "link";

        String name;
        String party;
        String state;
        String districtNumber;
        String phoneNumber;
        String officeAddress;
        String link;

        JSONObject baseJsonObject = new JSONObject(jsonString);

        JSONArray jsonArray = baseJsonObject.getJSONArray(WRY_RESULTS);

        ArrayList<MemberModel> members = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject itemJson = jsonArray.getJSONObject(i);

            name = itemJson.getString(WRY_NAME);
            party = itemJson.getString(WRY_PARTY);
            state = itemJson.getString(WRY_STATE);
            districtNumber = itemJson.getString(WRY_DISTRICT);
            phoneNumber = itemJson.getString(WRY_PHONE);
            officeAddress = itemJson.getString(WRY_OFFICE);
            link = itemJson.getString(WRY_LINK);

            MemberModel member = new MemberModel(name, party, state, districtNumber, phoneNumber, officeAddress, link);
            members.add(member);
        }
        return members;
    }
}
