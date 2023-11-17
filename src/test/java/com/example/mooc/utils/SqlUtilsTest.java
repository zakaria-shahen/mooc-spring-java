package com.example.mooc.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SqlUtilsTest {

    @Test
    void addNamedParameters() {
        String sql = """
                    insert into BOOTCAMP(#name, #description, #website, #phone, #email, #address, #housing, #job_assistance, #job_guarantee, #average_cost, #average_rating, user_id)
                    values(@@)
                """.strip();
        Assertions.assertThat(SqlUtils.addNamedParameters(sql))
                .isEqualTo("""
                        insert into BOOTCAMP(name, description, website, phone, email, address, housing, job_assistance, job_guarantee, average_cost, average_rating, user_id)
                            values(:name, :description, :website, :phone, :email, :address, :housing, :jobAssistance, :jobGuarantee, :averageCost, :averageRating)
                        """.strip());

        sql = """
                            update BOOTCAMP SET 
                    #name = @,
                    #description = @,
                    #website = @,
                    #phone = @,
                    #email = @,
                    #address = @,
                    #housing = @,
                    #job_assistance = @,
                    #job_guarantee = @,
                    #average_cost = @,
                    #average_rating = @,
                    #user_id = @
                where id = :id""".strip();
        Assertions.assertThat(SqlUtils.addNamedParameters(sql))
                .isEqualTo("""
                        update BOOTCAMP SET
                            name = :name,
                            description = :description,
                            website = :website,
                            phone = :phone,
                            email = :email,
                            address = :address,
                            housing = :housing,
                            job_assistance = :jobAssistance,
                            job_guarantee = :jobGuarantee,
                            average_cost = :averageCost,
                            average_rating = :averageRating,
                            user_id = :userId
                        where id = :id""".strip());
    }
}
