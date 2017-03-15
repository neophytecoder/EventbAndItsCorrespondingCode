/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.transport.suica;

import com.transport.suica.types.Door;
import com.transport.suica.types.Location;
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
public class Machine {
    public Map<Person, Integer> val;
    public Map<Person, Door> dap;
    public Map<Door, Person> dapInverse;
    public Map<Person, Location> sit;
    public Map<Door, Person> mCard;
    public Set<Door> grn;
    public Set<Door> red;
    public Map<Person, Door> org;
    public Set<Door> BLR;
    public Set<Door> mAckn;
    
    public void initialisation() {
        val = new HashMap<>();
        dap = new HashMap<>();
        sit = new HashMap<>();
        mCard = new HashMap<>();
        org = new HashMap<>();

        red = new HashSet<>();
        grn = new HashSet<>();
        BLR = new HashSet<>();
        mAckn = new HashSet<>();
    }
    
    public void enter(Person p, Door d) {
        if (mCard.get(d)==p && 
                sit.get(p)==Context.OUT && 
                sit.get(p)==Context.d_org.get(d) &&
                !dap.keySet().contains(p) &&
                val.keySet().contains(p) && 
                val.get(p)>=0 &&
                Context.AUT.contains(new Pair<>(p, Context.d_dst.get(d)))) {
            
            dap.put(p, d);
            grn.add(d);
            mCard.remove(d);
            org.put(p, d);
        }
    }
    
    public void exit(Person p, Door d) {
        if (mCard.get(d)==p && 
                org.keySet().contains(p) &&
                sit.get(p)!=Context.OUT && 
                Context.d_dst.get(d) == Context.OUT &&
                sit.get(p)==Context.d_org.get(d) &&
                !dap.keySet().contains(p) &&
                val.keySet().contains(p) && 
                val.get(p) - Context.COST.get(new Pair<>(Context.MAP.get(Context.d_dst.get(org.get(p))), Context.MAP.get(Context.d_org.get(d)))) >=0 &&
                Context.AUT.contains(new Pair<>(p, Context.d_dst.get(d)))) {
            
            dap.put(p, d);
            grn.add(d);
            mCard.remove(d);
            val.put(p, val.get(p) - Context.COST.get(new Pair<>(Context.MAP.get(Context.d_dst.get(org.get(p))), Context.MAP.get(Context.d_org.get(d)))));
            org.put(p, d);
        }
    }
    
    public void noEnter(Person p, Door d) {
        if (mCard.get(d)==p && 
                !(
                    sit.get(p)==Context.d_org.get(d) &&
                    !dap.keySet().contains(p) &&
                    Context.AUT.contains(new Pair<>(p, Context.d_dst.get(d)))
                ) &&
                val.keySet().contains(p) &&
                val.get(p)<0 &&
                sit.get(p)==Context.OUT) {
            
            red.add(d);
            mCard.remove(d);
            
        }
    }

    public void noExit(Person p, Door d) {
        if (mCard.get(d)==p && 
                org.keySet().contains(p) &&
                sit.get(p)!=Context.OUT && 
                Context.d_dst.get(d) == Context.OUT &&
                !(
                    sit.get(p)==Context.d_org.get(d) &&
                    !dap.keySet().contains(p) &&
                    Context.AUT.contains(new Pair<>(p, Context.d_dst.get(d)))
                ) &&
                val.keySet().contains(p) &&
                val.get(p) < Context.COST.get(new Pair<>(Context.MAP.get(Context.d_dst.get(org.get(p))), Context.MAP.get(Context.d_org.get(d))))  ) {
               

            red.add(d);
            mCard.remove(d);
        }
    }
    
    public void card(Person p, Door d) {
        Set<Door> difference = new HashSet<>(Context.door);
        difference.removeAll(BLR);
        if (Context.person.contains(p) && difference.contains(d)) {
            BLR.add(d);
            mCard.put(d, p);
        }
    }
    
    public void ackn(Door d) {
        if (mAckn.contains(d)) {
            BLR.remove(d);
            mAckn.remove(d);
        }
    }
    
    public void offGrn(Door d) {
        if (grn.contains(d)) {
            while(dap.values().remove(d));
            grn.remove(d);
            mAckn.add(d);
        }
    }
    
    public void offRed(Door d) {
        if (red.contains(d)) {
            red.remove(d);
            mAckn.add(d);
        }
    }
    
    public void pass(Door d) {
        if (grn.contains(d)) {
            sit.put(dapInverse.get(d), Context.d_dst.get(d));
            while(dap.values().remove(d));
            grn.remove(d);
            mAckn.add(d);
        }
    }
    
    public void reload(Person p, Integer n) {
        if (n >= 0 && 
                val.containsKey(p) && 
                val.get(p) + n >= 0 &&
                val.get(p) + n <= 20000
                ) {
            
            val.put(p, val.get(p)+n);
        }
    }
    
    public void buy(Person p, Integer n) {
        if (n >= 0 && 
                val.containsKey(p) && 
                val.get(p) - n >= 0
                ) {
            
            val.put(p, val.get(p)-n);
        }
    }

    public void register(Person p) {
        if (!val.containsKey(p) && 
                sit.get(p) == Context.OUT) {
            
            val.put(p, 0);
        }
    }

    public void unregister(Person p) {
        if (!val.containsKey(p) && 
                sit.get(p) == Context.OUT) {
            
            val.remove(p);
        }
    }
    
    public void transfer(Person p, Door d) {
        if (mCard.get(d)==p && 
                org.keySet().contains(p) &&
                sit.get(p)!=Context.OUT && 
                Context.d_dst.get(d) != Context.OUT &&
                sit.get(p)==Context.d_org.get(d) &&
                !dap.keySet().contains(p) &&
                Context.AUT.contains(new Pair<>(p, Context.d_dst.get(d))) &&
                Context.EXTRA.get(Context.d_org.get(d)) != Context.EXTRA.get(Context.d_dst.get(d)) &&
                val.keySet().contains(p) && 
                val.get(p) - Context.COST.get(new Pair<>(Context.MAP.get(Context.d_dst.get(org.get(p))), Context.MAP.get(Context.d_org.get(d)))) >=0
                ) {
            
            dap.put(p, d);
            grn.add(d);
            mCard.remove(d);
            val.put(p, val.get(p) - Context.COST.get(new Pair<>(Context.MAP.get(Context.d_dst.get(org.get(p))), Context.MAP.get(Context.d_org.get(d)))));
            org.put(p, d);
        }
    }

    public void noTransfer(Person p, Door d) {
        if (mCard.get(d)==p && 
                !(
                    sit.get(p)==Context.d_org.get(d) &&
                    !dap.keySet().contains(p) &&
                    Context.AUT.contains(new Pair<>(p, Context.d_dst.get(d)))
                ) &&
                org.keySet().contains(p) &&
                sit.get(p)!=Context.OUT  &&
                Context.d_dst.get(d) != Context.OUT &&
                val.keySet().contains(p) && 
                sit.get(p)==Context.d_org.get(d) &&
                Context.EXTRA.get(Context.d_org.get(d)) != Context.EXTRA.get(Context.d_dst.get(d)) &&
                val.get(p) < Context.COST.get(new Pair<>(Context.MAP.get(Context.d_dst.get(org.get(p))), Context.MAP.get(Context.d_org.get(d))))
                ) {
            
            red.add(d);
            mCard.remove(d);
        }
    }
}
