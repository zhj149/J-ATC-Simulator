package jatcsimlib.speaking.parsing.shortParsing;

import jatcsimlib.exceptions.ENotSupportedException;
import jatcsimlib.speaking.ICommand;
import jatcsimlib.speaking.fromAtc.commands.ChangeHeadingCommand;

class ChangeHeadingParser extends SpeechParser<ChangeHeadingCommand> {

  private static final String[] prefixes = new String[]{"FH", "TR", "TL"};
  private static final String pattern = "((FH)|(TR)|(TL)) ?(\\d{1,3})?";

  @Override
  String[] getPrefixes() {
    return prefixes;
  }

  @Override
  String getPattern() {
    return pattern;
  }

  @Override
  ChangeHeadingCommand parse(RegexGrouper rg) {
    ChangeHeadingCommand.eDirection d;
    switch (rg.getString(1)) {
      case "FH":
        d = ChangeHeadingCommand.eDirection.any;
        break;
      case "TL":
        d = ChangeHeadingCommand.eDirection.left;
        break;
      case "TR":
        d = ChangeHeadingCommand.eDirection.right;
        break;
      default:
        throw new ENotSupportedException();
    }
    ChangeHeadingCommand ret;

    if (rg.getString(5) == null) {
      ret = new ChangeHeadingCommand();
    } else {
      int h = rg.getInt(5);
      ret = new ChangeHeadingCommand(h, d);
    }
    return ret;
  }

}
