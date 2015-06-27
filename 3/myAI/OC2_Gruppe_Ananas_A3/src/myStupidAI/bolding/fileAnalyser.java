package myStupidAI.bolding;

/**
 * Created by Rolle on 27.06.2015.
 */
public class fileAnalyser {

    public static void main(String[] args){
        SimpleFileHandler simpleFileHandler = new SimpleFileHandler();
        ParamSetCollection paramSetCollection = simpleFileHandler.loadParamSetCollection();

        for(ParamSet pSet : paramSetCollection.getParams()){
            System.out.println(pSet.getMembersAsMap());
        }
    }
}
