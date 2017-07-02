package am.aca.orgflix.util;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
public class PropertyManager {
    private static final Logger LOGGER = Logger.getLogger(PropertyManager.class.getName());

    private String fileName;

    public PropertyManager(String fileName) {
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
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return properties;
    }


}
