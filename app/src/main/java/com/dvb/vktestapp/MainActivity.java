package com.dvb.vktestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiGroups;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private String[] scope = new String[] {VKScope.WALL};
    private ListView mListView;

    private String mUserId = "47893103";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Getting a fingerprint
//        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
//        System.out.println(Arrays.asList(fingerprints));



        VKSdk.login(this, scope);


    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(final VKAccessToken res) {

                mListView = (ListView) findViewById(R.id.listView);

                final VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, mUserId));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);

                        VKList vkList = (VKList) response.parsedModel;

                        try{
                            VKRequest request1 = new VKApiWall()
                                    .get(VKParameters.from(VKApiConst.OWNER_ID, "-" + vkList.get(0).fields
                                    .getInt("id"), VKApiConst.COUNT, 10));
                            request1.executeWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response) {


                                    try {
                                        JSONObject jsonObject = (JSONObject) response.json.get("response");
                                        System.out.println(jsonObject.getString("items"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>
//                                (MainActivity.this,
//                                        android.R.layout.simple_expandable_list_item_1, list);
//
//                        mListView.setAdapter(mArrayAdapter);

                    }
                });

            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(),
                        "Ошибка авторизации: Пользователь запретил авторизацию",
                        Toast.LENGTH_SHORT).show();
            }
        }))

        { super.onActivityResult(requestCode, resultCode, data);
        }
    }
}


















