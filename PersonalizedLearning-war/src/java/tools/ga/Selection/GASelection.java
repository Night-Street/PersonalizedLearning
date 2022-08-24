/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.ga.Selection;

import tools.ga.individual.Individual;

/**
 *
 * @author hgs
 * @param <T>
 */
public interface GASelection<T extends Individual> {
    public T [] getPopulationAfterSelection();
}
