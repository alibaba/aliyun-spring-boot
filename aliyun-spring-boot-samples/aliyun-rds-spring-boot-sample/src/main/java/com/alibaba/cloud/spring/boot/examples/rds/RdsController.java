/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.spring.boot.examples.rds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The RDS controller.
 *
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RestController
public class RdsController {

    private static final String READ_SQL = "SELECT * FROM order_tbl WHERE user_id = ?";

    private static final String UPDATE_SQL = "UPDATE order_tbl SET count = ? WHERE user_id = ?";

    @Autowired
    private DataSource dataSource;

    @GetMapping("/getDataSource")
    @ResponseBody
    public String getDataSource() {
        return String.valueOf(dataSource);
    }

    @GetMapping("/query/{userId}")
    @ResponseBody
    public List<Order> query(@PathVariable String userId) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(READ_SQL)) {
                stmt.setString(1, userId);
                try (ResultSet resultSet = stmt.executeQuery()) {
                    while (resultSet.next()) {
                        orders.add(convert(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @GetMapping("/update/{userId}/{count}")
    @ResponseBody
    public List<Order> update(@PathVariable String userId, @PathVariable Integer count) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
                stmt.setInt(1, count);
                stmt.setString(2, userId);
                stmt.execute();
            }
            orders.addAll(query(userId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private Order convert(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setUserId(resultSet.getString("user_id"));
        order.setCommodityCode(resultSet.getString("commodity_code"));
        order.setCount(resultSet.getInt("count"));
        order.setMoney(resultSet.getInt("money"));
        return order;
    }
}
