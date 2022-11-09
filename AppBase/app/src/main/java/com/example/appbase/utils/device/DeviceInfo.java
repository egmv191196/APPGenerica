package com.example.appbase.utils.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import com.example.appbase.utils.Utils;
import com.example.appbase.logger.CCLogger;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class DeviceInfo {
    private static final String TAG = DeviceInfo.class.getSimpleName();
    private TelephonyManager mTelephonyManager;
    private Context context;

    public DeviceInfo(Context context){
        this.context = context;
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @SuppressLint("HardwareIds")
    public String getImei() {
        String imei = "";
        try {
            imei = mTelephonyManager.getDeviceId();
        } catch (SecurityException e) {
            CCLogger.e(TAG, e);
        } catch (Exception e) {
            CCLogger.e(TAG, e);
        }

        return (imei != null && !imei.isEmpty()) ? imei : "000000000000000";
    }

    @SuppressLint("HardwareIds")
    public String getMacAdress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            CCLogger.e(TAG, e);
        }
        return "02:00:00:00:00:00";
    }

    @SuppressLint("HardwareIds")
    public String getSerialNumber() {
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                serial = Build.SERIAL;
            } else {
                serial = Build.getSerial();
            }
        } catch (SecurityException e) {
            CCLogger.e(TAG, e);
        } catch (Exception e) {
            CCLogger.e(TAG, e);
        }

        return serial;
    }

    public String getNumCell() {
        String numCell;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        numCell = mTelephonyManager.getLine1Number();
        return numCell;
    }

    public String getSystemVersion() {
        return "" + Utils.versionCode();
    }

    public String getProveedor() {
        return mTelephonyManager.getSimOperatorName();
    }

    public String getPlatformId() {
        return "401";
    }

    public float cargaBateria() {
        try {
            IntentFilter batIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent battery = context.registerReceiver(null, batIntentFilter);
            int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = battery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level / (float) scale;
            return batteryPct * 100;
        } catch (Exception e) {
            CCLogger.e(TAG, "Error al obtener estado de la batería");
            return 0;
        }
    }

    public boolean sim() {
        return mTelephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
    }
}
