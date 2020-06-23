<?xml version="1.0" encoding="utf-8"?>
<wsdl:definitions xmlns:s="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" xmlns:tns="http://tempuri.org/" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:tm="http://microsoft.com/wsdl/mime/textMatching/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" targetNamespace="http://tempuri.org/" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">
  <wsdl:types>
    <s:schema elementFormDefault="qualified" targetNamespace="http://tempuri.org/">
      <s:element name="GetAuthToken">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="userName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="passWord" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="isRememberPassword" type="s:boolean" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetAuthTokenResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetAuthTokenResult" type="tns:SsoAuthResponse" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="SsoAuthResponse">
        <s:sequence>
          <s:element minOccurs="1" maxOccurs="1" name="ResultAuth" type="s:boolean" />
          <s:element minOccurs="0" maxOccurs="1" name="DescriptionError" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="Token" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="CheckAuthToken">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="token" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CheckAuthTokenResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CheckAuthTokenResult" type="tns:SsoCheckResponse" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="SsoCheckResponse">
        <s:sequence>
          <s:element minOccurs="1" maxOccurs="1" name="ResultCheck" type="s:boolean" />
          <s:element minOccurs="0" maxOccurs="1" name="UserName" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="DescriptionError" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="TokenNew" type="s:string" />
        </s:sequence>
      </s:complexType>
    </s:schema>
  </wsdl:types>
  <wsdl:message name="GetAuthTokenSoapIn">
    <wsdl:part name="parameters" element="tns:GetAuthToken" />
  </wsdl:message>
  <wsdl:message name="GetAuthTokenSoapOut">
    <wsdl:part name="parameters" element="tns:GetAuthTokenResponse" />
  </wsdl:message>
  <wsdl:message name="CheckAuthTokenSoapIn">
    <wsdl:part name="parameters" element="tns:CheckAuthToken" />
  </wsdl:message>
  <wsdl:message name="CheckAuthTokenSoapOut">
    <wsdl:part name="parameters" element="tns:CheckAuthTokenResponse" />
  </wsdl:message>
  <wsdl:portType name="WebServiceSoap">
    <wsdl:operation name="GetAuthToken">
      <wsdl:input message="tns:GetAuthTokenSoapIn" />
      <wsdl:output message="tns:GetAuthTokenSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CheckAuthToken">
      <wsdl:input message="tns:CheckAuthTokenSoapIn" />
      <wsdl:output message="tns:CheckAuthTokenSoapOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:binding name="WebServiceSoap" type="tns:WebServiceSoap">
    <soap:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="GetAuthToken">
      <soap:operation soapAction="http://tempuri.org/GetAuthToken" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CheckAuthToken">
      <soap:operation soapAction="http://tempuri.org/CheckAuthToken" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="WebServiceSoap12" type="tns:WebServiceSoap">
    <soap12:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="GetAuthToken">
      <soap12:operation soapAction="http://tempuri.org/GetAuthToken" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CheckAuthToken">
      <soap12:operation soapAction="http://tempuri.org/CheckAuthToken" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:service name="WebService">
    <wsdl:port name="WebServiceSoap" binding="tns:WebServiceSoap">
      <soap:address location="https://sso.bkav.com/Webservice.asmx" />
    </wsdl:port>
    <wsdl:port name="WebServiceSoap12" binding="tns:WebServiceSoap12">
      <soap12:address location="https://sso.bkav.com/Webservice.asmx" />
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>