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
import java.util.Map;

/**
 *
 * @author tamvadss
 */
public class BranchHandler extends BranchCallback {
 
    protected void main() throws IloException {
        if ( getNbranches()> ZERO  ){ 
            
            //get the branches about to be created
            IloNumVar[][] vars = new IloNumVar[TWO][] ;
            double[ ][] bounds = new double[TWO ][];
            IloCplex.BranchDirection[ ][] dirs = new IloCplex.BranchDirection[ TWO][];
            getBranches( vars, bounds, dirs);

            if (upPseudoCostMap.size() < NUM_VARS_TO_BE_RECORDED)            {
                
                //record any branching variable not previously branched upon
                
                if (! isVarAlreadyInMap  (vars[ZERO][ZERO])  ){
                    upPseudoCostMap.put (  vars[ZERO][ZERO],MINUS_ONE);
                    downPseudoCostMap.put (vars[ZERO][ZERO],MINUS_ONE );
                }
                
            }
            
            //get a var previously  bracnhed upon
            IloNumVar previousBranchingVar = getPreviousBranchingVar(vars[ZERO][ZERO]);
            
            if (null!= previousBranchingVar){
                double up_PS = getUpPseudoCost(previousBranchingVar);
                upPseudoCostMap.put(previousBranchingVar, up_PS) ;
                double down_PS = getDownPseudoCost(previousBranchingVar );
                downPseudoCostMap.put (previousBranchingVar,down_PS );
            }
               
            if ( numVarsWithRecordedPseudoCosts()== NUM_VARS_TO_BE_RECORDED)    {
                abort();
            }
             
        }
    }
    
    private int numVarsWithRecordedPseudoCosts () {
        int result = ZERO;
        for (Double val : downPseudoCostMap.values()){
            if (val > MINUS_ONE) result ++;
        }
        return result;
    }
    
    private boolean isVarAlreadyInMap (IloNumVar var)  {
        boolean result = false;
        
        for (Map.Entry<IloNumVar, Double> entry : downPseudoCostMap.entrySet()){
            if (entry.getKey().getName().equals(var.getName())){                
                result = true;
                break;
            }
        }
        
        return result;
    }
    
    private IloNumVar getPreviousBranchingVar (IloNumVar var){
        IloNumVar result = null;
        
        for (Map.Entry<IloNumVar, Double> entry : downPseudoCostMap.entrySet()){
            if (entry.getValue() == MINUS_ONE && ! var.getName().equals(entry.getKey().getName())){
                result = entry.getKey();
                break;
            }
        }
        return result;
    }
    
}
