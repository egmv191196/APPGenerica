package com.example.appbase.singleton;

import com.example.appbase.base.NCEnum;
import com.example.appbase.utils.utilerias.UTDateUtilities;
import com.example.appbase.utils.utilerias.UTFileUtilities;
import com.example.appbase.utils.utilerias.UTJson;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.Date;

public class NCEmpleado implements Serializable {

    private final int MINUTOS_ESPERA_PASSWORD = 10;
    final String FILENAME = "RegData.json";

    public String empID;
    public String empNombre;
    public int empPais;
    public String empPassword;
    private int intentosPassword;
    private String fechaBloqueoPassword;

    public int puestoID;
    public String puestoDesc;
    public int regionalID;
    public String regionalDesc;
    public int tipoMapa;

    public int segmentoID;
    public String segmentoDesc;
    public int gerenciaID;
    public String gerenciaDesc;
    public int regionalOracle;
    public String deptoId;

    public String fechaCreacionPassword;
    public String fechaVencimientoPassword;

    public String telMovil;
    public String telOficina;
    public String fechaUltGestion;

    public String correoElectronico;

    public int idVersionApk;
    public boolean habilitoWorker;

    NCEmpleado() {
        init();
        loadInfoEmpleado();
    }

    NCEmpleado(boolean loadData) {
        init();
        if (loadData)
            loadInfoEmpleado();
    }

    private void init() {
        // Empleado Generico
        this.empID = "";
        this.empNombre = "";
        this.empPais = NCEnum.NCTipoPaisMexico;
        this.empPassword = "";

        this.puestoID = NCEnum.NCTipoEmpleadoSinAsignar;
        this.puestoDesc = "";
        this.regionalID = 0;
        this.regionalDesc = "";
        this.fechaCreacionPassword = UTDateUtilities.UTInvalidLargeDate;
        this.fechaVencimientoPassword = UTDateUtilities.UTInvalidLargeDate;
        this.telMovil = "";
        this.telOficina = "";
        this.fechaUltGestion = UTDateUtilities.UTInvalidLargeDate;
        this.segmentoID = NCEnum.NCTipoEmpleadoSinAsignar;
        this.segmentoDesc = "";
        this.gerenciaID = 0;
        this.gerenciaDesc = "";
        this.regionalOracle = 0;
        this.deptoId = "";
        this.idVersionApk = 0;
        this.habilitoWorker = false;

        // Mapa
        this.tipoMapa = 0;
    }

    void update() {
        Gson gson = new Gson();
        String cadenaJson = gson.toJson(this);
        UTFileUtilities.saveFile(FILENAME, cadenaJson);
    }

    private void loadInfoEmpleado() {
        if (!UTFileUtilities.fileExists(FILENAME))
            update();

        if (UTFileUtilities.fileExists(FILENAME)) {
            String cadena = UTFileUtilities.readFile(FILENAME);
            JsonObject empleadoJson = cadena.isEmpty() ? new JsonObject() : new Gson().fromJson(cadena, JsonObject.class);

            // Empleado Generico
            empID = UTJson.isJsonStringValideDefault(empleadoJson, "empID", "");
            empNombre = UTJson.isJsonStringValideDefault(empleadoJson, "empNombre", "");
            empPais = UTJson.isJsonIntValideDefault(empleadoJson, "empPais", NCEnum.NCTipoPaisMexico);
            empPassword = UTJson.isJsonStringValideDefault(empleadoJson, "empPassword", "");
            puestoID = UTJson.isJsonIntValideDefault(empleadoJson, "puestoID", NCEnum.NCTipoEmpleadoSinAsignar);
            puestoDesc = UTJson.isJsonStringValideDefault(empleadoJson, "puestoDesc", "");
            regionalID = UTJson.isJsonIntValideDefault(empleadoJson, "regionalID", 0);
            regionalDesc = UTJson.isJsonStringValideDefault(empleadoJson, "regionalDesc", "");
            fechaCreacionPassword = UTJson.isJsonStringValideDefault(empleadoJson, "fechaCreacionPassword", UTDateUtilities.UTInvalidLargeDate);
            fechaVencimientoPassword = UTJson.isJsonStringValideDefault(empleadoJson, "fechaVencimientoPassword", UTDateUtilities.UTInvalidLargeDate);
            telMovil = UTJson.isJsonStringValideDefault(empleadoJson, "telMovil", "");
            telOficina = UTJson.isJsonStringValideDefault(empleadoJson, "telOficina", "");
            fechaUltGestion = UTJson.isJsonStringValideDefault(empleadoJson, "fechaUltGestion", UTDateUtilities.UTInvalidLargeDate);
            segmentoID = UTJson.isJsonIntValideDefault(empleadoJson, "segmentoID", NCEnum.NCTipoEmpleadoSinAsignar);
            segmentoDesc = UTJson.isJsonStringValideDefault(empleadoJson, "segmentoDesc", "");
            gerenciaID = UTJson.isJsonIntValideDefault(empleadoJson, "gerenciaID", 0);
            gerenciaDesc = UTJson.isJsonStringValideDefault(empleadoJson, "gerenciaDesc", "");
            regionalOracle = UTJson.isJsonIntValideDefault(empleadoJson, "regionalOracle", 0);
            deptoId = UTJson.isJsonStringValideDefault(empleadoJson, "deptoId", "");
            idVersionApk = UTJson.isJsonIntValideDefault(empleadoJson, "idVersionApk", 0);
            habilitoWorker = UTJson.isJsonBooleanValideDefault(empleadoJson, "habilitoWorker", false);
            tipoMapa = UTJson.isJsonIntValideDefault(empleadoJson, "tipoMapa", 0);

        }
    }

//    public static Date fechaCargaRuta() {
//        NCEmpleado empleado = new NCEmpleado();
//        return UTDateUtilities.dateFromString(empleado.ruta.fechaCargaRuta, UTDateUtilities.UTDateLargeFormat);
//    }

    public static String numero() {
        NCEmpleado empleado = new NCEmpleado();
        return empleado.empID;
    }

    public String getEmpNombre() {
        String[] nombreCompleto = empNombre.split(" ");
        String nombre = "";
        if (nombreCompleto.length > 0) {
            nombre = nombreCompleto[0];
        }
        return nombre;
    }

    public String getEmpApPa() {
        String[] nombreCompleto = empNombre.split(" ");
        String apePa = "";
        if (nombreCompleto.length > 0) {
            apePa = nombreCompleto[1];
        }
        return apePa;
    }

    public String getEmpApMa() {
        String[] nombreCompleto = empNombre.split(" ");
        String apeMa = "";
        if (nombreCompleto.length > 0) {
            apeMa = nombreCompleto[2];
        }
        return apeMa;
    }

    public  boolean tipoMapa(){
        NCEmpleado empleado = new NCEmpleado();
        return empleado.tipoMapa == 1;
    }
}
