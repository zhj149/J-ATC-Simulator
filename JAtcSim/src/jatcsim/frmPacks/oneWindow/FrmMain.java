/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jatcsim.frmPacks.oneWindow;

import jatcsimdraw.painting.EJComponent;
import jatcsimdraw.painting.EJComponentCanvas;
import jatcsimdraw.painting.Radar;
import jatcsimdraw.painting.Settings;
import jatcsimdraw.shared.es.WithCoordinateEvent;
import jatcsimlib.Simulation;
import jatcsimlib.events.EventListener;
import jatcsimlib.world.Airport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author Marek
 */
public class FrmMain extends javax.swing.JFrame {

  /**
   * Creates new form FrmMain
   */
  public FrmMain() {
    initComponents();
  }
  
  /**
   * This method is called from within the constructor to
   * initialize the form. WARNING: Do NOT modify this code.
   * The content of this method is always regenerated by the
   * Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jTxtInput = new javax.swing.JTextField();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jTxtInput, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGap(0, 280, Short.MAX_VALUE)
        .addComponent(jTxtInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField jTxtInput;
  // End of variables declaration//GEN-END:variables

  void init(final Simulation sim, Settings displaySettings) {
    
    Airport aip = sim.getAirport();
    
    EJComponentCanvas canvas = new EJComponentCanvas();
    Radar r = new Radar(canvas, aip.getRadarRange(), sim, displaySettings);
    final EJComponent comp = canvas.getEJComponent();

    final FrmMain f = this;
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //f.setSize(900, 500);
    // anchor of radar:
    f.addComponentListener(new ComponentListener() {

      @Override
      public void componentResized(ComponentEvent e) {
        comp.setSize(
            f.getWidth(), 
            f.getHeight() - jTxtInput.getHeight());
      }

      @Override
      public void componentMoved(ComponentEvent e) {
      }

      @Override
      public void componentShown(ComponentEvent e) {
      }

      @Override
      public void componentHidden(ComponentEvent e) {
      }
    });
    f.add(comp);
    f.setVisible(true);

    int delay = 3000; //milliseconds
    ActionListener taskPerformer = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        sim.elapseSecond();
        comp.repaint();
      }
    };

    // mouse coord on title
    r.onMouseMove().addListener(new EventListener<Radar, WithCoordinateEvent>() {

      @Override
      public void raise(Radar parent, WithCoordinateEvent e) {
        f.setTitle(e.coordinate.toString());
      }
    });

    new Timer(delay, taskPerformer).start();
    
  }
}
