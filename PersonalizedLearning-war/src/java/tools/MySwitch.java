/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

/**
 *
 * @author hgs
 */
public class MySwitch implements java.io.Serializable {

    private boolean status = false;

    public MySwitch(boolean value) {
        status = value;
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
        return status;
    }

    public void turnOn() {
        status = true;
    }

    public void turnOff() {
        status = false;
    }
}
