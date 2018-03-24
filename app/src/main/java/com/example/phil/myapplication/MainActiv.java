package com.example.phil.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MainActiv extends AppCompatActivity implements Blacklist.OnFragmentInteractionListener, Message.OnFragmentInteractionListener, Schedule.OnFragmentInteractionListener
{
    static HashMap<String, String> blacklist;
    static ArrayList<String> blacklistStringList;

    static ArrayAdapter<String> blacklistAdapter;
    static CustomAdapter messageAdapter;

    SwitchCompat blacklistCall;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RotateAnimation rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(this,R.anim.anim_rotate);

        blacklist = new HashMap<String, String>();
        blacklistStringList = new ArrayList<String>();
        blacklistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, blacklistStringList);
        messageAdapter = new CustomAdapter(blacklistStringList, this);
        //messageAdapter = new Message.MyCustomAdapter(blacklistStringList, this);


        ImageButton settings = (ImageButton) findViewById(R.id.settingsCog);

        final DrawerLayout layout = findViewById(R.id.drawer_layout);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                view.startAnimation(rotateAnimation);
                layout.openDrawer(Gravity.START);
            }
        });


        blacklistCall = findViewById(R.id.blockBlacklistCalls);


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Blacklist"));
        tabLayout.addTab(tabLayout.newTab().setText("Message"));
        tabLayout.addTab(tabLayout.newTab().setText("Schedule"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });

        ActivityCompat.requestPermissions(MainActiv.this, new String[]{Manifest.permission.READ_CALL_LOG}, Constants.READ_CALL_LOG);
        ActivityCompat.requestPermissions(MainActiv.this, new String[]{Manifest.permission.SEND_SMS}, Constants.SEND_SMS);
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    public static HashMap<String, String> getBlacklist() { return blacklist; }

    public static String getBlacklistMessage(Object o) { return blacklist.get(o); }

    public static ArrayList<String> getBlacklistStringList()
    {
        return blacklistStringList;
    }

    public CustomAdapter getMessageAdapter() { return messageAdapter; }

    public void removeFromArray(Object o)
    {
        blacklist.remove(o);
        blacklistStringList.remove(o);
        messageAdapter.notifyDataSetChanged();
        blacklistAdapter.notifyDataSetChanged();
    }

    public static void addToArray(String o)
    {
        MainActiv.getBlacklistStringList().add(o);
        MainActiv.getBlacklist().put(o, null);
        blacklistAdapter.notifyDataSetChanged();
        messageAdapter.notifyDataSetChanged();
    }

    public List<RecyclerItem> getData()
    {
        List<RecyclerItem> data = new ArrayList<>();
        List<String> titles = getBlacklistStringList();
        for(int i = 0; i< titles.size(); i++)
        {
            RecyclerItem item = new RecyclerItem();
            item.title = titles.get(i);
            item.descriptor = titles.get(i);
            System.out.println("Main Title is: " + item.title);
            System.out.println("Main size: " + titles.size());
        }

        return data;
    }

    public static class CallReceiver extends PhonecallReceiver
    {
        @Override
        protected void onIncomingCallReceived(Context ctx, String number, Date start) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException
        {
            //Recalling preferences (blacklisted numbers) at the beginning of the activity:

            SharedPreferences prefs = ctx.getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
            String restoredText = prefs.getString("Numbers", null);

            //System.out.println("The incoming number is: " + number);
            //System.out.println(restoredText);
            //System.out.println(restoredText.charAt(0));

            boolean containsWild = restoredText.contains("#");
            String[] restoredArray = restoredText.split(":");
            ArrayList<String> restored = new ArrayList<>(Arrays.asList(restoredArray));

            //Wildcard number match handling:
            if(containsWild)
            {
                //System.out.println("Awh yeh, contains a wildcard number!");
                ArrayList<String> wilds = new ArrayList<>();

                for(int i = 0; i < restoredArray.length; i++)
                    if(restoredArray[i].contains("#"))
                        wilds.add(restoredArray[i]);

                for (int i = 0; i < wilds.size(); i++)
                {
                    for(int j = 0; i < wilds.get(i).length(); j ++)
                    {
                        if(number != null && (wilds.get(i).charAt(j) == '#' || wilds.get(i).charAt(j) == number.charAt(j)))
                        {
                            if (j == wilds.get(i).length()-1)
                            {
                                TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                                Class<?> c = Class.forName(tm.getClass().getName());
                                Method m = c.getDeclaredMethod("getITelephony");
                                m.setAccessible(true);
                                Object telephonyService = m.invoke(tm);
                                Class<?> telephonyServiceClass = Class.forName(telephonyService.getClass().getName());
                                Method endCallMethod = telephonyServiceClass.getDeclaredMethod("endCall");
                                endCallMethod.invoke(telephonyService);
                                containsWild = false;
                                break;
                            }
                        }
                        else if(wilds.get(i).charAt(j) != number.charAt(j))
                            break;
                    }
                }
            }

            //TODO Check the reported cursor numbers of "Unknown" and "Private caller" incoming calls
            //TODO to correctly handle below:

            /*if(number == "-2")
            {
                System.out.println("Null number was rejected.");

                TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                Class<?> c = Class.forName(tm.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                Object telephonyService = m.invoke(tm);
                Class<?> telephonyServiceClass = Class.forName(telephonyService.getClass().getName());
                Method endCallMethod = telephonyServiceClass.getDeclaredMethod("endCall");
                endCallMethod.invoke(telephonyService);
            }
            */

            //Explicit number match handling:
            if(restored.contains(number))
            {
                System.out.println("Contains number.");

                //String msg = "Get fucked m8.";

                TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                Class<?> c = Class.forName(tm.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                Object telephonyService = m.invoke(tm);
                Class<?> telephonyServiceClass = Class.forName(telephonyService.getClass().getName());
                Method endCallMethod = telephonyServiceClass.getDeclaredMethod("endCall");
                endCallMethod.invoke(telephonyService);

                //TODO This is the section that sends texts automatically after rejecting a detected number.
                //try
                //{
                //    SmsManager smsManager = SmsManager.getDefault();
                //    smsManager.sendTextMessage(number, null, msg, null, null);
                //    Toast.makeText(ctx, "Message Sent", Toast.LENGTH_LONG).show();
                //}
                //catch (Exception ex)
                //{
                //    System.out.println("Get fucked m8.");
                //    ex.printStackTrace();
                //}
            }
        }


        //Overriden methods to handle cases where calls are answered, ended, etc:
        @Override
        protected void onIncomingCallAnswered(Context ctx, String number, Date start)
        {
            //
        }

        @Override
        protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
        {
            //
        }

        @Override
        protected void onOutgoingCallStarted(Context ctx, String number, Date start)
        {
            //
        }

        @Override
        protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
        {
            //
        }

        @Override
        protected void onMissedCall(Context ctx, String number, Date start)
        {
            //
        }

    }

}
