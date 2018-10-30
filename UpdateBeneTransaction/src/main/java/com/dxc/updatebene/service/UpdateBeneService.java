package com.dxc.updatebene.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.soap.SOAPException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.dxc.updatebene.constants.UpdateBeneConstants;
import com.dxc.updatebene.helper.UpdateBeneHelper;
import com.dxc.updatebene.request.IndexedData;
import com.dxc.updatebene.request.TranDetails;
import com.dxc.updatebene.request.TxUpdateBene;
import com.dxc.updatebene.request.WMAData;
/*
 * developed by smedisetti4
 * 
 */

@WebService(endpointInterface="com.dxc.updatebene.service.UpdateBene", serviceName="UpdateBeneService",portName = "UpdateBenePort")
public class UpdateBeneService implements UpdateBene {

	private	 static final Logger log=Logger.getLogger(UpdateBeneService.class.getName());

	UpdateBeneHelper updateBeneHelper=new UpdateBeneHelper();
	String textFilePath;

	public  UpdateBeneService() {

		log.info("Log4j properties file loading starting...");
		Properties Config_properties = new Properties();
		InputStream fileInput=this.getClass().getResourceAsStream("/config.properties");
		try {
			Config_properties.load(fileInput);
			PropertyConfigurator.configure(Config_properties.getProperty("Log4jProperties_Location"));
			System.out.println(Config_properties.getProperty("Log4jProperties_Location"));
		} catch (IOException e) {
			log.error("Error occured while retreiving a log4j Property value"+e.getMessage());
			e.printStackTrace();
		}
	}

	@WebMethod
	public TranDetails updateBeneCreate(@WebParam(name="TxUpdateBene") TxUpdateBene txUpdateBene)  {
		log.info("updateBeneCreate  method starting");
		IndexedData indexedData = txUpdateBene.getIndexedData();
		TranDetails tranInfo = txUpdateBene.getTranDetails();
		WMAData wmaData = txUpdateBene.getWMAData();

		boolean validate = updateBeneHelper.indexedDataContainsNull(indexedData);
		log.info("Indexed data  null checking return is ::"+validate);
		if(validate == true) {
			log.info("Indexed Data contains null or empty values :");
			tranInfo.setTranID(tranInfo.getTransRefGUID());
			tranInfo.setTransExeDate(wmaData.getTransExeDate());
			tranInfo.setTransExeTime(updateBeneHelper.getTimeStamp());
			tranInfo.setTransType(indexedData.getTransType());
			tranInfo.setResultCode(UpdateBeneConstants.MSGCODEFAILURE);
		}
		else{
			boolean txtFile = updateBeneHelper.createTxtFile(wmaData);
			log.info("creteTxtfile return data value is ::"+txtFile);
			if(txtFile == true){
				log.info("Txt file creaed successfully.");
				textFilePath=updateBeneHelper.getText_file_path();
				boolean xmlFile=updateBeneHelper.createXmlFile(indexedData,textFilePath);
				log.info("creteXmlfile return data value is ::"+xmlFile);
				if(xmlFile == true){
					log.info("xml file created successfully.");
					tranInfo.setTranID(tranInfo.getTransRefGUID());
					tranInfo.setTransExeDate(wmaData.getTransExeDate());
					tranInfo.setTransExeTime(updateBeneHelper.getTimeStamp());
					tranInfo.setTransType(indexedData.getTransType());
					tranInfo.setResultCode(UpdateBeneConstants.MSGCODESUCCESS);

				}
			}
		}
		log.info("updateBeneCreate method end.");
		return tranInfo;
	}
}
