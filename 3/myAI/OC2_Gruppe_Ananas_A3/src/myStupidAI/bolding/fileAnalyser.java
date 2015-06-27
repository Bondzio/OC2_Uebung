package myStupidAI.bolding;

import java.util.*;

/**
 * Created by Rolle on 27.06.2015.
 */
public class fileAnalyser {

    public static void main(String[] args){
        SimpleFileHandler simpleFileHandler = new SimpleFileHandler();
        ParamSetCollection paramSetCollection = simpleFileHandler.loadParamSetCollection();


        printStatMap(createStatsMap(paramSetCollection));






    }

    private static void sortPSetCol(ParamSetCollection paramSetCollection){
        Collections.sort(paramSetCollection.getParams(), new Comparator<ParamSet>() {
            public int compare(ParamSet p_one, ParamSet p_two) {
                int reward_p_one = p_one.getReward();
                int reward_p_two = p_two.getReward();
                if(reward_p_one> reward_p_two)
                    return 1;
                else if(reward_p_one == reward_p_two)
                    return 0;
                else
                    return -1;
            }
        });
    }

    private static  HashMap<String,ParamSetCollection> createStatsMap(ParamSetCollection paramSetCollection){
        HashMap<String,ParamSetCollection> statMap = new HashMap<>();

        sortPSetCol(paramSetCollection);

        int offSet = 5;

        int currentReward = paramSetCollection.getParams().get(0).getReward();
        int rewardUpperLimit = currentReward + offSet;
        ParamSetCollection tempPSetCol = new ParamSetCollection();
        ArrayList<ParamSetCollection> tmp = new ArrayList<>();
        for(ParamSet pSet : paramSetCollection.getParams()){
            currentReward = pSet.getReward();

            if(currentReward <= rewardUpperLimit){
                tempPSetCol.addNewParamSet(pSet);
            }
            else{
                if(tempPSetCol.getParams().size() > 0){
                    tmp.add(tempPSetCol);
                    tempPSetCol = new ParamSetCollection();
                }
                rewardUpperLimit = currentReward + offSet;
            }
        }

        for (ParamSetCollection pSetCol : tmp){
            String key;
            if (pSetCol.getParams().size() == 1)
                key = Integer.toString(pSetCol.getParams().get(0).getReward());
            else
                key = Integer.toString(pSetCol.getParams().get(0).getReward()) + "-" + Integer.toString(pSetCol.getParams().get(pSetCol.getParams().size() - 1).getReward());
            statMap.put(key,pSetCol);
        }

        return statMap;
    }


    private static void printStatMap(HashMap<String,ParamSetCollection> statMap){
        HashMap<Integer,ArrayList<String>> helpMap = new HashMap<>();

        for(String key: statMap.keySet()){
            int leSize = statMap.get(key).getParams().size();
            if(helpMap.containsKey(leSize))
                helpMap.get(leSize).add(key);
            else {
                ArrayList<String> newi = new ArrayList<String>();
                newi.add(key);
                helpMap.put(leSize, newi);
            }
        }

        int[] intArray = new int[helpMap.keySet().size()];
        int pos = 0;
        for (Integer size : helpMap.keySet()){
            intArray[pos++] = size;
        }

        Arrays.sort(intArray);

        for(int inti: intArray){
            ArrayList<String> keys = helpMap.get(inti);
            for(String key : keys){
                String space = "";
                int spaces_needed = 10 - key.length();
                for(int i = 0 ; i <= spaces_needed; i++)
                    space += " ";

                System.out.println("[" + key + "]" + space +"n:" + Integer.toString(inti));
            }
        }


    }


    private static void printParamSetCollection(ParamSetCollection paramSetCollection){
        for(ParamSet pSet : paramSetCollection.getParams()){
            System.out.println(pSet.getMembersAsMap());
        }
    }

}
