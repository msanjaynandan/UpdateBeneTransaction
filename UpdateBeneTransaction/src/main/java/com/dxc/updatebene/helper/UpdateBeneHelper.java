package com.dxc.updatebene.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import com.dxc.updatebene.constants.UpdateBeneConstants;
import com.dxc.updatebene.request.Bene;
import com.dxc.updatebene.request.IndexedData;
import com.dxc.updatebene.request.TranDetails;
import com.dxc.updatebene.request.WMAData;
public class UpdateBeneHelper {

	private final static Logger log=Logger.getLogger(UpdateBeneHelper.class.getName());

	public Properties getConfigProp() throws IOException{
		Properties Config_properties = new Properties();
		InputStream fileInput=this.getClass().getResourceAsStream("/config.properties");
		Config_properties.load(fileInput);
		return Config_properties;
	}

	public Properties getProp() {
		Properties Service_properties=new Properties();
		log.info("Fetching paths from Properties file ::");
		try {
			FileInputStream Service_InputStream = new FileInputStream(new File(getConfigProp().getProperty("Service_Properties_Location")));
			log.info("Service Properties loaded from file: "+getConfigProp().getProperty("Service_Properties_Location"));
			Service_properties.load(Service_InputStream);
		} catch (IOException e) {
			log.error("Error occured while retreiving a Config Property value");
			e.printStackTrace();
		}
		return Service_properties;
	}

	public String getTimeStamp(){
		log.info("Getting TimeStamp ::");
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		log.info("Time Stamp :: "+formatter.format(new Date()));
		return formatter.format(new Date());	
	}

