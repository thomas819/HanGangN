package com.example.thomas.hangangn.model;

import java.util.ArrayList;
import java.util.List;

public class Address {

    private static List<String[]> addressLists;
    String[] placeName = {"강서", "광나루", "난지", "뚝섬", "망원", "반포", "양화", "여의도", "이촌", "잠원", "잠실"};

    static String[] gangseo = {"46788", "46783", "2891"};
    static String[] gwangnaru = {"46585", "46609", "1665"};
    static String[] nanji = {"46777", "46766", "3142"};
    static String[] ttukseom = {"46661", "46650", "2154"};
    static String[] mangwon = {"46737", "46733", "3727"};
    static String[] banpo = {"46727", "46717", "3702"};
    static String[] yanghwa = {"46804", "46801", "2810"};
    static String[] yeouido = {"46758", "46747", "3217"};
    static String[] leechon = {"46695", "46687", "2645"};
    static String[] jamwon = {"46678", "46671", "2551"};
    static String[] jamsil = {"46639", "46623", "1812"};


    public static List<String[]> get() {
        if (addressLists == null){
            addressLists = new ArrayList<>();
            addressLists.add(gangseo);
            addressLists.add(gwangnaru);
            addressLists.add(nanji);
            addressLists.add(ttukseom);
            addressLists.add(mangwon);
            addressLists.add(banpo);
            addressLists.add(yanghwa);
            addressLists.add(yeouido);
            addressLists.add(leechon);
            addressLists.add(jamwon);
            addressLists.add(jamsil);
        }
        return addressLists;
    }

    private Address() {}

}
