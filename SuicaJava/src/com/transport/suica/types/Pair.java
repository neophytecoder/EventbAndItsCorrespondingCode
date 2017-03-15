/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.transport.suica.types;

/**
 *
 * @author juankarsten
 */
public class Pair<S,T> {
    S first;
    T second;
    
    public Pair(S first, T second) {
        this.first = first;
        this.second = second;
    }
    
    public S first() {
        return first;
    }
    
    
    public T second() {
        return second;
    }
}
