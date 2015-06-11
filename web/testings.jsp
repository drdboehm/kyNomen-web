<%-- 
    Document   : testings
    Created on : 11.06.2015, 07:52:15
    Author     : dboehm
--%>
<%@ taglib uri="/WEB-INF/my_custom_tags.tld" prefix="custom" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>


    <f:view>
        <body>
            <h1>Hello World!</h1>
            <h:form id="halterSelect">
                <h:dataTable style="width: 900px;" value="#{halterController.halterList}" var="halter" >
                    <f:facet name="header">Liste der Halter</f:facet> 
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Select It"/>
                        </f:facet>
                        <custom:radioButton id="myRadioId1" itemValue="#{halter.halterId}" value="" name="myRadioCol" overrideName="true"/>
                    </h:column>

                    <h:column> 
                        <f:facet name="header">
                            <%--<h:outputText value="#{msgs.halter_id} -"/>--%> 
                            <h:commandButton id="halterIdSort" value="Sort" action="#{halterController.sortHalterById()}"/>
                        </f:facet>
                        <h:outputText value="#{halter.halterId}"></h:outputText>
                    </h:column>
                </h:dataTable>

            </h:form>
        </body>
    </f:view>
</html>
