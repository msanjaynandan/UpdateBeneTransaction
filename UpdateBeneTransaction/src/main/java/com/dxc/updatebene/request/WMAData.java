//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.08.10 at 05:00:37 PM IST 
//


package com.dxc.updatebene.request;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WMAData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WMAData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PolNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TransType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TransExeDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Benes" type="{http://www.dxc.com/request/}Bene" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WMAData", propOrder = {
    "polNumber",
    "transType",
    "transExeDate",
    "benes"
})
public class WMAData {

    @XmlElement(name = "PolNumber", required = true)
    protected String polNumber;
    @XmlElement(name = "TransType", required = true)
    protected String transType;
    @XmlElement(name = "TransExeDate", required = true)
    protected String transExeDate;
    @XmlElement(name = "Benes", required = true)
    protected List<Bene> benes;

    /**
     * Gets the value of the polNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolNumber() {
        return polNumber;
    }

    /**
     * Sets the value of the polNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolNumber(String value) {
        this.polNumber = value;
    }

    /**
     * Gets the value of the transType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransType() {
        return transType;
    }

    /**
     * Sets the value of the transType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransType(String value) {
        this.transType = value;
    }

    /**
     * Gets the value of the transExeDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransExeDate() {
        return transExeDate;
    }

    /**
     * Sets the value of the transExeDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransExeDate(String value) {
        this.transExeDate = value;
    }

    /**
     * Gets the value of the benes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the benes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBenes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Bene }
     * 
     * 
     */
    public List<Bene> getBenes() {
        if (benes == null) {
            benes = new ArrayList<Bene>();
        }
        return this.benes;
    }

}
