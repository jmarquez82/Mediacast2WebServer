package app.dev.com.mediacast.includes;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static app.dev.com.mediacast.includes.AppVars.pathHomeStatic;


/**
 * Created by Jmarquez on 04-12-17.
 */

public class Download {

    final String TAG = "Download Class";

    private String filename;
    private String pathUrl;
    private int fileSizeExternal;
    private String fileSizeExternalText;
    private String urlFileFull;
    private File filelocal;
    private String pathLocal = pathHomeStatic;
    private int fileSizeLocal;
    private String fileSizeLocalText;
    private InputStream inputStream = null;
    private String statusFile;
    private String pathLocalFull;
    private String statusConn;
    private boolean fileExist;

    /**
     * Método Contructor
     * @param filename Nombre del archivo, el path viene cargado por defecto.
     */
    public Download(String filename) throws MalformedURLException {
        URL url = new URL(filename);
        setFilename( filename.substring(filename.lastIndexOf("/")+1,filename.length()) );
        setPathUrl( filename.substring(0, filename.lastIndexOf("/")+1) );

        setUrlFileFull( filename );
        setPathLocalFull( this.getPathLocal() + this.getFilename() );
        setFileExist(fileExists());
        if(!this.isFileExist()) {
            Log.e(TAG, "Detecta que el archivo no existe");
            try {
                if (connection(new URL(this.getUrlFileFull()))) {
                    Log.e(TAG, "Conexión abierta");
                } else {
                    Log.e(TAG, "Problemas al abrir la conexión");
                }
            } catch (Exception ex) {

            }
        }else{
            try {
                Thread.sleep(100);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

        }
    }

    private boolean fileExists(){
        boolean status;
        File imagen = new File(this.getPathLocalFull());
        Log.e(TAG,this.getPathLocalFull() + " => Tamaño imagen " + imagen.length());
        if (imagen.exists()) {
            //setFileSizeLocal( Integer.parseInt(String.valueOf(imagen.length()/1024)) );
            status = true;
            this.setStatusFile("Exist");
        }else{
            status = false;
        }
        return status;
    }

    /**
     * Metodo que abre la conexión
     * @param urlExternal url externa http / https, formateada URL
     */
    private boolean connection(URL urlExternal){

        boolean status = false;
        if (isOnlineNet()) {

            try {
                /* Open a connection */
                URLConnection ucon = urlExternal.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) ucon;
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try {
                        setInputStream(httpConn.getInputStream());
                        setFileSizeExternal(httpConn.getContentLength());
                        status = true;
                        setStatusConn("OK");
                    }catch(Exception ex){
                        status = false;
                        setStatusConn("NOK");
                        setStatusFile("El archivo se encuentra dañado. " + ex);
                    }
                }else{
                    status = false;
                    setStatusConn("NOK");
                    setStatusFile("El archivo no esta disponible en Internet.");
                }


            } catch (IOException ex) {
                status = false;
                setStatusConn("NOK");
                setStatusConn("Error al abrir la conexión");
            }
        }else{
            status = false;
            setStatusConn("NOK");
            setStatusConn("No hay conexión a Internet");
        }
        return status;
    }

    /**
     * Metodo que descarga el archivo
     * @throws IOException
     */
    public void downloadFile() throws IOException {
        Log.e(TAG, "ENtra a download?");
        if(!fileExists()) {
            Log.e(TAG, "ENtra a download mas dentro?");
            File direct = new File(getPathLocal());

            if (!direct.exists()) {
                direct.mkdirs();
            }
            File imagen = new File(this.getPathLocalFull());

            try {
                FileOutputStream fos = new FileOutputStream(imagen);
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ((bufferLength = this.getInputStream().read(buffer)) > 0) {
                    fos.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + getFileSizeExternal());
                }
                this.setStatusFile("Download");
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Valida si la conexión se encuentra activa
     * @return
     */
    public Boolean isOnlineNet() {
        try {
            Process p = Runtime.getRuntime().exec("ping -c 1 www.google.cl");
            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Bytes to KB
     * @param bytes
     * @return
     */
    public String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2 , BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1)
            return(returnValue + " MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2 , BigDecimal.ROUND_UP).floatValue();
        return (returnValue + " KB");
    }



    /**
     * Getters and Setters
     *
     */
    public String getFilename() {
        return filename;
    }

    public void setFilename(String url) {
        this.filename = url;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public File getFilelocal() {
        return filelocal;
    }

    public void setFilelocal(File filelocal) {
        this.filelocal = filelocal;
    }

    public String getPathLocal() {
        return pathLocal;
    }

    public void setPathLocal(String pathLocal) {
        this.pathLocal = pathLocal;
    }

    public int getFileSizeLocal() {
        return fileSizeLocal;
    }

    public void setFileSizeLocal(int fileSizeLocal) {
        this.fileSizeLocal = fileSizeLocal;
        setFileSizeLocalText(bytes2kb(fileSizeLocal));
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int getFileSizeExternal() {
        return fileSizeExternal;
    }

    public void setFileSizeExternal(int fileSizeExternal) {
        this.fileSizeExternal = fileSizeExternal;
        setFileSizeExternalText(bytes2kb(fileSizeExternal));
    }

    public String getUrlFileFull() {
        return urlFileFull;
    }

    public void setUrlFileFull(String urlFileFull) {
        this.urlFileFull = urlFileFull;
    }


    public String getStatusConn() {
        return statusConn;
    }

    public void setStatusConn(String statusConn) {
        this.statusConn = statusConn;
    }

    public String getPathLocalFull() {
        return pathLocalFull;
    }

    public void setPathLocalFull(String pathLocalFull) {
        this.pathLocalFull = pathLocalFull;
    }

    public String getFileSizeExternalText() {
        return fileSizeExternalText;
    }

    public void setFileSizeExternalText(String fileSizeExternalText) {
        this.fileSizeExternalText = fileSizeExternalText;
    }

    public String getFileSizeLocalText() {
        return fileSizeLocalText;
    }

    public void setFileSizeLocalText(String fileSizeLocalText) {
        this.fileSizeLocalText = fileSizeLocalText;
    }


    public String getStatusFile() {
        return statusFile;
    }

    public void setStatusFile(String statusFile) {
        this.statusFile = statusFile;
    }

    public boolean isFileExist() {
        return fileExist;
    }

    public void setFileExist(boolean fileExist) {
        this.fileExist = fileExist;
    }


}
