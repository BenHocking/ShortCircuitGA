/*
 * Copyright (c) 2010 Ashlie Benjamin Hocking. All Rights reserved.
 */
package edu.virginia.cs.geneticalgorithm.neurojet;

/**
 * Driver for genetic algorithm exploring NeuroJet space
 * @author <a href="mailto:benjamin.hocking@gmail.com">Ashlie Benjamin Hocking</a>
 * @since May 16, 2010
 */
public class NeuroJetGeneticAlgorithm {

    private final ScriptUpdater _updater;

    public NeuroJetGeneticAlgorithm() {
        _updater = new ScriptUpdater();
        buildScriptUpdater();
    }

    private void buildScriptUpdater() {
        _updater.addDoubleMapping(0, "A", 2.0, 8.0); // InternrnExcDecay
        _updater.addIntegerMapping(1, "C", 1, 4); // InternrnAxonalDelay
        _updater.addIntegerMapping(2, "D", 3, 15); // dendFilterWidth
        _updater.addIntegerMapping(3, "E", 1, 5); // minAxDelay
        _updater.addIntegerMapping(4, "F", "E", 7); // maxAxDelay
    }

    /*
     * @param args
     */
    public static void main(final String[] args) {
        // TODO Auto-generated method stub
    }

}
