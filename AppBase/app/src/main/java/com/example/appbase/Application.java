package com.example.appbase;

import static com.example.appbase.logger.LGTest.*;

import com.example.appbase.singleton.NCEmpleadoSingleton;
import com.example.appbase.utils.Utils;
import com.example.appbase.logger.CCLogger;
import com.example.appbase.utils.device.DeviceInfo;
import com.example.appbase.utils.utilerias.UTDateUtilities;
import com.example.appbase.utils.utilerias.UTFileUtilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class Application extends android.app.Application {
    private static final String TAG = Application.class.getSimpleName();



    @Override
    public void onCreate() {
        super.onCreate();
        try {
            setDebug(true);  // FIXME Aqui configurar el modo DEBUG
            setNegocio(validarNumSerieUsuarios());
            if (isNegocio()) {
                setDebug(true);
            }

            if (isDebug()) {
                setRedInterna(true);
                setClon(false);
            }

            setDocuments();
            setEmpleado();
            setDatosDispositivo();
//            setErrorActivity();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDocuments() throws IOException {
        String pathDocuments = "/Documents/";
        CCLogger.PATH = getBaseContext().getApplicationInfo().dataDir + pathDocuments;
        CCLogger.PATH_CACHE = getBaseContext().getCacheDir().getAbsolutePath();
        if (!UTFileUtilities.fileExists(""))
            UTFileUtilities.createDirectory("");

        createFile(CCLogger.PATH, "CatCP_DB.sqlite");
        createFile(CCLogger.PATH, "tablasRespaldo.json");
        createFile(CCLogger.PATH, "DatosAdicionalesDB.sqlite");
        createFile(CCLogger.PATH,"CatGarantiasDB.sqlite");
        createFile(CCLogger.PATH,"AdnPock7.sqlite");
        createFile(CCLogger.PATH,"imgUserDefault.png");
        createFile(CCLogger.PATH,"imgUserSRCUDefault.jpg");
        createFile(CCLogger.PATH,"KPISCobranzaDB.sqlite");
        createFile(CCLogger.PATH,"BRMSDB.sqlite");
        createFile(CCLogger.PATH,"ArquitecturaNACDB.sqlite");
        createFile(CCLogger.PATH, "public.key");
        createFile(CCLogger.PATH, "Apartados_BRMS.json");
        createFile(CCLogger.PATH, "ValidaCelularCUImpl.json");
    }

    public void setEmpleado() {
        NCEmpleadoSingleton.getInstance();
        NCEmpleadoSingleton.update();
    }

    private void createFile(String path, String fileName) throws IOException {
        String pathFile = path + fileName;
        File fileData = new File(pathFile);
        if (!fileData.exists()) {
            InputStream stream = getBaseContext().getAssets().open(fileName);
            UTFileUtilities.writeExtractedFileToDisk(stream, new FileOutputStream(pathFile));
        }
    }

//    private void setErrorActivity() {
//        CaocConfig.Builder.create()
//                .restartActivity(CCLoginActivity.class)
//                .errorActivity(CCErrorActivity.class)
//                .apply();
//    }

    private void setDatosDispositivo() {
        DeviceInfo deviceInfo = new DeviceInfo(this);
        NCEmpleadoSingleton.getNCEmpleado().idVersionApk = Integer.parseInt(Utils.versionCode());
        CCLogger.systemVersion = deviceInfo.getSystemVersion();
        CCLogger.platformId = deviceInfo.getPlatformId();
        CCLogger.provedorCelular = deviceInfo.getProveedor();
        if (isDebug()) {
            NCEmpleadoSingleton.getNCEmpleado().empPassword = "123456";
            NCEmpleadoSingleton.getNCEmpleado().fechaVencimientoPassword = UTDateUtilities.stringFromDate(UTDateUtilities.addMonthsToDate(new Date(), 1), UTDateUtilities.UTDateLargeFormat);
        }
        NCEmpleadoSingleton.update();
    }

    private boolean validarNumSerieUsuarios(){
        boolean success = false;

        try {
            ArrayList<String> numSeries = new ArrayList<>();
            numSeries.add("1490266895");    // Raquel - Relacional
            numSeries.add("1490269190");    // Eduardo - Relacional
            numSeries.add("1491043428");    // Diego - Relacional
            numSeries.add("0821086692");    // Jose - Vencida
            numSeries.add("0821146876");    // Vencida
            numSeries.add("0821145863");    // Vencida
            numSeries.add("1490271887");    // Rafael - Mapas
            numSeries.add("1490321410");    // Tufiño - RMD

            DeviceInfo deviceInfo = new DeviceInfo(this);
            success = numSeries.contains(deviceInfo.getSerialNumber().toUpperCase());
        } catch(Exception e) {
            CCLogger.e(TAG, e.getMessage());
        }
        return success;
    }
}
