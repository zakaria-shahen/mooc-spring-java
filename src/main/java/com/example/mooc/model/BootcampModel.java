package com.example.mooc.model;

import com.example.mooc.utils.SQLQueryBuilderUtils;
import lombok.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class BootcampModel {
    private Long id;
    @NonNull private String name;
    @NonNull private String description;
    @NonNull private String website;
    @NonNull private String phone;
    @NonNull private String email;
    @NonNull private String address;
    @NonNull private Boolean housing;
    private Boolean jobAssistance;
    private Boolean jobGuarantee;
    private BigDecimal averageCost;
    private Float averageRating;
    @NonNull private Long userId;

    public static final SQLQueryBuilderUtils sqlModuleUtils = new SQLQueryBuilderUtils(BootcampModel.class);
}
