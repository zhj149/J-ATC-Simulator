package jatcsimlib.speaking.parsing.shortParsing;

import jatcsimlib.Acc;
import jatcsimlib.exceptions.EInvalidCommandException;
import jatcsimlib.speaking.fromAtc.commands.ProceedDirectCommand;
import jatcsimlib.world.Navaid;

class ShortcutParser extends SpeechParser<ProceedDirectCommand> {

  private static final String[] prefixes = new String[]{"SH"};
  private static final String pattern = "SH (\\S+)";

  @Override
  String[] getPrefixes() {
    return prefixes;
  }

  @Override
  String getPattern() {
    return pattern;
  }

  @Override
  ProceedDirectCommand parse(RegexGrouper rg) {
    String ns = rg.getString(1);

    Navaid n = Acc.area().getNavaids().tryGet(ns);
    if (n == null) {
      throw new EInvalidCommandException("Unable to find navaid named \"" + ns + "\".", rg.getMatch());
    }
    ProceedDirectCommand ret = new ProceedDirectCommand(n);
    return ret;
  }
}
