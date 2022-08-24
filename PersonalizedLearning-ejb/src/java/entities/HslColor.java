package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author haogs
 */
@Entity
@Table(name = "HSL_COLOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HslColor.findAll", query = "SELECT h FROM HslColor h")
    , @NamedQuery(name = "HslColor.findById", query = "SELECT h FROM HslColor h WHERE h.id = :id")
    , @NamedQuery(name = "HslColor.findByHue", query = "SELECT h FROM HslColor h WHERE h.hue = :hue")
    , @NamedQuery(name = "HslColor.findBySaturation", query = "SELECT h FROM HslColor h WHERE h.saturation = :saturation")
    , @NamedQuery(name = "HslColor.findByLightness", query = "SELECT h FROM HslColor h WHERE h.lightness = :lightness")
    , @NamedQuery(name = "HslColor.findByRate", query = "SELECT h FROM HslColor h WHERE h.rate = :rate")
    , @NamedQuery(name = "HslColor.findByPageId", query = "SELECT h FROM HslColor h WHERE h.pageId = :pageId")
    , @NamedQuery(name = "HslColor.findByType", query = "SELECT h FROM HslColor h WHERE h.type = :type")})
public class HslColor implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "HUE")
    private Integer hue;
    @Column(name = "SATURATION")
    private Integer saturation;
    @Column(name = "LIGHTNESS")
    private Integer lightness;
    @Column(name = "RATE")
    private Integer rate;
    @Column(name = "PAGE_ID")
    private Integer pageId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TYPE")
    private short type;
    
    public HslColor() {
    }
    
    public HslColor(Integer id) {
        this.id = id;
    }
    
    public HslColor(Integer id, short type) {
        this.id = id;
        this.type = type;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getHue() {
        return hue;
    }
    
    public void setHue(Integer hue) {
        this.hue = hue;
    }
    
    public Integer getSaturation() {
        return saturation;
    }
    
    public void setSaturation(Integer saturation) {
        this.saturation = saturation;
    }
    
    public Integer getLightness() {
        return lightness;
    }
    
    public void setLightness(Integer lightness) {
        this.lightness = lightness;
    }
    
    public Integer getRate() {
        return rate;
    }
    
    public void setRate(Integer rate) {
        this.rate = rate;
    }
    
    public Integer getPageId() {
        return pageId;
    }
    
    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }
    
    public short getType() {
        return type;
    }
    
    public void setType(short type) {
        this.type = type;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HslColor)) {
            return false;
        }
        HslColor other = (HslColor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    public String getValue(){
        return "{\"hue\":"+this.getHue()
                +",\"lightness\":"+getLightness()
                +",\"saturation\":"+getSaturation()+"}";
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{hue:").append(getHue())
                .append(",id:").append(getId())
                .append(",lightness:").append(getLightness())
                .append(",pageId:").append(getPageId())
                .append(",rate:").append(getRate())
                .append(",saturation:").append(getSaturation())
                .append(",type:").append(getType()+"}");
        return sb.toString();
    }
    
}
