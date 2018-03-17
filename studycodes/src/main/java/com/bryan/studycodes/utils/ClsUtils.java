package com.bryan.studycodes.utils;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Bryan on 2018/3/4.
 */

public class ClsUtils {

    public static boolean createBond(Class btClass, BluetoothDevice btDevice)
            throws Exception {
        Method createBondMethod = btClass.getMethod("createBond");
        return (boolean) createBondMethod.invoke(btDevice);
    }

    public static boolean removeBond(Class<?> btClass, BluetoothDevice btDevice)
            throws Exception {
        Method removeBondMethod = btClass.getMethod("removeBond");
        return (boolean) removeBondMethod.invoke(btDevice);

    }

    public static boolean setPin(Class<? extends BluetoothDevice> btClass, BluetoothDevice btDevice,
                                 String str) throws Exception {

        Method pinMethod = btClass.getDeclaredMethod("setPin", new Class[]{byte[].class});
        return (boolean) pinMethod.invoke(btDevice, new Object[]{str.getBytes()});
    }

    // 取消用户输入
     public static boolean cancelPairingUserInput(Class<?> btClass, BluetoothDevice device) throws Exception {
        Method cancelPairing = btClass.getMethod("cancelPairingUserInput");
        return (boolean) cancelPairing.invoke(device);

    }

    // 取消配对
     public static boolean cancelBondProcess(Class<?> btClass, BluetoothDevice device) throws Exception {
        Method cancelBondProcess = btClass.getMethod("cancelBondProcess");
        return (boolean) cancelBondProcess.invoke(device);
    }

    //确认配对
     public static boolean setPairingConfirmation(Class<?> btClass, BluetoothDevice device, boolean isConfirm) throws Exception {
        Method setPairingConfirmation = btClass.getDeclaredMethod("setPairingConfirmation", boolean.class);
        return (boolean) setPairingConfirmation.invoke(device, isConfirm);
    }


     public static void printAllInform(Class clsShow) {
        try {
            // 取得所有方法
            Method[] hideMethod = clsShow.getMethods();
            int i = 0;
            for (; i < hideMethod.length; i++) {
                Log.e("method name", hideMethod[i].getName() + ";and the i is:"
                        + i);
            }
            // 取得所有常量
            Field[] allFields = clsShow.getFields();
            for (i = 0; i < allFields.length; i++) {
                Log.e("Field name", allFields[i].getName());
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
