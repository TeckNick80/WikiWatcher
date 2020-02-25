/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikiwatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.UIManager;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author dominik
 */
public class WikiWatcher {
    
    public static Map<String, String> cookies;
    public static String useragent = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:65.0) Gecko/20100101 Firefox/65.0";
    public static final int LOGIN_OK = 1;
    public static final int LOGIN_FAIL_WRONG = 2;
    public static final int LOGIN_FAIL_SERVERSTATUS = 3;
    
    
    public static int login(String username, String password){
        String wikiurl = "https://www.wikifolio.com/";
        String wikilogin = "dynamic/de/de/login/login?ReturnUrl=/de/de/home&_=";
        String wikistamp = String.valueOf(System.currentTimeMillis());
        String token = "";
        String ufprt = "";
        String keepLoggedIn = "true";
        
        try{
            //Get hidden field values for login
            Connection.Response res = Jsoup.connect(wikiurl + wikilogin + wikistamp).method(Method.GET).execute();
            cookies = res.cookies();
            Document doc = res.parse();
            Elements hiddenfields = doc.getElementsByAttributeValue("type", "hidden");
            for(Element e : hiddenfields){
                if(e.attr("name").contains("Token")){
                    token = e.attr("value");
                }
                else if(e.attr("name").contains("ufprt")){
                    ufprt = e.attr("value");
                }
            }
            
            //Prepare Post-Data for login
            Map<String, String> postData = new HashMap<>();
            postData.put("__RequestVerificationToken", token);
            postData.put("KeepLoggedIn", keepLoggedIn);
            postData.put("ufprt", ufprt);
            postData.put("Username", username);
            postData.put("Password", password);
            
            //Excecute Post Request
            Connection.Response res2 = Jsoup.connect(wikiurl + wikilogin + wikistamp).ignoreContentType(true).userAgent(useragent).cookies(res.cookies()).data(postData).method(Method.POST).execute();
            
            //Check Login Result
            Document doc2 = res2.parse();
            if(doc2.body().text().equals("/de/de/home")){
                //Add new Cookies to Cookie-Map
                for(Map.Entry<String, String> cookie : res2.cookies().entrySet()){
                    cookies.put(cookie.getKey(), cookie.getValue());
                }
                return LOGIN_OK;
            }
            else{
                System.out.println("Falsche Credentials");
                return LOGIN_FAIL_WRONG;
            }
        }
        catch(Exception ex){
            System.out.println("Server-Fehler: " + ex.toString());
            return LOGIN_FAIL_SERVERSTATUS;
        }
    }
    
    public static void saveWiki(List<TableRow> data){
        try{
                FileOutputStream oS = new FileOutputStream("wikidata.dat");
                ObjectOutputStream oOS = new ObjectOutputStream(oS);
                oOS.writeObject(data);
                oOS.flush();
                oOS.close();
                oS.close();
            }catch(Exception ex){
                System.out.println("Fehler beim Schreiben der Wiki-Daten: " + ex.toString());
            }
    }
    
    public static List<TableRow> readWiki(){
        List<TableRow> readWiki = null;
        try{
            //trying to read saved Wikis
            File wikiFile = new File("wikidata.dat");
            if(wikiFile.exists()){
                InputStream iS = new FileInputStream(wikiFile);
                ObjectInputStream oIS = new ObjectInputStream(iS);
                readWiki = (List<TableRow>)oIS.readObject();
                oIS.close();
                iS.close();
            }
        }catch(Exception ex){
            System.out.println("Can't read WikiFile: " + ex.toString());
        }
        return readWiki;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception ex){
            System.out.println("Can't set Look and Feel: " + ex.toString());
        }
        new MainWindow().setVisible(true);
    }
    
}
