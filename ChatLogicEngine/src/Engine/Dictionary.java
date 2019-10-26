package Engine;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class Dictionary {
    private String content = "";
    private ArrayList<String> illegalChar=new ArrayList<String>();
    private  HashMap<String,Integer> dictionary = new HashMap<String,Integer>();
    private  HashMap<String,Integer> ScoresOfWords = new HashMap<String,Integer>();
    private int totalWords=0;
    private  ArrayList<Tile> tileList;
    private HashMap<String,Integer> wordsSection=new HashMap<String,Integer>();

    public Dictionary(String content, ArrayList<Tile> tileList){
        this.content = content;
        initIllegalChar();
        this.tileList=tileList;
    }

    public HashMap<String,Integer> getDictionary(){
        String[] splitWords;

        Scanner sc = new Scanner(content);

        while (sc.hasNextLine()) {
            Scanner s2 = new Scanner(sc.nextLine());
            while (s2.hasNext()) {
                String word = s2.next();
                splitWords=getPureWord(word);
                if (splitWords!=null) {
                    for(String w:splitWords) {
                        if (w != null) {
                            w = w.toUpperCase();
                            if (dictionary.containsKey(w)) {
                                dictionary.put(w, dictionary.get(w) + 1);
                            } else {
                                dictionary.put(w, 1);
                                totalWords++;
                            }
                        }
                    }
                }
            }
        }
        return dictionary;
    }


    private void initIllegalChar(){
        illegalChar.add("$");
        illegalChar.add("%");
        illegalChar.add("]");
        illegalChar.add("[");
        illegalChar.add("}");
        illegalChar.add("{");
        illegalChar.add(")");
        illegalChar.add("(");
        illegalChar.add("'");
        illegalChar.add("\"");
        illegalChar.add("*");
        illegalChar.add("+");
        illegalChar.add("=");
        illegalChar.add("_");
        illegalChar.add("-");
        illegalChar.add(";");
        illegalChar.add(":");
        illegalChar.add(".");
        illegalChar.add(",");
        illegalChar.add("?");
        illegalChar.add("!");
        illegalChar.add("ג€");
    }

    String[] getPureWord(String word){
        String[] result=new String[4];
        if (word.length()<=1) return null;
        if (word.contains("-")){
            result=word.split("-");
        } else if(word.contains("ג€")){
            result=word.split("ג€");
        } else if(word.contains("_")){
            result=word.split("_");
        }else result[0]=word;
        for (int i=0;i<illegalChar.size();i++){
            for(int j=0;j<result.length;j++) {
                if (result[j]!=null && result[j].contains(illegalChar.get(i))) {
                    result[j] = result[j].replace(illegalChar.get(i), "");
                }
            }
        }
        return result;
    }

    public void initHashMaps() {

        int firstSplit= totalWords/3;
        int secondSplit= (totalWords*2)/3;
        int count=0;
        ScoresOfWords.clear();
        wordsSection.clear();

        ScoresOfWords = dictionary.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1,e2) -> e1, LinkedHashMap::new));

        Iterator it = ScoresOfWords.entrySet().iterator();
        while (it.hasNext()) {
            count++;
            Map.Entry pair = (Map.Entry)it.next();
            int sum=calcSumOfLetters((String)pair.getKey());
            if (count<firstSplit){
                pair.setValue(sum*3);
                wordsSection.put((String)pair.getKey(),3);
            } else if (count>=firstSplit && count<secondSplit){
                pair.setValue((sum*2));
                wordsSection.put((String)pair.getKey(),2);
            } else{
                pair.setValue(sum);
                wordsSection.put((String)pair.getKey(),1);
            }
        }

    }


    private int calcSumOfLetters(String word){
        int sum=0;
        for(int i=0;i<word.length();i++){
            String s=Character.toString(word.charAt(i));
            for (Tile t:tileList){
                if (t.getSign().contains(s)){
                    sum=sum+t.getScore();
                }
            }
        }
        return sum;
    }

    public  HashMap<String,Integer> getTenRareWords(){
        HashMap<String,Integer> res=new HashMap<String,Integer>();
        HashMap<String,Integer> temp=new HashMap<String,Integer>();
        temp= dictionary.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1,e2) -> e1, LinkedHashMap::new));

        Iterator it = temp.entrySet().iterator();
        int count=0;
        while (it.hasNext() && count<10) {
            Map.Entry pair = (Map.Entry)it.next();
            if( pair.getKey().toString().length()>1) {
                res.put((String) pair.getKey(), (Integer) pair.getValue());
                count++;
            }
        }

        Iterator it1 = res.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry pair = (Map.Entry)it1.next();
            Integer inte=(Integer)ScoresOfWords.get(pair.getKey());
            pair.setValue((Integer)inte);
        }
    /*
     //print the list
       Iterator it2 = res.entrySet().iterator();
       while (it2.hasNext()) {
           Map.Entry pair = (Map.Entry)it2.next();
           System.out.println(pair.getKey()+" = "+pair.getValue());
       } */
        return res;
    }

    public int getScore(String word){
        return ScoresOfWords.get(word);
    }

    public String getCalcWord(String word){

        String s="";
        int sum=calcSumOfLetters(word);
        int section=wordsSection.get(word);
        return s+String.valueOf(sum)+" * "+String.valueOf(section);
    }
}
