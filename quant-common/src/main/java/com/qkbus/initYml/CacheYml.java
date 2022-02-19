package com.qkbus.initYml;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CacheYml {


    /*
     * 版本号
     * */
    public static String PROFILES_ACTIVE;

    @Value("${spring.profiles.active}")
    public void setProfilesActive(String profilesActive) {
        PROFILES_ACTIVE = profilesActive;
    }


    public static boolean getProfiles() {
        if (PROFILES_ACTIVE.equals("dev")) {
            return true;
        }
        return false;
    }

}
