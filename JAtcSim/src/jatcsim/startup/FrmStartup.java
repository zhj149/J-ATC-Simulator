/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jatcsim.startup;

import eng.eIni.IniFile;
import eng.eSystem.Exceptions;
import eng.eSystem.dateTime.DateTime;
import jatcsimlib.airplanes.AirplaneTypes;
import jatcsimlib.world.Airport;
import jatcsimlib.world.Area;
import jatcsimxml.serialization.Serializer;
import java.awt.Component;
import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Marek
 */
public class FrmStartup extends javax.swing.JFrame {

  public FrmStartup() {
    initComponents();
    eComponentInit();
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel1 = new javax.swing.JLabel();
    txtAreaXml = new javax.swing.JTextField();
    btnAreaXml = new javax.swing.JButton();
    jLabel2 = new javax.swing.JLabel();
    txtTypesXml = new javax.swing.JTextField();
    btnTypesXml = new javax.swing.JButton();
    jLabel4 = new javax.swing.JLabel();
    cmbAirports = new javax.swing.JComboBox();
    jLabel5 = new javax.swing.JLabel();
    txtTime = new javax.swing.JTextField();
    jLabel6 = new javax.swing.JLabel();
    txtMetar = new javax.swing.JTextField();
    rdbWeatherFromWeb = new javax.swing.JRadioButton();
    rdbWeatherFromUser = new javax.swing.JRadioButton();
    jLabel7 = new javax.swing.JLabel();
    btnRadars = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    txtError = new javax.swing.JTextArea();

    setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N

    jLabel1.setText("Area XML file:");

    txtAreaXml.setText("--");

    btnAreaXml.setText("(browse)");

    jLabel2.setText("Plane types XML file:");

    txtTypesXml.setText("--");

    btnTypesXml.setText("(browse)");

    jLabel4.setText("Select airport:");

    cmbAirports.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

    jLabel5.setText("Simulation start time:");

    txtTime.setText("8:57");
    txtTime.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtTimeKeyReleased(evt);
      }
      public void keyTyped(java.awt.event.KeyEvent evt) {
        txtTimeKeyTyped(evt);
      }
    });

    jLabel6.setText("Weather:");

    txtMetar.setText("METAR ZZZZ 111111Z 20212KTS 9000 OVC012");

    rdbWeatherFromWeb.setSelected(true);
    rdbWeatherFromWeb.setText("download from web");

    rdbWeatherFromUser.setText("user set - insert METAR string:");

    jLabel7.setText("Radars:");

    btnRadars.setText("(select radars)");

    txtError.setColumns(20);
    txtError.setForeground(new java.awt.Color(255, 0, 0));
    txtError.setLineWrap(true);
    txtError.setRows(5);
    jScrollPane1.setViewportView(txtError);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel1)
            .addGap(18, 18, 18)
            .addComponent(txtAreaXml)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnAreaXml))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel2)
            .addGap(18, 18, 18)
            .addComponent(txtTypesXml, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnTypesXml))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel6)
              .addComponent(jLabel7))
            .addGap(73, 73, 73)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(rdbWeatherFromWeb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(txtMetar)
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(rdbWeatherFromUser)
                    .addGap(0, 0, Short.MAX_VALUE))))
              .addGroup(layout.createSequentialGroup()
                .addComponent(btnRadars)
                .addGap(0, 0, Short.MAX_VALUE))))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel4)
              .addComponent(jLabel5))
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(cmbAirports, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 0, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(txtAreaXml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnAreaXml))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(txtTypesXml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnTypesXml))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel4)
          .addComponent(cmbAirports, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel5)
          .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel6)
          .addComponent(rdbWeatherFromWeb)
          .addComponent(rdbWeatherFromUser))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtMetar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel7)
          .addComponent(btnRadars))
        .addGap(46, 46, 46))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void txtTimeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimeKeyTyped

  }//GEN-LAST:event_txtTimeKeyTyped

  private void txtTimeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimeKeyReleased
    String s = txtTime.getText();
    Pattern p = Pattern.compile("^(\\d{1,2}):(\\d{2})$");
    Matcher m = p.matcher(s);
    if (m.find() == false) {
      txtTime.setBackground(bad);
    } else {
      txtTime.setBackground(good);
    }
  }//GEN-LAST:event_txtTimeKeyReleased

  private static java.awt.Color bad = new java.awt.Color(255, 200, 200);
  private static java.awt.Color good = java.awt.Color.WHITE;

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnAreaXml;
  private javax.swing.JButton btnRadars;
  private javax.swing.JButton btnTypesXml;
  private javax.swing.JComboBox cmbAirports;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JRadioButton rdbWeatherFromUser;
  private javax.swing.JRadioButton rdbWeatherFromWeb;
  private javax.swing.JTextField txtAreaXml;
  private javax.swing.JTextArea txtError;
  private javax.swing.JTextField txtMetar;
  private javax.swing.JTextField txtTime;
  private javax.swing.JTextField txtTypesXml;
  // End of variables declaration//GEN-END:variables

  XmlFileSelector fsAreaFile;
  XmlFileSelector fsTypesFile;

  private void eComponentInit() {
    fsAreaFile = new XmlFileSelector(txtAreaXml, btnAreaXml, new FsAreaFileChangedHandler());
    fsTypesFile = new XmlFileSelector(txtTypesXml, btnTypesXml, new FsTypesFileChangedHandler());

    setFontAll(this.getComponents());
  }

  private static final Font f = new Font("Verdana", 0, 12);

  private void setFontAll(Component[] components) {

    for (Component c : components) {
      c.setFont(f);
      if (c instanceof java.awt.Container) {
        setFontAll(((java.awt.Container) c).getComponents());
      }
    }
  }

  public boolean isDataValid() {
    return false;
  }

  public void eInit() {
    String iniFileName =  jatcsim.JAtcSim.resFolder.toString() + "\\settings\\config.ini";

    IniFile inf = IniFile.tryLoad(iniFileName);

    fsAreaFile.setFile(
      inf.getValue("Xml", "areaXmlFile"));
    fsTypesFile.setFile(
      inf.getValue("Xml", "planesXmlFile"));

    String isDownS = inf.getValue("Weather", "isDownloaded");
    boolean isDown = eng.eSystem.Parsers.toBool(isDownS);
    rdbWeatherFromWeb.setSelected(isDown);
    rdbWeatherFromUser.setSelected(!isDown);

    txtMetar.setText(
      inf.getValue("Weather", "userMetar"));

    txtTime.setText(
      DateTime.getNow().toString("HH:mm"));
  }

  class FsAreaFileChangedHandler extends XmlFileSelectorFileChangedHandler {

    @Override
    public void fileChanged(String newFileName) {
      Area area = Area.create();
      Serializer ser = new Serializer();
      try {
        ser.fillObject(
          newFileName,
          area);
                        
        txtAreaXml.setForeground(java.awt.Color.BLACK);
        
        String[] data = new String[ area.getAirports().size() ]; 
        for (int i = 0; i < area.getAirports().size(); i++) {
          Airport aip = area.getAirports().get(i);
          data[i] = aip.getIcao() + " - " + aip.getName();
        }
        ComboBoxModel<String> model = new DefaultComboBoxModel<>(data);        
        cmbAirports.setModel(model);
      } catch (Exception ex) {
        txtError.setText("Error loading area! " + Exceptions.toString(ex));
        txtAreaXml.setForeground(java.awt.Color.RED);
      }
    }

  }

  class FsTypesFileChangedHandler extends XmlFileSelectorFileChangedHandler {

    @Override
    public void fileChanged(String newFileName) {
      AirplaneTypes types = new AirplaneTypes();
      Serializer ser = new Serializer();
      try {
        ser.fillList(
          newFileName,
          types);
        txtTypesXml.setForeground(java.awt.Color.BLACK);
      } catch (Exception ex) {
        txtError.setText("Error loading types! " + Exceptions.toString(ex));
        txtTypesXml.setForeground(java.awt.Color.RED);
      }
    }
  }
}
