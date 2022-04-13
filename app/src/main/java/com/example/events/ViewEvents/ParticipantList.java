package com.example.events.ViewEvents;

import java.util.ArrayList;

public class ParticipantList {
    ArrayList<Participants> participants;
    ArrayList<Participants> admins;
    ArrayList<Participants> notAdmins;

    public ParticipantList() {
        participants = new ArrayList<>();
        admins = new ArrayList<>();
        notAdmins = new ArrayList<>();

        Participants p1 = new Participants(true);
        admins.add(p1);
        notAdmins.add(p1);
    }

    public void add(Participants p) {
        boolean contains = checkAlreadyExists(participants, p);
        if (contains) {
            if (p.role.equals("a")) checkAlreadyExists(admins, p);
            else checkAlreadyExists(notAdmins, p);
            //System.out.println("Contains True");
        } else {
            //System.out.println("Contains False");
            participants.add(p);
            if (p.role.equals("a")) admins.add(p);
            else notAdmins.add(p);
        }
    }

    private boolean checkAlreadyExists(ArrayList<Participants> list, Participants part) {
        for (int i = 0; i < participants.size(); i++)
            if (part.UID.equals(participants.get(i).UID)) {
                list.set(i, part);
                return true;
            }
        return false;
    }

    public Participants get(int index) {
        if (index < admins.size()) return admins.get(index);
        else return notAdmins.get(index - admins.size());
    }

    public int size() {
        return admins.size() + notAdmins.size();
    }

     /*public int adjustedSize(int pos){
         if (pos==0)
             return pos+1;
         if(pos< admins.size())
             return pos+1;

     }*/

}
