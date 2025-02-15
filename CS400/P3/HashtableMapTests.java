import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class HashtableMapTests {
  private HashtableMap<String, Integer> hashtable;
  
  
  //this is to set up test environment 
  @BeforeEach
  public void setup() {
    hashtable = new HashtableMap<>();
  }
  
  /**
   * Testing my Put and Get methods
   * I created a test for inserting a key-value pair into hashtable map
   * Expected - Value gets returned by get() method and matches inserted test value
   */
  @Test
  public void testPutGet() {
    hashtable.put("test", 123);
    int value = hashtable.get("test");
    Assertions.assertEquals(123, value);
  }
  
  /**
   * Testing my Remove method
   * I created same test now to remove the test and then get the value with the key
   * Expected: get() has to throw exception after the test is removed
   */
  @Test
  public void testRemove() {
    hashtable.put("test", 123);
    hashtable.remove("test");
    Assertions.assertThrows(NoSuchElementException.class, () -> hashtable.get("test"));
  }

  /**
   * Testing my resize method
   * Insert like 7ish elements so resize can work
   * then elements need to get checked
   * get() should do this by returning the right value for the keys after //resize
   */
  @Test
  public void testResize() {
    for(int i = 0; i < 7; i++) {
      hashtable.put("test" + i, i);
    }
    //resize
    for(int i = 0; i < 7; i++) {
      int value = hashtable.get("test" + i);
      Assertions.assertEquals(i, value);
    }
  }
}
