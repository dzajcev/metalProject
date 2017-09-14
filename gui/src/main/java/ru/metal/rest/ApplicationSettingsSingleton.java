package ru.metal.rest;

import java.io.*;
import java.util.Properties;

/**
 * Created by User on 08.08.2017.
 */
public class ApplicationSettingsSingleton {
    private static ApplicationSettingsSingleton ourInstance = new ApplicationSettingsSingleton();

    public static ApplicationSettingsSingleton getInstance() {
        return ourInstance;
    }

    private String serverAddress;

    private String publicKey;

    private String privateKey;

    private final String workApplicationName="MetalControl";

    private final String settingFileName="config.properties";

    private ApplicationSettingsSingleton() {
        loadProperties();
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getWorkApplicationName() {
        return workApplicationName;
    }

    public String getSettingFileName() {
        return settingFileName;
    }

    public void save(){

        Properties prop = new Properties();
        OutputStream output = null;

        try {
            String userHome = System.getProperty("user.home");
            File propDirectory=new File(userHome+File.separator+ ApplicationSettingsSingleton.getInstance().getWorkApplicationName());
            propDirectory.mkdirs();

            File propFile=new File(propDirectory.getAbsolutePath()+File.separator+ApplicationSettingsSingleton.getInstance().getSettingFileName());
            if (!propFile.exists()){
                propFile.createNewFile();
            }
            output = new FileOutputStream(propFile);

            // set the properties value
            prop.setProperty("serverAddress", serverAddress);
            prop.setProperty("privateKey", privateKey);
            prop.setProperty("publicKey",publicKey);
            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    private void loadProperties(){
        Properties prop = new Properties();
        InputStream input = null;

        try {
            String userHome = System.getProperty("user.home");
            File propDirectory=new File(userHome+File.separator+ getWorkApplicationName());
            propDirectory.mkdirs();

            File propFile=new File(propDirectory.getAbsolutePath()+File.separator+getSettingFileName());
            if (!propFile.exists()){
                propFile.createNewFile();
            }
            input = new FileInputStream(propFile);

            // load a properties file
            prop.load(input);

            setServerAddress(prop.getProperty("serverAddress"));
            setPrivateKey(prop.getProperty("privateKey"));
            setPublicKey(prop.getProperty("publicKey"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
