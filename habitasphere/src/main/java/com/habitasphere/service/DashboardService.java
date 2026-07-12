package com.habitasphere.service;

import com.habitasphere.dto.AdminDashboardResponse;
import com.habitasphere.dto.ResidentDashboardResponse;

public interface DashboardService {

    AdminDashboardResponse getAdminDashboard();

    ResidentDashboardResponse getResidentDashboard(String email);

}