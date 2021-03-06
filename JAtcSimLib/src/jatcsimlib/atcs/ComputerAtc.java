/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jatcsimlib.atcs;

import jatcsimlib.Acc;
import jatcsimlib.airplanes.Airplane;
import jatcsimlib.messaging.Message;

import java.util.List;

/**
 *
 * @author Marek
 */
public abstract class ComputerAtc extends Atc {

  protected final WaitingList waitingRequestsList = new WaitingList();

  public ComputerAtc(AtcTemplate template) {
    super(template);
  }

  @Override
  public boolean isHuman() {
    return false;
  }

  protected abstract void _elapseSecond();

  public void elapseSecond() {
    _elapseSecond();

    // opakovani starych zadosti
    List<Airplane> awaitings = waitingRequestsList.getAwaitings();
    for (Airplane p : awaitings) {
      Message m = new Message(this, Acc.atcApp(),
        new PlaneSwitchMessage(p, " to you (repeated)"));
      Acc.messenger().send(m);
      recorder.logMessage(m);
    }

  }

  protected void requestSwitch(Airplane plane) {
    getPrm().requestSwitch(this, Acc.atcApp(), plane);
    Message m = new Message(this, Acc.atcApp(),
      new PlaneSwitchMessage(plane, " to you"));
    Acc.messenger().send(m);
  }

  protected void refuseSwitch(Airplane plane) {
    getPrm().refuseSwitch(this, plane);
    Message m = new Message(this, Acc.atcApp(),
      new PlaneSwitchMessage(plane, " refused. Not in my coverage."));
    Acc.messenger().send(m);
    recorder.logMessage(m);
  }

  protected void confirmSwitch(Airplane plane) {
    getPrm().confirmSwitch(this, plane);
    Message m = new Message(this, Acc.atcApp(),
      new PlaneSwitchMessage(plane, " accepted"));
    Acc.messenger().send(m);
    recorder.logMessage(m);
  }

  protected void approveSwitch(Airplane plane) {
    getPrm().approveSwitch(plane);
    recorder.log(this, "OTH", "approveSwitch " + plane.getCallsign().toString());
  }
}
