package com.minis.beans.factory.config;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ConstructorArgumentValues {
//  private final Map<Integer, ArgumentValue> indexedArgumentValues = new HashMap<>();
private final List<ConstructorArgumentValue> constructorArgumentValues = new LinkedList<>();
  private final List<ConstructorArgumentValue> genericConstructorArgumentValues = new LinkedList<>();

  public ConstructorArgumentValues() {
  }

  public void addArgumentValue(ConstructorArgumentValue constructorArgumentValue){
    constructorArgumentValues.add(constructorArgumentValue);
  }

//  public boolean hasIndexedArgumentValue(int index) {
//    return indexedArgumentValues.containsKey(index);
//  }

  public ConstructorArgumentValue getIndexedArgumentValue(int index) {
    return constructorArgumentValues.get(index);
  }

  public void addGenericArgumentValue(ConstructorArgumentValue newValue) {

    if(newValue.getName() !=null ){
      Iterator<ConstructorArgumentValue> it = genericConstructorArgumentValues.iterator();
      while(it.hasNext()) {
        ConstructorArgumentValue currentValue = it.next();
        if(newValue.getName().equals(currentValue.getName())){
          it.remove();
        }
      }

    }
    this.genericConstructorArgumentValues.add(newValue);
  }

  public ConstructorArgumentValue getGenericArgumentValue(String argumentName){
    for (ConstructorArgumentValue genericConstructorArgumentValue : genericConstructorArgumentValues) {
      if(genericConstructorArgumentValue.getValue()!=null && genericConstructorArgumentValue.getName().equals(argumentName)){
        return genericConstructorArgumentValue;
      }
    }
    return null;

  }

  public int getArgumentCount() {
    return constructorArgumentValues.size();
  }

  public boolean isEmpty (){
    return constructorArgumentValues.isEmpty();
  }
}
