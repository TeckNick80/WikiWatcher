/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikiwatcher;

import java.util.HashMap;
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
    public static final int LOGIN_OK = 1;
    public static final int LOGIN_FAIL_WRONG = 2;
    public static final int LOGIN_FAIL_SERVERSTATUS = 3;
    
    
    public static int login(String username, String password){
        String useragent = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:65.0) Gecko/20100101 Firefox/65.0";
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
