package com.dxc.updatebene.service;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.soap.SOAPException;
import com.dxc.updatebene.request.TranDetails;
import com.dxc.updatebene.request.TxUpdateBene;

/*
 * developed by smedisetti4
 * 
 */

@WebService(serviceName="UpdateBeneService",portName="UpdateBenePort")
public interface UpdateBene {
	@WebMethod(operationName="UpdateBene")
	public TranDetails updateBeneCreate(@WebParam(name="TxUpdateBene") TxUpdateBene txUpdateBene) ;

}
