package com.example.phil.myapplication.receivers;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.example.phil.myapplication.constants.PreferencesConstants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by habibokanla on 23/03/2018.
 */

public class CallReceiver extends PhonecallReceiver {
    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //Recalling preferences (blacklisted numbers) at the beginning of the activity:

        SharedPreferences prefs = ctx.getSharedPreferences(PreferencesConstants.MY_PREFS_NAME, Context.MODE_PRIVATE);
        String restoredText = prefs.getString("Numbers", null);

        //System.out.println("The incoming number is: " + number);
        //System.out.println(restoredText);
        //System.out.println(restoredText.charAt(0));

        boolean containsWild = restoredText.contains("#");
        String[] restoredArray = restoredText.split(":");
        ArrayList<String> restored = new ArrayList<>(Arrays.asList(restoredArray));

        //Wildcard number match handling:
        if (containsWild) {
            //System.out.println("Awh yeh, contains a wildcard number!");
            ArrayList<String> wilds = new ArrayList<>();

            for (int i = 0; i < restoredArray.length; i++)
                if (restoredArray[i].contains("#"))
                    wilds.add(restoredArray[i]);

            for (int i = 0; i < wilds.size(); i++) {
                for (int j = 0; i < wilds.get(i).length(); j++) {
                    if (number != null && (wilds.get(i).charAt(j) == '#' || wilds.get(i).charAt(j) == number.charAt(j))) {
                        if (j == wilds.get(i).length() - 1) {
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
                    } else if (wilds.get(i).charAt(j) != number.charAt(j))
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
        if (restored.contains(number)) {
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
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        //
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        //
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        //
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        //
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        //
    }

}
