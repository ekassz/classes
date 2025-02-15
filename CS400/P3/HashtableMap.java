import java.util.NoSuchElementException;

/**
 * Welcome to my lovely hashtable map
 * We'll be using linear probing for this program (store, retrieve and remove key-value pairs,
 * and resizes when load factor becomes too high)
 * 
 * @author erobl
 *
 * @param <KeyType>
 * @param <ValueType>
 */
public class HashtableMap<KeyType, ValueType> {
  protected Entry<KeyType, ValueType>[] hashtable;
  private static final int DEFAULT_CAP = 8;
  private int size;
  private Entry<KeyType, ValueType> TOMBSTONE; //TOMBSTONE - used to mark spots of removed entries? 
  //idk but is good to make sure everything still works even after removals
  
  /**
   * Entry will be created for key-value pairs in the hashtable
   * @author erobl
   *
   * @param <KeyType>
   * @param <ValueType>
   */
  static class Entry<KeyType, ValueType>{
    KeyType key;
    ValueType value;
    
    /**
     * Lets do a new Entry ^ with the specified key & value
     * @param key
     * @param value
     */
    public Entry(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }
  }
  
  /**
   * 1st constructor for capacity! 
   * Here we're just initializing a new hashtable with the initial cap
   * @param capacity
   */
  public HashtableMap(int capacity) {
    @SuppressWarnings("unchecked")
    Entry<KeyType, ValueType>[] tempHashtable = new Entry[capacity];
    this.hashtable = tempHashtable;
    this.TOMBSTONE = new Entry<>(null, null);
    //setting to 0 everywhere!
    size = 0;
    
  }
  
  /**
   * 2nd constructor for the default capacity
   */
  public HashtableMap() {
    this(DEFAULT_CAP);
    
  }
  
  /**
   * This is our hash function
   * ^computes hash of a key
   * @param key
   * @return hash of the key
   */
  private int hash(KeyType key) {
    return (key.hashCode()& 0x7fffffff) % hashtable.length;
  }
  
  /**
   * lets load factor check
   * need to check if the load factor is too hgih
   * @return true if it is , false otherwise
   */
  private boolean isLoadFactorTooHigh() {
    return size >= 0.7 * hashtable.length;
}
  /**
   * Adding key-value pairs to hash function ^
   * if load factor is too high, then we have to move resize
   * @param key
   * @param value
   * @throws IllegalArgumentException - added to check if null for val or key
   */
  public void put(KeyType key, ValueType value) {
    if(key == null || value == null) {
      throw new IllegalArgumentException("key or value is null");
    }
    int i;
    for (i = hash(key); hashtable[i] != null && hashtable[i] != TOMBSTONE; i = (i + 1) % hashtable.length) {
        // If the key already exists, replace the value and return
        if (hashtable[i].key != null && hashtable[i].key.equals(key)) {
          //adding for test, cant add a duplicate
            throw new IllegalArgumentException("cant do a duplicate key");
        }
    }

    hashtable[i] = new Entry<>(key, value);
    size++;
    
    if (isLoadFactorTooHigh()) {
      resize(2 * hashtable.length);
    }
  }
  


  /**
   * so that above works, we now have to resize the hashtable
   * ^ goes into new hashtable
   * @param capacity
   */
  private void resize(int capacity) {
    HashtableMap<KeyType, ValueType> temp = new HashtableMap<>(capacity);
    //copy to new
    for (Entry<KeyType, ValueType> entry : hashtable) {
      if (entry != null && entry != TOMBSTONE) {
          temp.put(entry.key, entry.value);
      }
  }
    hashtable = temp.hashtable;
  }
  
  /**
   * lets retrieve value by key
   * @param key
   * @return value - key
   * @throws IllegalArgumentException
   * @throws NoSuchElementException
   */
  public ValueType get(KeyType key) {
    if(key == null) {
      throw new IllegalArgumentException("key is null");
    }
    int startIndex = hash(key); //to stop search for test
    for(int i = startIndex; hashtable[i] != null; i = (i + 1)%hashtable.length) {
      if(hashtable[i] != TOMBSTONE && hashtable[i].key.equals(key)) {
        return hashtable[i].value;
      }
      //so now if we hit TOMBSTONE, or looped back to start we just stop
      if((i+1)%hashtable.length == startIndex) {
        break;
      }
    }
    throw new NoSuchElementException("Key not found");  
    }
  
  /**
   * let remove key-value pairs by key now from hashtable
   * @param key
   * @return value associated w key
   * @throws IllegalArgumentException 
   * @throws NoSuchElementException 
   */
  public ValueType remove(KeyType key) {
    if (key == null) {
        throw new IllegalArgumentException("Key is null");
    }

    for (int i = hash(key); hashtable[i] != null; i = (i + 1) % hashtable.length) {
        if (hashtable[i].key.equals(key)) {
            ValueType value = hashtable[i].value;
            hashtable[i] = TOMBSTONE;
            size--;
            return value;
        }
    }
    throw new NoSuchElementException("Key not found");
}
  /**
   * checking if key is in the hash table (I dont even need this but just in case? idk)
   * @param key
   * @return true
   */
  @SuppressWarnings("unused")
  private boolean contains(KeyType key) {
    for (int i = hash(key); hashtable[i] != null; i = (i + 1) % hashtable.length) {
      if (hashtable[i].key.equals(key)) {
          return true;
      }
  }
  return false;
}
}
