package com.ssafy.recto.publiccard;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.ssafy.recto.MainActivity;
import com.ssafy.recto.R;
import com.ssafy.recto.api.ApiInterface;
import com.ssafy.recto.api.CardData;
import com.ssafy.recto.api.HttpClient;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicFragment extends Fragment{

    RecyclerView recyclerView;
    ApiInterface api;
    List<CardData> photoCards = new ArrayList<>();
    private View view;
    private GridLayoutManager mGridLayoutManager;

    MainActivity mainActivity;
    PublicFragmentMyAdapter publicFragmentMyAdapter;
    public RequestManager requestManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestManager = Glide.with(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.public_fragment, container, false);
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        try {
            requestGet();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        recyclerView = view.findViewById(R.id.recyclerView);

        publicFragmentMyAdapter = new PublicFragmentMyAdapter(getActivity(), photoCards);
        recyclerView.setAdapter(publicFragmentMyAdapter);

        int numberOfColumns = 3;
        mGridLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerView.setLayoutManager(mGridLayoutManager);

        PublicFragmentMyAdapter.setOnItemClickListener(new PublicFragmentMyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos)
            {
                Log.i("this is", String.valueOf(pos));
                mainActivity.setFragment("public_card_detail");
            }
        });

        return view;
    }

    public void requestGet()  throws ParseException {
        Call<List<CardData>> call = api.getPublicCard();

        call.enqueue(new Callback<List<CardData>>() {
            @Override
            public void onResponse(Call<List<CardData>> call, Response<List<CardData>> response) {
                String uid, video, photo, phrase, date, pwd;
                boolean publication;
                int design;

                photoCards.clear();
                for (int i = 0; i < response.body().size(); i++) {
                    uid = response.body().get(i).getUser_uid();
                    publication = response.body().get(i).isPublication();
                    design = response.body().get(i).getDesign();
                    video = response.body().get(i).getVideo_url();
                    photo = response.body().get(i).getPhoto_url();
                    phrase = response.body().get(i).getPhrase();
                    date = response.body().get(i).getPhoto_date();
                    pwd = response.body().get(i).getPhoto_pwd();

                    photoCards.add(new CardData(uid, publication, design, video, photo, phrase, date, pwd));

                }

                publicFragmentMyAdapter = new PublicFragmentMyAdapter(getActivity(), photoCards);
                publicFragmentMyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(publicFragmentMyAdapter);

            }

            @Override
            public void onFailure(Call<List<CardData>> call, Throwable t) {
                Log.e("nooooo", "failed :<" + t.toString());
            }
        });
    }
}