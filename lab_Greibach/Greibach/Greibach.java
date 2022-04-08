package Greibach;
import Chomsky.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class Greibach extends Chomsky {
    private LinkedHashMap<String, ArrayList<String>> production = new LinkedHashMap<>();

    public Greibach() {}

    // get a valid new key

    public String getValidKey(String search) {
        for (String list: production.keySet()) {
            if (production.get(list).contains(search) && production.get(list).size() == 1){
                return list;
            }
        }
        String keys = "ZYXWVUTSRQPONMLKJIHGFEDCBA";
        for (char key : keys.toCharArray()) {
            //returns a key which is not present in map
            if (!production.containsKey(String.valueOf(key))) {
                return String.valueOf(key);
            }
        }
        return "";
    }

    public void initialise(){
        production = productions;
    }

    public boolean findGreibachTransitions(){
        for (String key: production.keySet()) {
            for (String value: production.get(key)) {
                if (!checkIfGreibachForm(value)){ // S-> AB   A->SA  value - AB
                    String firstChar = value.substring(0,1);

                    //Eliminate recursion
                    if (firstChar.equals(key)){
                        removeLeftRecursion(value, firstChar);
                        return false;
                    }
                    // check indirect recursion
                    else if(firstChar.equals(firstChar.toUpperCase())){
                        //eliminate indirect recursion
                        if (checkIndirectRecursion(value, key)){
                            //we break and start again
                            createRecursion(value, key, firstChar);
                            return false;
                        }
                        //copy contents of first letter to the value
                        else{
                            //Ca  so we copy C into Ca
                            copyContents(value, key);
                            return false;
                        }
                    }
                    else if (firstChar.equals(firstChar.toLowerCase())){
                        createGreibachForm(value, key);
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public boolean checkIfGreibachForm(String value){
        if (value.length()==1 && Character.isLowerCase(value.charAt(0))){
            return true;
        }
        else if (Character.isLowerCase(value.charAt(0))){
            String sub = value.substring(1);
            if (sub.equals(sub.toUpperCase())) return true;
        }
        return false;
    }

    // S-> SAB  key = S  recursion = SAB
    public void removeLeftRecursion(String recursion, String key){
        ArrayList<String> newProductions = new ArrayList<>();
        String newKey = getValidKey("");

        for (String value: production.get(key)) {
            StringBuilder newValue = new StringBuilder(value);
            //if the start letter is not recursive we put the new key to the end
            if (!String.valueOf(value.charAt(0)).equals(key)){
                newValue.append(newKey);
                newProductions.add(String.valueOf(newValue));
            }
            //if its recursive we remove the recursive key at the start and add the newKey to the end
            else {
                newValue.deleteCharAt(0);
                newValue.append(newKey);

                ArrayList<String> singleList = new ArrayList<>();
                singleList.add(String.valueOf(newValue));
                //singleList.add("#");
                if (production.containsKey(newKey)){
                    production.get(newKey).add(String.valueOf(newValue));
                }else{
                    productions.put(newKey, singleList);
                }

            }

        }
        production.get(key).remove(recursion);
        production.get(key).addAll(newProductions);
    }

    public boolean checkIndirectRecursion(String value, String recursiveKey){

        String firstKey = String.valueOf(value.charAt(0));
        for (String key: production.get(firstKey)) {
            if (String.valueOf(key.charAt(0)).equals(recursiveKey)){
                return true;
            }
        }
        return false;
    }

    //value = AB  key == S so we copy all contents of A instead of AB and put into S
    public void createRecursion(String value, String recursiveKey, String firstChar){
        ArrayList<String> newProductions = new ArrayList<>();

        for (String key: production.get(firstChar)) {
            StringBuilder newProduction = new StringBuilder(value);
            newProduction.replace(0,1, key);
            newProductions.add(String.valueOf(newProduction));
        }
        //we added new transitions to arraylist and now we have to remove the recursive value
        production.get(recursiveKey).remove(value);
        production.get(recursiveKey).addAll(newProductions);
    }

    public void createGreibachForm(String value, String key){
        //value = bAcB  c->X  so we get  bAXB

        StringBuilder newValue = new StringBuilder(value);
        for (int i = 1; i < value.length(); i++) {
            if (Character.isLowerCase(value.charAt(i))){
                //create a new key for that letter
                String newKey = getValidKey(String.valueOf(value.charAt(i)));
                ArrayList<String> singleList = new ArrayList<>();
                singleList.add(String.valueOf(value.charAt(i)));
                production.put(newKey, singleList);

                // replace letter with new key
                newValue.replace(i, i+1, newKey);
            }
        }
        production.get(key).add(String.valueOf(newValue));
        production.get(key).remove(value);
    }

    public void copyContents(String value, String key){
        String firstKey = String.valueOf(value.charAt(0));
        ArrayList<String> newProductions = new ArrayList<>();
        for (String element: production.get(firstKey)){
            StringBuilder newValue = new StringBuilder(value);
            newValue.replace(0,1, element);

            newProductions.add(String.valueOf(newValue));
        }
        production.get(key).addAll(newProductions);
        production.get(key).remove(value);
    }


}
