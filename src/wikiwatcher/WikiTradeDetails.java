/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikiwatcher;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author dominik
 */
public class WikiTradeDetails {
    
    private String xmlDocument;
    
    private class TradeElement {
        private String executionDate, orderType, tradeName, tradeIsin, executionPrice, weightage, corporateActionType;
    }
    
    private ArrayList<TradeElement> tradeElements = new ArrayList<>(); 
    
    public WikiTradeDetails(String xmlDocument){
        this.xmlDocument = xmlDocument;
        initDetails();
    }
            
    private void initDetails(){
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new ByteArrayInputStream(xmlDocument.getBytes()));
            doc.getDocumentElement().normalize();
            NodeList trades = doc.getElementsByTagName("d2p1:WikifolioTrade");
            for(int i = 0; i < trades.getLength(); i++){
                TradeElement element = new TradeElement();
                NodeList details = trades.item(i).getChildNodes();
                for(int n = 0; n < details.getLength(); n++){
                    if(details.item(n).getNodeName().equals("d2p1:ExecutionDate"))
                        element.executionDate =  details.item(n).getTextContent().strip();
                    if(details.item(n).getNodeName().equals("d2p1:OrderType"))
                        element.orderType =  details.item(n).getTextContent().strip();
                    if(details.item(n).getNodeName().equals("d2p1:Name"))
                        element.tradeName =  details.item(n).getTextContent().strip();
                    if(details.item(n).getNodeName().equals("d2p1:Isin"))
                        element.tradeIsin =  details.item(n).getTextContent().strip();
                    if(details.item(n).getNodeName().equals("d2p1:ExecutionPrice"))
                        element.executionPrice =  details.item(n).getTextContent().strip();
                    if(details.item(n).getNodeName().equals("d2p1:Weightage"))
                        element.weightage =  details.item(n).getTextContent().strip();
                    if(details.item(n).getNodeName().equals("d2p1:CorporateActionType"))
                        element.corporateActionType =  details.item(n).getTextContent().strip();
                }
                tradeElements.add(element);
            }
        }
        catch(Exception ex){
            System.out.println("XML-Parser-Fehler: " + ex.toString());
        }
    }
    
    public String toString(){
        String ret = "";
        for(TradeElement element : tradeElements){
            ret += "New Element:\n" + 
                    "Trade-Name: " + element.tradeName + 
                    "\nTrade-ISIN: " + element.tradeIsin + 
                    "\nOrder-Type: " + element.orderType + 
                    "\nCorporateActionType: " + element.corporateActionType + 
                    "\nExecution-Price: " + element.executionPrice +
                    "\nExecution-Date: " + element.executionDate +
                    "\nWeightage: " + element.weightage +
                    "\n-------------------------------------\n\n";
        }
        return ret; 
    }
}
