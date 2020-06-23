/**
 * WebServiceSoap_PortType.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bsc.sso.authentication.soap;

public interface WebServiceSoap_PortType extends java.rmi.Remote {
    public SsoAuthResponse getAuthToken(String userName, String passWord, boolean isRememberPassword) throws java.rmi.RemoteException;

    public SsoCheckResponse checkAuthToken(String token) throws java.rmi.RemoteException;
}
