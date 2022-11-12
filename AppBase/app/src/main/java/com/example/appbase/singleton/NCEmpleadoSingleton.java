package com.example.appbase.singleton;

import android.util.Log;

public class NCEmpleadoSingleton {
    private NCEmpleado empleado;
    private static volatile NCEmpleadoSingleton instance;

    private static final String TAG = NCEmpleadoSingleton.class.getSimpleName();

    /**
     * Constructor del Singleton
     * */
    private NCEmpleadoSingleton() {}

    /**
     * Metodo para crear instancia del Singleton
     * */
    private static NCEmpleadoSingleton sharedInstance() {
        if (instance == null) {
            instance = new NCEmpleadoSingleton();
        }
        return instance;
    }

    /**
     * Metodo para obteber instancia del Singleton
     * */
    public synchronized static NCEmpleado getNCEmpleado(){
        return sharedInstance().empleado;
    }

    /**
     * Metodo para destruir NCEmpleado
     * */
    public static void destroyNCEmpleado() {
        sharedInstance().empleado = null;
    }

    /**
     * Metodo para crear NCEmpleado
     * */
    public synchronized static void getInstance() {
        if (sharedInstance().empleado != null) {
            destroyNCEmpleado();
        }
        instance.empleado = new NCEmpleado();
        Log.e(TAG,"Se creo instancia de NCEmpleado");
    }

    /**
     * Metodo para actualizar NCEmpleado en modo de cola
     * */
    public synchronized static void update() {
        instance.empleado. update();
    }

}
