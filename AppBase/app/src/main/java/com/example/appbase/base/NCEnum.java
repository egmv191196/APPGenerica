package com.example.appbase.base;

public class NCEnum {
    public static final int NCTipoPaisMexico = 1;
    public static final int NCTipoPaisArgentina = 9;

    public static final int NCTipoEmpleadoGCRJr = 105;
    public static final int NCTipoEmpleadoGCR = 109;
    public static final int NCTipoEmpleadoGCV = 110;
    public static final int NCTipoEmpleadoJVC = 115;
    public static final int NCTipoEmpleadoGCR_Banca = 119;
    public static final int NCTipoEmpleadoGCV_Banca = 120;
    public static final int NCTipoEmpleado_Alterno = 121;
    public static final int NCTipoEmpleadoLCR = 138;
    public static final int NCTipoEmpleadoLCV = 148;
    public static final int NCTipoEmpleadoAvante = 2001;
    public static final int NCTipoEmpleadoAvante2 = 2002;
    public static final int NCTipoEmpleadoSinAsignar = -1;

    public static final int NCTipoSegmentoSinAsignar = -1;
    public static final int NCTipoSinSegmento = 0;
    public static final int NCTipoSegmento1A = 1;
    public static final int NCTipoSegmento1B = 2;
    public static final int NCTipoSegmento2A = 4;
    public static final int NCTipoSegmento2B = 44;

    public static final int NCSubTipoEmpleadoJC = 2;
    public static final int NCSubTipoEmpleadoJV = 3;
    public static final int NCSubTipoEmpleadoSinAsignar = -1;

    public static final int NCTipoActivoFijoDesconocido = 0;
    public static final int NCTipoActivoFijoAndroid= 4;

    public static final int NCTipoStatusPasswordInactivo = 0;
    public static final int NCTipoStatusPasswordActivo = 1;

    //origen descarga de aplicacion
    public final static int NCOrigenWindows = 0;
    public final static int NCOrigenUnix = 1;
    public final static int NCOrigenAmazon = 2;

    public static final int NCTypePictureTypeJPG = 1;
    public static final int NCTypePictureTypeTIF = 2;

    public static int NC_ORIGEN_DESCARGA(String x) {
        switch (x) {
            case "BancoAztecaWindows":
                return NCOrigenWindows;

            case "BancoAztecaUnix":
                return NCOrigenUnix;
            case "AWS":
            default:
                return NCOrigenAmazon;
        }
    }

    public static final int NCTipoPortafolioNoAsignado = 0;
    public static final int NCTipoPortafolioConcentrado = 1;
    public static final int NCTipoPortafolioMixto = 2;
    public static final int NCTipoPortafolioDisperso = 3;
}
