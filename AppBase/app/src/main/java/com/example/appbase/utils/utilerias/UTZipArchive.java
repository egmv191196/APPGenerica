package com.example.appbase.utils.utilerias;

import com.example.appbase.logger.CCLogger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UTZipArchive {
    private static final int BUFFER_SIZE = 8192;
    private static String parentPath = "";
    private static final String TAG = UTZipArchive.class.getSimpleName();

    public static byte[] decodifica(String respuestaString) {
        if (respuestaString.equals("")) {
            throw new IllegalArgumentException("Data response vacio");
        }
        return android.util.Base64.decode(respuestaString, android.util.Base64.DEFAULT);
    }

    public static String descomprime(byte[] respuestaBase64) {
        String cadena = "";
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(respuestaBase64);
            InputStream gzis = new GZIPInputStream(bais);
            InputStreamReader reader = new InputStreamReader(gzis);
            BufferedReader in = new BufferedReader(reader);
            String readed;
            while ((readed = in.readLine()) != null) {
                cadena += readed;
            }
            bais.close();
            gzis.close();
            reader.close();
            in.close();
        } catch (Exception ex) {
            CCLogger.e(TAG, ex);
        }
        return cadena;
    }

    public static String codifica(byte[] bytes) {
        return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
    }

    public static String compressEncryptStringBase64(String str) {
        String strZipAndCode = null;
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            ByteArrayOutputStream obj = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(obj);
            //gzip.write(str.getBytes("UTF-8"));
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
            gzip.close();

            strZipAndCode = android.util.Base64.encodeToString(obj.toByteArray(), android.util.Base64.DEFAULT);

        } catch (Exception ex) {
            CCLogger.e(TAG, ex);
        }
        return strZipAndCode;
    }

    public static String compressEncryptStringBase64Windows(String str) {
        String strZipAndCode = null;
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            ByteArrayOutputStream obj = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(obj);
            gzip.write(str.getBytes("windows-1252"));
            gzip.close();

            strZipAndCode = android.util.Base64.encodeToString(obj.toByteArray(), android.util.Base64.DEFAULT);

        } catch (Exception ex) {
            CCLogger.e(TAG, ex);
        }
        return strZipAndCode;
    }

    public static String encodeFileToBase64Binary(String fileName)
            throws IOException {

        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encodeBase64(bytes);
        String encodedString = new String(encoded);

        return encodedString;
    }

    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    public static byte[] encodeFileToBytesBase64Binary(String fileName) throws IOException {
        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encodeBase64(bytes);
        return encoded;
    }

    public static byte[] decodeFileToBytesBase64Binary(String fileName) {
        byte[] fileBytes = new byte[0];
        try {
            File file = new File(fileName);
            if (file.exists()) {
                fileBytes = loadFile(file);
            }
            fileBytes = Base64.decodeBase64(fileBytes);
        } catch (Exception e) {
            CCLogger.e(TAG, "No se pudo decodificar: " + fileName);
        }
        return fileBytes;
    }

    public static boolean zip(String sourcePath, String destinationPath, String destinationFileName, Boolean includeParentFolder) {
        new File(destinationPath).mkdirs();
        FileOutputStream fileOutputStream;
        ZipOutputStream zipOutputStream = null;
        try {
            if (!destinationPath.endsWith("/")) destinationPath += "/";
            String destination = destinationPath + destinationFileName;
            File file = new File(destination);
            if (!file.exists()) file.createNewFile();

            fileOutputStream = new FileOutputStream(file);
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));

            if (includeParentFolder)
                parentPath = new File(sourcePath).getParent() + "/";
            else
                parentPath = sourcePath;

            zipFile(zipOutputStream, sourcePath);

        } catch (Exception ioe) {
            CCLogger.e(TAG, ioe.getMessage());
            return false;
        } finally {
            if (zipOutputStream != null)
                try {
                    zipOutputStream.close();
                } catch (Exception e) {
                    CCLogger.e(TAG, "finally");
                    CCLogger.e(TAG, e);
                }
        }
        return true;
    }

    private static void zipFile(ZipOutputStream zipOutputStream, String sourcePath) throws IOException {
        java.io.File files = new java.io.File(sourcePath);
        java.io.File[] fileList = files.listFiles();

        String entryPath = "";
        BufferedInputStream input;
        for (java.io.File file : fileList) {
            if (file.isDirectory()) {
                zipFile(zipOutputStream, file.getPath());
            } else {
                byte data[] = new byte[BUFFER_SIZE];
                FileInputStream fileInputStream = new FileInputStream(file.getPath());
                input = new BufferedInputStream(fileInputStream, BUFFER_SIZE);
                entryPath = file.getAbsolutePath().replace(parentPath, "");

                ZipEntry entry = new ZipEntry(entryPath);
                zipOutputStream.putNextEntry(entry);

                int count;
                while ((count = input.read(data, 0, BUFFER_SIZE)) != -1) {
                    zipOutputStream.write(data, 0, count);
                }
                input.close();
            }
        }
    }
}
