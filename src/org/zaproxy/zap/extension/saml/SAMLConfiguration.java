package org.zaproxy.zap.extension.saml;

import org.parosproxy.paros.model.Model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Set;

public class SAMLConfiguration {

    private static final String SAML_CONF_FILE = "zap_saml_conf.xml";
    private static SAMLConfiguration configuration = new SAMLConfiguration();

    private boolean initialized;

    private SAMLConfigData configData;

    public static SAMLConfiguration getConfiguration() throws SAMLException {
        if(!configuration.initialized){
            configuration.initialize();
            configuration.initialized = true;
        }
        return configuration;
    }

    /**
     * Initialize the attributes and configurations
     * @throws SAMLException
     */
    public void initialize() throws SAMLException {
        String confPath = Model.getSingleton().getOptionsParam(). getUserDirectory().getAbsolutePath()+ "/" +
                SAML_CONF_FILE;
        File confFile = new File(confPath);

        if(!confFile.exists()){
            confFile = new File(SAMLConfiguration.class.getResource(SAML_CONF_FILE).getFile());
            if (!confFile.exists()){
                throw new SAMLException("Configuration file not found");
            }
            //todo:create a copy at user directory
        }

        //load the configuration
        configData = (SAMLConfigData) loadXMLObject(SAMLConfigData.class,confFile);
    }

    public Set<Attribute> getAvailableAttributes() {
        return configData.getAvailableAttributes();
    }

    public Set<Attribute> getAutoChangeAttributes(){
        return configData.getAutoChangeValues();
    }

    /**
     * Unmarshall the XML file and extract the object using JAXB
     * @param clazz class of the object
     * @param file xml file
     * @return
     * @throws SAMLException
     */
    private Object loadXMLObject(Class clazz, File file) throws SAMLException {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            throw new SAMLException("XML loading failed",e);
        }
    }
}
