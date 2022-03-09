/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.helpers;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Javier Palacios Botejara, Houssam Amrouch, Mateo Crespi, Cristian Echauri, Mildred Rámirez
 */

//Esta classe finaliza los subprocesos de busqueda
public class KillSearchThread extends TimerTask {
    private Thread t;
    private Timer timer;

    public KillSearchThread(Thread t, Timer timer){
        this.t = t;
        this.timer = timer;
    }

    public void run() {
        if (t != null && t.isAlive()) {
            t.interrupt();
            timer.cancel();
        }
    }
}