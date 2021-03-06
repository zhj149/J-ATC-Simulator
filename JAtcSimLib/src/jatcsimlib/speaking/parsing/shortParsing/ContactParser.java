package jatcsimlib.speaking.parsing.shortParsing;

import jatcsimlib.atcs.Atc;
import jatcsimlib.exceptions.ENotSupportedException;
import jatcsimlib.speaking.ICommand;
import jatcsimlib.speaking.fromAtc.commands.ContactCommand;

class ContactParser extends SpeechParser<ContactCommand> {

  private static final String[] prefixes = new String[]{"CT", "CA", "CC"};
  private static final String pattern = "(CT|CA|CC)";

  @Override
  String[] getPrefixes() {
    return prefixes;
  }

  @Override
  String getPattern() {
    return pattern;
  }

  @Override
  ContactCommand parse(RegexGrouper rg) {
    Atc.eType t;
    switch (rg.getString(1)) {
      case "CT":
        t = Atc.eType.twr;
        break;
      case "CA":
        t = Atc.eType.app;
        break;
      case "CC":
        t = Atc.eType.ctr;
        break;
      default:
        throw new ENotSupportedException();
    }
    ContactCommand ret = new ContactCommand(t);
    return ret;
  }
}
