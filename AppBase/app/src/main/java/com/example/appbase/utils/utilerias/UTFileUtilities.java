package com.example.appbase.utils.utilerias;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.appbase.logger.CCLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class UTFileUtilities {
    public static final String SEPARATOR = "/";
    public static final String EXT_JPG = ".jpg";
    public static final String EXT_PNG = ".png";
    public static final String EXT_PDF = ".pdf";
    public static final String EXT_TXT = ".txt";
    public static final String SIGNATURES_DIRECTORY = "Firmas";

    private static final String TAG = UTFileUtilities.class.getSimpleName();

    public static boolean fileExists(String path) {
        //public static boolean existeArchivo(String path) { //TODO AVM DELETE
        boolean exists = false;
        File file = new File(CCLogger.PATH + path);
        if (file.exists()) {
            exists = true;
        }
        return exists;
    }

    /**
     * Copiar archivos
     */
    public static void writeExtractedFileToDisk(InputStream in, OutputStream outs) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            outs.write(buffer, 0, length);
        }
        outs.flush();
        outs.close();
        in.close();
    }

    public static String readFile(String fileName) {
        //public static String leerArchivo(String fileName) { //TODO AVM DELETE
        String path = CCLogger.PATH + fileName;
        String chain = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            chain = sb.toString();
            br.close();
        } catch (Exception e) {
            CCLogger.e(TAG, e);
        }
        return chain;
    }

    public static boolean updateFile(String fileName, String chain) {
        return deleteFile(fileName) && saveFile(fileName, chain);
    }

    public static boolean saveFile(String fileName, String chain) {
        //public static boolean guardarArchivo(String fileName, String chain) { //TODO AVM DELETE
        try {
            File file = new File(CCLogger.PATH + fileName);
            File directory = new File(file.getParent());
            if (!directory.exists() && !directory.mkdirs()) {
                CCLogger.e(TAG, "No se pudo crear el directorio: " + directory.getAbsolutePath());
                return false;
            }
            fileName = file.getAbsolutePath();
            FileOutputStream outputStream = new FileOutputStream(fileName);
            outputStream.write(chain.getBytes());
            outputStream.close();
            return true;
        } catch (Exception e) {
            CCLogger.e(TAG, e);
            return false;
        }
    }

    public static boolean saveFile(String fileName, byte[] chain) {
        //public static boolean guardarArchivo(String fileName, byte[] chain) { //TODO AVM DELETE
        boolean success = true;
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            outputStream.write(chain);
            outputStream.close();
        } catch (Exception e) {
            success = false;
            CCLogger.e(TAG, e);
        }
        return success;
    }

    public static boolean deleteFile(String archive) {
        //public static boolean eliminarArchivo(String archive) { //TODO AVM DELETE
        String pathArchivo = CCLogger.PATH + archive;
        File file = new File(pathArchivo);

        if (file.isDirectory()) {
            for (File lista : file.listFiles()) {
                lista.delete();
            }
        }
        return file.delete();
    }

    public static File createFile(String path, String fold) {
        createDirectory(fold);
        return new File(CCLogger.PATH + path);
    }

    public static boolean createDirectory(String name) {
        //public static boolean crearDirectorio(String name) { //TODO AVM DELETE
        boolean b = false;
        String path = CCLogger.PATH + name;
        File docum = new File(path);
        if (!docum.exists() && !docum.isDirectory()) {
            b = docum.mkdir();
        } else {
            CCLogger.v(TAG, "Carpeta ya existe  " + path);
        }
        return b;
    }

    public static boolean copyFileAssets(AssetManager manager, String file) throws IOException {
        // public static boolean copiarArchivoAssets(AssetManager manager, String file) throws IOException { //TODO AVM DELETE
        boolean sucess = false;
        InputStream is = null;
        OutputStream os = null;
        try {
            is = manager.open(file);
            os = new FileOutputStream(CCLogger.PATH + file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            sucess = true;
        } catch (Exception e) {
            CCLogger.e(TAG, e);
        } finally {
            is.close();
            os.close();
        }
        return sucess;
    }

    public static String documentDirectory() {
        return CCLogger.PATH;
    }

    public static boolean saveObjectWithPath(Object obj, String path) {
        boolean success = false;
        try {
            FileOutputStream f = new FileOutputStream(new File(path));
            ObjectOutputStream o = new ObjectOutputStream(f);
            // Write object to file
            o.writeObject(obj);
            o.close();
            f.close();
            success = true;
        } catch (Exception e) {
            CCLogger.e(TAG, e);
        }
        return success;
    }

    public static Object readObjectWithPath(String path) {
        Object obj = null;
        try {
            FileInputStream fi = new FileInputStream(new File(path));
            ObjectInputStream oi = new ObjectInputStream(fi);
            // Read object
            obj = oi.readObject();
            oi.close();
            fi.close();
        } catch (Exception e) {
            CCLogger.e(TAG, e);
        }
        return obj;
    }

    public static void addInfoFile(String file, String chain) {
        //public static void agregarInfoFile(String file, String chain) { //TODO AVM DELETE
        String cadenaOriginal = readFile(file);
        chain += "\n";
        chain += cadenaOriginal;
        saveFile(file, chain);
    }

    public static String readFileAsString(File file) throws IOException {
        final InputStream inputStream = new FileInputStream(file);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder stringBuilder = new StringBuilder();
        boolean done = false;

        while (!done) {
            final String line = reader.readLine();
            done = (line == null);

            if (line != null) {
                stringBuilder.append(line);
            }
        }
        reader.close();
        inputStream.close();
        return stringBuilder.toString();
    }

    public static void removeFilesWithExtension(final String extension) {
        File[] files = new File(CCLogger.PATH).listFiles(new FileFilter() {

            public boolean accept(File file) {
                if (file.isFile())
                    return file.getName().endsWith('.' + extension);
                return false;
            }
        });
        for (File file : files) {
            if (!file.getName().equals("imgUserDefault.png")) {
                file.delete();
            }
        }
    }

    public static boolean writeToFile(String file, String chain) {
        boolean success;
        String cadenaOriginal = readFile(file);
        chain += "\n";
        chain += cadenaOriginal;
        try {
            file = CCLogger.PATH + file;
            FileOutputStream outputStream = new FileOutputStream(new File(file));
            outputStream.write(chain.getBytes());
            outputStream.close();
            success = true;
        } catch (Exception e) {
            CCLogger.e(TAG, e);
            success = false;
        }
        return success;
    }

    public static boolean writeInFile(String file, String chain) {
        boolean success;
        try {
            file = CCLogger.PATH + file;
            FileOutputStream outputStream = new FileOutputStream(new File(file));
            outputStream.write(chain.getBytes());
            outputStream.close();
            success = true;
        } catch (Exception e) {
            CCLogger.e(TAG, e);
            success = false;
        }
        return success;
    }

    public static boolean deleteFileWithPath(String pathFile) {
        File file = new File(pathFile);
        if (file.isDirectory()) {
            for (File list : file.listFiles()) {
                list.delete();
            }
        }
        return file.delete();
    }

    public static void copyItemAtDocumentsPath(String srcPath, String dstPath) throws IOException {
        File srcLocation = new File(CCLogger.PATH + srcPath);
        File dstLocation = new File(CCLogger.PATH + dstPath);

        InputStream in = new FileInputStream(srcLocation);
        OutputStream out = new FileOutputStream(dstLocation);

        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static ArrayList<File> contentsOfDirectoryAtPath(String path, StringBuilder error) {
        path = CCLogger.PATH + path;
        File file = new File(path);
        if (!file.exists()) {
            error.append(path + ": No existe el directorio");
            return new ArrayList<>();
        }
        if (!file.isDirectory()) {
            error.append(path + ": No es un directorio");
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(file.listFiles()));
    }

    public static String getPathExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    public static boolean copyFile(String src, String dst) {
        boolean success = true;
        InputStream in = null;
        try {
            in = new FileInputStream(src);
        } catch (Exception e) {
            success = false;
            CCLogger.e(TAG, "foto:" + e);
        }
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (Exception ex) {
                success = false;
                CCLogger.e(TAG, "foto:" + ex);
            } finally {
                out.close();
            }
        } catch (Exception ex) {
            success = false;
            CCLogger.e(TAG, "foto:" + ex);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                success = false;
                CCLogger.e(TAG, "foto:" + e);
            }
        }
        return success;
    }

    public static void createFile(String path, String fileName, Context context) {
        String pathFile = path + fileName;
        File fileData = new File(pathFile);
        if (!fileData.exists()) {
            InputStream stream;
            try {
                stream = context.getAssets().open(fileName);
                writeExtractedFileToDisk(stream, new FileOutputStream(pathFile));
                Log.e(TAG, "just create :" + fileName);
            } catch (IOException e) {
                CCLogger.e(TAG, e);
            } catch (Exception e) {
                CCLogger.e(TAG, e);
            }

        }
    }

    public static ArrayList<String> listFiles(File carpeta) {
        ArrayList<String> archivos = new ArrayList<>();
        if (carpeta.listFiles() != null) {
            for (final File ficheroEntrada : carpeta.listFiles()) {
                if (ficheroEntrada.isDirectory()) {
                    listFiles(ficheroEntrada);
                } else {
                    archivos.add(ficheroEntrada.getName());
                }
            }
        }
        return archivos;
    }

    public static void deleteWeeklyFilesByFolder(String carpeta) {
        //private void borrarArchviosSemanalesPorCarpeta(String carpeta)
        String nombreArchivo;
        StringBuilder error = new StringBuilder();
        String path = UTFileUtilities.documentDirectory() + carpeta;
        ArrayList<File> archivos = UTFileUtilities.contentsOfDirectoryAtPath(path, error);
        if (archivos.size() > 0) {
            for (File file : archivos) {
                nombreArchivo = file.getName();

                if (UTFileUtilities.getPathExtension(nombreArchivo).equals("jpg") || UTFileUtilities.getPathExtension(nombreArchivo).equals("plist")) {
                    Date fileCreationDate = new Date();
                    fileCreationDate.setTime(file.lastModified());

                    if (UTDateUtilities.daysFromDate(fileCreationDate, new Date()) > 7) {
                        if (!file.delete()) {
                            CCLogger.e(TAG, "No se pudo borrar la foto " + nombreArchivo);
                        }
                    }
                }

            }
        } else {
            CCLogger.e(TAG, "No se pudo obtener datos del directorio " + carpeta);
        }
    }

    public static boolean clearDocuments() {
        if (UTFileUtilities.fileExists("")) {
            return deleteDirectoryContent(new File(documentDirectory()));
        }
        return true;
    }

    private static boolean deleteDirectoryContent(File archivo) {
        if (archivo.isDirectory()) {
            File[] files = archivo.listFiles();
            for (int x = 0; x < files.length; x++) {
                if (files[x].isDirectory()) {
                    if (!deleteDirectoryContent(files[x])) {
                        return false;
                    }
                } else {
                    if (!files[x].delete()) {
                        return false;
                    }
                }
            }
        }
        if (!archivo.delete()) {
            return false;
        }
        return true;
    }
}
