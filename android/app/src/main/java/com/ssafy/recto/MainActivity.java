package com.ssafy.recto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView iv_info;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private HomeFragment homeFragment;
    private CreateFragment_1_SelectOpen createFragment1SelectOpen;
    private CreateFragment_2_SelectDesign createFragment2SelectDesign;
    private CreateFragment_3_SelectVideo createFragment3SelectVideo;
    private CreateFragment_4_SelectPhoto createFragment4SelectPhoto;
    private CreateFragment_5_WriteInfo_prph createFragment5WriteInfoPrph;
    private CreateFragment_5_WriteInfo_pron createFragment5WriteInfoPron;
    private CreateFragment_5_WriteInfo_puph createFragment5WriteInfoPuph;
    private CreateFragment_5_WriteInfo_puon createFragment5WriteInfoPuon;
    private CreateFragment_6_Success_onlyphoto createFragment6SuccessOnlyPhoto;
    private CreateFragment_6_Success_phrases createFragment6SuccessPhrases;
    private ScanFragment scanFragment;
    private PublicFragment publicFragment;
    private PublicFragmentCardDetail publicFragmentCardDetail;
    private ProfileFragment profileFragment;
    private ProfileFragmentMine profileFragmentMine;
    private ProfileFragmentMineDetail profileFragmentMineDetail;
    private ProfileFragmentGift profileFragmentGift;
    private ProfileFragmentGiftDetail profileFragmentGiftDetail;
    private InfoFragment infoFragment;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        iv_info = findViewById(R.id.iv_info);
        iv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment("information");
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        setFragment("home");
                        break;
                    case R.id.nav_create:
                        try {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            String UserUid = account.setIdToken(firebaseUser.getUid());
                            setFragment("create_selectopen");
                        } catch (Exception e) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.nav_scan:
                        setFragment("scan");
                        break;
                    case R.id.nav_public:
                        setFragment("public");
                        break;
                    case R.id.nav_profile:
                        try {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            String UserUid = account.setIdToken(firebaseUser.getUid());
                            setFragment("profile");
                        } catch (Exception e) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
                return true;
            }
        });

        homeFragment = new HomeFragment();
        createFragment1SelectOpen = new CreateFragment_1_SelectOpen();
        createFragment2SelectDesign = new CreateFragment_2_SelectDesign();
        createFragment3SelectVideo = new CreateFragment_3_SelectVideo();
        createFragment4SelectPhoto = new CreateFragment_4_SelectPhoto();
        createFragment5WriteInfoPrph = new CreateFragment_5_WriteInfo_prph();
        createFragment5WriteInfoPron = new CreateFragment_5_WriteInfo_pron();
        createFragment5WriteInfoPuph = new CreateFragment_5_WriteInfo_puph();
        createFragment5WriteInfoPuon = new CreateFragment_5_WriteInfo_puon();
        createFragment6SuccessOnlyPhoto = new CreateFragment_6_Success_onlyphoto();
        createFragment6SuccessPhrases = new CreateFragment_6_Success_phrases();
        scanFragment = new ScanFragment();
        publicFragment = new PublicFragment();
        publicFragmentCardDetail = new PublicFragmentCardDetail();
        profileFragment = new ProfileFragment();
        profileFragmentMine = new ProfileFragmentMine();
        profileFragmentMineDetail = new ProfileFragmentMineDetail();
        profileFragmentGift = new ProfileFragmentGift();
        profileFragmentGiftDetail = new ProfileFragmentGiftDetail();
        infoFragment = new InfoFragment();

        setFragment("home");
    }
    
    // fragment 교체
    protected void setFragment(String str) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (str) {
            case "home":
                ft.replace(R.id.main_frame, homeFragment);
                ft.commit();
                break;
            case "create_selectopen":
                ft.replace(R.id.main_frame, createFragment1SelectOpen);
                ft.commit();
                break;
            case "create_selectdesign":
                ft.replace(R.id.main_frame, createFragment2SelectDesign);
                ft.commit();
                break;
            case "create_selectvideo":
                ft.replace(R.id.main_frame, createFragment3SelectVideo);
                ft.commit();
                break;
            case "create_selectphoto":
                ft.replace(R.id.main_frame, createFragment4SelectPhoto);
                ft.commit();
                break;
            case "create_writeinfo_prph":
                ft.replace(R.id.main_frame, createFragment5WriteInfoPrph);
                ft.commit();
                break;
            case "create_writeinfo_pron":
                ft.replace(R.id.main_frame, createFragment5WriteInfoPron);
                ft.commit();
                break;
            case "create_writeinfo_puph":
                ft.replace(R.id.main_frame, createFragment5WriteInfoPuph);
                ft.commit();
                break;
            case "create_writeinfo_puon":
                ft.replace(R.id.main_frame, createFragment5WriteInfoPuon);
                ft.commit();
                break;
            case "create_success_onlyphoto":
                ft.replace(R.id.main_frame, createFragment6SuccessOnlyPhoto);
                ft.commit();
                break;
            case "create_success_phrases":
                ft.replace(R.id.main_frame, createFragment6SuccessPhrases);
                ft.commit();
                break;
            case "scan":
                ft.replace(R.id.main_frame, scanFragment);
                ft.commit();
                break;
            case "public":
                ft.replace(R.id.main_frame, publicFragment);
                ft.commit();
                break;
            case "public_card_detail":
                ft.replace(R.id.main_frame, publicFragmentCardDetail);
                ft.commit();
                break;
            case "profile":
                ft.replace(R.id.main_frame, profileFragment);
                ft.commit();
                break;
            case "profile_mine":
                ft.replace(R.id.main_frame, profileFragmentMine);
                ft.commit();
                break;
            case "profile_mine_detail":
                ft.replace(R.id.main_frame, profileFragmentMineDetail);
                ft.commit();
                break;
            case "profile_gift":
                ft.replace(R.id.main_frame, profileFragmentGift);
                ft.commit();
                break;
            case "profile_gift_detail":
                ft.replace(R.id.main_frame, profileFragmentGiftDetail);
                ft.commit();
                break;
            case "information":
                ft.replace(R.id.main_frame, infoFragment);
                ft.commit();
                break;
        }
    }
}