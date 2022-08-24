/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Administrator
 */
public class DateTimeConvertor implements Converter<Calendar>{

    @Override
    public Calendar getAsObject(FacesContext fc, UIComponent uic, String string) {
        Date date=null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        try { 
            date = (Date)formatter.parse(string);
        } catch (ParseException ex) {
            Logger.getLogger(DateTimeConvertor.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Calendar t) {
       return t.get(Calendar.YEAR)+"-"
               +t.get(Calendar.MONTH)+"-"
               +t.get(Calendar.DAY_OF_MONTH)+"T"
               +t.get(Calendar.HOUR_OF_DAY)+"-"
               +t.get(Calendar.MINUTE);
    }
    
}
