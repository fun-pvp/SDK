package fr.funpvp.sdk.utils;

import org.jetbrains.annotations.NotNull;

/*
 * This file is part of NELYRIA.
 *
 * Copyright © 2025, OCTA-GROUP. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
public class GsonUtils {
    public static <T> T fromJson(final @NotNull String json, final @NotNull Class<T> targetClass) {
        return GsonProvider.GSON.fromJson(json, targetClass);
    }
}
