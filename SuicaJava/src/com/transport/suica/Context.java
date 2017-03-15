/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.transport.suica;

import com.transport.suica.types.Door;
import com.transport.suica.types.Location;
import com.transport.suica.types.Network;
import com.transport.suica.types.Pair;
import com.transport.suica.types.Person;
import com.transport.suica.types.Zone;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author juankarsten
 */
public class Context {
    // assume the value of constants is already pre-defined
    
    // ******************************************************
    // Context 0
    public static final Set<Person> person = new HashSet<>();
    public static final Set<Location> location = new HashSet<>();
    
    public static final Location OUT = new Location();
    public static final Set<Pair<Person, Location>> AUT = new HashSet<Pair<Person, Location>>();
    
    // ******************************************************
    // Context 1
    public static final Set<Pair<Location, Location>> COM = new HashSet<>();

    // ******************************************************
    // Context 2
    public static final Set<Door> door = new HashSet<>();
    public static final Map<Door, Location> d_org = new HashMap<>();
    //public static final Map<Location, Door> d_orgInverse = new HashMap<>();
    public static final Map<Door, Location> d_dst = new HashMap<>();

    // ******************************************************
    // Context 3
    public static final Map<Location,Zone> MAP = new HashMap<>();
    public static final Map<Location, Network> EXTRA = new HashMap<>();
    public static final Map<Pair<Zone, Zone>, Integer> COST = new HashMap<>();
    
    
    
    public static void axiomTwoContextOne() {
        for (Pair<Location, Location> com: COM) {
            assert com.first()!=com.second();
        }
    }
}