	public boolean createTxtFile(WMAData wdata){
		log.info("createTxtFile starting...");
		Properties Service_properties=getProp();
		String txt_file_Path;
		WMAData wmData=new WMAData();
		wmData.setTransExeDate(wdata.getTransExeDate());
		List<Bene> benes=wdata.getBenes();
		log.info("List Of Bene Objects Size ::"+benes.size());
		List<Map<String, String>> hashMapList = getBendataMap(wdata);
		FileWriter writer;
		BufferedWriter bwriter;

		try {
			txt_file_Path=Service_properties.getProperty("Text_File_Path")+getTimeStamp()+".txt";
			log.info("AbsolutePath of the updatebenename txt file to be created:  "+txt_file_Path);
			File f=new File(txt_file_Path);
			writer=new FileWriter(f);
			bwriter=new BufferedWriter(writer);
			String key;
			for(Map<String, String> wmaDataMap: hashMapList){
				//HashMap<String,String> wmaDataMap=getBendataMap(wdata);
				Iterator<String> wmaDataKeys= wmaDataMap.keySet().iterator();
				while(wmaDataKeys.hasNext()){
					key=wmaDataKeys.next();	
					bwriter.write(String.format("%20s \t %20s \r\n",key.toUpperCase(),wmaDataMap.get(key)));
					bwriter.newLine();
					bwriter.newLine();
				}
			}
			bwriter.flush();
			bwriter.close();
			this.setText_file_path(f.getAbsolutePath());

		} catch (FileNotFoundException e) {
			log.error("contains file not found exception ::"+e.getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			log.error("contains IO exception ::"+e.getMessage());
			e.printStackTrace();
			return false;
		}
		log.info("cretaeTextFile ending.");
		return true;
	}
	public boolean createXmlFile(IndexedData data,String text_file_path){
		log.info("createXmlFile starting...");
		IndexedData indxData=new IndexedData();
		indxData.setTransType(data.getTransType());
		String xml_file_path;
		Properties prop;
		try{
			prop = getProp();
			Document outdoc=DocumentHelper.createDocument();
			Element awd=outdoc.addElement("AWDRIP");
			Element tr=awd.addElement("Transaction");
			tr.addElement("field").addAttribute("name", UpdateBeneConstants.IDX_POL_NUMBER).addAttribute("value", data.getPolNumber());
			tr.addElement("field").addAttribute("name", UpdateBeneConstants.IDX_FIRST_NAME).addAttribute("value", data.getFirstName());
			tr.addElement("field").addAttribute("name", UpdateBeneConstants.IDX_LAST_NAME).addAttribute("value", data.getLastName());
			tr.addElement("field").addAttribute("name", UpdateBeneConstants.IDX_GOVT_ID).addAttribute("value", data.getGovtID());
			tr.addElement("field").addAttribute("name", UpdateBeneConstants.IDX_TRANS_TYPE).addAttribute("value", data.getTransType());
			tr.addElement("field").addAttribute("name", UpdateBeneConstants.IDX_TRANS_SUB_TYPE).addAttribute("value", data.getTransSubType());
			tr.addElement("field").addAttribute("name", UpdateBeneConstants.IDX_LINE_OF_BUSINESS).addAttribute("value", data.getLineOfBusiness());
			tr.addElement("field").addAttribute("name", UpdateBeneConstants.IDX_PRODUCT_CODE).addAttribute("value", data.getProductCode());
			tr.addElement("field").addAttribute("name",UpdateBeneConstants.UNIT).addAttribute("value",prop.getProperty("UNIT"));
			tr.addElement("field").addAttribute("name",UpdateBeneConstants.WRKT).addAttribute("value",prop.getProperty("WRKT"));
			tr.addElement("field").addAttribute("name",UpdateBeneConstants.STAT).addAttribute("value",prop.getProperty("STAT"));

			Element src=tr.addElement("source");

			src.addElement("field").addAttribute("name",UpdateBeneConstants.UNIT).addAttribute("value",prop.getProperty("UNIT"));
			src.addElement("field").addAttribute("name",UpdateBeneConstants.WRKT).addAttribute("value",prop.getProperty("WRKT"));
			src.addElement("field").addAttribute("name",UpdateBeneConstants.STAT).addAttribute("value",prop.getProperty("STAT"));
			src.addElement("field").addAttribute("name",UpdateBeneConstants.OBJT).addAttribute("value", prop.getProperty("OBJT"));

			src.addElement("path").addAttribute("name",this.getText_file_path());

			xml_file_path=prop.getProperty("AWDRIP_XML_Path")+getTimeStamp()+".xml";
			log.info("AbsolutePath of the updatebenename xml to be created:  "+xml_file_path);
			FileOutputStream o=new FileOutputStream(new File(xml_file_path));
			OutputFormat form=OutputFormat.createPrettyPrint();
			XMLWriter writer=new XMLWriter(o,form);
			writer.write(outdoc);
			writer.flush();
			writer.close();
			o.close();
		} catch (FileNotFoundException e) {
			log.error("contains file not found exception ::"+e.getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			log.error("contains IO exception ::"+e.getMessage());
			e.printStackTrace();
			return false;
		}
		log.info("createXmlFile ending.");
		return true;
	}

	public boolean wmaDataContainsNull(WMAData wmaData){

		String wmaPolNumber = wmaData.getPolNumber();
		String wmaTransType = wmaData.getTransType();
		String wmaTransExeDate = wmaData.getTransExeDate();

		List<Bene> benes=wmaData.getBenes();

		for(Bene bene : benes){

			String firstName=bene.getFirstName();
			String lastName=bene.getLastName();
			String line1=bene.getLine1();
			String city=bene.getCity();
			String birthDate=bene.getBirthDate();
			String areaCode=bene.getAreaCode();
			String addressStateTC=bene.getAddressStateTC();
			String addressTypeCode=bene.getAddressTypeCode();
			String zip=bene.getZip();
			String gender=bene.getGender();
			String govtId=bene.getGOVTID();
			String govtIdTC=bene.getGovtIDTC();
			String dialNumber=bene.getDialNumber();
			String reasonOfChange=bene.getReasonOfChange();


			if (wmaPolNumber == null || wmaPolNumber.isEmpty()
					|| wmaTransType == null || wmaTransType.isEmpty()
					|| wmaTransExeDate == null || wmaTransExeDate.isEmpty()
					|| firstName == null || firstName.isEmpty()
					|| lastName == null || lastName.isEmpty()
					|| line1 == null || line1.isEmpty()
					|| city == null || city.isEmpty()
					|| birthDate == null || birthDate.isEmpty()
					|| addressTypeCode == null || addressTypeCode.isEmpty()
					|| addressStateTC == null || addressStateTC.isEmpty()
					|| areaCode == null || areaCode.isEmpty()
					|| zip == null || zip.isEmpty()
					|| gender == null || gender.isEmpty()
					|| govtId == null || govtId.isEmpty()
					|| govtIdTC == null || govtIdTC.isEmpty()
					|| dialNumber == null || dialNumber.isEmpty()
					|| reasonOfChange == null || reasonOfChange.isEmpty()){

				return true;
			}
		}
		return false;
	}

	public boolean indexedDataContainsNull(IndexedData indexedData){

		String polNumber = indexedData.getPolNumber();
		String firstName = indexedData.getFirstName();
		String lastName = indexedData.getLastName();
		String govtId = indexedData.getGovtID();
		String transType = indexedData.getTransType();
		String transSubType = indexedData.getTransSubType();
		String lineOfBusiness = indexedData.getLineOfBusiness();
		String productCode = indexedData.getProductCode();

		if (polNumber == null || polNumber.isEmpty()
				|| firstName == null || firstName.isEmpty()
				|| lastName == null || lastName.isEmpty()
				|| govtId == null || govtId.isEmpty()
				|| transType == null || transType.isEmpty()
				|| transSubType == null || transSubType.isEmpty()
				|| lineOfBusiness == null || lineOfBusiness.isEmpty()
				|| productCode == null || productCode.isEmpty()){
			return true;
		}
		return false;
	}

	public boolean transDetailsContainsNull(TranDetails tranDetails){

		String transRefGUID = tranDetails.getTransRefGUID();
		if (transRefGUID == null || transRefGUID.isEmpty()){
			return true;
		}
		return false;
	}

	private List<Map<String, String>> getBendataMap(WMAData wdata){
		log.info(" getBendataMap calling for storing data ...");
		List<Map<String, String>> hashMapList = new ArrayList<Map<String,String>>();
		List<Bene> benes=wdata.getBenes();

		log.info("List Of Bene Objects Size ::"+benes.size());

		for(int index=0;index<benes.size();index++){
			Bene bene = benes.get(index);
			log.info("Bene Objects iterating from Bene List :"+bene.toString());
			HashMap<String,String> WMABeneDataMap=new LinkedHashMap<String,String>();
			if(index==0){
				log.info("WMAData Object will execute only once :");
				WMABeneDataMap.put(UpdateBeneConstants.WMA_POL_NUMBER, wdata.getPolNumber());
				WMABeneDataMap.put(UpdateBeneConstants.WMA_TRANS_TYPE, wdata.getTransType());
				WMABeneDataMap.put(UpdateBeneConstants.WMA_TRANS_EXE_DATE, wdata.getTransExeDate());
			}
			WMABeneDataMap.put(UpdateBeneConstants.BEN_FIRST_NAME, bene.getFirstName());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_LAST_NAME, bene.getLastName());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_AREA_CODE, bene.getAreaCode());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_ADDRESS_TYPE_CODE, bene.getAddressTypeCode());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_ADDRESS_STATE_TC, bene.getAddressStateTC());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_BIRTH_DATE, bene.getBirthDate());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_GENDER, bene.getGender());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_CITY, bene.getCity());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_GOVT_ID, bene.getGOVTID());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_GOVT_ID_TC, bene.getGovtIDTC());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_LINE1, bene.getLine1());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_REASON_OF_CHANGE, bene.getReasonOfChange());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_ZIP, bene.getZip());
			WMABeneDataMap.put(UpdateBeneConstants.BEN_DIAL_NUMBER, bene.getDialNumber());

			hashMapList.add(WMABeneDataMap);

		}
		return hashMapList;
	}

	public String getDate(){
		Date date = new Date();  
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");  
		String strDate= formatter.format(date); 
		return strDate;
	}

	public String getText_file_path() {
		return Text_file_path;
	}

	public void setText_file_path(String text_file_path) {
		Text_file_path = text_file_path;
	}
	private String Text_file_path;
}

