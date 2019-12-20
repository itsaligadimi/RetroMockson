package com.agadimi.RetroMocksonTestApp;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    private EditText searchEt;
    private RoundedImageView avatarIv;
    private TextView nameTv, bioTv, postsCount, followersCount, followingCount;
    private ProgressBar loadingPb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();

        searchEt.addTextChangedListener(new TextWatcher()
        {
            private int delay = 600;
            Handler handler = new Handler();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, delay);
            }

            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    String query = searchEt.getText().toString();
                    if (!query.isEmpty())
                    {
                        search(query);
                    }
                }
            };
        });
    }

    private void bindViews()
    {
        searchEt = findViewById(R.id.search_et);
        avatarIv = findViewById(R.id.avatar_iv);
        nameTv = findViewById(R.id.name_tv);
        bioTv = findViewById(R.id.bio_tv);
        postsCount = findViewById(R.id.post_count);
        followersCount = findViewById(R.id.followers_count);
        followingCount = findViewById(R.id.following_count);
        loadingPb = findViewById(R.id.loading_pb);
    }

    private void search(String username)
    {
        loadingPb.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = Retlication.getInstagramService().getProfile(username);
        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                loadingPb.setVisibility(View.GONE);


                if (response.isSuccessful())
                {
                    try
                    {
                        populateData(response.body().string());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Parsing failed", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                loadingPb.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateData(String data)
    {
        try
        {
            JSONObject user = new JSONObject(data).getJSONObject("graphql").getJSONObject("user");
            String fullName = user.getString("full_name");
            String bio = user.getString("biography");
            int posts = user.getJSONObject("edge_owner_to_timeline_media").getInt("count");
            int followers = user.getJSONObject("edge_followed_by").getInt("count");
            int following = user.getJSONObject("edge_follow").getInt("count");
            String avatarUrl = user.getString("profile_pic_url_hd");


            Picasso.get().load(avatarUrl).into(avatarIv);
            nameTv.setText(fullName);
            bioTv.setText(bio);
            postsCount.setText(String.valueOf(posts));
            followersCount.setText(String.valueOf(followers));
            followingCount.setText(String.valueOf(following));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
