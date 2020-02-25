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
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


/**
 *
 * @author dominik
 */
public class WikiTableModel extends AbstractTableModel implements Serializable {
    
    public List<TableRow> tableData;
    private String columnNames[] = {"Id", "Url", "Int."};
    
    public WikiTableModel(){
        tableData = WikiWatcher.readWiki();
        if(tableData == null){
            tableData = new ArrayList<>();
        }
        else{
            for(TableRow reihe : tableData){
                System.out.println("XML-Daten: \n"+reihe.html+"\n\n\n");
            }
        }
    }
    
    @Override public int getRowCount(){
        return tableData.size();
    }
    
    @Override public int getColumnCount(){
        return 3;
    }
    
    @Override public Object getValueAt(int row, int col){
        String ret = "";
        switch(col){
            case 0:
                ret += row;
                break;
            case 1:
                ret += tableData.get(row).url;
                break;
            case 2:
                ret += tableData.get(row).interval+" Sek.";
                break;
        }
        
        return ret;
    }
    
    @Override public String getColumnName(int column){
        return columnNames[column];
    }
    
    public void addRow(String url, int interval){
        tableData.add(new TableRow(url, interval));
    }
    
    public void removeRow(int row){
        tableData.remove(row);
    }
}
