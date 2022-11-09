package com.example.appbase.logger;
public class LGTest {
    private static LGTest instance;
    private boolean debug;
    private boolean clon;
    private boolean puenteGoogleMaps;
    private boolean openStreetTiles;
    private boolean esRedInterna;
    private boolean esNegocio;

    private LGTest() {
        this.debug = false;
        this.clon = false;
        this.puenteGoogleMaps = true;
        this.openStreetTiles = false;
        this.esNegocio = false;
        this.esRedInterna = false;
    }

    private static LGTest getInstance() {
        if (instance == null)
            instance = new LGTest();

        return instance;
    }

    public static boolean isDebug() {
        LGTest test = getInstance();

        return test.debug;
    }

    public static void setDebug(boolean debug) {
        LGTest test = getInstance();
        test.debug = debug;
    }

    public static boolean isClon() {
        LGTest test = getInstance();
        return test.debug ? test.clon : false;
    }

    public static void setClon(boolean clon) {
        LGTest test = getInstance();
        test.clon = test.debug ? clon : false;
    }

    public static boolean isPuenteGoogleMaps() {
        LGTest test = getInstance();

        return test.puenteGoogleMaps;
    }

    public static void setPuenteGoogleMaps(boolean puenteGoogleMaps) {
        LGTest test = getInstance();
        test.puenteGoogleMaps = puenteGoogleMaps;
    }

    public static  boolean isOpenStreetTiles() {
        LGTest test = getInstance();

        return test.openStreetTiles;
    }

    public static void setOpenStreetTiles(boolean openStreetTiles) {
        LGTest test =  getInstance();
        test.openStreetTiles = openStreetTiles;
    }

    public static void setRedInterna(boolean redInterna) {
        LGTest test = getInstance();
        test.esRedInterna = redInterna;
    }

    public static boolean isRedInterna() {
        LGTest test = getInstance();

        return test.esRedInterna;
    }

    public static void setNegocio(boolean negocio) {
        LGTest test = getInstance();
        test.esNegocio = negocio;
    }

    public static boolean isNegocio() {
        LGTest test = getInstance();

        return test.esNegocio;
    }
}

