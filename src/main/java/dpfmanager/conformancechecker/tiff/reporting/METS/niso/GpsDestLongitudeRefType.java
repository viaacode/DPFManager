//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.08 at 01:08:28 PM CEST 
//


package dpfmanager.conformancechecker.tiff.reporting.METS.niso;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gpsDestLongitudeRefType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gpsDestLongitudeRefType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="E"/>
 *     &lt;enumeration value="W"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gpsDestLongitudeRefType")
@XmlEnum
public enum GpsDestLongitudeRefType {

    E,
    W;

    public String value() {
        return name();
    }

    public static GpsDestLongitudeRefType fromValue(String v) {
        return valueOf(v);
    }
    public static boolean verifyTag(String v) {
        try{
            valueOf(v);
            return true;
        }catch(Exception e){
            return false;
        }
    }

}
