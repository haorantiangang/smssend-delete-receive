package com.example.testsms;

import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SmSUtils {

	// 处理返回的发送状态
	public static String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	// 处理返回的接收状态
	public static String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

	public static void init(final Context context) {
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context _context, Intent _intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT)
							.show();
		
					//deleteSms(context);
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					break;
				}
			}
		}, new IntentFilter(SENT_SMS_ACTION));

		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context _context, Intent _intent) {
				Toast.makeText(context, "收信人已经成功接收", Toast.LENGTH_SHORT).show();
			}
		}, new IntentFilter(DELIVERED_SMS_ACTION));
        ContentObserver observer = new SmsContentObserver(context);
        context.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, observer);
	}

	public static void sendSMS(Context context, String phoneNumber,
			String message) {
		// 获取短信管理器
		android.telephony.SmsManager smsManager = android.telephony.SmsManager
				.getDefault();
		// 拆分短信内容（手机短信长度限制）
		List<String> divideContents = smsManager.divideMessage(message);
		for (String text : divideContents) {
			smsManager.sendTextMessage(phoneNumber, null, text,
					getSendStatus(context), geBackStatus(context));
		}
	}

	public static PendingIntent getSendStatus(final Context context) {

		Intent sentIntent = new Intent(SENT_SMS_ACTION);
		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
				sentIntent, 0);
		// register the Broadcast Receivers
		return sentPI;
	}

	public static PendingIntent geBackStatus(final Context context) {

		// create the deilverIntent parameter
		Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
		PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,
				deliverIntent, 0);
		return deliverPI;
	}

    public static void deleteSms(final Context context) {
		if (!SmsWriteOpUtil.isWriteEnabled(context)) {
            SmsWriteOpUtil.setWriteEnabled(context, true);
            Log.e("TAG","設置WriteEnable");
}
        try {
            ContentResolver CR = context.getContentResolver();
            Uri uriSms = Uri.parse("content://sms/sent");
            Cursor c = CR.query(uriSms,
                    new String[]{"_id", "thread_id", "address", "body"}, null, null, null);
            if (null != c && c.moveToFirst()) {
                int id = c.getInt(c.getColumnIndex("_id"));
                Log.e("TAG", "Delete SMS address:" + c.getString(c.getColumnIndex("address")) + "\nbody:" + c.getString(c.getColumnIndex("body")) + "\n_id: " + c.getInt(c.getColumnIndex("_id")));
                CR.delete(Uri.parse("content://sms"), "_id=" + id, null);
            } else {
                Log.e("TAG", "Nothing Delete");
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
