package myStupidAI.bolding;

import java.util.*;


/**
 * Created by Rolle on 26.06.2015.
 */
public class Flock_Ga {

    Random r;

    public Flock_Ga() {
        this.r = new Random();
    }

    public void doGa(ParamSetCollection p_collaction){
        if(p_collaction.getParams().size() < 2){
            System.out.println("Flock_Ga: there are not enough ParamSets for GA");
            return;
        }

        ParamSet[] parents = findRouletParents(p_collaction);
        //System.out.println("Parents are: " + Arrays.toString(parents));
        List<HashMap<String,String>> crossovered = doCrossOver(parents);
        //System.out.println("Children after crossover: " +crossovered);
        doMutation(crossovered);
        //System.out.println("Children after mutation: " + crossovered);
        calcNewReward(crossovered);

        p_collaction.addNewParamSet(new ParamSet(crossovered.get(0)));
        p_collaction.addNewParamSet(new ParamSet(crossovered.get(1)));
    }

    private ParamSet[] findRouletParents(ParamSetCollection p_collaction){
        ParamSet[] foundClassifiers = new ParamSet[2];
        // copy only with the same action
        ArrayList<ParamSet> copyOfBase = new ArrayList<>(p_collaction.getParams());
        //so we get two parents
        for(int i = 0; i < 2; i ++){
            int rewardSum = 0;
            for (ParamSet pS : copyOfBase){
                rewardSum += pS.getReward();
            }

            int randomRew = r.nextInt(rewardSum + 1);
            rewardSum = 0;

            int pos = 0;
            for(ParamSet pS : copyOfBase){
                rewardSum += pS.getReward();
                if(rewardSum < randomRew){
                    pos++;
                }
                else
                    break;
            }
            foundClassifiers[i] = copyOfBase.get(pos);
            copyOfBase.remove(pos);
        }

        return foundClassifiers;
    }

    private List<HashMap<String,String>> doCrossOver(ParamSet[] parents){
        HashMap<String,String> fathersMemberMap = parents[0].getMembersAsMap();
        HashMap<String,String> mothersMemberMap = parents[1].getMembersAsMap();

        int memberSize = fathersMemberMap.keySet().size(); // mother is the same

        int numToSwap = r.nextInt(memberSize); // so we dont swap all members
        //System.out.println("Crossover: how many to Crossover? " + numToSwap);

        String[] copyOf = Arrays.copyOf(fathersMemberMap.keySet().toArray(), fathersMemberMap.keySet().toArray().length, String[].class);
        String[] memberNamesToSwap = getRndMembersToSwap( copyOf ,numToSwap);
        //System.out.println("Crossover: which members to swap? " + Arrays.toString(memberNamesToSwap));

        for(String member: memberNamesToSwap){
            String tmp = fathersMemberMap.get(member);
            fathersMemberMap.put(member,mothersMemberMap.get(member));
            mothersMemberMap.put(member,tmp);
        }
        ArrayList<HashMap<String,String>> retList = new ArrayList<>();
        retList.add(fathersMemberMap);
        retList.add(mothersMemberMap);

        return retList;
    }

    private String[] getRndMembersToSwap(String[] keySet, int numberOfMembers ){
        ArrayList<String> memberNameList = new ArrayList<>();
        String[] memberNamesToSwap = new String[numberOfMembers];

        for(String memberName: keySet)
            memberNameList.add(memberName);

        Collections.shuffle(memberNameList);

        for(int i = 0; i < memberNamesToSwap.length; i++)
            memberNamesToSwap[i] = memberNameList.get(i);

        return memberNamesToSwap;
    }

    private void doMutation(List<HashMap<String,String>> childernAsMemberMap){

        for (HashMap<String,String> membersAsMap: childernAsMemberMap){
            String[] memberNames = Arrays.copyOf(membersAsMap.keySet().toArray(), childernAsMemberMap.get(0).keySet().toArray().length, String[].class);;

            String mutationMemberName;
            while(true){
                int rndMutationMember = r.nextInt(membersAsMap.keySet().size());
                mutationMemberName = memberNames[rndMutationMember];
                if(mutationMemberName.equals("reward")){
                    // do it again
                }
                else
                    break;

            }


            String[] tmp = membersAsMap.get(mutationMemberName).split(";");

            if(tmp[1].equals("D")){
                double currentValue =  Double.valueOf(tmp[0]);
                double mutatedValue = mutationForDoubleValue(currentValue);
                membersAsMap.put(mutationMemberName,Double.toString(mutatedValue));
            }
            else if(tmp[1].equals("I")){
                int currentValue = Integer.valueOf(tmp[0]);
                int mutatedValue = mutationForIntValue(currentValue);
                membersAsMap.put(mutationMemberName, Integer.toString(mutatedValue));
            }
        }
    }

    private void calcNewReward(List<HashMap<String,String>> childernAsMemberMap){

        int newReward = (Integer.valueOf(childernAsMemberMap.get(0).get("reward").split(";")[0])
                +  Integer.valueOf(childernAsMemberMap.get(1).get("reward").split(";")[0]))/2;

        childernAsMemberMap.get(0).put("reward",Integer.toString(newReward));
        childernAsMemberMap.get(1).put("reward",Integer.toString(newReward));

    }

    private double mutationForDoubleValue(double currentValue){
        double retValue;
        double chance = r.nextDouble();
        double highestLimit = 1.0;
        double lowestLimit = 0;

        if(chance < 0.5)
            retValue = currentValue + (r.nextDouble() * (highestLimit - currentValue));
        else
            retValue = lowestLimit + (r.nextDouble() * (currentValue - lowestLimit));

        if(retValue < lowestLimit || retValue > highestLimit){
            System.out.println("Flock_GA: mutation value was not in defined limit!");
            return currentValue;
        }

        return retValue;
    }

    private int mutationForIntValue(int currentValue){
        int retValue;
        int lowestLimit = 0;

        double chance = r.nextDouble();

        if(chance < 0.5)
            retValue = currentValue + r.nextInt(100);
        else
            retValue = r.nextInt(currentValue - lowestLimit + 1) + lowestLimit;

        if(retValue < lowestLimit){
            System.out.println("Flock_GA: mutation value was not in defined limit!");
            return currentValue;
        }

        return retValue;

    }
}
