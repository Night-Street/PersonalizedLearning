/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.ga.problem;

import tools.ga.individual.Individual;

/**
 *
 * @author hgs
 */
public interface Problem<T extends Individual> {
      
    public float evaluate(T individual);

   }
