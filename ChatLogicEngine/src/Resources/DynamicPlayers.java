//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.04.01 at 10:13:37 AM IDT 
//


package Resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="total-players" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}byte">
 *             &lt;minInclusive value="2"/>
 *             &lt;maxInclusive value="6"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="game-title" use="required">
 *         &lt;simpleType>
 *           &lt;list itemType="{http://www.w3.org/2001/XMLSchema}string" />
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "DynamicPlayers")
public class DynamicPlayers implements Serializable {

    @XmlAttribute(name = "total-players", required = true)
    protected byte totalPlayers;
    @XmlAttribute(name = "game-title", required = true)
    protected List<String> gameTitle;

    /**
     * Gets the value of the totalPlayers property.
     * 
     */
    public byte getTotalPlayers() {
        return totalPlayers;
    }

    /**
     * Sets the value of the totalPlayers property.
     * 
     */
    public void setTotalPlayers(byte value) {
        this.totalPlayers = value;
    }

    /**
     * Gets the value of the gameTitle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gameTitle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGameTitle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getGameTitle() {
        if (gameTitle == null) {
            gameTitle = new ArrayList<String>();
        }
        return this.gameTitle;
    }

}
