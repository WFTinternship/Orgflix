package am.aca.util;

import com.oracle.webservices.internal.api.message.PropertySet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by David on 6/6/2017.
 */
public class PropertyManager {
    private String fileName;
    public PropertyManager(String fileName){
        this.fileName = fileName;
    }
    public Properties getProperties() throws IOException {
        InputStream inputStream = null;
        Properties properties = null;
        try {
            properties = new Properties();
            inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
            }
        }catch (Exception e){

        }finally {
            if(inputStream != null)
                inputStream.close();
        }
        return properties;
    }


}
