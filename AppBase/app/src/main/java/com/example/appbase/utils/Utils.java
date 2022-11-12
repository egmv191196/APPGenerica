package com.example.appbase.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.appbase.logger.CCLogger;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Date;
import java.util.Enumeration;

public class Utils {

    public static ArrayList<String> conjuncionNombre = new ArrayList<>();
    private static final String TAG = Utils.class.getSimpleName();
    //Progress
    private static androidx.appcompat.app.AlertDialog.Builder dialog;
    private static androidx.appcompat.app.AlertDialog alertDialogTransparent;

    /**
     * Este metodo regresa la equivalencia de DP a pixeles.
     *
     * @param dp Valor en dp (density independent pixels). Que se transformara en px
     * @param context Context para obtener el resources y las metricas del display
     * @return float con px
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * Este metodo regresa la equivalencia de pixeles a DP.
     *
     * @param px Valor en px (pixels). Que se transformara en dp
     * @param context Context para obtener el resources y las metricas del display
     * @return float con dp
     */
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null)
                return bitmapDrawable.getBitmap();
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        else
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String capitalizar(String cadena) {
        String[] partes = cadena.toLowerCase().split(" ");
        StringBuilder cadCapitalizada = new StringBuilder();
        for (String parte : partes) {
            parte = parte.replaceAll("( +)", " ").trim();

            if (parte.length() > 0) {
                if (validarConjunciones(parte)) {
                    cadCapitalizada.append(parte).append(" ");
                } else {
                    cadCapitalizada.append(Character.toUpperCase(parte.toLowerCase().charAt(0))).append(parte.substring(1)).append(" ");
                }
            } else {
                cadCapitalizada.append(parte.toUpperCase());
            }
        }
        return cadCapitalizada.toString();
    }

    private static boolean validarConjunciones(String parte) {
        conjuncionNombre.add("de");
        conjuncionNombre.add("la");
        conjuncionNombre.add("los");

        return conjuncionNombre.contains(parte);
    }

    public static int getImageResource(Context context, String nombreArchivo) {
        return context.getResources().getIdentifier(nombreArchivo, "drawable", context.getPackageName());
    }

    public static Bitmap leerImagen(String path) {
        File file = new File(path);
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static String capitalizarPrimeraLetra(String cadena) {
        if (cadena == null)
            return "";

        cadena = cadena.replaceAll("( +)", " ").trim();
        if (cadena.length() > 1)
            return Character.toUpperCase(cadena.charAt(0)) + cadena.substring(1).toLowerCase();

        return cadena.toUpperCase();
    }

    /**
     * Método para redimensionar un Dialogo a un porcentaje especifico de la pantalla
     *
     * @param dialog        dialogo al que se le cambiará el tamaño puede ser un dialogo simple o un dialogFragment
     * @param widthPercent  porcentaje del 1 al 100 para el ancho del dialogo
     * @param heightPercent porcentaje del 1 al 100 para el alto del dialogo
     */
    public static void setDialogWindowSize(Dialog dialog, int widthPercent, int heightPercent) {
        float newHeight;
        float newWidth;
        if (dialog == null)
            return;
        if (widthPercent > 0 && widthPercent <= 100 && heightPercent > 0 && heightPercent <= 100) {
            try {
                Window window = dialog.getWindow();
                Point size = new Point();
                Display display;
                if (window != null) {
                    display = window.getWindowManager().getDefaultDisplay();
                    display.getSize(size);
                    newWidth = size.x * ((float) widthPercent / 100);
                    newHeight = size.y * ((float) heightPercent / 100);
                    window.setLayout((int) newWidth, (int) newHeight);
                    window.setGravity(Gravity.CENTER);
                }
            } catch (Exception e) {
                CCLogger.e(TAG, "Ocurrió un error al modificar el tamaño de la ventana del dialogo");
                CCLogger.e(TAG, e);
            }
        }
    }

    /**
     * Patrones de expresión regular de uso común.
     * agregado en APi 8
     * @param email string de correo electronico
     * componente que debe contener para su validacion verdadera
     * <username>@<mail-server>.<mail-servertype or server-location>
     * @return boolean
     * */
    public static boolean validEmail(@NonNull String email) {
        return (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static String getIp() {
        String ipAddress = "100.100.100.1";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
                        ipAddress = (inetAddress.getHostAddress() != null && !inetAddress.getHostAddress().isEmpty()) ? inetAddress.getHostAddress() : ipAddress;
                }
            }
        } catch (Exception ex) {
            CCLogger.i(TAG, ex.getLocalizedMessage());
        }
        return ipAddress;
    }

    public static Bitmap redimensionarBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static String formatCurrencyNumber(double number) {
        Locale locale = new Locale("es", "MX");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        return numberFormat.format(number);
    }

    /**
     * Método para verificar si una fecha es valida
     * @param date string de la fecha que se validara
     * @return  true si la fecha es valida, false si no lo es
     */
    // TODO: LEGM - Validar actualizacion
    public static Boolean isValidDate(String date) {
        try {
            long d = Date.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void esconderProgressDialog() {
        if (dialog != null && alertDialogTransparent.isShowing()) {
            alertDialogTransparent.dismiss();
            dialog = null;
        }
    }

    public static String formatCurrencyNumberWith(double number) {
        Locale locale = new Locale("es", "MX");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format(number);
    }

    public static String versionCode() {
        return "";
    }
}
