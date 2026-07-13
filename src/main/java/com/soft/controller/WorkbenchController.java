package com.soft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
public class WorkbenchController {

    @Autowired
    private DataSource dataSource;

    @RequestMapping("/workbench")
    public Map<String, Object> workbench() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 200);
        result.put("elderlyCount", queryCount("SELECT COUNT(*) FROM t_elderly"));
        result.put("checkedInCount", queryCount("SELECT COUNT(*) FROM t_bed WHERE status=1"));
        result.put("orderCount", queryCount("SELECT COUNT(*) FROM t_order"));
        result.put("staffCount", queryCount("SELECT COUNT(*) FROM t_user"));
        result.put("revenue", queryString("SELECT IFNULL(SUM(amount),0) FROM t_bill WHERE status='已支付'"));
        result.put("outCount", queryCount("SELECT COUNT(*) FROM t_leave WHERE status='请假中'"));
        result.put("checkInOutTrend", queryCheckInOutTrend());
        result.put("todos", queryTodos());
        result.put("todayAppointments", queryTodayAppointments());
        return result;
    }

    private int queryCount(String sql) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) { return 0; }
    }

    private String queryString(String sql) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getString(1) : "0";
        } catch (Exception e) { return "0"; }
    }

    private List<Map<String, Object>> queryCheckInOutTrend() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = """
            SELECT d.dt AS date,
                   (SELECT COUNT(*) FROM t_check_in WHERE DATE(create_time)=d.dt) AS checkIn,
                   (SELECT COUNT(*) FROM t_check_out WHERE DATE(create_time)=d.dt) AS checkOut
            FROM (
                SELECT CURDATE() - INTERVAL 6 DAY AS dt UNION ALL
                SELECT CURDATE() - INTERVAL 5 DAY UNION ALL
                SELECT CURDATE() - INTERVAL 4 DAY UNION ALL
                SELECT CURDATE() - INTERVAL 3 DAY UNION ALL
                SELECT CURDATE() - INTERVAL 2 DAY UNION ALL
                SELECT CURDATE() - INTERVAL 1 DAY UNION ALL
                SELECT CURDATE()
            ) d ORDER BY d.dt
            """;
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("date", rs.getString("date"));
                m.put("checkIn", rs.getInt("checkIn"));
                m.put("checkOut", rs.getInt("checkOut"));
                list.add(m);
            }
        } catch (Exception e) { /* ignore */ }
        return list;
    }

    private List<Map<String, Object>> queryTodos() {
        List<Map<String, Object>> list = new ArrayList<>();
        addTodosFrom(list, "t_check_in", "入住申请", "bill_no", "elder_name", "flow_status", "申请中");
        addTodosFrom(list, "t_check_out", "退住申请", "bill_no", "elder_name", "flow_status", "申请中");
        addTodosFrom(list, "t_leave", "请假申请", "leave_no", "elder_name", "status", "请假中");
        addAppointmentTodos(list);
        return list.size() > 5 ? list.subList(0, 5) : list;
    }

    private void addTodosFrom(List<Map<String, Object>> list, String table, String type, String noCol, String nameCol, String statusCol, String statusVal) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT " + noCol + "," + nameCol + ",create_time FROM " + table + " WHERE " + statusCol + "=? ORDER BY create_time DESC LIMIT 5")) {
            ps.setString(1, statusVal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("type", type);
                m.put("billNo", rs.getString(1));
                m.put("name", rs.getString(2));
                m.put("time", rs.getTimestamp(3));
                list.add(m);
            }
        } catch (Exception e) { /* ignore */ }
    }

    private void addAppointmentTodos(List<Map<String, Object>> list) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT visitor_name,elder_name,appointment_time FROM t_appointment WHERE status=0 ORDER BY appointment_time DESC LIMIT 3");
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("type", "预约待处理");
                m.put("billNo", "");
                m.put("name", rs.getString("visitor_name") + "→" + rs.getString("elder_name"));
                m.put("time", rs.getTimestamp("appointment_time"));
                list.add(m);
            }
        } catch (Exception e) { /* ignore */ }
    }

    private List<Map<String, Object>> queryTodayAppointments() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT visitor_name,elder_name,appointment_time,appointment_type,status FROM t_appointment WHERE DATE(appointment_time)=CURDATE() OR DATE(create_time)=CURDATE() ORDER BY appointment_time LIMIT 5");
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("visitorName", rs.getString("visitor_name"));
                m.put("elderName", rs.getString("elder_name"));
                m.put("time", rs.getTimestamp("appointment_time"));
                m.put("type", rs.getString("appointment_type"));
                m.put("status", rs.getInt("status") == 1 ? "已完成" : "待上门");
                list.add(m);
            }
        } catch (Exception e) { /* ignore */ }
        return list;
    }
}
