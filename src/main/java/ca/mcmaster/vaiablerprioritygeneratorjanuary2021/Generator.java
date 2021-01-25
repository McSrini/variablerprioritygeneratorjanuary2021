/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.vaiablerprioritygeneratorjanuary2021;

import static ca.mcmaster.vaiablerprioritygeneratorjanuary2021.Constants.*;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author tamvadss
 */
public class Generator {
    
    public static Map<IloNumVar, Double> upPseudoCostMap = Collections.synchronizedMap (new  HashMap<IloNumVar, Double> ());
    public static Map<IloNumVar, Double> downPseudoCostMap = Collections.synchronizedMap (new  HashMap<IloNumVar, Double> ());
    
    public static void main(String[] args) throws Exception {
        IloCplex cplex = new IloCplex ();
        
        long startTime = System.currentTimeMillis();
                
        cplex.importModel( MIP_FOLDER + MIP_FILENAME);
        
        cplex.setParam( IloCplex.Param.MIP.Strategy.File,CPX_PARAM_NODEFILEIND  );
        cplex.setParam( IloCplex.Param.MIP.Strategy.HeuristicFreq , CPX_PARAM_HEURFREQ);
        cplex.setParam( IloCplex.Param.MIP.Strategy.VariableSelect , CPX_PARAM_VARSEL);
        cplex.setParam( IloCplex.Param.MIP.Strategy.Search , CPX_PARAM_MIPSEARCH);
        cplex.setParam( IloCplex.Param.Emphasis.MIP , CPX_PARAM_MIPEMPHASIS );
        cplex.setParam( IloCplex.Param.TimeLimit, TIME_LIMIT_MINUTES*SIXTY);
        cplex.setParam( IloCplex.Param.Threads, MAX_THREADS);
        
        BranchHandler bh = new BranchHandler (true);
        cplex.use ( bh);
        cplex.solve ();
        bh.isCollectionPhase= false;
        cplex.setParam( IloCplex.Param.Threads, ONE);
        cplex.solve();
        
        long endTime = System.currentTimeMillis();
        
        
        System.out.println ("Time taken minutes" + (endTime-startTime)/ (1000*60) ) ;
        
        System.out.println(upPseudoCostMap.size() + " "+ downPseudoCostMap.size()) ;
        
        List<String> varPriorityList  = new ArrayList<String> ( );
        
        while (upPseudoCostMap.size()> ZERO){
            String nextVar = getWinningVar ();
            varPriorityList.add (nextVar) ;
        }
        
        savePriorityListToDisk (varPriorityList);
        
    }
    
   
    
    private static String getWinningVar (){
        IloNumVar winner = null;
        Double maxPrimaryMetric = MINUS_ONE;
        Double maxSecondaryMetric = MINUS_ONE;
        
        for (IloNumVar var : upPseudoCostMap.keySet()){
            double upPC = upPseudoCostMap.get(var);
            double downPC = downPseudoCostMap.get(var);
            double thisMax =  Math.max(upPC,downPC ) ;
            double thisMin =  Math.min(upPC,downPC ) ;
            if (thisMin > maxPrimaryMetric  ){
                maxPrimaryMetric =thisMin;
                maxSecondaryMetric = thisMax;
                winner = var ;
            }else  if (thisMin == maxPrimaryMetric  && thisMax> maxSecondaryMetric ){
                maxPrimaryMetric =thisMin;
                maxSecondaryMetric = thisMax;
                winner = var ;
            }
        }
        
        upPseudoCostMap.remove(winner);
        downPseudoCostMap.remove(winner);
        
        System.out.println ("Metrics : " + winner + " " + maxPrimaryMetric + " " + maxSecondaryMetric );
        
        return winner.getName();
    }
    
    private static void savePriorityListToDisk (List<String> varPriorityList) throws Exception {
        
        for (String str : varPriorityList){
            System.out.println(str) ;
        }
        
        FileOutputStream fos =                     new FileOutputStream(PRIORITY_LIST_FILENAME);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(varPriorityList);
        oos.close();
        fos.close();
        
        FileInputStream fis = new FileInputStream(PRIORITY_LIST_FILENAME);
        ObjectInputStream ois = new ObjectInputStream(fis);
        List<String>  recreatedVarPriorityList = (List<String>) ois.readObject();
        ois.close();
        fis.close();
        
        System.out.println() ;
        System.out.println("Printing recreated Map") ;
        for (String str : recreatedVarPriorityList){
            System.out.println(str) ;
        }
    }
    
}

