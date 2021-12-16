package ca.jrvs.practice.codingChallenge;

// https://www.notion.so/jarvisdev/How-to-compare-two-maps-cef4e2e4312041c8a741d1af34eb324b
class Solution {

  // O(n)
  public <K,V> boolean compareMapsBuiltIn(Map<K,V> m1, Map<K,V> m2) {
    return m1.equals(m2);
  }

  // O(n), check all key/value in m1, m2 once
  public <K,V> boolean compareMapsHashJMap(Map<K,V> m1, Map<K,V> m2) {
    if (m2 == m1) {
      return true;
    }
    if (m1.size() != m2.size()) {
      return false;
    }
      for (Entry<K,V> entry : m1.entrySet()) {
        K key = entry.getKey();
        V value = entry.getValue();
        if (!m2.containsKey(key) || !Objects.equals(value, m2.get(key))) {
          return false;
        }
      }
      return true;
  }
}
