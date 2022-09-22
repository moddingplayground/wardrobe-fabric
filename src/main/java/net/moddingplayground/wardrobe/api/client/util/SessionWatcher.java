package net.moddingplayground.wardrobe.api.client.util;

import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.util.Session;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.donator.DonatorUtil;

import java.util.Optional;

/**
 * Checks for session changes every frame.
 */
public final class SessionWatcher {
    private static Session lastSession;

    private SessionWatcher() {
    }

    public static void update(Session session) {
        if (session != lastSession) {
            lastSession = session;

            Optional.ofNullable(session.getUuidOrNull()).map(UUIDTypeAdapter::fromUUID).ifPresent(uuid -> {
                Wardrobe.LOGGER.info("{}", DonatorUtil.getJsonObjectForUser(uuid));
            });
        }
    }
}
