/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packager;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author Admin
 */
public class VideoPackage implements Serializable{
    String ip, status;
    ImageIcon frame;

    public VideoPackage(String ip, String status, ImageIcon frame) {
        this.ip = ip;
        this.status = status;
        this.frame = frame;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ImageIcon getFrame() {
        return frame;
    }

    public void setFrame(ImageIcon frame) {
        this.frame = frame;
    }
    
}
