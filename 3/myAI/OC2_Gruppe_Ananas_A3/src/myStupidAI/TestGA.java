package myStupidAI;

/**
 * Created by Rolle on 26.06.2015.
 */
public class TestGA {

    public static void main(String[] args){
        Flock_Ga myGa = new Flock_Ga();

        ParamSet p_one = new ParamSet(0.5,1,0.4,50,3,4,50);
        ParamSet p_two = new ParamSet(1,1,0,50,3,4,10);
        ParamSet p_three = new ParamSet(0.3,1,0.2,50,3,4,40);

        ParamSetCollection p_coll = new ParamSetCollection();
        p_coll.addNewParamSet(p_one);
        p_coll.addNewParamSet(p_two);
        p_coll.addNewParamSet(p_three);

        myGa.doGa(p_coll);

        for(ParamSet pSet : p_coll.getParams())
            System.out.println(pSet.getMembersAsMap().toString());
    }
}
