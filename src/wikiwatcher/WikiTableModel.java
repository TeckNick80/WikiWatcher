/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikiwatcher;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


/**
 *
 * @author dominik
 */
public class WikiTableModel extends AbstractTableModel {

    public class TableRow{
        protected int id, interval;
        protected String url;
        public TableRow(String url, int interval){
            this.url = url;
            this.interval = interval;
        }
    }
    
    public List<TableRow> tableData = new ArrayList<>();
    private String columnNames[] = {"Id", "Url", "Int."};
    
    public WikiTableModel(){
        for(int i = 0; i < 10; i++){
            tableData.add(new TableRow("Test "+i, i*10));
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
