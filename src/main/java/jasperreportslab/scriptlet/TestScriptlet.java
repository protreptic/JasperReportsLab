package jasperreportslab.scriptlet;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import jasperreportslab.scriptlet.numbertransformator.RussianNumber;

/**
 * @author Peter Bukhal <bukhal as i-teco.ru> on 29.09.15.
 */
public class TestScriptlet extends JRDefaultScriptlet {

  public TestScriptlet() {

  }

  public String translateFormat(Number number) throws JRScriptletException {
    return new RussianNumber().format(number);
  }

  public String translateAmount(Number number) throws JRScriptletException {
    return new RussianNumber().amount(number);
  }

  public static void main(String[] args) {
    
  }

}
