package jasperreportslab.scriptlet;

import static org.junit.Assert.*;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Peter Bukhal <bukhal as i-teco.ru> on 29.09.15.
 */
public class TestScriptletTest {

  TestScriptlet testScriptlet;

  @Before
  public void setUp() {
    testScriptlet = new TestScriptlet();
  }

  @Test
  public void testTestScriptletCreation() {
    Assert.assertTrue(testScriptlet instanceof JRDefaultScriptlet);
  }

}