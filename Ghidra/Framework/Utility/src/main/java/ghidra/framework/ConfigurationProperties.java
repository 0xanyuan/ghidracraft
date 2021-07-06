/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.framework;

import generic.jar.ResourceFile;
import ghidra.util.NullOutputStream;
import ghidra.util.SystemUtilities;
import ghidra.util.config.PropertiesEnhance;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static ghidra.util.config.ColorHexConvert.toColorFromString;


public class ConfigurationProperties extends PropertiesEnhance {

    /**
     * The name of the Color properties file.
     */
    public static final String COLOR_PROPERTY_NAME = "Color.properties";
    public static final String COLOR_PROPERTY_FILE = "/RuntimeScripts/Common/support/Color.properties";
    public static final String COLOR_PROPERTY_FILE_INS = "/support/Color.properties";

    /**
     * Creates a new configuration properties from the given config properties file.
     *
     * @param configPropertiesFile The application properties file.
     * @throws IOException If there was a problem loading/reading a discovered properties file.
     */
    public ConfigurationProperties(ResourceFile configPropertiesFile) throws IOException {

        if (!configPropertiesFile.exists()) {
            throw new FileNotFoundException(
                    COLOR_PROPERTY_NAME + " file does not exist: " + configPropertiesFile);
        }
        try (InputStream in = configPropertiesFile.getInputStream()) {
            load(in);
        }
    }

    /**
     * Creates a new configuration properties from the configuration properties files found
     * in the given application root directories.  If multiple configuration properties files
     * are found, the properties from the files will be combined.  If duplicate keys exist,
     * the newest key encountered will overwrite the existing key.
     *
     * @param applicationRootDirs The application root directories to look for the properties files in.
     * @throws IOException If there was a problem loading/reading a discovered properties file.
     */
    public ConfigurationProperties(Collection<ResourceFile> applicationRootDirs) throws IOException {
        boolean found = false;
        // Application installation directory
        ResourceFile applicationInstallationDir = applicationRootDirs.iterator().next().getParentFile();
        if (SystemUtilities.isInDevelopmentMode()) {
            for (ResourceFile appRoot : applicationRootDirs) {
                ResourceFile configPropertiesFile = new ResourceFile(appRoot, COLOR_PROPERTY_FILE);
                if (configPropertiesFile.exists()) {
                    try (InputStream in = configPropertiesFile.getInputStream()) {
                        load(in);
                        found = true;
                    }
                }
            }
        }
        else {
            ResourceFile configPropertiesFile = new ResourceFile(applicationInstallationDir, COLOR_PROPERTY_FILE_INS);
            if (configPropertiesFile.exists()) {
                try (InputStream in = configPropertiesFile.getInputStream()) {
                    load(in);
                    found = true;
                }
            }
        }
        if (!found) {
            throw new IOException(COLOR_PROPERTY_NAME + " was not found!");
        }
    }

    /**
     * Get Properties from Color.properties by key
     *
     * @param key Color.properties key
     * @return Color Object
     * */
	public Color ReadColorFromProperties(String key) {
		Color color = toColorFromString(getProperty(key));
		return color;
	}
}
