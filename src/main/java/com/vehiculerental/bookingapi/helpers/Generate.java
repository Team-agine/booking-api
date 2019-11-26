package com.vehiculerental.bookingapi.helpers;

import java.util.UUID;

public final class Generate {
    public static String id() {
        return UUID.randomUUID().toString();
    }
}
