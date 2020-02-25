/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikiwatcher;

import java.io.Serializable;
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
public class TableRow implements Serializable{
    protected int interval;
    protected String url;
    protected String html;

    public TableRow(String url, int interval){
        super();
        this.url = url;
        this.interval = interval;
        readWebsiteData();
        
    }
    
    private void readWebsiteData(){
        try{
            Connection.Response res = Jsoup.connect(url).cookies(WikiWatcher.cookies).userAgent(WikiWatcher.useragent).method(Method.GET).execute();
            Document doc = res.parse();
            Elements elements = doc.getElementsByTag("script");
            String wikifolioId = "";
            String siteData = "";
            for(Element e : elements){
                siteData += e.data()+"\n";
            }
            String siteDataArray[] = siteData.split("\n");
            for(String line : siteDataArray){
                if(line.trim().startsWith("wikifolioId:")){
                    wikifolioId = line.trim().replace("wikifolioId: \"", "").replace("\",", "");
                }
            }
            String detailUrl = "https://www.wikifolio.com/api/wikifolio/"+wikifolioId+"/tradehistory?page=0&pageSize=10&country=de&language=de&_="+System.currentTimeMillis();
            System.out.println("Neue Url: " + detailUrl);
            Connection.Response res2 = Jsoup.connect(detailUrl).cookies(WikiWatcher.cookies).userAgent(WikiWatcher.useragent).method(Method.GET).execute();
            Document doc2 = res2.parse();
            html = doc2.html();
        }catch(Exception ex){
            System.out.println("Kann Dokument nicht parsen... : " + ex.toString());
        }
    }


    @Override
    public String toString(){
        return "TableRow [url=" + url + ", interval=" + interval + ", html=" + html + "]";
    }

}
