/**
 * SsoAuthResponse.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bsc.sso.authentication.soap;

public class SsoAuthResponse implements java.io.Serializable {
    private boolean resultAuth;

    private String descriptionError;

    private String token;

    public SsoAuthResponse() {
    }

    public SsoAuthResponse(
            boolean resultAuth,
            String descriptionError,
            String token) {
        this.resultAuth = resultAuth;
        this.descriptionError = descriptionError;
        this.token = token;
    }


    /**
     * Gets the resultAuth value for this SsoAuthResponse.
     *
     * @return resultAuth
     */
    public boolean isResultAuth() {
        return resultAuth;
    }


    /**
     * Sets the resultAuth value for this SsoAuthResponse.
     *
     * @param resultAuth
     */
    public void setResultAuth(boolean resultAuth) {
        this.resultAuth = resultAuth;
    }


    /**
     * Gets the descriptionError value for this SsoAuthResponse.
     *
     * @return descriptionError
     */
    public String getDescriptionError() {
        return descriptionError;
    }


    /**
     * Sets the descriptionError value for this SsoAuthResponse.
     *
     * @param descriptionError
     */
    public void setDescriptionError(String descriptionError) {
        this.descriptionError = descriptionError;
    }


    /**
     * Gets the token value for this SsoAuthResponse.
     *
     * @return token
     */
    public String getToken() {
        return token;
    }


    /**
     * Sets the token value for this SsoAuthResponse.
     *
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    private Object __equalsCalc = null;

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof SsoAuthResponse)) return false;
        SsoAuthResponse other = (SsoAuthResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
                this.resultAuth == other.isResultAuth() &&
                ((this.descriptionError == null && other.getDescriptionError() == null) ||
                        (this.descriptionError != null &&
                                this.descriptionError.equals(other.getDescriptionError()))) &&
                ((this.token == null && other.getToken() == null) ||
                        (this.token != null &&
                                this.token.equals(other.getToken())));
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
        _hashCode += (isResultAuth() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDescriptionError() != null) {
            _hashCode += getDescriptionError().hashCode();
        }
        if (getToken() != null) {
            _hashCode += getToken().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
            new org.apache.axis.description.TypeDesc(SsoAuthResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "SsoAuthResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultAuth");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ResultAuth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("token");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Token"));
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
