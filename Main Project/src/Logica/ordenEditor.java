/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Logica;
/**
 *
 * @author juaco_000
 */
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ordenEditor extends DefaultCellEditor implements TableCellRenderer{
    private final JComponent component = new JCheckBox();    
    private boolean value = false; // valor de la celda
    
    /** Constructor de clase */
    public ordenEditor() {
        super( new JCheckBox() );
    }

    /** retorna valor de celda
     * @return  */
    @Override
    public Object getCellEditorValue() {
        return ((JCheckBox)component).isSelected();        
    }

    /** Segun el valor de la celda selecciona/deseleciona el JCheckBox
     * @param table
     * @param value
     * @param isSelected
     * @param row
     * @param column
     * @return  */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Color de fondo en modo edicion
        ( (JCheckBox) component).setBackground( new Color(200,200,0) );
        //obtiene valor de celda y coloca en el JCheckBox
        boolean b = ((Boolean) value).booleanValue();
        ( (JCheckBox) component).setSelected( b );
        return ( (JCheckBox) component);     
    }

    /** cuando termina la manipulacion de la celda
     * @return  */
    @Override
    public boolean stopCellEditing() {        
        value = ((Boolean)getCellEditorValue()).booleanValue() ;
        ((JCheckBox)component).setSelected( value );
        return super.stopCellEditing();
    }

    /** retorna componente
     * @return  */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         if (value == null)
            return null;         
         return ( (JCheckBox) component );
    }
}

