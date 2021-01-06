package com.goldenappstudio.coachinginstitutes2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentChangeListener {

    SNavigationDrawer sNavigationDrawer;
    Class fragmentClass;
    public static Fragment fragment;
    public FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        BottomNavigationView mainbottomNav = findViewById(R.id.mainBottomNav);

        initializeFragment();



        mainbottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_action_home:
                    replaceFragment(new HomeFragment());
                    return true;
                case R.id.bottom_action_account:
                    replaceFragment(new Account());
                    return true;
                case R.id.bottom_action_notif:
                    replaceFragment(new NotificationFragment());
                    return true;
                case R.id.bottom_action_store:
                    replaceFragment(new Store());
                    return true;
                case R.id.bottom_action_batch:
                    replaceFragment(new Batch());
                    return true;
                default:
                    return false;
            }
        });

        sNavigationDrawer = findViewById(R.id.navigationDrawer);

        //Creating a list of menu Items
        List<MenuItem> menuItems = new ArrayList<>();

        //Use the MenuItem given by this library and not the default one.
        //First parameter is the title of the menu item and then the second parameter is the image which will be the background of the menu item.

        menuItems.add(new MenuItem("Home", R.drawable.news_bg));
        menuItems.add(new MenuItem("My Profile", R.drawable.news_bg));
        menuItems.add(new MenuItem("Log Out", R.drawable.news_bg));
        menuItems.add(new MenuItem("Contact Us", R.drawable.news_bg));
        menuItems.add(new MenuItem("Rate this app", R.drawable.news_bg));
        menuItems.add(new MenuItem("Share this app", R.drawable.news_bg));
        menuItems.add(new MenuItem("Report bugs", R.drawable.news_bg));
        menuItems.add(new MenuItem("Developers", R.drawable.news_bg));

        sNavigationDrawer.setMenuItemList(menuItems);
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
        }

        sNavigationDrawer.setOnMenuItemClickListener(position -> {
            System.out.println("Position " + position);

            switch (position) {
                case 0: {
                    // My Account
                    FragmentChangeListener fc = (FragmentChangeListener) MainActivity.this;
                    fc.replaceFragment(new HomeFragment());
                    break;
                }
                case 1: {
                    // My Account
                    FragmentChangeListener fc = (FragmentChangeListener) MainActivity.this;
                    fc.replaceFragment(new Account());
                    break;
                }
                case 2: {
                    // Log Out
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(this, "You have been logged out.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case 3: {
                    // Contact Us
                    Intent intent = new Intent(MainActivity.this, ContactUs.class);
                    startActivity(intent);
                    break;
                }

                case 4: {
                    // Rate Us
                    // TODO : Change package name here ...
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + "com.goldenappstudio.coachinginstitutes2020")));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                    break;
                }
                case 5: {
                    // Share
                    // TODO : Change EXTRA_TEXT here ...
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Download Coaching Institute application for preparation of govt jobs and learning. Download Link\n"+"https://play.google.com/store/apps/details?id=com.goldenappstudio.coachinginstitutes2020");
                    shareIntent.setType("text/plane");
                    startActivity(Intent.createChooser(shareIntent, "Share Coaching App via"));
                    break;
                }
                case 6: {
                    // Report Bugs
                    // TODO : Change subject here ...
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"studiogoldenapp@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Bugs in Coaching App...");
                    intent.putExtra(Intent.EXTRA_TEXT, "Body of the content here...");
                    intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com");
                    intent.setType("text/html");
                    intent.setPackage("com.google.android.gm");
                    startActivity(Intent.createChooser(intent, "Send mail"));
                    break;
                }
                case 7: {
                    // Developers
                    String website = "http://www.goldenappstudio.com";
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                    startActivity(browserIntent);
                    break;
                }
            }

            sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                @Override
                public void onDrawerOpened() {
                }

                @Override
                public void onDrawerOpening() {
                }

                @Override
                public void onDrawerClosing() {
                    System.out.println("Drawer closed");
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (fragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
                    }
                }

                @Override
                public void onDrawerClosed() {
                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    System.out.println("State " + newState);
                }
            });
        });
    }

    private void initializeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, new HomeFragment());
        fragmentTransaction.add(R.id.frameLayout, new NotificationFragment());
        fragmentTransaction.add(R.id.frameLayout, new Account());
        fragmentTransaction.add(R.id.frameLayout, new Batch());
        fragmentTransaction.add(R.id.frameLayout, new Store());
        fragmentTransaction.hide(new NotificationFragment());
        fragmentTransaction.hide(new Account());
        fragmentTransaction.hide(new Store());
        fragmentTransaction.hide(new Batch());
        fragmentTransaction.commit();

        replaceFragment(new HomeFragment());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        new HomeFragment().onSaveInstanceState();
        new NotificationFragment().onSaveInstanceState();
        new Account().onSaveInstanceState();
    }

    // Old replaceFragment function
    /*public void replaceFragment(Fragment fragment, Fragment currentFragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == homeFragment) {
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(store);
            fragmentTransaction.hide(batch);
        }
        if (fragment == accountFragment) {
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(store);
            fragmentTransaction.hide(batch);
        }
        if (fragment == notificationFragment) {
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(store);
            fragmentTransaction.hide(batch);
        }
        if (fragment == store) {
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(batch);
        }
        if (fragment == batch) {
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(store);
            fragmentTransaction.hide(notificationFragment);
        }

        fragmentTransaction.show(fragment);

        //fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }*/

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    public void quit() {
        Intent start = new Intent(Intent.ACTION_MAIN);
        start.addCategory(Intent.CATEGORY_HOME);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(start);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


}
