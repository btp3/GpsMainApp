package models;

import java.util.HashMap;
import java.util.Map;

public class QRModel {

    private String dname;
    private String dcontact;
    private String daddress;
    private String dlicense;
    private String daadhar;
    private String dphoto;
    private String dscan;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDcontact() {
        return dcontact;
    }

    public void setDcontact(String dcontact) {
        this.dcontact = dcontact;
    }

    public String getDaddress() {
        return daddress;
    }

    public void setDaddress(String daddress) {
        this.daddress = daddress;
    }

    public String getDlicense() {
        return dlicense;
    }

    public void setDlicense(String dlicense) {
        this.dlicense = dlicense;
    }

    public String getDaadhar() {
        return daadhar;
    }

    public void setDaadhar(String daadhar) {
        this.daadhar = daadhar;
    }

    public String getDphoto() {
        return dphoto;
    }

    public void setDphoto(String dphoto) {
        this.dphoto = dphoto;
    }

    public String getDscan() {
        return dscan;
    }

    public void setDscan(String dscan) {
        this.dscan = dscan;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

