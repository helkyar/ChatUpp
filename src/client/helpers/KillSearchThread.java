/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.helpers;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Esta clase se encarga de recibir la dirección de un hilo y, después de un 
 * tiempo determinado, finalizarlo. En esta aplicación su finalidad es parar
 * los multiples hilos que se generan al buscar el servidor del chat.
 *
 * @author Javier Palacios Botejara
 */
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