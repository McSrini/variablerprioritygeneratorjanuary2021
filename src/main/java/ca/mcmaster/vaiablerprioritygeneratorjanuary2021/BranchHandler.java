/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.vaiablerprioritygeneratorjanuary2021;

import static ca.mcmaster.vaiablerprioritygeneratorjanuary2021.Constants.*;
import static ca.mcmaster.vaiablerprioritygeneratorjanuary2021.Generator.*;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.BranchCallback;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author tamvadss
 */
public class BranchHandler extends BranchCallback {
    
    boolean isCollectionPhase = false;
    public BranchHandler (boolean isCollectionPhase){
        this . isCollectionPhase = isCollectionPhase;
    }
    
    protected void main() throws IloException {
        if ( getNbranches()> ZERO  ){ 
            
            if (isCollectionPhase){
                //get the branches about to be created
                IloNumVar[][] vars = new IloNumVar[TWO][] ;
                double[ ][] bounds = new double[TWO ][];
                IloCplex.BranchDirection[ ][] dirs = new IloCplex.BranchDirection[ TWO][];
                getBranches( vars, bounds, dirs);
                
                if (upPseudoCostMap.size()%TEN == ZERO)System.out.println("Map size is  "+ upPseudoCostMap.size()) ;

                if (upPseudoCostMap.size() < NUM_VARS_TO_BE_RECORDED)            {
                    upPseudoCostMap.put (  vars[ZERO][ZERO],MINUS_ONE);
                    downPseudoCostMap.put (vars[ZERO][ZERO],MINUS_ONE );
                    
                } else {
                    abort();
                }
            }else {
                //populat ethe pseudo cost values
                //this will be in single threaded mode
                Set<IloNumVar> keys = new HashSet <IloNumVar> ();
                keys.addAll(upPseudoCostMap.keySet()) ;
                for (IloNumVar var : keys){
                    upPseudoCostMap.put(var, getUpPseudoCost(var)) ;
                    downPseudoCostMap.put (var,getDownPseudoCost(var ) );
                }
                abort();
            }
            
            
            
        }         
    }
    
}
