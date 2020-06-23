/**
 * WebService.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bsc.sso.authentication.soap;

public interface WebService extends javax.xml.rpc.Service {
    public String getWebServiceSoapAddress();

    public WebServiceSoap_PortType getWebServiceSoap() throws javax.xml.rpc.ServiceException;

    public WebServiceSoap_PortType getWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;

    public String getWebServiceSoap12Address();

    public WebServiceSoap_PortType getWebServiceSoap12() throws javax.xml.rpc.ServiceException;

    public WebServiceSoap_PortType getWebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
