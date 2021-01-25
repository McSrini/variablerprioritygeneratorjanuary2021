/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.mcmaster.vaiablerprioritygeneratorjanuary2021;

/**
 *
 * @author tamvadss
 */
public class Constants {
    
    public static final String MIP_NAME = "neoshuahum";
    //public static final String MIP_NAME = "opm2-z10-s4";
    public static final String MIP_FOLDER = ""; // "F:\\temporary files here recovered\\";
    public static final String MIP_FILENAME =MIP_NAME  + ".pre.sav";    
    public static String PRIORITY_LIST_FILENAME = MIP_NAME + "_priorityList.ser";
    
    public static int NUM_VARS_TO_BE_RECORDED  = 50;
    public static int TIME_LIMIT_MINUTES = 30;
    
    public static int ZERO  = 0;
    public static int ONE  = 1;
    public static int TWO  = 2;
    public static int TEN  = 10;
    public static double MINUS_ONE  = -1.0;
    public static int SIXTY  = 60;
    
    public static int CPX_PARAM_VARSEL = 2; //pseudo cost branching
    public static int CPX_PARAM_NODEFILEIND = 3; //disk and compressed
    public static int CPX_PARAM_HEURFREQ = -1; //disabled
    public static int CPX_PARAM_MIPSEARCH = 1 ; //traditional 
    //public static int CPXPARAM_DistMIP_Rampup_Duration = 1;// unused here, forces dist mip
    public static int CPX_PARAM_MIPEMPHASIS = 2 ;//optimality    
    public static int MAX_THREADS = 4;
      
}
