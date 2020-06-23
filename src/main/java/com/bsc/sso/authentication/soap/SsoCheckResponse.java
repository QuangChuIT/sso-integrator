/**
 * SsoCheckResponse.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bsc.sso.authentication.soap;

public class SsoCheckResponse implements java.io.Serializable {
    private boolean resultCheck;

    private String userName;

    private String descriptionError;

    private String tokenNew;

    public SsoCheckResponse() {
    }

    public SsoCheckResponse(
            boolean resultCheck,
            String userName,
            String descriptionError,
            String tokenNew) {
        this.resultCheck = resultCheck;
        this.userName = userName;
        this.descriptionError = descriptionError;
        this.tokenNew = tokenNew;
    }


    /**
     * Gets the resultCheck value for this SsoCheckResponse.
     *
     * @return resultCheck
     */
    public boolean isResultCheck() {
        return resultCheck;
    }


    /**
     * Sets the resultCheck value for this SsoCheckResponse.
     *
     * @param resultCheck
     */
    public void setResultCheck(boolean resultCheck) {
        this.resultCheck = resultCheck;
    }


    /**
     * Gets the userName value for this SsoCheckResponse.
     *
     * @return userName
     */
    public String getUserName() {
        return userName;
    }


    /**
     * Sets the userName value for this SsoCheckResponse.
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    /**
     * Gets the descriptionError value for this SsoCheckResponse.
     *
     * @return descriptionError
     */
    public String getDescriptionError() {
        return descriptionError;
    }


    /**
     * Sets the descriptionError value for this SsoCheckResponse.
     *
     * @param descriptionError
     */
    public void setDescriptionError(String descriptionError) {
        this.descriptionError = descriptionError;
    }


    /**
     * Gets the tokenNew value for this SsoCheckResponse.
     *
     * @return tokenNew
     */
    public String getTokenNew() {
        return tokenNew;
    }


    /**
     * Sets the tokenNew value for this SsoCheckResponse.
     *
     * @param tokenNew
     */
    public void setTokenNew(String tokenNew) {
        this.tokenNew = tokenNew;
    }

    private Object __equalsCalc = null;

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof SsoCheckResponse)) return false;
        SsoCheckResponse other = (SsoCheckResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
                this.resultCheck == other.isResultCheck() &&
                ((this.userName == null && other.getUserName() == null) ||
                        (this.userName != null &&
                                this.userName.equals(other.getUserName()))) &&
                ((this.descriptionError == null && other.getDescriptionError() == null) ||
                        (this.descriptionError != null &&
                                this.descriptionError.equals(other.getDescriptionError()))) &&
                ((this.tokenNew == null && other.getTokenNew() == null) ||
                        (this.tokenNew != null &&
                                this.tokenNew.equals(other.getTokenNew())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += (isResultCheck() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getUserName() != null) {
            _hashCode += getUserName().hashCode();
        }
        if (getDescriptionError() != null) {
            _hashCode += getDescriptionError().hashCode();
        }
        if (getTokenNew() != null) {
            _hashCode += getTokenNew().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
            new org.apache.axis.description.TypeDesc(SsoCheckResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "SsoCheckResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultCheck");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ResultCheck"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "UserName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descriptionError");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "DescriptionError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tokenNew");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "TokenNew"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
            String mechType,
            Class _javaType,
            javax.xml.namespace.QName _xmlType) {
        return
                new org.apache.axis.encoding.ser.BeanSerializer(
                        _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
            String mechType,
            Class _javaType,
            javax.xml.namespace.QName _xmlType) {
        return
                new org.apache.axis.encoding.ser.BeanDeserializer(
                        _javaType, _xmlType, typeDesc);
    }

}
