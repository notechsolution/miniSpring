package com.minis.beans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ArgumentValues {
//  private final Map<Integer, ArgumentValue> indexedArgumentValues = new HashMap<>();
private final List<ArgumentValue> argumentValues = new LinkedList<>();
  private final List<ArgumentValue> genericArgumentValues = new LinkedList<>();

  public ArgumentValues() {
  }

  public void addArgumentValue(ArgumentValue argumentValue){
    argumentValues.add(argumentValue);
  }

//  public boolean hasIndexedArgumentValue(int index) {
//    return indexedArgumentValues.containsKey(index);
//  }

  public ArgumentValue getIndexedArgumentValue(int index) {
    return argumentValues.get(index);
  }

  public void addGenericArgumentValue(ArgumentValue newValue) {

    if(newValue.getName() !=null ){
      Iterator<ArgumentValue> it = genericArgumentValues.iterator();
      while(it.hasNext()) {
        ArgumentValue currentValue = it.next();
        if(newValue.getName().equals(currentValue.getName())){
          it.remove();
        }
      }

    }
    this.genericArgumentValues.add(newValue);
  }

  public ArgumentValue getGenericArgumentValue(String argumentName){
    for (ArgumentValue genericArgumentValue : genericArgumentValues) {
      if(genericArgumentValue.getValue()!=null && genericArgumentValue.getName().equals(argumentName)){
        return genericArgumentValue;
      }
    }
    return null;

  }

  public int getArgumentCount() {
    return argumentValues.size();
  }

  public boolean isEmpty (){
    return argumentValues.isEmpty();
  }
}
