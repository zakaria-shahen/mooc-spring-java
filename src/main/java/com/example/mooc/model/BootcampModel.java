package com.example.mooc.model;

import com.example.mooc.utils.SQLQueryBuilderUtils;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public record BootcampModel(
        @NonNull Long id,
        @NonNull String name,
        @NonNull String description,
        @NonNull String website,
        @NonNull String phone,
        @NonNull String email,
        @NonNull String address,
        @NonNull Boolean housing,
        Boolean jobAssistance,
        Boolean jobGuarantee,
        BigDecimal averageCost,
        Float averageRating,
        @NonNull Long userId

) {
    public static final SQLQueryBuilderUtils sqlModuleUtils = new SQLQueryBuilderUtils(BootcampModel.class);

    public BootcampModel(@NonNull Long id, @NonNull String name, @NonNull String description, @NonNull String website, @NonNull String phone, @NonNull String email, @NonNull String address, @NonNull Boolean housing, @NonNull Long userId) {
        this(id, name, description, website, phone, email, address, housing, null, null, null, null, userId);
    }
}
