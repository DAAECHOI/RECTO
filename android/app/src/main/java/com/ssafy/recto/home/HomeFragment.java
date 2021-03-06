package com.ssafy.recto.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ssafy.recto.MainActivity;
import com.ssafy.recto.R;
import com.ssafy.recto.api.ApiInterface;
import com.ssafy.recto.api.CardData;
import com.ssafy.recto.api.HttpClient;
import com.ssafy.recto.mypage.ProfileFragmentMineDetail;
import com.ssafy.recto.mypage.ProfileFragmentMineDetail2;
import com.ssafy.recto.publiccard.PublicFragmentCardDetail;
import com.ssafy.recto.publiccard.PublicFragmentCardDetail2;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    MainActivity mainActivity;
    ApiInterface api;
    List<CardData> photoCards = new ArrayList<>();
    private View view;
    private MyAdapter adapter;
    private RecyclerView listview;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth mFirebaseAuth;
    private TextView tv_id;
    private View upline;
    private SharedPreferences sharedPreferences;

    int[] seq;
    int[] design_num;
    int size;

    @Override
    public void onAttach(Context context) {
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
        checkSelfPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // ?????? ??????
        if(requestCode == 1){
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity","?????? ?????? : " + permissions[i]);
                }
            }
        }
    }

    public void checkSelfPermission() {
        String temp = "";

        //?????? ?????? ?????? ??????
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        //?????? ?????? ?????? ??????
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        //????????? ?????? ??????
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.CAMERA + " ";
        }

        if (TextUtils.isEmpty(temp) == false) {
            // ?????? ??????
            ActivityCompat.requestPermissions(getActivity(), temp.trim().split(" "),1);
        } else {
            // ?????? ?????? ??????
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    // Fragment??? ?????? ???????????? ??? ?????? ?????? ??????
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        // User ??????
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser current = mFirebaseAuth.getCurrentUser();
        // Shared Preferences ?????????
        sharedPreferences = this.getActivity().getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);

        // api ??????
        api = HttpClient.getRetrofit().create( ApiInterface.class );

        // ?????????
        listview = view.findViewById(R.id.main_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(linearLayoutManager);

        adapter = new MyAdapter(getActivity(), photoCards);
        listview.setAdapter(adapter);

        MyListDecoration decoration = new MyListDecoration();
        listview.addItemDecoration(decoration);


        // ????????? ?????????
        if (current != null) {
            // ?????? ?????? ??????
            String nickname = sharedPreferences.getString("nickname", "RECTO??? ??????");
            tv_id = view.findViewById(R.id.tv_id);
            tv_id.setText(nickname + "?????? Moment");

            // ?????? ?????? ?????????
            try {
                requestGet1();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // ??? ????????? ?????????
        else {
            // ?????? ?????? ??????
            tv_id = view.findViewById(R.id.tv_id);
            tv_id.setText("????????? Moment??? ??????????????????.");

            // ?????? ?????? ?????????
            try {
                requestGet2();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    // ????????? ????????? - ?????? ?????? ?????????
    public void requestGet1() throws ParseException {
        // ????????? ??????
        MyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                // ?????? ?????? ??? ?????? ?????? ????????? ??????
                if (size <= pos) {
                    mainActivity.setFragment("create_selectopen");
                }
                else if (design_num[pos] == 1) {
                    // ?????? ???????????? photo_seq ??? (sep[pos]) ????????????
                    Bundle bundle = new Bundle(); // ???????????? ?????? ??????
                    bundle.putInt("seq", seq[pos]);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    ProfileFragmentMineDetail profileFragmentMineDetail = new ProfileFragmentMineDetail();
                    profileFragmentMineDetail.setArguments(bundle);
                    transaction.replace(R.id.main_frame, profileFragmentMineDetail);
                    transaction.commit();
                } else if (design_num[pos] == 2) {
                    // ?????? ???????????? photo_seq ??? (sep[pos]) ????????????
                    Bundle bundle = new Bundle(); // ???????????? ?????? ??????
                    bundle.putInt("seq", seq[pos]);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    ProfileFragmentMineDetail2 profileFragmentMineDetail2 = new ProfileFragmentMineDetail2();
                    profileFragmentMineDetail2.setArguments(bundle);
                    transaction.replace(R.id.main_frame, profileFragmentMineDetail2);
                    transaction.commit();
                }
            }
        });

        // ?????? user??? ????????? ?????? ????????????
        String userUid = sharedPreferences.getString("userUid", "");
        Call<List<CardData>> call = api.getMainCard(userUid);

        call.enqueue(new Callback<List<CardData>>() {
            @Override
            public void onResponse(Call<List<CardData>> call, Response<List<CardData>> response) {
                String uid, video, photo, phrase, date, pwd;
                int design;
                seq = new int[response.body().size()];
                design_num = new int[response.body().size()];
                size = response.body().size();

                photoCards.clear();
                Log.e("????????? ???", String.valueOf(response.body().size()));
                for (int i = 0; i < response.body().size(); i++) {
                    uid = response.body().get(i).getUser_uid();
                    design = response.body().get(i).getDesign();
                    video = response.body().get(i).getVideo_url();
                    photo = response.body().get(i).getPhoto_url();
                    phrase = response.body().get(i).getPhrase();
                    date = response.body().get(i).getPhoto_date();
                    pwd = response.body().get(i).getPhoto_pwd();

                    photoCards.add(new CardData(uid, design, video, photo, phrase, date, pwd));
                    seq[i] = response.body().get(i).getPhoto_seq();
                    design_num[i] = design;
                }
                // ????????? ????????? ??????????????? 5??? ????????? ??????, (5 - ????????? ?????? ???)?????? ??? ????????? ??????
                for (int i = 0; i < 5 - response.body().size(); i++) {
                    String photo_url = "https://project-recto.s3.ap-northeast-2.amazonaws.com/slot.png";
                    photoCards.add(new CardData(photo_url));
                }

                adapter = new MyAdapter(getActivity(), photoCards);
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CardData>> call, Throwable t) {
                Log.e("????????? ????????? - ????????? ????????????", "??????:<" + t.toString());
            }
        });
    }

    // ??? ????????? ????????? - ?????? ?????? ?????????
    public void requestGet2() throws ParseException {
        // ????????? ??????
        MyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                if (design_num[pos] == 1) {
                    // ?????? ???????????? photo_seq ??? (sep[pos]) ????????????
                    Bundle bundle = new Bundle(); // ???????????? ?????? ??????
                    bundle.putInt("seq", seq[pos]);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    PublicFragmentCardDetail publicFragmentCardDetail = new PublicFragmentCardDetail();
                    publicFragmentCardDetail.setArguments(bundle);
                    transaction.replace(R.id.main_frame, publicFragmentCardDetail);
                    transaction.commit();
                } else {
                    // ?????? ???????????? photo_seq ??? (sep[pos]) ????????????
                    Bundle bundle = new Bundle(); // ???????????? ?????? ??????
                    bundle.putInt("seq", seq[pos]);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    PublicFragmentCardDetail2 publicFragmentCardDetail2 = new PublicFragmentCardDetail2();
                    publicFragmentCardDetail2.setArguments(bundle);
                    transaction.replace(R.id.main_frame, publicFragmentCardDetail2);
                    transaction.commit();
                }
            }
        });

        Call<List<CardData>> call = api.getPublicCard();

        call.enqueue(new Callback<List<CardData>>() {
            @Override
            public void onResponse(Call<List<CardData>> call, Response<List<CardData>> response) {
                String uid, video, photo, phrase, date, pwd;
                int design;
                seq = new int[response.body().size()];
                design_num = new int[response.body().size()];

                photoCards.clear();
                // ?????? ????????? ?????? ???????????? ??????
                for (int i = 0; i < 5; i++) {
                    uid = response.body().get(i).getUser_uid();
                    design = response.body().get(i).getDesign();
                    video = response.body().get(i).getVideo_url();
                    photo = response.body().get(i).getPhoto_url();
                    phrase = response.body().get(i).getPhrase();
                    date = response.body().get(i).getPhoto_date();
                    pwd = response.body().get(i).getPhoto_pwd();

                    photoCards.add(new CardData(uid, design, video, photo, phrase, date, pwd));
                    seq[i] = response.body().get(i).getPhoto_seq();
                    design_num[i] = design;
                }

                adapter = new MyAdapter(getActivity(), photoCards);
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CardData>> call, Throwable t) {
                Log.e("??? ????????? ????????? - ????????? ????????????", "??????:<" + t.toString());
            }
        });
    }
}