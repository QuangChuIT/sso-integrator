package com.bsc.sso.authentication.xml;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

public class XmlUtil {
    public static String getExtractUserFromCas(String casResponse) {
        String username = "";
        try {
            //Parse XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(casResponse)));

            //Get XPath expression
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();
            xpath.setNamespaceContext(new NamespaceResolver(doc));
            XPathExpression expr = xpath.compile("//cas:serviceResponse//cas:authenticationSuccess/cas:user/text()");
            //Search XPath expression
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            //Iterate over results and fetch book names
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                username = nodes.item(i).getNodeValue();
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return username;
    }

    public static void main(String[] args) {
        System.out.println(getExtractUserFromCas("<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>\n" +
                "    <cas:authenticationSuccess>\n" +
                "        <cas:user>admin_ioc</cas:user>\n" +
                "        </cas:authenticationSuccess>\n" +
                "</cas:serviceResponse>"));
    }

    private static final Logger LOGGER = Logger.getLogger(XmlUtil.class);
}
