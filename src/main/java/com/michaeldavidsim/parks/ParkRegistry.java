package com.michaeldavidsim.parks;

import java.awt.Color;

public final class ParkRegistry {

    public static final Park EE_ROBINSON = new Park(
        "EE Robinson",
        "https://secure.rec1.com/GA/gwinnett-county-parks-recreation/catalog/getFacilityHours/",
        new String[] {
            "/92843/665450/",
            "/92844/665452/",
            "/93224/665454/",
            "/93225/665456/",
            "/141095/665468/",
            "/141096/665470/"
        },
        new String[] {
            "Pickleball $5",
            "Pickleball EE Robinson"
        },
        "34.0977",
        "-84.0429",
        new Color(34, 139, 34)  // Forest green
    );

    public static final Park RHODES_JORDAN = new Park(
            "Rhodes Jordan",
            "https://secure.rec1.com/GA/gwinnett-county-parks-recreation/catalog/getFacilityHours/",
            new String[] {
                    "/10908/665442/",
                    "/135568/665458/",
                    "/135569/665460/",
                    "/135570/665462/",
                    "/135571/665464/"
            },
            new String[] {
                    "Pickleball $5"
            },
            "33.9645",
            "-83.9781",
            new Color(30, 144, 255)  // Dodger blue
    );

    public static final Park[] PARKS = {
        EE_ROBINSON,
        RHODES_JORDAN
    };
}
